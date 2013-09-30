package org.jbpm.bpmn2.objects;

/**
 * Class used in editor tests.
 * 
 * @author mbaluch
 */
public class Person {

	private String name;
	
	public Person() {
	}
	
	public Person(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
