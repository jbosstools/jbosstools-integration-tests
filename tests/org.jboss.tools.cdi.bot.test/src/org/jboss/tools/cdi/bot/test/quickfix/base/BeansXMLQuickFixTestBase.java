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

package org.jboss.tools.cdi.bot.test.quickfix.base;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.annotations.ValidationType;
import org.jboss.tools.cdi.bot.test.quickfix.validators.BeansXmlValidationProvider;
import org.jboss.tools.cdi.bot.test.quickfix.validators.IValidationProvider;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeanCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewDecoratorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewStereotypeCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.QuickFixWizard;

public class BeansXMLQuickFixTestBase extends CDITestBase {

	private IValidationProvider validationProvider = new BeansXmlValidationProvider();
	
	public IValidationProvider getValidationProvider() {
		return validationProvider;
	}
	
	/**
	 * Method resolves validation error where there is no such Alternative as 
	 * configured in beans.xml. It opens quick fix and through finishWithWait button
	 * the Bean Wizard dialog is opened where both parameters are used to create
	 * the new alternative bean
	 * @param name
	 * @param pkg
	 */
	public void resolveAddNewAlternative(String name, String pkg) {
		
		openBeanXMLValidationProblem(ValidationType.NO_CLASS, getProjectName());
		NewBeanCreationWizard bw = new NewBeanCreationWizard();
		if (bw.isAlternative() && new PushButton("Finish").isEnabled()) {
			bw.setName(name);
			bw.setPackage(pkg);
			bw.finish();
			new DefaultEditor(name+".java");
		}else {
			fail("Dialog can't be finishWithWaited");
		}
		
	}
	
	/**
	 * Method resolves validation error where there is no such Stereotype as 
	 * configured in beans.xml. It opens quick fix and through finishWithWait button
	 * the Stereotype Wizard dialog is opened where both parameters are used to create
	 * the new stereotype annotation
	 * @param name
	 * @param pkg
	 */
	public void resolveAddNewStereotype(String name, String pkg) {
		
		openBeanXMLValidationProblem(ValidationType.NO_ANNOTATION, getProjectName());
		NewStereotypeCreationWizard sw = new NewStereotypeCreationWizard();
		if (sw.isAlternative()  && new PushButton("Finish").isEnabled()) {
			sw.setName(name);
			sw.setPackage(pkg);
			sw.finish();
			new DefaultEditor(name+".java");
		}else {
			fail("Dialog can't be finishWithWaited");
		}
		
	}
	
	/**
	 * Method resolves validation error where there is no such decorator as 
	 * configured in beans.xml. It opens quick fix and through finishWithWait button
	 * the Decorator Wizard dialog is opened where both parameters are used to create
	 * the new decorator. Interface "java.util.List" is automatically used. 
	 * @param name
	 * @param pkg
	 */
	public void resolveAddNewDecorator(String name, String pkg) {
		
		openBeanXMLValidationProblem(ValidationType.NO_CLASS, getProjectName());
		NewDecoratorCreationWizard dw = new NewDecoratorCreationWizard();
		dw.addDecoratedTypeInterfaces("java.util.List");
		if (new PushButton("Finish").isEnabled()) {
			dw.setName(name);
			dw.setPackage(pkg);
			dw.finish();
			new DefaultEditor(name+".java");
		} else {
			fail("Dialog can't be finishWithWaited");
		}
		
	}
	
	/**
	 * Method resolves validation error where there is no such Interceptor as 
	 * configured in beans.xml. It opens quick fix and through finishWithWait button
	 * the Interceptor Wizard dialog is opened where both parameters are used to create
	 * the new Interceptor
	 * @param name
	 * @param pkg
	 */
	public void resolveAddNewInterceptor(String name, String pkg) {
		
		openBeanXMLValidationProblem(ValidationType.NO_CLASS, getProjectName());
		NewInterceptorCreationWizard iw = new NewInterceptorCreationWizard();
		iw.setName(name);
		iw.setPackage(pkg);
		iw.finish();
		new DefaultEditor(name+".java");
	}
	
	/**
	 * Method corrects Bean which has no @Alternative annotation in it 
	 * by adding these parameter.
	 * @param name
	 * @param pkg
	 */
	public void resolveAddAlternativeToBean(String name) {
		
		openBeanXMLValidationProblem(ValidationType.NO_ALTERNATIVE, getProjectName());
		String content = new TextEditor(name + ".java").getText();
		assertTrue(content.contains("@Alternative"));
		
	}
	
	/**
	 * Method corrects Stereotype which has no @Alternative annotation in it 
	 * by adding these parameter.
	 * @param name
	 * @param pkg
	 */
	public void resolveAddAlternativeToStereotype(String name) {
		
		openBeanXMLValidationProblem(ValidationType.NO_ALTERNATIVE_STEREOTYPE, getProjectName());
		String content = new TextEditor(name + ".java").getText();
		assertTrue(content.contains("@Alternative"));
		
	}
	
	/**
	 * Method firstly gets beans.xml validation problem. Then
	 * it opens quick fix wizard, selects default value and
	 * press finishWithWait button
	 */
	private void openBeanXMLValidationProblem(ValidationType validationProblemType, String projectName) {
		
		TreeItem validationProblem = quickFixHelper.getProblem(validationProblemType, 
				projectName, validationProvider);		
		assertNotNull(validationProblem);
		
		quickFixHelper.openQuickFix(validationProblem);	
		QuickFixWizard qfWizard = new QuickFixWizard();
		qfWizard.setFix(qfWizard.getDefaultCDIQuickFix());
		qfWizard.setResource(qfWizard.getResources().get(0));
		qfWizard.finish();
	}
	
}
