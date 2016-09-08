package test;

public class SecuredBean {

	@CustomSecurityBinding(4)
    public SecuredBean doSomething() {
        return null;
    }
   
}
