package aries.subsystem.feature;

import org.osgi.service.component.annotations.Component;

import aries.subsystem.feature.api.Service;

@Component
public class ServiceProvider implements Service {

	public void sayHello() {
		System.out.println("Hello World");
	}

}
