package org.exigencecorp.bd.resources.java;

import java.io.File;
import java.io.IOException;

import org.exigencecorp.bd.resources.Dir;

public class Tests extends Source {

    public Tests(String basePath, Dir destination) {
        super(basePath, destination);
    }

    public void run() {
        String javaHome = System.getProperty("java.home");
        ProcessBuilder pb = new ProcessBuilder(javaHome + File.separator + "bin" + File.separator + "java");
        try {
            pb.start();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}
