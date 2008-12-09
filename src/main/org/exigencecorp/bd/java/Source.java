package org.exigencecorp.bd.java;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.exigencecorp.bd.Files;

public class Source {

    private final List<Files> sourceFiles = new ArrayList<Files>();
    private final File destination;
    private final List<Files> libraries = new ArrayList<Files>();

    public Source(String basePath, File destination) {
        this.sourceFiles.add(new Files(basePath));
        this.destination = destination;
    }

    public Source lib(Lib lib) {
        this.libraries.add(lib.getJarFiles());
        return this;
    }

    public Source addSource(String basePath) {
        this.sourceFiles.add(new Files(basePath));
        return this;
    }

    public void compile() {
        this.destination.mkdirs();
        try {
            Class<?> clazz = Class.forName("com.sun.tools.javac.Main");
            Method compile = clazz.getMethod("compile", new Class[] { String[].class });
            Object instance = clazz.newInstance();

            List<String> args = new ArrayList<String>();
            args.add("-d");
            args.add(this.destination.getPath());
            args.add("-cp");
            String cp = "";
            for (Files files : this.libraries) {
                cp += files.getPathsJoined();
            }
            args.add(cp);
            for (Files files : this.sourceFiles) {
                args.addAll(files.getPaths());
            }

            String[] argsArray = args.toArray(new String[args.size()]);
            Integer result = ((Integer) compile.invoke(instance, new Object[] { argsArray })).intValue();
            if (result != 0) {
                throw new Error("An error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
