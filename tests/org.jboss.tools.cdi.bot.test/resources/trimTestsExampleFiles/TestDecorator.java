package package1;

import javax.inject.Inject;
import javax.decorator.Decorator;
import javax.decorator.Delegate;

@Decorator
public class Test implements TestInterface {
@Inject @Delegate TestInterface bean;

}
