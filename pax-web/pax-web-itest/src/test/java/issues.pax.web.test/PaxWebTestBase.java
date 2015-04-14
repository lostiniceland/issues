package issues.pax.web.test;

import org.junit.After;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.web.service.spi.WebListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.*;

public class PaxWebTestBase {

    protected static final String VERSION_PAX_WEB = "4.1.1";
    protected static final String VERSION_JETTY = "9.2.10.v20150310";


    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    protected BundleContext bundleContext;

    protected Bundle installWarBundle;

    protected HttpTestClient httpTestClient;
    protected WebListener webListener;


    @After
    public void tearDown() throws Exception {
        if(httpTestClient != null) {
            httpTestClient.close();
            httpTestClient = null;
        }
        if (installWarBundle != null) {
            String symbolicName = installWarBundle.getSymbolicName();
            logger.warn("Bundle '{}' will be stopped and uninstalled", symbolicName);
            installWarBundle.stop();
            installWarBundle.uninstall();
            logger.warn("Bundle '{}' successfully uninstalled", symbolicName);
        }
    }

    protected void initWebListener() {
        webListener = new WebListenerImpl();
        bundleContext.registerService(WebListener.class, webListener, null);
    }

    protected void waitForServer(final String path) throws InterruptedException {
        new WaitCondition("server") {
            @Override
            protected boolean isFulfilled() throws Exception {
                return httpTestClient.checkServer(path);
            }
        }.waitForCondition();
    }

    protected void waitForWebListener() throws InterruptedException {
        new WaitCondition("webapp startup") {
            @Override
            protected boolean isFulfilled() {
                return ((WebListenerImpl) webListener).gotEvent();
            }
        }.waitForCondition();
    }

    protected Bundle installAndStartBundle(String bundlePath)
            throws BundleException, InterruptedException {
        final Bundle bundle = bundleContext.installBundle(bundlePath);
        logger.warn("Bundle '{}' was installed and will be started now", bundle.getSymbolicName());
        bundle.start();
        new WaitCondition("bundle startup") {
            @Override
            protected boolean isFulfilled() {
                return bundle.getState() == Bundle.ACTIVE;
            }
        }.waitForCondition();
        logger.warn("Bundle '{}' successfully started", bundle.getSymbolicName());
        return bundle;
    }

    public Option[] configureBase() {
        return options(
                workingDirectory("target/paxexam"),
                cleanCaches(true),
                // Home
                //systemProperty("org.ops4j.pax.url.mvn.localRepository").value("/home/marc/.m2/repository"),
                // Work
                //systemProperty("org.ops4j.pax.url.mvn.localRepository").value("C:/Development/temp/maven-local-repository"),
                //repository("https://uenexus1.nbg.sdv.spb.de/nexus/content/groups/repo/").id("central"),

                // Framework
                mavenBundle("org.apache.felix", "org.apache.felix.eventadmin").version("1.4.2"),
                mavenBundle("org.apache.felix", "org.apache.felix.configadmin").version("1.8.2"),
                mavenBundle("org.apache.felix", "org.apache.felix.scr").version("1.8.2"),
                mavenBundle("org.apache.felix", "org.apache.felix.metatype").version("1.0.10"),
                // Commons
                mavenBundle("commons-io", "commons-io").version("1.4"),
                mavenBundle("commons-codec", "commons-codec").version("1.10"),
                mavenBundle("commons-beanutils", "commons-beanutils").version("1.8.3"),
                mavenBundle("commons-collections", "commons-collections").version("3.2.1"),
                mavenBundle("commons-digester", "commons-digester").version("1.8.1"),
                // Pax-Web
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-war").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jsp").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.eclipse.jdt.core.compiler").artifactId("ecj").version("4.4.2"),
                // Others
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-aether").version("2.4.0").type("jar"),
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-war").type("jar").classifier("uber").version("2.4.0"),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpcore").version("4.3.3")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpmime").version("4.4.1")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpclient").version("4.3.2")),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-reflect").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder-shaded").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-bundleutils").version("4.1"),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-asm5-shaded").version("4.1"),
                mavenBundle().groupId("org.ow2.asm").artifactId("asm-all").version("5.0.3"),
                junitBundles(),
                systemProperty("org.osgi.service.http.hostname").value("127.0.0.1"),
                systemProperty("org.ops4j.pax.web.listening.addresses").value("127.0.0.1"),
                systemProperty("org.osgi.service.http.port").value("8181"),
                systemProperty("java.protocol.handler.pkgs").value("org.ops4j.pax.url"),
                systemProperty("org.ops4j.pax.url.war.importPaxLoggingPackages").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.enabled").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.directory").value("target/logs"),
                systemProperty("org.ops4j.pax.web.jsp.scratch.dir").value("target/paxexam/scratch-dir"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO"),
                frameworkProperty("osgi.console").value("6666"),
                frameworkProperty("osgi.console.enable.builtin").value("true"),
                systemProperty("org.ops4j.pax.url.mvn.certificateCheck").value("false"),
                frameworkProperty("felix.bootdelegation.implicit").value("false")

//                // needed when runnin in exam-default mode (exam.properties)
//                ,
//                // Pax-Exam
//                mavenBundle("org.ops4j.base", "ops4j-base-io").version("1.5.0"),
//                mavenBundle("org.ops4j.base", "ops4j-base-lang").version("1.5.0"),
//                mavenBundle("org.ops4j.base", "ops4j-base-monitors").version("1.5.0"),
//                mavenBundle("org.ops4j.base", "ops4j-base-store").version("1.5.0"),
//                mavenBundle("org.ops4j.base", "ops4j-base-util").version("1.5.0"),
//                mavenBundle("org.ops4j.base", "ops4j-base-util-property").version("1.5.0"),
//                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-tracker").version("1.8.0"),
//                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-lifecycle").version("1.8.0"),
//                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-extender").version("1.8.0"),
//                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-core").version("1.8.0"),
//                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-optional-jcl").version("1.8.0"),
//                mavenBundle("org.ops4j.pax.exam", "pax-exam").version("4.4.0"),
//                mavenBundle("org.ops4j.pax.exam", "pax-exam-extender-service").version("4.4.0"),
//                mavenBundle("org.ops4j.pax.exam", "pax-exam-inject").version("4.4.0"),
                // Pax-Logging
//                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-api").version("1.8.2"),
//                mavenBundle().groupId("org.ops4j.pax.logging").artifactId("pax-logging-service").version("1.8.2"),
        );
    }
}
