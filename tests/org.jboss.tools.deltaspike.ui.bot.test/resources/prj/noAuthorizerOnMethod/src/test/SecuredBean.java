package test;

public class SecuredBean {

	@CustomSecurityBinding(1)
    public SecuredBean doSomething() {
        return null;
    }
    
}
