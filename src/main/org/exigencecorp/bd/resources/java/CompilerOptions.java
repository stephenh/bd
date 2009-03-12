package org.exigencecorp.bd.resources.java;

import java.util.ArrayList;
import java.util.List;

public class CompilerOptions {

    private final List<String> compilerOptions = new ArrayList<String>();

    public CompilerOptions debug() {
        this.compilerOptions.add("-g");
        return this;
    }

    public CompilerOptions oneSix() {
        this.add("-source", "1.6", "-target", "1.6");
        return this;
    }

    public CompilerOptions processor(String className, String path) {
        this.add("-processor", className, "-processorpath", path);
        return this;
    }

    public CompilerOptions processor() {
        this.add("-XprintRounds", "-XprintProcessorInfo");
        return this;
    }

    public CompilerOptions add(String... options) {
        for (String option : options) {
            this.compilerOptions.add(option);
        }
        return this;
    }

    public List<String> getCompilerOptions() {
        return this.compilerOptions;
    }

}
