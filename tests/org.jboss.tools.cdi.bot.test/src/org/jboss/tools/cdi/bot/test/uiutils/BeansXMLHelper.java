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

package org.jboss.tools.cdi.bot.test.uiutils;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.openon.OpenOnTest;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeansXMLCreationWizard;
import org.jboss.tools.common.reddeer.label.IDELabel;

/**
 * Helper for beans.xml validation
 * 
 * @author Jaroslav Jankovic
 * 
 */

public class BeansXMLHelper {

	private static final String CLEAR_BEANS_XML = "/resources/beansXML/"
			+ "beans.xml.cdi";
	private static final String CLEAR_BEANS_XML_WITH_TAG = "/resources/beansXML/"
			+ "beansXmlWithEmptyTag.xml.cdi";
	private static final String BEANS_XML_WITH_INTERCEPTOR = "/resources/beansXML/"
			+ "beansXmlWithInterceptor.xml.cdi";
	private static final String BEANS_XML_WITH_DECORATOR = "/resources/beansXML/"
			+ "beansXmlWithDecorator.xml.cdi";
	private static final String BEANS_XML_WITH_STEREOTYPE = "/resources/beansXML/"
			+ "beansXmlWithStereotype.xml.cdi";
	private static final String BEANS_XML_WITH_ALTERNATIVE = "/resources/beansXML/"
			+ "beansXmlWithAlternative.xml.cdi";
	
	private EditorResourceHelper editResourceUtil = new EditorResourceHelper();
	
	/**
	 * Methods creates beans.xml with no tags for entered project.
	 * 
	 * @param projectName
	 */
	public void createEmptyBeansXML(String projectName) {

		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, CLEAR_BEANS_XML);

	}

	/**
	 * Methods creates beans.xml with empty tag <> for entered project.
	 * 
	 * @param projectName
	 */
	public void createBeansXMLWithEmptyTag(String projectName) {

		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, CLEAR_BEANS_XML_WITH_TAG);
	}

	/**
	 * Methods creates beans.xml with interceptor tags in it for entered
	 * project. Package and interceptor component name which should be showed in
	 * tag is determined by parameters. If className is null, then Component
	 * text will be removed
	 * 
	 * @param projectName
	 * @param packageName
	 * @param className
	 */
	public void createBeansXMLWithInterceptor(String projectName,
			String packageName, String className) {

		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, BEANS_XML_WITH_INTERCEPTOR);
		if (className == null || className.length() == 0) {
			editResourceUtil.replaceInEditor("beans.xml","<class>Component</class>",
					"<class></class>");
		} else {
			editResourceUtil.replaceInEditor("beans.xml","Component", packageName + "."
					+ className);
		}

	}

	/**
	 * Methods creates beans.xml with decorator tags in it for entered project.
	 * Package and decorator component name which should be showed in tag is
	 * determined by parameters. If className is null, then Component text will
	 * be removed
	 * 
	 * @param projectName
	 * @param packageName
	 * @param className
	 */
	public void createBeansXMLWithDecorator(String projectName,
			String packageName, String className) {

		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, BEANS_XML_WITH_DECORATOR);
		if (className == null || className.length() == 0) {
			editResourceUtil.replaceInEditor("beans.xml","<class>Component</class>",
					"<class></class>");
		} else {
			editResourceUtil.replaceInEditor("beans.xml","Component", packageName + "."
					+ className);
		}

	}

	/**
	 * Methods creates beans.xml with stereotype tags in it for entered project.
	 * Package and stereotype component name which should be showed in tag is
	 * determined by parameters. If className is null, then Component text will
	 * be removed
	 * 
	 * @param projectName
	 * @param packageName
	 * @param className
	 */
	public void createBeansXMLWithStereotype(String projectName,
			String packageName, String className) {

		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, BEANS_XML_WITH_STEREOTYPE);
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
			String packageName, String className) {
		createBeansXMLWithAlternative(projectName, packageName, className, true);
	}
	
	/**
	 * Methods creates beans.xml with alternative tags in it for entered
	 * project. Package and alternative bean component name which should be
	 * showed in tag is determined by parameters. If className is null, then
	 * Component text will be removed
	 * 
	 * @param projectName
	 * @param packageName
	 * @param className
	 */
	public void createBeansXMLWithAlternative(String projectName,
			String packageName, String className, boolean save) {

		createBeansXML(projectName);
		replaceBeansXMLContent(projectName, BEANS_XML_WITH_ALTERNATIVE);
		if (className == null || className.length() == 0) {
			editResourceUtil.replaceInEditor("beans.xml","<class>Component</class>",
					"<class></class>");
		} else {
			editResourceUtil.replaceInEditor("beans.xml","Component", packageName + "."
					+ className, save);
		}
	}

	/**
	 * If there is no beans.xml file in the project, then this method creates a
	 * new one
	 * 
	 * @param projectName
	 */
	
	private void createBeansXML(String projectName) {
		Project p = new PackageExplorer().getProject(projectName);
		
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
	
	private void replaceBeansXMLContent(String projectName, String path) {
		Project p = new PackageExplorer().getProject(projectName);
		
		if (p.containsItem(CDIConstants.WEB_INF_BEANS_XML_PATH.split("/"))) {
			p.getProjectItem(CDIConstants.WEB_INF_BEANS_XML_PATH.split("/")).open();
		} else {
			p.getProjectItem(CDIConstants.META_INF_BEANS_XML_PATH.split("/")).open();
		}
		new DefaultEditor("beans.xml");
		new DefaultCTabItem("Source").activate();
		editResourceUtil.replaceClassContentByResource("beans.xml",
				OpenOnTest.class.getResourceAsStream(path), false);
	}
	

}
