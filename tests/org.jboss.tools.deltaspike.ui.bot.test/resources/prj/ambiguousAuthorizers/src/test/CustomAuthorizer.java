package test;

import org.apache.deltaspike.security.api.authorization.annotation.Secures;

public class CustomAuthorizer {
	   
    @Secures
    @CustomSecurityBinding(4)
    public boolean check() {       
        return true;
    }

    @Secures
    @CustomSecurityBinding(4)
    public boolean check1() {       
        return true;
    }
    
    @Secures
    @CustomSecurityBinding(1)
    public boolean check3() {       
        return true;
    }

}
