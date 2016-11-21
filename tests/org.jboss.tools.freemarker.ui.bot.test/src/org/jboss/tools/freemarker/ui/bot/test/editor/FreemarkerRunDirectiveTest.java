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
 * directives via FTL template file
 * @author odockal
 *
 */
public class FreemarkerRunDirectiveTest extends AbstractFreemarkerTest {

	private static Logger log = Logger.getLogger(FreemarkerRunDirectiveTest.class);
	
	@Test 
	public void autoEscDirectiveTest() {
		log.step("Run AutoEscTest java class to check resulting output");
		checkFreemMarkerOutput(false, "&amp;\n&\n&amp;\n", "", new String[] {"src", projectName, "AutoEscTest.java"});
		checkErrorLog();
	}
	
	@Test
	public void compressTest() {
		log.step("Run CompressTest java class to check resulting output");
		checkFreemMarkerOutput(true, 
				"resources/results/compress.txt", 
				"", new String[] {"src", projectName, "CompressTest.java"});
		checkErrorLog();		
	}
	
	@Test
	public void functionTest() {
		log.step("Run FunctionTest java class to check resulting output");
		checkFreemMarkerOutput(true, 
				"resources/results/function.txt", 
				"", new String[] {"src", projectName, "FunctionTest.java"});
		checkErrorLog();		
	}
	
	@Test
	public void includeTest() {
		log.step("Run FunctionTest java class to check resulting output");
		checkFreemMarkerOutput(true, 
				"resources/results/include.txt", 
				"", new String[] {"src", projectName, "IncludeTest.java"});
		checkErrorLog();		
	}
	
	@Test
	public void ifTest() {
		log.step("Run IfTest java class to check resulting output");
		checkFreemMarkerOutput(false, 
				"x=1\nx!=1, y=1", 
				"", new String[] {"src", projectName, "IfTest.java"});
		checkErrorLog();		
	}
	
	@Test
	public void noparseTest() {
		log.step("Run NoParse java class to check resulting output");
		checkFreemMarkerOutput(true, 
				"resources/results/noparse.txt", 
				"", new String[] {"src", projectName, "NoParseTest.java"});
		checkErrorLog();			
	}
	
	@Test
	public void settingsTest() {
		log.step("Run Setting java class to check resulting output");
		checkFreemMarkerOutput(true, 
				"resources/results/settings.txt", 
				"", new String[] {"src", projectName, "SettingTest.java"});
		checkErrorLog();		
	}
	
	@Test
	public void listTest() {
		log.step("Run List java class to check resulting output");
		checkFreemMarkerOutput(true, 
				"resources/results/list.txt", 
				"", new String[] {"src", projectName, "ListTest.java"});
		checkErrorLog();		
	}
}
