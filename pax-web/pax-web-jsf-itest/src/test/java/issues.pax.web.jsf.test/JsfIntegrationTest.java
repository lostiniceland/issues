package issues.pax.web.jsf.test;


import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

import java.util.Arrays;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;



@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JsfIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //@Inject
    //HttpTestClient httpTestClient;

    @Inject
    private BundleContext context;

    @Configuration
    public Option[] config() {
        return options(
                workingDirectory("target/paxexam"),
                cleanCaches(true),
                // Framework
               // mavenBundle("org.apache.felix", "org.apache.felix.eventadmin").version("1.4.2"),
                mavenBundle("org.apache.felix", "org.apache.felix.configadmin").version("1.8.2"),
                //mavenBundle("org.apache.felix", "org.apache.felix.metatype").version("1.0.10"),
                //mavenBundle("org.apache.felix", "org.apache.felix.scr").version("1.8.2"),
                // Pax-Exam
                mavenBundle("org.ops4j.base", "ops4j-base-io").version("1.5.0"),
                mavenBundle("org.ops4j.base", "ops4j-base-lang").version("1.5.0"),
                mavenBundle("org.ops4j.base", "ops4j-base-monitors").version("1.5.0"),
                mavenBundle("org.ops4j.base", "ops4j-base-store").version("1.5.0"),
                mavenBundle("org.ops4j.base", "ops4j-base-util").version("1.5.0"),
                mavenBundle("org.ops4j.base", "ops4j-base-util-property").version("1.5.0"),
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-tracker").version("1.8.0"),
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-lifecycle").version("1.8.0"),
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-extender").version("1.8.0"),
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-core").version("1.8.0"),

                mavenBundle("org.ops4j.pax.exam", "pax-exam").version("4.4.0"),
                mavenBundle("org.ops4j.pax.exam", "pax-exam-extender-service").version("4.4.0"),
                mavenBundle("org.ops4j.pax.exam", "pax-exam-inject").version("4.4.0"),
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javax-inject").version("1_2"),

                //mavenBundle("org.ops4j.base.io", "pax-exam-inject").version("4.4.0"),

                // add SLF4J and logback bundles
                // mavenBundle("org.slf4j", "slf4j-api").versionAsInProject(),
                //mavenBundle("ch.qos.logback", "logback-core").versionAsInProject().startLevel(START_LEVEL_SYSTEM_BUNDLES ),
                //mavenBundle("ch.qos.logback", "logback-classic").versionAsInProject().startLevel( START_LEVEL_SYSTEM_BUNDLES ),
                //mavenBundle().groupId("issues").artifactId("pax-web-jsf").versionAsInProject(),
                //mavenBundle().groupId("issues").artifactId("pax-web-jsf-itest").versionAsInProject(),
                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-logback").version("1.8.2"),
                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-api").version("1.8.2"),
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-war").type("jar").classifier("uber").version("2.4.0"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-spi").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-api").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-war").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-whiteboard").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jsp").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").version("4.1.1"),
                mavenBundle().groupId("org.eclipse.jdt.core.compiler").artifactId("ecj").version("4.4"),
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-aether").version("2.4.0").type("jar"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-reflect").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-bundleutils").version("4.1"),
                mavenBundle().groupId("org.ow2.asm").artifactId("asm-all").version("5.0.3"),
                mavenBundle("commons-codec", "commons-codec").version("1.10"),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpcore").version("4.3.3")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpmime").version("4.3.6")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpclient").version("4.3.6")),
                junitBundles(), //kein hamcrest-all
                systemProperty("org.ops4j.pax.url.mvn.localRepository").value("C:/Development/temp/maven-local-repository"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("DEBUG"),
                systemProperty("org.osgi.service.http.hostname").value("127.0.0.1"),
                systemProperty("org.osgi.service.http.port").value("8181"),
                systemProperty("java.protocol.handler.pkgs").value("org.ops4j.pax.url"),
                systemProperty("org.ops4j.pax.url.war.importPaxLoggingPackages").value("false"),
                systemProperty("org.ops4j.pax.web.log.ncsa.enabled").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.directory").value("target/logs"),
                systemProperty("org.ops4j.pax.web.jsp.scratch.dir").value("target/paxexam/scratch-dir"),
                systemProperty("ProjectVersion").value("0.0.1-SNAPSHOT"),
                systemProperty("org.ops4j.pax.url.mvn.certificateCheck").value("false"),
                frameworkProperty("osgi.console").value("6666"),
                frameworkProperty("osgi.console.enable.builtin").value("true"),
                frameworkProperty("felix.bootdelegation.implicit").value("true")
        );
    }

    @After
    public void tearDownITestBase() throws Exception {
        //httpTestClient.close();
        //httpTestClient = null;
    }

    @Test
    public void testInstalledBundle() throws Exception{

        assertThat(Arrays.asList(context.getBundles()), hasItem(new CustomTypeSafeMatcher<Bundle>("pax-web-jsf Bundle") {
            @Override
            protected boolean matchesSafely(Bundle item) {
                return "pax-web-jsf".equals(item.getSymbolicName());
            }
        }));

    }

    @Test
    public void testDispatchJsp() throws Exception {
        //httpTestClient.testWebPath("http://127.0.0.1:8181/wab-jetty-web/index.xhtml", "It works");
    }
}
