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


import org.jboss.tools.cdi.bot.test.CDIConstants;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test operates on resource openOn in Seam3 using CDI tools
 * 
 * @author Jaroslav Jankovic
 */

public class ResourceOpenOnTest extends Seam3TestBase {

	private static String projectName = "resource";
	
	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3);
	}

	/**
	 * https://issues.jboss.org/browse/JBIDE-8202
	 */	
	@Test
	public void testResourceOpenOn() {
			
		String className = "MyBean.java";
		
		packageExplorer.openFile(projectName, CDIConstants.SRC, 
				"cdi.seam", className);

		OpenOnHelper.checkOpenOnFileIsOpened(bot, className, 
				CDIConstants.RESOURCE_ANNOTATION, "Open Resource", CDIConstants.BEANS_XML);
		
		editResourceUtil.moveFileInExplorerBase(packageExplorer, CDIConstants.BEANS_XML, 
				projectName + "/" + CDIConstants.WEBCONTENT + "/" + CDIConstants.WEB_INF,
				projectName + "/" + CDIConstants.WEBCONTENT + "/" + CDIConstants.META_INF);
		LOGGER.info("bean.xml was moved to META-INF");
		
		bot.swtBotEditorExtByTitle(className).show();
		editResourceUtil.replaceInEditor("WEB", "META");
		
		OpenOnHelper.checkOpenOnFileIsOpened(bot, className, 
				CDIConstants.RESOURCE_ANNOTATION, "Open Resource", CDIConstants.BEANS_XML);
		
	}
	
}
	
