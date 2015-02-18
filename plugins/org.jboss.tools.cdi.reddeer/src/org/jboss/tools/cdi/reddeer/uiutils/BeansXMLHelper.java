/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.reddeer.uiutils;

import java.io.InputStream;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeansXMLCreationWizard;
import org.jboss.tools.common.reddeer.label.IDELabel;

/**
 * Helper for beans.xml validation
 * 
 * @author Jaroslav Jankovic
 * 
 */

public class BeansXMLHelper {

	private EditorResourceHelper editResourceUtil = new EditorResourceHelper();
	/*
	
	public void createEmptyBeansXML(String projectName) {

		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, CLEAR_BEANS_XML);

	}

	public void createBeansXMLWithEmptyTag(String projectName) {

		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, CLEAR_BEANS_XML_WITH_TAG);
	}
*/
	public void createBeansXMLWithInterceptor(String projectName,
			String packageName, String className, InputStream path) {

		createBeansXMLWithContent(projectName, path);
		if (className == null || className.length() == 0) {
			editResourceUtil.replaceInEditor("beans.xml","<class>Component</class>",
					"<class></class>");
		} else {
			editResourceUtil.replaceInEditor("beans.xml","Component", packageName + "."
					+ className);
		}

	}

	public void createBeansXMLWithDecorator(String projectName,
			String packageName, String className, InputStream path) {

		createBeansXMLWithContent(projectName, path);
		if (className == null || className.length() == 0) {
			editResourceUtil.replaceInEditor("beans.xml","<class>Component</class>",
					"<class></class>");
		} else {
			editResourceUtil.replaceInEditor("beans.xml","Component", packageName + "."
					+ className);
		}

	}


	public void createBeansXMLWithStereotype(String projectName,
			String packageName, String className, InputStream path) {

		createBeansXMLWithContent(projectName, path);
		if (className == null || className.length() == 0) {
			editResourceUtil.replaceInEditor("beans.xml",
					"<stereotype>Component</stereotype>",
					"<stereotype></stereotype>");
		} else {
			editResourceUtil.replaceInEditor("beans.xml","Component", packageName + "."
					+ className);
		}

	}

	public void createBeansXMLWithAlternative(String projectName,
			String packageName, String className, InputStream path) {
		createBeansXMLWithAlternative(projectName, packageName, className, true, path);
	}
	

	public void createBeansXMLWithAlternative(String projectName,
			String packageName, String className, boolean save, InputStream path) {

		createBeansXMLWithContent(projectName, path);
		if (className == null || className.length() == 0) {
			editResourceUtil.replaceInEditor("beans.xml","<class>Component</class>",
					"<class></class>");
		} else {
			editResourceUtil.replaceInEditor("beans.xml","Component", packageName + "."
					+ className, save);
		}
	}

	public void createBeansXMLWithContent(String projectName, InputStream path) {
		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, path);
	}
	
	
	/**
	 * If there is no beans.xml file in the project, then this method creates a
	 * new one
	 * 
	 * @param projectName
	 */
	
	private void createBeansXML(String projectName) {
		Project p = new ProjectExplorer().getProject(projectName);
		
		if (!p.containsItem(
				CDIConstants.META_INF_BEANS_XML_PATH.split("/"))
				&& !p.containsItem(
						CDIConstants.WEB_INF_BEANS_XML_PATH.split("/"))) {
			
			NewBeansXMLCreationWizard xw = new NewBeansXMLCreationWizard();
			xw.open();
			xw.setSourceFolder(projectName,IDELabel.WebProjectsTree.WEB_CONTENT,
					IDELabel.WebProjectsTree.WEB_INF);
			xw.finish();
		}
	}

	/**
	 * Methods create beans.xml for entered project with content of file
	 * determined by parameter path. If there is beans.xml in project, its
	 * content is simply replaced
	 * 
	 * @param projectName
	 * @param path
	 */
	
	private void replaceBeansXMLContent(String projectName, InputStream path) {
		Project p = new ProjectExplorer().getProject(projectName);
		
		if (p.containsItem(CDIConstants.WEB_INF_BEANS_XML_PATH.split("/"))) {
			p.getProjectItem(CDIConstants.WEB_INF_BEANS_XML_PATH.split("/")).open();
		} else {
			p.getProjectItem(CDIConstants.META_INF_BEANS_XML_PATH.split("/")).open();
		}
		new DefaultEditor("beans.xml");
		new DefaultCTabItem("Source").activate();
		editResourceUtil.replaceClassContentByResource("beans.xml",
				path, false);
	}
	

}
