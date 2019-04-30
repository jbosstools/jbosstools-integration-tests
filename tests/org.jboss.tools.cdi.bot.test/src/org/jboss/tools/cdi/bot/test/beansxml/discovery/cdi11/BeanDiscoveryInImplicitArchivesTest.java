/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.junit.Test;

/**
 * JBIDE-
 * @author odockal
 *
 */
@JRE(cleanup=true)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
public class BeanDiscoveryInImplicitArchivesTest extends BeanDiscoveryInArchivesTemplate {

	@RequirementRestriction
	public static Collection<RequirementMatcher> getRestrictionMatcher() {
		if (isJavaLE8()) { 
			return Arrays.asList(new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly()));
		} else {
			return Arrays.asList(
					new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly()),
					new RequirementMatcher(JRE.class, "version", "1.8"));
		}
	}
	
	@Test
	public void testValidationOfBeanDiscoveryInImplicitArchives() {
		prepareBeanXml("annotated", false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC, "test", "CdiBean1.java").open();
		TextEditor ed = new TextEditor("CdiBean2.java");
		try{
			new WaitUntil(new EditorHasValidationMarkers(ed));
		} catch (WaitTimeoutExpiredException ex){
			fail("There is supposed to be warning present");
		}
		List<Marker> markers = ed.getMarkers();
		assertEquals(1, markers.size());
		Marker validation = markers.get(0);
		assertEquals(validation.getLineNumber(), 7);
		assertTrue(validation.getText().contains("No bean is eligible for injection"));
	}
	
}
