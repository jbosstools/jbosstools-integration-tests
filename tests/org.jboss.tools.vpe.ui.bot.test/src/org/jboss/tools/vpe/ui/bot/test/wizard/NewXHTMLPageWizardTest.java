/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.wizard;

import org.jboss.reddeer.core.lookup.EditorPartLookup;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardPage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLFileWizardXHTMLTemplatePage;
import org.jboss.tools.jst.reddeer.web.ui.NewXHTMLWizard;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.junit.Test;

/**
 * @author mareshkau
 *
 */
public class NewXHTMLPageWizardTest extends VPEAutoTestCase{
	/**
	 * Test new xhtml page wizard basic functionality.
	 */
    @Test
    public void testNewXHTMLPageWizard(){
    	checkNewXHTMLPageWizard();
    	checkBlankResultWithoutAnyTemplateText_JBIDE6921();
    }
	/**
	 * Checks new xhtml page wizard basic functionality.
	 */
	public void checkNewXHTMLPageWizard() {
    	NewXHTMLWizard newXHTMLWizard = new NewXHTMLWizard();
    	newXHTMLWizard.open();
    	NewXHTMLFileWizardPage newXHTMLFileWizardPage = new NewXHTMLFileWizardPage(); 
    	newXHTMLFileWizardPage.setFileName("test");
    	newXHTMLFileWizardPage.setParentFolder(JBT_TEST_PROJECT_NAME + "/WebContent/pages");
		newXHTMLWizard.next();
		/*
		 * Check that the checkbox is disabled by default
		 */
		NewXHTMLFileWizardXHTMLTemplatePage templatePage = new NewXHTMLFileWizardXHTMLTemplatePage(); 
		assertFalse("'Use XHTML Template' checkbox should be disabled by default",
				templatePage.getUseXHTMLTemplate());
		templatePage.setUseXHTMLTemplate(true);
		templatePage.setTemplate("Form Facelet Page");
		newXHTMLWizard.finish();
		assertEquals("Active Editor Title should be" ,"test.xhtml", EditorPartLookup.getInstance().getActiveEditor().getTitle());
	}

	/**
	 * Checks blank result without any template text.
	 * Tests https://jira.jboss.org/browse/JBIDE-6921
	 */
	public void checkBlankResultWithoutAnyTemplateText_JBIDE6921() {
		NewXHTMLWizard newXHTMLWizard = new NewXHTMLWizard();
    	newXHTMLWizard.open();
    	NewXHTMLFileWizardPage newXHTMLFileWizardPage = new NewXHTMLFileWizardPage(); 
    	newXHTMLFileWizardPage.setFileName("test2");
    	newXHTMLFileWizardPage.setParentFolder(JBT_TEST_PROJECT_NAME + "/WebContent/pages");
		newXHTMLWizard.next();
		NewXHTMLFileWizardXHTMLTemplatePage templatePage = new NewXHTMLFileWizardXHTMLTemplatePage();
		/*
		 * Check that the checkbox is stored between the dialog's launches
		 */
		assertTrue("'Use XHTML Template' checkbox should be enabled (after previous dialog call)",
				templatePage.getUseXHTMLTemplate());
		/*
		 * Make some click on the checkbox and leave it disabled
		 */
		templatePage.setUseXHTMLTemplate(true);
		templatePage.setUseXHTMLTemplate(false);
		templatePage.setUseXHTMLTemplate(true);
		templatePage.setUseXHTMLTemplate(false);
		newXHTMLWizard.finish();
		assertEquals("Active Editor Title should be" ,"test2.xhtml", EditorPartLookup.getInstance().getActiveEditor().getTitle());
		assertEquals("Created XHTML file should be blank" ,"", new TextEditor("test2.xhtml").getText());

	}

}
