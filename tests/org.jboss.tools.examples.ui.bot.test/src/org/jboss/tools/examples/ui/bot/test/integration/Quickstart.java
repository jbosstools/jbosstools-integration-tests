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


package org.jboss.tools.examples.ui.bot.test.integration;

import java.io.File;
import java.util.ArrayList;

/**
 * Pojo representing one quickstart.
 * 
 * @author rhopp
 *
 */

public class Quickstart {

	private String name;
	private File path;
	private ArrayList<String> deployableProjects;
	
	
	public Quickstart(String name, String path) {
		this.name = name;
		this.path = new File(path);
		if (!this.path.isDirectory()){
			throw new IllegalArgumentException("Parameter path does not point to directory:"+path);
		}
		deployableProjects= new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public File getPath() {
		return path;
	}
	
	public void setPath(File path) {
		this.path = path;
	}
	
	public String toString(){
		return name;
	}
	
	public void addDeployableProjectName(String projectName){
		this.deployableProjects.add(projectName);
	}
	
	public ArrayList<String> getDeployableProjectNames(){
		return this.deployableProjects;
	}
}
