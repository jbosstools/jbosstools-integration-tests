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

package org.jboss.tools.cdi.bot.test.wizard;

import static org.junit.Assert.*;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Test;

/**
* Test checks if beans.xml is created when selecting CDI Facet
* 
* @author Jaroslav Jankovic
*/
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class FacetTest extends CDITestBase {
	
	@Override	
	public void prepareWorkspace() {
		if (!projectHelper.projectExists(getProjectName())) {
			projectHelper.createDynamicWebProjectWithCDIFacets(getProjectName());			
		}
	}
	
	@Override
	public String getProjectName() {
		return "CDIFacetsProject";
	}
	
	@Test
	public void testCDIFacet() {
		if (projectHelper.projectExists(getProjectName())) {
			PackageExplorer pe = new PackageExplorer();
			pe.open();
			assertTrue("Error: beans.xml should be created when selecting CDI Facet", 
					pe.getProject(getProjectName()).containsItem(CDIConstants.WEB_INF_BEANS_XML_PATH.split("/")));			
		}else {
			fail("CDI project was not succesfully created with Dynamic Web Project wizard with CDI facet");
		}
		
	}
		
}
