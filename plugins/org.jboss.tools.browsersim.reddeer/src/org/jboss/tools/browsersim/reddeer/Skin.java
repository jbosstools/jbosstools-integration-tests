/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.reddeer;

public enum Skin {
	
	DESKTOP ("Desktop (Default User-Agent)"),
	IPAD4 ("Apple iPad 4 Retina"),
	IPAD_MINI ("Apple iPad mini"),
	IPHONE6_PLUS("Apple iPhone 6+"),
	IPHONE6 ("Apple iPhone 6"),
	IPHONE5 ("Apple iPhone 5"),
	IPHONE4 ("Apple iPhone 4"),
	HTC_ONE ("HTC One (M8)"),
	GALAXY4 ("Samsung Galaxy S IV"),
	GALAXY3 ("Samsung Galaxy S III"),
	NEXUS ("Samsung Galaxy Nexus"),
	NOTE3 ("Samsung Galaxy Note III"),
	NOTE2 ("Samsung Galaxy Note II"),
	TAB101 ("Samsung Galaxy Tab 10.1");
	
	private String name;
	
	Skin(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	

}
