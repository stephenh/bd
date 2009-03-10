package org.exigencecorp.bd;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Files {

    private final File basePath;
    private final List<String> includes = new ArrayList<String>();

    public Files(String basePath) {
        this.basePath = new File(basePath);
    }

    public Files(File basePath) {
        this.basePath = basePath;
    }

    public Files includes(String includes) {
        this.includes.add(includes);
        return this;
    }

    public Files subset(String includes) {
        Files subset = new Files(this.basePath);
        subset.includes.addAll(this.includes);
        subset.includes.add(includes);
        return subset;
    }

    public String getPathsJoined() {
        StringBuilder sb = new StringBuilder();
        for (String path : this.getPaths()) {
            sb.append(path);
            sb.append(File.pathSeparator);
        }
        return sb.toString();
    }

    public List<File> getFiles() {
        List<File> files = new ArrayList<File>();
        List<File> dirs = new ArrayList<File>();
        dirs.add(this.basePath);
        while (dirs.size() != 0) {
            for (File f : dirs.remove(0).listFiles()) {
                if (f.isDirectory()) {
                    dirs.add(f);
                } else {
                    files.add(f);
                }
            }
        }
        return files;
    }

    public List<String> getPaths() {
        List<String> paths = new ArrayList<String>();
        for (File file : this.getFiles()) {
            paths.add(file.getPath());
        }
        return paths;
    }

    public List<File> getFilesBySuffix() {
        // for better compressibility, sort by suffix, then name
        List<File> files = this.getFiles();
        Collections.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                String p1 = (o1).getPath();
                String p2 = (o2).getPath();
                int comp = Files.this.getSuffix(p1).compareTo(Files.this.getSuffix(p2));
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

    public File getBasePath() {
        return this.basePath;
    }

}
