package org.exigencecorp.bd;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public abstract class AbstractZip<T extends AbstractZip<T>> {

    private final File destination;
    private final List<FilesWithPrefix> files = new ArrayList<FilesWithPrefix>();

    public AbstractZip(String destinationPath) {
        this.destination = new File(destinationPath);
    }

    public T includes(Files files) {
        this.files.add(new FilesWithPrefix("", files));
        return (T) this;
    }

    public T includes(File path) {
        this.files.add(new FilesWithPrefix("", new Files(path)));
        return (T) this;
    }

    public T includes(String prefix, Files files) {
        this.files.add(new FilesWithPrefix(prefix, files));
        return (T) this;
    }

    public T includesFlat(String prefix, Files files) {
        this.files.add(new FilesWithPrefix(prefix, files, true));
        return (T) this;
    }

    public T includes(String prefix, File path) {
        this.files.add(new FilesWithPrefix(prefix, new Files(path)));
        return (T) this;
    }

    public T includesFlat(String prefix, File path) {
        this.files.add(new FilesWithPrefix(prefix, new Files(path), true));
        return (T) this;
    }

    public void create() {
        this.destination.getParentFile().mkdirs();
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(this.destination));
            ZipOutputStream zipOut = this.makeZipOutputStream(out);
            zipOut.setLevel(Deflater.BEST_COMPRESSION);
            for (FilesWithPrefix fwp : this.files) {
                for (File file : fwp.files.getFilesBySuffix()) {
                    // normalize the path (replace / with \ if required)
                    String entryName = this.removeBase(fwp.files.getBasePath().getPath(), file.getPath());
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

        private FilesWithPrefix(String prefix, Files files) {
            this.prefix = prefix;
            this.files = files;
            this.flatten = false;
        }

        private FilesWithPrefix(String prefix, Files files, boolean flatten) {
            this.prefix = prefix;
            this.files = files;
            this.flatten = flatten;
        }
    }

}
