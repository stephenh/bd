package org.exigencecorp.bd.resources.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.exigencecorp.bd.BuildRunner;
import org.exigencecorp.bd.resources.Files;
import org.exigencecorp.bd.util.Execute;
import org.exigencecorp.bd.util.Execute.Result;

public class Tests {

    private final Source source;

    public Tests(Source source) {
        this.source = source;
    }

    public void run() throws InterruptedException {
        String javaHome = System.getProperty("java.home");

        List<String> command = new ArrayList<String>();
        command.add("-cp");
        command.add(this.source.getJoinedClasspath() + this.source.getDestination().getPath());
        command.add("org.junit.runner.JUnitCore");

        for (Files files : this.source.getSources().getThisAndOthers()) {
            for (File file : files.getFiles()) {
                if (file.getName().contains("Abstract")) {
                    continue;
                }
                if (!file.getName().contains("Test")) {
                    continue;
                }
                String prefix = files.getDir().getPath();
                String filePath = file.getPath().replaceAll(".java", "");
                filePath = filePath.substring(prefix.length() + 1).replace(File.separator, ".");
                // System.out.println("prefix=" + prefix);
                // System.out.println("fileName=" + filePath);
                command.add(filePath);
            }
        }

        Result r = new Execute("java").copyEnv().path(javaHome + File.separator + "bin").args(command).toBuffer();
        System.out.println(r.out.toString());
        System.out.println(r.err.toString());
        if (!r.success) {
            BuildRunner.fail("Tests failed");
        }
    }

}
