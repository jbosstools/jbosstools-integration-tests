/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.uiutils;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.matcher.WithRegexMatchers;
import org.jboss.tools.ws.ui.bot.test.rest.RESTFulAnnotations;

public class RESTFullExplorer {
	
	private ProjectItem restFulExplorer;
	
	public RESTFullExplorer(String wsProjectName) {
		Project project = new ProjectExplorer().getProject(wsProjectName);
		
		/* REDDEER-369
		 * workaround for bug that will show project with no items
		 */
		project.getTreeItem().expand();
		project.getTreeItem().collapse();
		
		/* open JAX-RS / RESTful Explorer */
		restFulExplorer = project.getProjectItem(RESTFulAnnotations.REST_EXPLORER_LABEL.getLabel());
		restFulExplorer.open();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ProjectItem> getAllRestServices() {
		return restFulExplorer.getChildren();
	}
	
	public ProjectItem restService(String method) {
		for (ProjectItem ti : getAllRestServices()) {
			if (ti.getText().contains(method)) {
				return ti;
			}
		}
		return null;
	}
	
	public RunOnServerDialog runOnServer(ProjectItem service) {
		service.select();
		
		Menu menu = new ContextMenu(new WithRegexMatchers(
				".*Run.*", ".*Run on Server.*").getMatchers());
		menu.select();

		return new RunOnServerDialog();
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public List<ProjectItem> getAllInfoAboutRestService(ProjectItem restService) {
		restService.open();
		return restService.getChildren();
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getConsumesInfo(ProjectItem restService) {
		for (ProjectItem ti: getAllInfoAboutRestService(restService)) {
			if (ti.getText().contains("consumes:")) {
				return ti.getText().split(" ")[1];
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getProducesInfo(ProjectItem restService) {
		for (ProjectItem ti: getAllInfoAboutRestService(restService)) {
			if (ti.getText().contains("produces:")) {
				return ti.getText().split(" ")[1];
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getClassMethodName(ProjectItem restService) {
		for (ProjectItem ti: getAllInfoAboutRestService(restService)) {
			if (!ti.getText().contains("produces:") && !ti.getText().contains("consumes:")) {
				return ti.getText();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getRestServiceName(ProjectItem restService) {
		return restService.getText().split(" ")[0];
	}
	
	/**
	 * 
	 * @param restService
	 * @return
	 */
	public String getPathForRestFulService(ProjectItem restService) {
		return restService.getText().split(" ")[1];
	}
	
}