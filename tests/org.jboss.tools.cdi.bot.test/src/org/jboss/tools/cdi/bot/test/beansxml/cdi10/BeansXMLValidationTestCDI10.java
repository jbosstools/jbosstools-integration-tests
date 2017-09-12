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

package org.jboss.tools.cdi.bot.test.beansxml.cdi10;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.condition.EditorIsDirty;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.beansxml.template.BeansXMLValidationTemplate;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.matcher.ServerMatcher;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.jboss.tools.cdi.reddeer.validators.BeansXmlValidationProviderCDI10;
import org.junit.Before;

/**
 * Test operates on beans validation in beans.xml
 * 
 * @author Jaroslav Jankovic
 * 
 */
@JBossServer(state = ServerRequirementState.PRESENT, cleanup = false)
@OpenPerspective(JavaEEPerspective.class)
public class BeansXMLValidationTestCDI10 extends BeansXMLValidationTemplate {

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
		return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.Eap());
	}

	@Before
	public void changeBeansXml() {
		validationProvider = new BeansXmlValidationProviderCDI10();

		EditorPartWrapper beansEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beansEditor.activateSourcePage();
		new EditorResourceHelper().replaceInEditor("/>", "></beans>", false);
		new WaitUntil(new EditorIsDirty(beansEditor), false);
		beansEditor.save();
		beansEditor.close();
	}
}