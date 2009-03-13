package org.exigencecorp.bd.resources.java;

import org.exigencecorp.bd.resources.Dir;

public class StandardJavaBuild {

    protected Dir base = new Dir(".");
    protected Dir bin = this.base.dir("bin");
    protected Dir lib = this.base.dir("lib");
    protected Dir src = this.base.dir("src");
    protected Dir tests = this.base.dir("tests");
    protected CompilerOptions compilerOptions = new CompilerOptions().debug().oneSix();

    public Module src(String name) {
        return new Module(name, this.bin.dir(name), this.src.dir(name), this.lib.dir(name));
    }

    public Module tests(String name) {
        return new Module(name, this.bin.dir(name), this.tests.dir(name), this.lib.dir(name));
    }

}
