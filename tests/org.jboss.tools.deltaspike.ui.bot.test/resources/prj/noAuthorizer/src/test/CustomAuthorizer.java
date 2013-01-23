package test;

import org.apache.deltaspike.security.api.authorization.annotation.Secures;

public class CustomAuthorizer {
	   
    @Secures
    @CustomSecurityBinding(4)
    public boolean check() {       
        return true;
    }

}
