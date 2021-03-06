package issues.pax.web.test;


import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.OptionUtils.combine;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JspIntegrationTest extends PaxWebTestBase {



    @Configuration
    public Option[] config() {
        return combine(configureBase(),
                // Web
                mavenBundle("javax.el", "javax.el-api").version("2.2.5"),
                mavenBundle().groupId("org.ops4j.pax.web").artifactId("pax-web-jetty-bundle").version(VERSION_PAX_WEB));
    }

    @Before
    public void setUp() throws Exception {
        httpTestClient = new HttpTestClient();
        initWebListener();

        String bundlePath = "reference:file:../pax-web-jsp/target/pax-web-jsp-0.0.1-SNAPSHOT.jar";
        installWarBundle = installAndStartBundle(bundlePath);

        waitForWebListener();
    }


    @Test
    public void testJspBundleActive() throws Exception{
        assertThat(Arrays.asList(bundleContext.getBundles()), hasItem(new CustomTypeSafeMatcher<Bundle>("pax-web-jsf Bundle (active)") {
            @Override
            protected boolean matchesSafely(Bundle item) {
                return "pax-web-jsp".equals(item.getSymbolicName()) && item.getState() == Bundle.ACTIVE;
            }
        }));
    }


    @Test
    public void testDispatchJsp() throws Exception {
        httpTestClient.testWebPath("http://127.0.0.1:8181/osgi-jsp/index.jsp", "Hello World");
    }

}
