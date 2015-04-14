package issues.pax.web.test;


import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.Before;
import org.junit.Ignore;
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
                mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.javax-inject").version("1_2"));
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
    public void testInstalledBundle() throws Exception{
        assertThat(Arrays.asList(bundleContext.getBundles()), hasItem(new CustomTypeSafeMatcher<Bundle>("pax-web-jsf Bundle (active)") {
            @Override
            protected boolean matchesSafely(Bundle item) {
                return "pax-web-jsf".equals(item.getSymbolicName()) && item.getState() == Bundle.ACTIVE;
            }
        }));
    }

    @Test
    public void testDispatchJsf() throws Exception {
        httpTestClient.testWebPath("http://127.0.0.1:8181/osgi-jsf/index.xhtml", "It works");
    }

}
