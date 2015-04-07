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

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class JspIntegrationTest extends PaxWebTestBase {



    @Configuration
    public Option[] config() {
        return configureBase();
    }

    @Before
    public void setUp() throws Exception {
        httpTestClient = new HttpTestClient();
        initWebListener();

        String bundlePath = "file:../pax-web-jsp/target/pax-web-jsp-0.0.1-SNAPSHOT.jar";
        installWarBundle = installAndStartBundle(bundlePath);

        //waitForServer("http://127.0.0.1:8181/");
        //waitForWebListener();
    }


    @Test
    public void testInstalledBundle() throws Exception{
        assertThat(Arrays.asList(bundleContext.getBundles()), hasItem(new CustomTypeSafeMatcher<Bundle>("issues.pax-web-jsf Bundle (active)") {
            @Override
            protected boolean matchesSafely(Bundle item) {
                return "issues.pax-web-jsp".equals(item.getSymbolicName()) && item.getState() == Bundle.ACTIVE;
            }
        }));
        logger.warn("testInstalledBundle finished");
    }


    @Test
    public void testDispatchJsp() throws Exception {
        httpTestClient.testWebPath("http://127.0.0.1:8181/osgi-jsp/index.jsp", "It works");
        logger.warn("testDispatchJsp finished");
    }

}
