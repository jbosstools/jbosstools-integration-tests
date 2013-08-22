package org.apodhrad.example.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class HelloBean {

	public String sayHello(String name) {
		return "EJB: Hello " + name;
	}

}
