/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.freemarker.ui.bot.test.editor;

import org.jboss.reddeer.common.logging.Logger;
import org.junit.Test;


/**
 * Test class for verification of processing/executing Freemarker 
 * data-model conversion via FTL template file
 * @author odockal
 *
 */
public class FreemarkerDataModelTest extends AbstractFreemarkerTest {

	private static Logger log = Logger.getLogger(FreemarkerDataModelTest.class);
	
	@Test
	public void dataModelTest() {
		log.step("Run DataModel java class to check resulting output");
		checkFreemMarkerOutput(true, 
				"resources/results/data-model.txt", 
				"", new String[] {"src", projectName, "DataModelTest.java"});
		checkErrorLog();			
	}
	
}
