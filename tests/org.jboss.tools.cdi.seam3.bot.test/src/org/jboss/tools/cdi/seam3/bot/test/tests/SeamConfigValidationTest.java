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
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class SeamConfigValidationTest extends Seam3TestBase {

	private static String projectName = "seamConfigValidation";
	private static final String SEAM_CONFIG = "seam-beans.xml";
	private static List<Marker> markers = null;
	
	@InjectRequirement
    private static ServerRequirement sr;
	
	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
		getAllSeamConfigMarkers();
		assertExpectedCount(markers.size(), 4);
	}
	
	@Test
	public void testNonExistedField() {
		
		/* get marker by its location */
		Marker marker = getMarkerByLocation(markers, 8);
		assertNotNull(marker);
		
		/* test the message of marker */
		assertMessageContainsExpectedValue(marker.getText(), 
				"Cannot resolve field or method");
	}
	
	@Test
	public void testNonExistedConstructor() {
	
		/* get marker by its location */
		Marker marker = getMarkerByLocation(markers, 11);
		assertNotNull(marker);
		
		/* test the message of marker */
		assertMessageContainsExpectedValue(marker.getText(), 
				"Cannot resolve constructor");
		
	}

	@Test
	public void testNonSupportedParameters() {
		
		/* get marker by its location */
		Marker marker = getMarkerByLocation(markers,15);
		assertNotNull(marker);
		
		/* test the message of marker */
		assertMessageContainsExpectedValue(marker.getText(), 
				"Cannot resolve method");
		
	}
	
	@Test
	public void testNonExistedClass() {
		
		/* get marker by its location */
		Marker marker = getMarkerByLocation(markers, 24);
		assertNotNull(marker);
		
		/* test the message of marker */
		assertMessageContainsExpectedValue(marker.getText(), 
				"Cannot resolve type");
		
	}
	
	private static void getAllSeamConfigMarkers() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(IDELabel.WebProjectsTree.WEB_CONTENT, 
				IDELabel.WebProjectsTree.WEB_INF, SEAM_CONFIG).open();
		
		markers = new DefaultEditor(SEAM_CONFIG).getMarkers();
	}
	
	private static void assertExpectedCount(int realCount, int expectedCount) {
		String knowsIssue = "";
		if (realCount == 0 && expectedCount == 4) {
			knowsIssue = ". Known issue JBIDE-12335";
		}
		assertTrue("Expected count: " + expectedCount + " real count: " + realCount
				+ knowsIssue, realCount == expectedCount);
	}
	
	private Marker getMarkerByLocation(List<Marker> markers, int location) {
		for (Marker m : markers) {
			if (m.getLineNumber() == location) {
				return m;
			}
		}
		return null;
	}
	
	private void assertMessageContainsExpectedValue(String message,
			String... expectedValues) {
		for (String value : expectedValues) {
			assertTrue(value.contains(message));
		}
	}

}
