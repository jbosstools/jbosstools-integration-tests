/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.sample.test;

import org.eclipse.core.resources.IFile;
import org.jboss.tools.ws.ui.bot.test.sample.SampleRESTTestBase;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 * 
 */
public class SimpleRESTWebServiceTest extends SampleRESTTestBase {

	@Override
	protected String getWsProjectName() {
		return "SimpleRESTWS";
	}

	@Test
	public void testSimpleRestWS() {
		IFile dd = getDD(getWsProjectName());
		if (!dd.exists()) {
			createDD(getWsProjectName());
		}
		assertTrue(dd.exists());
		createSimpleRESTWS(getWsProjectName(), "RESTSample", "rest.sample", "Sample", "RESTApp");
		checkRESTService(getWsProjectName(), "RESTSample", "rest.sample", "Sample", "Hello World!", "RESTApp");
	}

}
