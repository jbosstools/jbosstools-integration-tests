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
package org.jboss.tools.cdi.seam3.bot.test.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class InterfaceAndAbstractValidationTest extends Seam3TestBase {
	
	@InjectRequirement
    private ServerRequirement sr;

	@After
	public void cleanWS() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.deleteAllProjects();
	}
	
	@Test
	public void testInterfaceValidation() {
		
		/* import test project */
		String projectName = "interface1";
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		AbstractWait.sleep(TimePeriod.SHORT); // necessary to CDI Validation computation
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(IDELabel.WebProjectsTree.WEB_CONTENT, 
				IDELabel.WebProjectsTree.WEB_INF, IDELabel.WebProjectsTree.BEANS_XML).open();
		
		List<Marker> markers = new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML).getMarkers();
		
		/* assert expected count */
		assertExpectedCount(markers.size() ,1);
		
		/* assert message contains expected value */
		assertMessageContainsExpectedValue(markers.get(0).getText(), 
				"Abstract type", "cannot be configured as a bean");
		
	}
	
	@Test
	public void testAbstractTypeValidation() {
		
		/* import test project */
		String projectName = "abstract1";
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		AbstractWait.sleep(TimePeriod.SHORT); // necessary to CDI Validation computation
		
		/* get markers for beans.xml */
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(IDELabel.WebProjectsTree.WEB_CONTENT, 
				IDELabel.WebProjectsTree.WEB_INF, IDELabel.WebProjectsTree.BEANS_XML).open();
		
		List<Marker> markers = new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML).getMarkers();
		
		/* assert expected count */
		assertExpectedCount(markers.size() ,1);
		
		/* assert message contains expected value */
		assertMessageContainsExpectedValue(markers.get(0).getText(), 
				"Abstract type", "cannot be configured as a bean");
		
	}
	
	private void assertMessageContainsExpectedValue(String message,
			String... expectedValues) {
		for (String value : expectedValues) {
			assertTrue(message.contains(value));
		}
	}
	
	private void assertExpectedCount(int realCount, int expectedCount) {
		assertTrue("Expected count: " + expectedCount + " real count: " + realCount, 
				realCount == expectedCount);
	}
	
}
