package org.jboss.tools.hibernate.reddeer.wizard;

public enum JpaPlatform  {
	
	GENERIC ("Generic 2.1"),
	HIBERNATE10 ("Hibernate (JPA 1.x)"),
	HIBERNATE20 ("Hibernate (JPA 2.0)"),
	HIBERNATE21 ("Hibernate (JPA 2.1)");    

    private final String name;       

    private JpaPlatform(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }

}