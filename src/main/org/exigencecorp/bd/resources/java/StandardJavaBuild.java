package org.exigencecorp.bd.resources.java;

import org.exigencecorp.bd.resources.Dir;

public class StandardJavaBuild {

    protected Dir bin = new Dir("bin");
    protected Dir lib = new Dir("lib");
    protected Dir src = new Dir("src");
    protected Dir tests = new Dir("tests");
    protected CompilerOptions compilerOptions = new CompilerOptions().debug().oneSix();

    public Module src(String name) {
        return new Module(name, this.bin.dir(name), this.src.dir(name), this.lib.dir(name));
    }

    public Module tests(String name) {
        return new Module(name, this.bin.dir(name), this.tests.dir(name), this.lib.dir(name));
    }

}
