/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.annotation;

import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.rest.explorer.RESTfulExplorerTest;
import org.jboss.tools.ws.ui.bot.test.uiutils.views.AnnotationProperties;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class AnnotationPropertiesTest extends RESTfulTestBase {
	
	
	@Override
	public String getWsProjectName() {
		return "AnnotationViewTest";
	}
	
	@Override
	public String getWsPackage() {
		return "org.ws.annotation.properties.test";
	}

	@Override
	public String getWsName() {
		return "WSService";
	}
	
	/**
	 * 1 there are no incorrectly checked annotations
	 * 2 there are no incorrectly unchecked annotations 
	 */
	@Test
	public void testAbsenceOfAnnotation() {
		
		/*
		resourceHelper.copyResourceToClass(bot.editorByTitle(getWsName() + ".java"), 
										   RESTfulExplorerTest.class.
										   getResourceAsStream(BASIC_WS_RESOURCE), 
										   false, getWsPackage(), getWsName());
		
		SWTJBTExt.selectTextInSourcePane(bot, getWsName() + ".java", getWsName(), 0, 0);
		
		AnnotationProperties annotProperties = new AnnotationProperties();
		*/
		
	}
	
	/**
	 * 1 there are correctly checked annotations
	 * 2 there are correctly unchecked annotations
	 */
	@Test
	public void testPresenceOfAnnotation() {
		
	}
	
	/**
	 * 1 check equality of param values
	 * 2 changing param values is mirrored to class
	 */
	@Test
	public void testAnnotationParamValues() {
		
	}
	
	/**
	 * 1 activating annotation through view is mirrored to class
	 */
	@Test
	public void testAnnotationActivating() {
		
	}
	
	/**
	 * 1 deactivating annotation through view is mirrored to class
	 */
	@Test
	public void testAnnotationDeactivating() {
		
	}
	
}
