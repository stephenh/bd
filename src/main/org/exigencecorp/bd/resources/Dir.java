package org.exigencecorp.bd.resources;

import java.io.File;

public class Dir {

    private File path;

    public Dir(String basePath) {
        this.path = new File(basePath);
    }

    public Dir(File path) {
        this.path = path;
    }

    public Dir dir(String name) {
        return new Dir(new File(this.path, name));
    }

    public void delete() {
        this.files().delete();
    }

    public void mkdirs() {
        this.path.mkdirs();
    }

    public String getPath() {
        return this.path.getPath();
    }

    public File getFile() {
        return this.path;
    }

    public Files files() {
        return new Files(this);
    }

}
