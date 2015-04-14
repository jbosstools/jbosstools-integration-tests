/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.validation;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.bot.test.beansxml.BeansXMLValidationTest;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.condition.AsYouTypeMarkerExists;
import org.junit.After;
import org.junit.Test;

/**
 * Tests as-you-type validation in java editor
 * 
 * @author jjankovi
 * 
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace

//https://issues.jboss.org/browse/JBIDE-17294
public class AsYouTypeValidationTest extends CDITestBase {

	private static final String ELIGIBLE_VALIDATION_PROBLEM = "Multiple beans are eligible " +
			"for injection to the injection point.*";
	
	private static final String BEAN_IS_NOT_ALTERNATIVE = ".*is not an alternative bean class.*";
	
	@After
	public void cleanUp() {
		try{
			new DefaultEditor().save();
		} catch (CoreLayerException ex){
			
		}
	}
	
	/**
	 * failing
	 * reported by jjankovi JBIDE-12575
	 */
	@Test
	public void testJavaAYTValidation() {
		
		wizard.createCDIComponent(
				CDIWizardType.BEAN, "Test", getPackageName(), null);
		
		//=======================================================================
		// 	Invoke as-you-type validation marker appearance without saving file
		//=======================================================================
		new WaitUntil(new AsYouTypeMarkerExists("TODO Auto-generated constructor stub"));
		
		editResourceUtil.replaceInEditor("Test.java","// TODO Auto-generated constructor stub", "");
		
		new WaitWhile(new AsYouTypeMarkerExists("TODO Auto-generated constructor stub"));
		
		editResourceUtil.replaceClassContentByResource("Test.java",AsYouTypeValidationTest.class.
				getResourceAsStream("/resources/validation/Test1.java.cdi"), 
				false, false, getPackageName(), "Test");
		
		new WaitUntil(new AsYouTypeMarkerExists(ELIGIBLE_VALIDATION_PROBLEM));
		
		//==========================================================================
		// 	Invoke as-you-type validation marker disappearance without saving file
		//==========================================================================
		
		editResourceUtil.replaceInEditor("Test.java","@Inject ", "@Inject @Named ", false);
		new WaitWhile(new AsYouTypeMarkerExists(ELIGIBLE_VALIDATION_PROBLEM));
	}
	
	/**
	 * failing
	 * reported by jjankovi JBIDE-12575
	 */
	@Test
	public void testBeansXmlAYTValidation() {
		
		wizard.createCDIComponent(
				CDIWizardType.BEAN, "A1", getPackageName(), null);
		
		wizard.createCDIComponent(
				CDIWizardType.BEAN, "A2", getPackageName(), "alternative");
		
		//=======================================================================
		// 	Invoke as-you-type validation marker appearance without saving file
		//=======================================================================
		
		beansHelper.createBeansXMLWithAlternative(getProjectName(), getPackageName(), "A1", false,
				this.getClass().getResourceAsStream(BeansXMLValidationTest.BEANS_XML_WITH_ALTERNATIVE));
		
		
		new WaitUntil(new AsYouTypeMarkerExists(BEAN_IS_NOT_ALTERNATIVE));
		
		//==========================================================================
		// 	Invoke as-you-type validation marker disappearance without saving file
		//==========================================================================
		
		editResourceUtil.replaceInEditor("beans.xml","A1", "A2", false);
		
		new WaitWhile(new AsYouTypeMarkerExists(BEAN_IS_NOT_ALTERNATIVE));
	}
	
}