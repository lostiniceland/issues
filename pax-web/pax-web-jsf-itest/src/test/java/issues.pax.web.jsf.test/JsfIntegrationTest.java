package issues.pax.web.jsf.test;


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

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.MavenUtils.*;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JsfIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    HttpTestClient httpTestClient;

    @Inject
    private BundleContext context;

    @Configuration
    public Option[] config() {
        return options(
                workingDirectory("target/paxexam"),
                cleanCaches(true),
                mavenBundle().groupId("issues").artifactId("pax-web-jsf").version("0.0.1-SNAPSHOT"), // asInProject()
                mavenBundle().groupId("issues").artifactId("pax-web-jsf-itest").version("0.0.1-SNAPSHOT"), // asInProject()
                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-service").version("1.8.1"),
                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-api").version("1.8.1"),
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-war").type("jar").classifier("uber").version("2.4.0"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-spi").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-api").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-war").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-whiteboard").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-runtime").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jsp").version("4.1.1"),
                mavenBundle().groupId("org.eclipse.jdt.core.compiler").artifactId("ecj").version("3.10.2.v20150120-1634"),
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-aether").version("2.4.0").type("jar"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-reflect").version("4.1.0"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder").version("4.1.0"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-bundleutils").version("4.1.0"),
                mavenBundle().groupId("org.ow2.asm").artifactId("asm-all").version("5.0.3"),
                mavenBundle("commons-codec", "commons-codec").version("1.1.0"),
                mavenBundle("org.apache.felix", "org.apache.felix.eventadmin").version("1.4.2"),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpcore").version("4.3.3")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpmime").version("4.3.3")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpclient").version("4.3.2")),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-api").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-spi").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jsp").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-war").version("4.1.1"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").version("4.1.1"),
                junitBundles(),
                systemProperty("org.ops4j.pax.url.mvn.proxySupport").value("true"),
                systemProperty("org.ops4j.pax.url.mvn.proxySupport").value("C:/Development/temp/maven-local-repository"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("NFO"),
                systemProperty("org.osgi.service.http.hostname").value("127.0.0.1"),
                systemProperty("org.osgi.service.http.port").value("8181"),
                systemProperty("java.protocol.handler.pkgs").value("org.ops4j.pax.url"),
                systemProperty("org.ops4j.pax.url.war.importPaxLoggingPackages").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.enabled").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.directory").value("target/logs"),
                systemProperty("org.ops4j.pax.web.jsp.scratch.dir").value("target/paxexam/scratch-dir"),
                systemProperty("ProjectVersion").value("0.0.1-SNAPSHOT"),
                systemProperty("org.ops4j.pax.url.mvn.certificateCheck").value("false"),
                frameworkProperty("osgi.console").value("6666")
        );
    }

    @Test
    public void testInstalledBundle() throws Exception{
        for(Bundle bundle : context.getBundles()){
            System.out.println(bundle.getSymbolicName());
        }

    }

    @Test
    public void testDispatchJsp() throws Exception {

        httpTestClient.testWebPath("http://127.0.0.1:8181/wab-jetty-web/index.xhtml", "It works");

    }
}
