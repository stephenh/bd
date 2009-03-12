package org.exigencecorp.bd.resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Files {

    private final Dir dir;
    private final List<String> includes = new ArrayList<String>();
    private final List<Files> others = new ArrayList<Files>();

    public Files() {
        this.dir = null;
    }

    public Files(Dir dir) {
        this.dir = dir;
    }

    public void delete() {
        List<File> all = this.getFilesAndDirectories();
        while (all.size() != 0) {
            File file = all.remove(all.size() - 1);
            if (!file.delete()) {
                throw new RuntimeException("Could not delete " + file);
            }
        }
    }

    public void add(Files other) {
        this.others.add(other);
    }

    public Files includes(String includes) {
        this.includes.add(includes);
        return this;
    }

    public List<File> getFiles() {
        return this.getFilesAndDirectories(false);
    }

    public List<File> getFilesAndDirectories() {
        return this.getFilesAndDirectories(true);
    }

    public List<String> getPaths() {
        List<String> paths = new ArrayList<String>();
        for (File file : this.getFiles()) {
            paths.add(file.getPath());
        }
        return paths;
    }

    /** @return the directory first and then its files */
    private List<File> getFilesAndDirectories(boolean includeDirs) {
        List<File> returnFiles = new ArrayList<File>();
        if (this.dir != null) {
            List<File> queue = new ArrayList<File>();
            queue.add(this.dir.getFile());
            if (includeDirs) {
                returnFiles.add(this.dir.getFile());
            }
            while (queue.size() != 0) {
                File dir = queue.remove(0);
                for (File file : dir.listFiles()) {
                    if (file.isDirectory()) {
                        queue.add(file);
                        if (includeDirs) {
                            returnFiles.add(file);
                        }
                    } else {
                        returnFiles.add(file);
                    }
                }
            }
        }
        for (Files other : this.others) {
            returnFiles.addAll(other.getFilesAndDirectories(includeDirs));
        }
        return returnFiles;
    }

    public List<Files> getThisAndOthers() {
        List<Files> files = new ArrayList<Files>();
        if (this.dir != null) {
            files.add(this);
        }
        for (Files other : this.others) {
            files.addAll(other.getThisAndOthers());
        }
        return files;
    }

    public Dir getDir() {
        return this.dir;
    }

}
