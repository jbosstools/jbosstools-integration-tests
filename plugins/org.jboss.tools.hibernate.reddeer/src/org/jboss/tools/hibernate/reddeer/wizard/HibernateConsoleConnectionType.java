package org.jboss.tools.hibernate.reddeer.wizard;

/**
 * Hibernate console connection type enum
 * @author jpeterka
 *
 */
public enum HibernateConsoleConnectionType {
	JPA ("[JPA Project Configured Connection]"),
	HIBERNATE ("[Hibernate configured connection]");
	

    private final String name;       

    private HibernateConsoleConnectionType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}
