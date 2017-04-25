/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/
package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

/**
 * Abstract run configuration. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class RunConfiguration {

	private String category;
	
	private String name;
	
	public RunConfiguration(String category, String name) {
		checkArguments(category, name);
		this.category = category;
		this.name = name;
	}

	public String getCategory() {
		return category;
	}
	
	public String getName() {
		return name;
	}
	
	private void checkArguments(String category, String name) {
		if (category == null){
			throw new IllegalArgumentException("Category must be specified");
		}
		
		if (name == null){
			throw new IllegalArgumentException("Name must be specified");
		}
	}
}
