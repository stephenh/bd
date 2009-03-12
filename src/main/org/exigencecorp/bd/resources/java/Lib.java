package org.exigencecorp.bd.resources.java;

import java.io.File;

import org.exigencecorp.bd.HomeCache;
import org.exigencecorp.bd.resources.Files;

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
