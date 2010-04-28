package jbossws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService()
public class ClassA {

	@WebMethod()
	public int method() {
	    System.out.println("JbossWS Service : method() was called");
	    return 1234567890;
	}
}
