/*******************************************************************************
 * Copyright (c) 2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi20.BeanValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.named.cdi20.NamedComponentsSearchingTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi20.BeanInjectOpenOnTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi20.FindObserverEventTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLBeansEditorTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLDiscoveryModesTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLValidationTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi20.BeansXMLCompletionTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi20.BeansXMLValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.microprofiile.cdi20.ConfigPropertyTestCDI20;
import org.jboss.tools.cdi.bot.test.validation.cdi20.CDIValidatorTestCDI20;
import org.jboss.tools.cdi.bot.test.wizard.cdi20.CDIWebProjectWizardTestCDI20;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/** 
 * 
 * @author zcervink@redhat.com
 * 
 */
@RunWith(RedDeerSuite.class)
@SuiteClasses({
	ConfigPropertyTestCDI20.class,
	BeanValidationQuickFixTestCDI20.class,
	NamedComponentsSearchingTestCDI20.class,
	BeanInjectOpenOnTestCDI20.class,
	FindObserverEventTestCDI20.class,
	BeansXMLCompletionTestCDI20.class,
	BeansXMLBeansEditorTestCDI20.class,
	BeansXMLValidationTestCDI20.class,
	BeansXMLValidationQuickFixTestCDI20.class,
	CDIValidatorTestCDI20.class,
	CDIWebProjectWizardTestCDI20.class,
	BeansXMLDiscoveryModesTestCDI20.class
})
public class CDI20SmokeSuite {

}
