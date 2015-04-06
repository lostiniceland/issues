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
import org.osgi.service.packageadmin.PackageAdmin;
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
                // MyFaces
                mavenBundle("org.apache.myfaces.core", "myfaces-api").version("2.2.7"),
                mavenBundle("org.apache.myfaces.core", "myfaces-impl").version("2.2.7"),
                mavenBundle("javax.annotation", "javax.annotation-api").version("1.2"),
                mavenBundle("javax.interceptor", "javax.interceptor-api").version("1.2"),
                mavenBundle("javax.enterprise", "cdi-api").version("1.2"),
                mavenBundle("javax.validation", "validation-api").version("1.1.0.Final"),
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javax-inject").version("1_2"),
                mavenBundle("commons-codec", "commons-codec").version("1.10"),
                mavenBundle("commons-beanutils", "commons-beanutils").version("1.8.3"),
                mavenBundle("commons-collections", "commons-collections").version("3.2.1"),
                mavenBundle("commons-digester", "commons-digester").version("1.8.1"),
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
                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-logback").version("1.8.2"),
                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-api").version("1.8.2"),
                // Pax-Web
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-spi").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-api").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-war").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jsp").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").version("4.1.1"),
                mavenBundle().groupId("org.eclipse.jdt.core.compiler").artifactId("ecj").version("4.4"),
                // Others
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-aether").version("2.4.0").type("jar"),
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-war").type("jar").classifier("uber").version("2.4.0"),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpcore").version("4.3.3")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpmime").version("4.3.6")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpclient").version("4.3.6")),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-reflect").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder-shaded").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-bundleutils").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-asm5-shaded").version("4.1"),
                mavenBundle().groupId("org.ow2.asm").artifactId("asm-all").version("5.0.3"),
                // Finally, the WAB (SUT)
                bundle("reference:file:../pax-web-jsf/target/pax-web-jsf-0.0.1-SNAPSHOT.jar"),
                junitBundles(), //kein hamcrest-all
                systemProperty("org.ops4j.pax.url.mvn.localRepository").value("~/.m2/repository"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("DEBUG"),
                systemProperty("org.osgi.service.http.hostname").value("127.0.0.1"),
                systemProperty("org.osgi.service.http.port").value("8181"),
                systemProperty("java.protocol.handler.pkgs").value("org.ops4j.pax.url"),
                systemProperty("org.ops4j.pax.url.war.importPaxLoggingPackages").value("false"),
                systemProperty("org.ops4j.pax.web.log.ncsa.enabled").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.directory").value("target/logs"),
                systemProperty("org.ops4j.pax.web.jsp.scratch.dir").value("target/paxexam/scratch-dir"),
                systemProperty("ProjectVersion").value("0.0.1-SNAPSHOT"),
                //frameworkProperty("osgi.console").value("6666"),
                //frameworkProperty("osgi.console.enable.builtin").value("true"),
                //frameworkProperty("felix.bootdelegation.implicit").value("true")
                systemProperty("org.ops4j.pax.url.mvn.certificateCheck").value("false")
        );
    }

    @After
    public void tearDownITestBase() throws Exception {
        //httpTestClient.close();
        //httpTestClient = null;
    }

    @Test
    public void testInstalledBundle() throws Exception{
        assertThat(Arrays.asList(context.getBundles()), hasItem(new CustomTypeSafeMatcher<Bundle>("issues.pax-web-jsf Bundle (active)") {
            @Override
            protected boolean matchesSafely(Bundle item) {
                return "issues.pax-web-jsf".equals(item.getSymbolicName()) && item.getState() == Bundle.ACTIVE;
            }
        }));
    }

    @Test
    public void testDispatchJsp() throws Exception {
        //httpTestClient.testWebPath("http://127.0.0.1:8181/wab-jetty-web/index.xhtml", "It works");
    }
}
