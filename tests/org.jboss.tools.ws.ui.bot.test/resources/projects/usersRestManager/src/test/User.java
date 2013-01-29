package test;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

	private int id;
	
	private String name;
	
	private BigInteger phoneNumber;

	public User() {
		super();
	}
	
	public User(int id, String name, BigInteger phoneNumber) {
		this();
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(BigInteger phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
		
}
