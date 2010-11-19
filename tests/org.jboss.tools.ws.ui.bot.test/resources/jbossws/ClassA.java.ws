package {0};

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService()
public class {1} '{'

	@WebMethod()
	public String method() '{'
	    System.out.println({1}.class.getName() + " Service: method() was called");
	    return "{0}.{1}";
	'}'
'}'
