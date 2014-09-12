/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.webservice.eap;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Documentation/description is available at <a href="https://docspace.corp.redhat.com/docs/DOC-43675">Docspace</a>}
 * 
 * @author jlukas, jjankovi
 * @see <a href="https://docspace.corp.redhat.com/docs/DOC-43675">DOC-43675</a>
 */
@SuiteClasses({
	EAPFromJavaTest.class,
    EAPFromWSDLTest.class
})
@RunWith(RedDeerSuite.class)
public class EAPCompAllTests {
}
