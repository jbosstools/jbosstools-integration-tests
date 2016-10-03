package umbiguous.authorizer;

import org.apache.deltaspike.security.api.authorization.Secures;

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
