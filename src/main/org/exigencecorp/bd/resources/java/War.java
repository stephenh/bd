package org.exigencecorp.bd.resources.java;

import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipOutputStream;

import org.exigencecorp.bd.resources.AbstractZip;
import org.exigencecorp.bd.resources.Dir;
import org.exigencecorp.bd.resources.Files;

public class War extends AbstractZip<War> {

    public War(String destinationPath) {
        super(destinationPath);
    }

    @Override
    protected ZipOutputStream makeZipOutputStream(OutputStream out) throws IOException {
        return new JarOutputStream(out);
    }

    public War classes(Dir destination) {
        return super.includes("WEB-INF/classes/", destination.files());
    }

    public War lib(Dir lib) {
        return super.includesFlat("WEB-INF/lib/", lib.files());
    }

    public War lib(Files lib) {
        return super.includesFlat("WEB-INF/lib/", lib);
    }

}
