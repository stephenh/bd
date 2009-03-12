package org.exigencecorp.bd.resources.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.exigencecorp.bd.resources.Files;

public class Tests {

    private final Source source;

    public Tests(Source source) {
        this.source = source;
    }

    public void run() throws InterruptedException {
        String javaHome = System.getProperty("java.home");

        List<String> command = new ArrayList<String>();
        command.add(javaHome + File.separator + "bin" + File.separator + "java");
        command.add("-cp");
        command.add(this.source.getJoinedClasspath() + this.source.getDestination().getPath());
        command.add("junit.textui.TestRunner");

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

        System.out.println(command);

        try {
            ProcessBuilder pb = new ProcessBuilder().command(command);
            Process p = pb.start();
            p.getOutputStream().close();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
            r.close();

            r = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = r.readLine()) != null) {
                System.out.println(line);
            }
            r.close();

            p.waitFor();
            System.out.println(p.exitValue());

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
