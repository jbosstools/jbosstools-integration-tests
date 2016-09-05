/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.freemarker.testprj.model;

import java.util.Date;

public class User {

	private String firstName;
	private String surname;
	private String fullName;
	private int age;
	private boolean male;
	private Date birthDate;
	
	public User() {}
	
	public User(String first, String second, int age, boolean isMale, Date birth) {
		firstName = first;
		surname = second;
		this.age = age;
		male = isMale;
		birthDate = birth;
		fullName = first + " " + second;
	}

	public Date getBirthDate() {
		return birthDate;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public int getAge() {
		return age;
	}
	
	public boolean isMale() {
		return male;
	}
	
	
	
}
