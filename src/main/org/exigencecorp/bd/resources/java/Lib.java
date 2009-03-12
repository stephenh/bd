package org.exigencecorp.bd.resources.java;

import java.io.File;

import org.exigencecorp.bd.HomeCache;
import org.exigencecorp.bd.resources.Dir;
import org.exigencecorp.bd.resources.Files;

public class Lib extends Dir {

    public static Lib homeCache() {
        return new Lib(HomeCache.get().getPath());
    }

    public Lib(String basePath) {
        super(basePath);
    }

    public Files getJarFiles() {
        return this.files().includes("*.jar");
    }

    public void updateFromHomeCache() {
        for (File sourceFile : this.files().getFiles()) {
            HomeCache.updateIfNeeded(sourceFile);
        }
    }

}
