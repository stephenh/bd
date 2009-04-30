import joist.domain.codegen.JoistTask;

import org.exigencecorp.bd.HomeCache;
import org.exigencecorp.bd.resources.Dir;
import org.exigencecorp.bd.resources.java.Module;
import org.exigencecorp.bd.resources.java.Source;
import org.exigencecorp.bd.resources.java.StandardJavaBuild;
import org.exigencecorp.bd.resources.java.Tests;
import org.exigencecorp.bd.resources.java.War;

public class Build extends StandardJavaBuild {

    static {
        Source.defaultIncludeHomeCache = true;
    }

    public JoistTask joist = new JoistTask("log");
    public Dir testsLib = this.lib.dir("tests");
    public Module bd = this.src("bd");
    public Module main = this.src("main");
    public Module jetty = this.src("jetty");
    public Module unit = this.tests("unit").dep(this.main).dep(this.testsLib);
    public Module integration = this.tests("integration").dep(this.unit);
    public Module web = this.tests("web").dep(this.integration).dep(this.jetty);

    public Dir content = new Dir("content");
    public War war = new War("bin/log.war").classes(this.main.bin).lib(this.main.lib).includes(this.content);

    public Build() {
        this.compilerOptions.processor("org.exigencecorp.bindgen.processor.BindgenAnnotationProcessor", "lib/main/bindgen.jar");
        this.main.src.addSource("src/codegen");
        this.main.src.compilerOptions(this.compilerOptions);
    }

    public void clean() {
        this.bin.delete();
    }

    public void compile() {
        this.main.compile();
    }

    public void war() {
        this.compile();
        this.war.create();
    }

    public void tests() throws InterruptedException {
        this.unit.compile();
        this.integration.compile();
        new Tests(this.unit.src).run();
        new Tests(this.integration.src).run();
    }

    public void updateLibs() {
        HomeCache.updateIfNeeded(this.main.lib.files());
        HomeCache.updateIfNeeded(this.bd.lib.files());
    }

}
