package org.exigencecorp.bd.resources;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.exigencecorp.bd.HomeCache;

public abstract class AbstractZip<T extends AbstractZip<T>> {

    private final File destination;
    private final List<FilesWithPrefix> files = new ArrayList<FilesWithPrefix>();

    public AbstractZip(String destinationPath) {
        this.destination = new File(destinationPath);
    }

    public T includes(Files files) {
        return this.includes("", files);
    }

    public T includes(Dir path) {
        return this.includes("", path.files());
    }

    public T includes(String prefix, Dir dir) {
        return this.includes(prefix, dir.files());
    }

    public T includes(String prefix, Files files) {
        this.files.add(new FilesWithPrefix(prefix, files, false));
        return (T) this;
    }

    public T includesFlat(String prefix, Files files) {
        this.files.add(new FilesWithPrefix(prefix, files, true));
        return (T) this;
    }

    public void create() {
        this.destination.getParentFile().mkdirs();
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(this.destination));
            ZipOutputStream zipOut = this.makeZipOutputStream(out);
            zipOut.setLevel(Deflater.BEST_COMPRESSION);
            for (FilesWithPrefix fwp : this.files) {
                for (Files files : fwp.files.getThisAndOthers()) {
                    for (File file : this.getFilesBySuffix(files)) {
                        // normalize the path (replace / with \ if required)
                        String entryName = this.removeBase(files.getDir().getPath(), file.getPath());
                        if (fwp.flatten && entryName.contains("/")) {
                            entryName = entryName.substring(entryName.lastIndexOf("/") + 1);
                        }
                        byte[] data = this.readFile(file);
                        ZipEntry entry = new ZipEntry(fwp.prefix + entryName);
                        CRC32 crc = new CRC32();
                        crc.update(data);
                        entry.setSize(file.length());
                        entry.setCrc(crc.getValue());
                        zipOut.putNextEntry(entry);
                        zipOut.write(data);
                        zipOut.closeEntry();
                    }
                }
            }
            zipOut.closeEntry();
            zipOut.close();
        } catch (IOException e) {
            throw new Error("Error creating file " + this.destination, e);
        }
    }

    public void publishToHomeCache() {
        HomeCache.publish(this.destination);
    }

    protected abstract ZipOutputStream makeZipOutputStream(OutputStream out) throws IOException;

    // for better compression, sort by suffix, then name
    private List<File> getFilesBySuffix(Files filesObject) {
        List<File> files = filesObject.getFiles();
        Collections.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                String p1 = (o1).getPath();
                String p2 = (o2).getPath();
                int comp = AbstractZip.this.getSuffix(p1).compareTo(AbstractZip.this.getSuffix(p2));
                if (comp == 0) {
                    comp = p1.compareTo(p2);
                }
                return comp;
            }
        });
        return files;
    }

    private String getSuffix(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return idx < 0 ? "" : fileName.substring(idx);
    }

    private String removeBase(String basePath, String path) {
        if (path.startsWith(basePath)) {
            path = path.substring(basePath.length());
        }
        path = path.replace('\\', '/');
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

    private byte[] readFile(File file) {
        try {
            RandomAccessFile ra = new RandomAccessFile(file, "r");
            long len = ra.length();
            if (len >= Integer.MAX_VALUE) {
                throw new Error("File " + file.getPath() + " is too large");
            }
            byte[] buffer = new byte[(int) len];
            ra.readFully(buffer);
            ra.close();
            return buffer;
        } catch (IOException e) {
            throw new Error("Error reading from file " + file, e);
        }
    }

    private static final class FilesWithPrefix {
        private final String prefix;
        private final Files files;
        private final boolean flatten;

        private FilesWithPrefix(String prefix, Files files, boolean flatten) {
            this.prefix = prefix;
            this.files = files;
            this.flatten = flatten;
        }
    }

}
