package org.exigencecorp.bd.resources.java;

import org.exigencecorp.bd.resources.Dir;

public class StandardJavaBuild {

    public Dir bin = new Dir("bin");
    public Dir lib = new Dir("lib");
    public Dir src = new Dir("src");
    public Dir tests = new Dir("tests");
    public CompilerOptions compilerOptions = new CompilerOptions().debug().oneSix();

    public Module source(String name) {
        return new Module(name, this.bin.dir(name), this.src.dir(name), this.lib.dir(name));
    }

    public Module tests(String name) {
        return new Module(name, this.bin.dir(name), this.tests.dir(name), this.lib.dir(name));
    }

}
