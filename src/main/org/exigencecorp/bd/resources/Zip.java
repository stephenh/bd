package org.exigencecorp.bd.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

public class Zip extends AbstractZip<Zip> {

    public Zip(String destinationPath) {
        super(destinationPath);
    }

    @Override
    protected ZipOutputStream makeZipOutputStream(OutputStream out) throws IOException {
        return new ZipOutputStream(out);
    }

}
