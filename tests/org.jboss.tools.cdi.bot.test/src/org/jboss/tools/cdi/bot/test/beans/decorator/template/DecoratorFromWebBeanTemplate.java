/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beans.decorator.template;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewDecoratorCreationWizard;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test operates on creating new decorator from web bean 
 * implementing some existing interface
 * 
 * @author Jaroslav Jankovic
 * 
 */
public abstract class DecoratorFromWebBeanTemplate extends CDITestBase {
	
	private static final String ACCOUNT = "Account";
	
	private static final String ACCOUNT_JAVA = ACCOUNT + ".java";
	
	private static final String ACCOUNT_DECORATOR = "AccountDecorator";
	
	private static final String ACCOUNT_DECORATOR_JAVA = ACCOUNT_DECORATOR + ".java";
	
	@Before
	public void addClassesToProject(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("Java Resources","src").select();
		createPageWithContent("Account","resources/classes/Account.java");
		createPageWithContent("User","resources/classes/User.java");
	}
	
	private void createPageWithContent(String name, String contentPath){
		NewClassCreationWizard cd = new NewClassCreationWizard();
		cd.open();
		NewClassWizardPage jp = new NewClassWizardPage(cd);
		jp.setName(name);
		jp.setPackage("cdi");
		cd.finish();
		new EditorResourceHelper().replaceClassContentByResource(name+".java", readFile(contentPath), true, true);
	}

	@After
	public void deleteProjects() {
		cleanUp();
	}
	
	@Test
	public void testCreatingDecoratorWithMenu() {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES,CDIConstants.SRC,getPackageName(),ACCOUNT_JAVA).select();
		
		
		NewDecoratorCreationWizard dw = new NewDecoratorCreationWizard();
		dw.open();
		
		assertTrue(dw.getName().equals(ACCOUNT_DECORATOR));
		
		assertTrue(dw.getDecoratedTypeInterfaces().size() == 1);
		
		assertTrue(dw.getDecoratedTypeInterfaces().
				get(0).getText().equals(getPackageName() + "." + ACCOUNT));
		
		assertTrue(new PushButton("Finish").isEnabled());
		
		dw.finish();
		
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.WEB_INF_BEANS_XML_PATH.split("/")).open();
		
		EditorPartWrapper ew = new EditorPartWrapper();
		ew.activateSourcePage();
		
		assertTrue(new DefaultStyledText().getText().contains(System.getProperty("line.separator")+
				" <decorators>"+System.getProperty("line.separator")+"  " +
				"<class>cdi.AccountDecorator</class>"+System.getProperty("line.separator")+" </decorators>"));
		
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES,CDIConstants.SRC, 
				getPackageName(), ACCOUNT_DECORATOR_JAVA).open();
		
		TextEditor activeEditor = new TextEditor(ACCOUNT_DECORATOR_JAVA);
		
		assertTrue(activeEditor.getText().contains("@Decorator"));
		assertTrue(activeEditor.getText().contains("@Inject"+System.getProperty("line.separator")+"\t@Delegate"+
				System.getProperty("line.separator")+"\t@Any" +
				System.getProperty("line.separator")+"\tprivate Account account;"));
		assertTrue(activeEditor.getText().contains("BigDecimal getBalance()"));
		assertTrue(activeEditor.getText().contains("User getOwner()"));
		assertTrue(activeEditor.getText().contains("void withdraw(BigDecimal amount)"));
		assertTrue(activeEditor.getText().contains("void deposit(BigDecimal amount)"));
		
		
	}
	
	@Test
	public void testCreatingDecoratorWithWizard() {
		
		NewDecoratorCreationWizard dw = new NewDecoratorCreationWizard();
		dw.open();
		dw.setName(ACCOUNT_DECORATOR);
		dw.setPackage(getPackageName());
		dw.addDecoratedTypeInterfaces(getPackageName() + "." + ACCOUNT);
		dw.finish();
	
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.WEB_INF_BEANS_XML_PATH.split("/")).open();
		
		EditorPartWrapper ew = new EditorPartWrapper();
		ew.activateSourcePage();

		assertTrue(new DefaultStyledText().getText().contains(System.getProperty("line.separator")+
				" <decorators>"+System.getProperty("line.separator")+"  " +
				"<class>cdi.AccountDecorator</class>"+System.getProperty("line.separator")+" </decorators>"));
		
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.JAVA_RESOURCES,CDIConstants.SRC, 
				getPackageName(), ACCOUNT_DECORATOR_JAVA).open();
		
		TextEditor activeEditor = new TextEditor(ACCOUNT_DECORATOR_JAVA);
		
		assertTrue(activeEditor.getText().contains("@Decorator"));
		assertTrue(activeEditor.getText().contains("@Inject"+System.getProperty("line.separator")+"\t@Delegate"
				+ System.getProperty("line.separator")+"\t@Any" +
				System.getProperty("line.separator")+"\tprivate Account account;"));
		assertTrue(activeEditor.getText().contains("BigDecimal getBalance()"));
		assertTrue(activeEditor.getText().contains("User getOwner()"));
		assertTrue(activeEditor.getText().contains("void withdraw(BigDecimal amount)"));
		assertTrue(activeEditor.getText().contains("void deposit(BigDecimal amount)"));
		
	}

}
