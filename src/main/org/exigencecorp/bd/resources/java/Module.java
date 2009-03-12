package org.exigencecorp.bd.resources.java;

import org.exigencecorp.bd.resources.Dir;

public class Module {

    public final String name;
    public final Dir bin;
    public final Dir lib;
    public final Source src;

    public Module(String name, Dir bin, Dir src, Dir lib) {
        this.name = name;
        this.bin = bin;
        if (lib != null && lib.getFile().exists()) {
            this.lib = lib;
        } else {
            this.lib = null;
        }
        if (src != null && src.getFile().exists()) {
            this.src = new Source(src.getPath(), this.bin);
            if (this.lib != null) {
                this.src.lib(this.lib);
            }
        } else {
            this.src = null;
        }
    }

    public void compile() {
        this.src.compile();
    }

    public Module dep(Module other) {
        this.src.lib(other.src);
        return this;
    }

    public Module dep(Dir other) {
        this.src.lib(other);
        return this;
    }

}
