package org.exigencecorp.bd.java;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipOutputStream;

import org.exigencecorp.bd.AbstractZip;
import org.exigencecorp.bd.Files;

public class War extends AbstractZip<War> {

    public War(String destinationPath) {
        super(destinationPath);
    }

    @Override
    protected ZipOutputStream makeZipOutputStream(OutputStream out) throws IOException {
        return new JarOutputStream(out);
    }

    public War classes(File destination) {
        return super.includes("WEB-INF/classes", destination);
    }

    public War classes(Files destination) {
        return super.includes("WEB-INF/classes", destination);
    }

    public War lib(File lib) {
        return super.includes("WEB-INF/lib", lib);
    }

    public War lib(Files lib) {
        return super.includes("WEB-INF/lib", lib);
    }

}
