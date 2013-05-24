package test;

import org.apache.deltaspike.security.api.authorization.Secures;

public class CustomAuthorizer {
	   
    @Secures
    @CustomSecurityBinding
    public void check() {

    }

}
