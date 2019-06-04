package package1;

import javax.inject.Inject;
import javax.interceptor.Interceptors;

public class Test {
@Inject TestBean bean;

@Interceptors(TestInterceptor.class)
public void testBeanMethod() {}

}
