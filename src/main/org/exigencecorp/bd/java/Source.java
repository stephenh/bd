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
    private CompilerOptions options;

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

    public Source compilerOptions(CompilerOptions options) {
        this.options = options;
        return this;
    }

    public List<Files> getSourceFiles() {
        return this.sourceFiles;
    }

    public void compile() {
        this.destination.mkdirs();

        List<String> options = new ArrayList<String>();
        options.addAll(this.options.getCompilerOptions());
        this.addClasspathToOptions(options);
        this.addDestinationToOptions(options);
        this.addFilesToOptions(options);

        int result;
        try {
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            result = compiler.run(null, null, null, options.toArray(new String[options.size()]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (result != 0) {
            throw new RuntimeException("Compile failed");
        }
    }

    protected void addFilesToOptions(List<String> options) {
        for (Files files : this.sourceFiles) {
            for (File file : files.getFiles()) {
                if (file.getName().endsWith(".java")) {
                    options.add(file.getPath());
                }
            }
        }
    }

    protected void addClasspathToOptions(List<String> options) {
        options.add("-cp");
        for (Files files : this.libraries) {
            options.add(files.getPathsJoined());
        }
    }

    protected void addDestinationToOptions(List<String> options) {
        options.add("-d");
        options.add(this.destination.getPath());
    }

}
