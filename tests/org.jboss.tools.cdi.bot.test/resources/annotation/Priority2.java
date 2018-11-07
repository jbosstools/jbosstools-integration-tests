package test;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

@Alternative
@Priority(Interceptor.Priority.APPLICATION+2)
public class Priority2 implements PriorityTestInterface {

}
