package org.jboss.tools.hibernate.reddeer.wizard;

/**
 * Hibernate console type enum
 * @author jpeterka
 *
 */
public enum HibernateConsoleType {
	CORE ("Core"),
	ANNOTATION ("Annotations (jdk 1.5+)"),
	JPA ("JPA (jdk 1.5+)");
	

    private final String name;       

    private HibernateConsoleType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}
