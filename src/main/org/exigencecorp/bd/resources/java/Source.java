package org.exigencecorp.bd.resources.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.exigencecorp.bd.resources.Dir;
import org.exigencecorp.bd.resources.Files;

public class Source extends Dir {

    private final Files sources = new Files();
    private final Files libraries = new Files();
    private final Dir destination;
    private CompilerOptions compilerOptions;

    public Source(String basePath, Dir destination) {
        super(basePath);
        this.sources.add(this.files());
        this.destination = destination;
    }

    public Source lib(Lib lib) {
        this.libraries.add(lib.files());
        return this;
    }

    public Source addSource(String basePath) {
        this.sources.add(new Dir(basePath).files());
        return this;
    }

    public Source compilerOptions(CompilerOptions compilerOptions) {
        this.compilerOptions = compilerOptions;
        return this;
    }

    public void compile() {
        this.destination.make();

        List<String> options = new ArrayList<String>();
        this.addCompilerOptions(options);
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

    protected void addCompilerOptions(List<String> options) {
        if (this.compilerOptions != null) {
            options.addAll(this.compilerOptions.getCompilerOptions());
        }
    }

    protected void addFilesToOptions(List<String> options) {
        for (File file : this.sources.getFiles()) {
            if (file.getName().endsWith(".java")) {
                options.add(file.getPath());
            }
        }
    }

    protected void addClasspathToOptions(List<String> options) {
        options.add("-cp");
        options.add(this.libraries.getPathsJoined());
    }

    protected void addDestinationToOptions(List<String> options) {
        options.add("-d");
        options.add(this.destination.getPath());
    }

    public Files getSources() {
        return this.sources;
    }

}
