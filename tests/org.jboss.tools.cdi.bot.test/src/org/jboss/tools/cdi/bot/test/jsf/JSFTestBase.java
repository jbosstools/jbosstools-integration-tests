/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.jsf;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.CDIRefactorWizard;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectFirstPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectWizard;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardPage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLWizard;
import org.junit.Before;

public class JSFTestBase extends CDITestBase {
	
	private static final Logger LOGGER = Logger.getLogger(JSFTestBase.class.getName());
	private String env = "JSF 2.0";
	private String template = "JSFBlankWithLibs";
	protected static final String WEB_FOLDER = "pages";
	
	public String getEnv() {
		return env; 
	}
	
	public String getTemplate() {
		return template;
	}
	
	@Before
	public void checkAndCreateProject() {
		
		if (!projectHelper.projectExists(getProjectName())) {
			createJSFProjectWithCDISupport(getProjectName(), getEnv(), getTemplate());
		}
		
	}
	
	/**
	 * Method created new XHTML page with selected name
	 * @param pageName
	 */
	protected void createXHTMLPage(String pageName) {
		NewXHTMLWizard xhtmlWizard = new NewXHTMLWizard();
		xhtmlWizard.open();
		NewXHTMLFileWizardPage page = (NewXHTMLFileWizardPage)xhtmlWizard.getWizardPage(0);
		page.setParentFolder(getProjectName() + "/" 
				+ IDELabel.WebProjectsTree.WEB_CONTENT 
				+ "/" + WEB_FOLDER);
		page.setFileName(pageName);
		xhtmlWizard.finish();
	}
	
	/**
	 * Method created new XHTML page with content of resource
	 * @param pageName
	 */
	protected void createXHTMLPageWithContent(String pageName, String resource) {
		createXHTMLPage(pageName);
		editResourceUtil.replaceClassContentByResource(pageName, JSFTestBase.class.
				getResourceAsStream(resource), false);
	}
	
	
	/**
	 * Method opens context menu for CDI Refactor for selected class
	 * @param className
	 * @throws AnnotationException if no menu for CDI Refactor was found
	 */
	protected void openContextMenuForCDIRefactor(String className) throws AnnotationException {
		String text = getNamedAnnotationForClass(className);
		if (text == null) {
			throw new AnnotationException("There is no Named " +
					"annotation in class:" + className);
		}
		String renameContextMenuText = "Rename '" + 
				parseNamedAnnotation(className, text) + 
				"' Named Bean ";
		//TODO
		new TextEditor(className + ".java").selectText(text);
		new ContextMenu(IDELabel.Menu.CDI_REFACTOR,renameContextMenuText).select();
		new DefaultShell("Refactoring");	
	}
	
	/**
	 * Method returns @Named annotation or null if there is no such annotation
	 * @param className
	 * @return
	 */
	private String getNamedAnnotationForClass(String className) {
		try {
			new DefaultEditor(className+".java");
		} catch (CoreLayerException exc) {
			ProjectExplorer pe = new ProjectExplorer();
			pe.open();
			pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.JAVA_SOURCE, 
					 getPackageName(), className);
		}
		
		
		TextEditor activeEditor = new TextEditor(className+".java");
		for(int i=0;i<activeEditor.getNumberOfLines();i++){
			String line = activeEditor.getTextAtLine(i);
			if (line.contains("@Named") &&
					!line.contains("//") && !line.contains("*")) {
				return line;
			}
		}
		return null;
	}

	/**
	 * Method parses @Named annotation and returns correct EL name for class
	 * @param className
	 * @param text
	 * @return
	 */
	private String parseNamedAnnotation(String className, String text) {
		if (!text.contains("\"")) {
			return className.substring(0,1).toLowerCase() + className.substring(1);
		} else {
			return text.split("\"")[1];
		}
		
	}
/*
	protected void openContextMenuForTextInEditor(final String text, 
			final TextEditor editorTitle, final String... menu) {
		assert menu.length > 0;	
		SWTJBTExt.selectTextInSourcePane(bot, editorTitle.getTitle(), 
				text, 0, text.length());	
					
		ContextMenuHelper.clickContextMenu(editorTitle, menu);
		
	}
*/	
	/**
	 * Method changes @Named annotation to "newNamed" for selected class
	 * @param className
	 * @param newNamed
	 * @return all affected files by CDI refactoring
	 */
	protected List<String> changeNamedAnnotation(String className, String newNamed) {
		List<String> affectedFiles = new ArrayList<String>();
		try {
			openContextMenuForCDIRefactor(className);
			
			CDIRefactorWizard cdiRefactorWizard = new CDIRefactorWizard();
			cdiRefactorWizard.setName(newNamed);
			cdiRefactorWizard.next();
			affectedFiles = cdiRefactorWizard.getAffectedFiles();
			cdiRefactorWizard.finish();
		} catch (AnnotationException exc) {
			LOGGER.info("There is no named annotation in tested class");
			fail(exc.getMessage());
		} catch (SWTLayerException exc) {
			new PushButton("Close").click();
		}
		return affectedFiles;
	}
	
	/**
	 * Methods creates new JSF Project with selected name, environment and template. Finnaly
	 * it adds CDI support to this project.
	 * @param projectName
	 * @param env
	 * @param template
	 */
	private void createJSFProjectWithCDISupport(String projectName, String env, 
			String template) {
		
		createJSFProject(projectName, env, template);
		projectHelper.addCDISupport(projectName);
		
	}

	/**
	 * Methods creates new JSF Project with selected name, environment and template.
	 * @param projectName
	 * @param env
	 * @param template
	 */
	private void createJSFProject(String projectName, String env, 
			String template) {		
		JSFNewProjectWizard jw = new JSFNewProjectWizard();
		jw.open();
		JSFNewProjectFirstPage fp = (JSFNewProjectFirstPage)jw.getWizardPage(0);
		fp.setProjectName(getProjectName());
		fp.setJSFType(template);
		fp.setJSFVersion(env);
		jw.finish();
	}
				
}