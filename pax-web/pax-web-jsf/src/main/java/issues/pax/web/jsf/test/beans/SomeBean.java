package issues.pax.web.jsf.test.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@RequestScoped
@ManagedBean
public class SomeBean {

	public String getHello(){
		return "Hello World";
	}
}
