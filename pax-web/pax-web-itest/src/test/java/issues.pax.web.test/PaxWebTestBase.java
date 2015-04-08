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
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

public class PaxWebTestBase {

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
            installWarBundle.stop();
            installWarBundle.uninstall();
            logger.warn("Bundle '{}' uninstalled", symbolicName);
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
        bundle.start();
        new WaitCondition("bundle startup") {
            @Override
            protected boolean isFulfilled() {
                return bundle.getState() == Bundle.ACTIVE;
            }
        }.waitForCondition();
        logger.warn("Install-Bundle '{}' started", bundle.getSymbolicName());
        return bundle;
    }

    public Option[] configureBase() {
        return options(
                workingDirectory("target/paxexam"),
                cleanCaches(true),

                // Framework
                mavenBundle("org.apache.felix", "org.apache.felix.eventadmin").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.configadmin").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.scr").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.metatype").versionAsInProject(),
                // MyFaces
                mavenBundle("org.apache.myfaces.core", "myfaces-api").versionAsInProject(),
                mavenBundle("org.apache.myfaces.core", "myfaces-impl").versionAsInProject(),
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
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-tracker").versionAsInProject(),
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-lifecycle").versionAsInProject(),
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-extender").versionAsInProject(),
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-core").versionAsInProject(),
                mavenBundle("org.ops4j.pax.swissbox", "pax-swissbox-optional-jcl").versionAsInProject(),
                mavenBundle("org.ops4j.pax.exam", "pax-exam").version("4.4.0"),
                mavenBundle("org.ops4j.pax.exam", "pax-exam-extender-service").version("4.4.0"),
                mavenBundle("org.ops4j.pax.exam", "pax-exam-inject").version("4.4.0"),
                // Pax-Web
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-spi").versionAsInProject(),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-api").versionAsInProject(),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-runtime").versionAsInProject(),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-war").versionAsInProject(),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-extender-whiteboard").versionAsInProject(),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jsp").versionAsInProject(),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").versionAsInProject(),
                mavenBundle().groupId("org.eclipse.jdt.core.compiler").artifactId("ecj").versionAsInProject(),
                // Others
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-aether").version("2.4.0").type("jar"),
                mavenBundle().groupId("org.ops4j.pax.url").artifactId("pax-url-war").type("jar").classifier("uber").version("2.4.0"),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpcore").version("4.3.3")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpmime").version("4.3.6")),
                wrappedBundle(mavenBundle("org.apache.httpcomponents", "httpclient").version("4.3.6")),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-reflect").versionAsInProject(),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder").versionAsInProject(),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-finder-shaded").versionAsInProject(),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-bundleutils").versionAsInProject(),
                mavenBundle().groupId("org.apache.xbean").artifactId("xbean-asm5-shaded").versionAsInProject(),
                mavenBundle().groupId("org.ow2.asm").artifactId("asm-all").version("5.0.3"),
                junitBundles(), //kein hamcrest-all
                //systemProperty("org.ops4j.pax.url.mvn.localRepository").value("~/.m2/repository"),
                systemProperty("org.osgi.service.http.hostname").value("127.0.0.1"),
                systemProperty("org.ops4j.pax.web.listening.addresses").value("localhost,127.0.0.1"),
                systemProperty("org.osgi.service.http.port").value("8181"),
                systemProperty("java.protocol.handler.pkgs").value("org.ops4j.pax.url"),
                systemProperty("org.ops4j.pax.url.war.importPaxLoggingPackages").value("true"),
                systemProperty("org.ops4j.pax.web.log.ncsa.enabled").value("true"),
                //systemProperty("org.ops4j.pax.web.log.ncsa.directory").value("target/logs"),
                systemProperty("org.ops4j.pax.web.jsp.scratch.dir").value("target/paxexam/scratch-dir"),
                systemProperty("ProjectVersion").value("0.0.1-SNAPSHOT"),
                //frameworkProperty("osgi.console").value("6666"),
                //frameworkProperty("osgi.console.enable.builtin").value("true"),
                frameworkProperty("felix.bootdelegation.implicit").value("true"),
//                frameworkProperty(
//                        "org.osgi.framework.system.packages.extra")
//                        .value("org.ops4j.pax.exam;version=4.4.0,org.ops4j.pax.exam.options;version=4.4.0,org.ops4j.pax.exam.util;version=4.4.0,org.w3c.dom.traversal"),
                systemProperty("org.ops4j.pax.url.mvn.certificateCheck").value("false")
        );
    }
}
