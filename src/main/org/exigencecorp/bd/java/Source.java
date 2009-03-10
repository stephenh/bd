package org.exigencecorp.bd.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

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
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            List<String> options = new ArrayList<String>();

            options.add("-cp");
            for (Files files : this.libraries) {
                options.add(files.getPathsJoined());
            }

            options.add("-d");
            options.add(this.destination.getPath());

            for (Files files : this.sourceFiles) {
                for (File file : files.getFiles()) {
                    if (file.getName().endsWith("java")) {
                        options.add(file.getPath());
                    }
                }
            }

            int result = compiler.run(null, null, null, options.toArray(new String[options.size()]));
            if (result != 0) {
                throw new Error("An error occurred");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Files> getSourceFiles() {
        return this.sourceFiles;
    }

}
