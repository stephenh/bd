package org.exigencecorp.bd.java;

import java.io.File;

import org.exigencecorp.bd.Files;
import org.exigencecorp.bd.HomeCache;

public class Lib extends Files {

    public Lib(String basePath) {
        super(basePath);
    }

    public Files getJarFiles() {
        return this.subset("*.jar");
    }

    public void updateFromHomeCache() {
        for (File sourceFile : this.getFiles()) {
            HomeCache.updateIfNeeded(sourceFile);
        }
    }

}
