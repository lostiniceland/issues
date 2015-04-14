package issues.pax.web.test;


import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JsfIntegrationTest extends PaxWebTestBase{

    @Configuration
    public Option[] config() {
        return OptionUtils.combine(
                configureBase(),
                // MyFaces
                mavenBundle("org.apache.myfaces.core", "myfaces-api").version("2.2.7"),
                mavenBundle("org.apache.myfaces.core", "myfaces-impl").version("2.2.7"),
                mavenBundle("javax.annotation", "javax.annotation-api").version("1.2"),
                mavenBundle("javax.interceptor", "javax.interceptor-api").version("1.2"),
                mavenBundle("javax.enterprise", "cdi-api").version("1.2"),
                mavenBundle("javax.validation", "validation-api").version("1.1.0.Final"),
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javax-inject").version("1_2"),
                // mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").version(VERSION_PAX_WEB),
                // Following lines are necessary because pax-web-jetty-bundle doesnt work with jsf
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-api").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-spi").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-runtime").version(VERSION_PAX_WEB),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty").version(VERSION_PAX_WEB),
                // Jetty
                mavenBundle().groupId("javax.servlet").artifactId("javax.servlet-api").version("3.1.0"),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-util").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-io").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-http").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-continuation").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-server").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-client").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-security").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-xml").version(VERSION_JETTY),
                mavenBundle().groupId("org.eclipse.jetty").artifactId("jetty-servlet").version(VERSION_JETTY));
    }

    @Before
    public void setUp() throws Exception {
        httpTestClient = new HttpTestClient();
        initWebListener();

        String bundlePath = "reference:file:../pax-web-jsf/target/pax-web-jsf-0.0.1-SNAPSHOT.jar";
        installWarBundle = installAndStartBundle(bundlePath);

        waitForWebListener();
    }

    @Test()
    public void testJsfBundleActive() throws Exception{
        assertThat(Arrays.asList(bundleContext.getBundles()), hasItem(new CustomTypeSafeMatcher<Bundle>("pax-web-jsf Bundle (active)") {
            @Override
            protected boolean matchesSafely(Bundle item) {
                return "pax-web-jsf".equals(item.getSymbolicName()) && item.getState() == Bundle.ACTIVE;
            }
        }));
    }

    @Test
    public void testDispatchJsf() throws Exception {
        httpTestClient.testWebPath("http://127.0.0.1:8181/osgi-jsf/index.xhtml", "Hello World");
    }

}
