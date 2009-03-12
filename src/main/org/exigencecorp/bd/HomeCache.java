package org.exigencecorp.bd;

import java.io.File;

import org.exigencecorp.bd.util.FileCopy;

/**
 * Caches projects jars in <code>~/.bd</code> for automatic copying around.
 *
 * After building libraries with local changes, they are pushed out to the home cache.
 *
 * Any bd-enabled project that then uses the library will check for new, in-development
 * versions of the libraries.
 *
 * This works when no version numbers are on the jars--or, god forbid,
 * <code>SNAPSHOT</code> version. If an app has <code>library-1.0.jar</code>
 * and <code>library.jar</code> is pushed to the home cache, it will not be
 * automatically updated.
 *
 * Note this is not maven--all of this is only for local devs that are actively
 * changing libraries that apps depends on. Libraries should be checked in SCM
 * and the whole <code>~/.bd</code> won't mean a thing to non-library developers.
 */
public class HomeCache {

    public static void publish(File file) {
        FileCopy.copy(file, new File(HomeCache.get(), file.getName()));
    }

    public static void updateIfNeeded(File file) {
        File cacheFile = new File(HomeCache.get(), file.getName());
        if (cacheFile.exists() && cacheFile.lastModified() > file.lastModified()) {
            System.out.println("Found new " + file.getName());
            FileCopy.copy(cacheFile, file);
        }
    }

    public static File get() {
        File dotbd = new File(new File(System.getProperty("user.home")), ".bd");
        dotbd.mkdirs();
        return dotbd;
    }

}
