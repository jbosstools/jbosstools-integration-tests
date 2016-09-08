package test;

import org.apache.deltaspike.security.api.authorization.Secures;

public class CustomAuthorizer {
    
    @Secures
    @CustomSecurityBinding()
    @CustomSecurityBinding2()
    public boolean check() {       
        return true;
    }
}
