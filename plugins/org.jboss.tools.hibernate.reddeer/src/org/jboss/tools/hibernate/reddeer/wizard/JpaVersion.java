package org.jboss.tools.hibernate.reddeer.wizard;

public enum JpaVersion {
	JPA10 ("1.0"),
	JPA20 ("2.0"),
	JPA21 ("2.1");

    private final String name;       

    private JpaVersion(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}
