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
package org.jboss.tools.cdi.bot.test.decorator;

import static org.junit.Assert.*;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewDecoratorCreationWizard;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on creating new decorator from web bean 
 * implementing some existing interface
 * 
 * @author Jaroslav Jankovic
 * 
 */

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class DecoratorFromWebBeanTest extends CDITestBase {
	
	private static final String ACCOUNT = "Account";
	
	private static final String ACCOUNT_JAVA = ACCOUNT + ".java";
	
	private static final String ACCOUNT_DECORATOR = "AccountDecorator";
	
	private static final String ACCOUNT_DECORATOR_JAVA = ACCOUNT_DECORATOR + ".java";
	
	@Override
	public String getProjectName() {
		return "DecoratorFromWebBean";
	}

	@After
	public void cleanUp() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		for(Project p: pe.getProjects()){
			p.delete(true);
		}
	}
	
	@Test
	public void testCreatingDecoratorWithMenu() {
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC,getPackageName(),ACCOUNT_JAVA).select();
		
		
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
		
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC, 
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
	
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.WEB_INF_BEANS_XML_PATH.split("/")).open();
		
		EditorPartWrapper ew = new EditorPartWrapper();
		ew.activateSourcePage();

		assertTrue(new DefaultStyledText().getText().contains(System.getProperty("line.separator")+
				" <decorators>"+System.getProperty("line.separator")+"  " +
				"<class>cdi.AccountDecorator</class>"+System.getProperty("line.separator")+" </decorators>"));
		
		pe.getProject(getProjectName()).getProjectItem(CDIConstants.SRC, 
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
