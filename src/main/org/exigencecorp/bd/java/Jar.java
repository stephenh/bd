package org.exigencecorp.bd.java;

import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipOutputStream;

import org.exigencecorp.bd.AbstractZip;

public class Jar extends AbstractZip<Jar> {

    public Jar(String destinationPath) {
        super(destinationPath);
    }

    @Override
    protected ZipOutputStream makeZipOutputStream(OutputStream out) throws IOException {
        return new JarOutputStream(out);
    }

}
