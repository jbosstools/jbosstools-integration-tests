package org.jboss.tools.hibernate.reddeer.wizard;

/**
 * JPA implementation enum type
 * @author jpeterka
 *
 */
public enum JpaImplementation {
	
	USER_LIBRARY ("User Library"),
	DISABLE_LIBRARY_CONFIGURATION ("Disable Library Configuration");    

    private final String name;       

    private JpaImplementation(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }

}