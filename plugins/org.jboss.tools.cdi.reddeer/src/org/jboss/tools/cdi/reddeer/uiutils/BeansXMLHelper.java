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

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;

/**
 * Helper for beans.xml validation
 * 
 * @author Jaroslav Jankovic
 * 
 */

public class BeansXMLHelper {
	
	public EditorPartWrapper openBeansXml(String project){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(project).getProjectItem("src","main","webapp","WEB-INF","beans.xml").open();
		return new EditorPartWrapper();
	}
	
	public void removeFromBeans(String tag){
		EditorPartWrapper ep = new EditorPartWrapper();
		ep.activateSourcePage();
		DefaultStyledText dt = new DefaultStyledText();
		int start = dt.getPositionOfText("<"+tag+">");
		int end = dt.getPositionOfText("</"+tag+">");
		dt.setSelection(start, end+tag.length()+3);
		dt.insertText("");
		ep.save();
	}
	
	
/*
	private EditorResourceHelper editResourceUtil = new EditorResourceHelper();

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
	*/

}
