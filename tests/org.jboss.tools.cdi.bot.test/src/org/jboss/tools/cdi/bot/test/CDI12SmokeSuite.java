/*******************************************************************************
 * Copyright (c) 2007-2020 Red Hat, Inc.
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
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.BeanValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.named.cdi12.NamedComponentsSearchingTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi12.BeanInjectOpenOnTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi12.FindObserverEventTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLBeansEditorTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLDiscoveryModesTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLValidationTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi12.BeansXMLCompletionTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi12.BeansXMLValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.validation.cdi12.CDIValidatorTestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.CDIWebProjectWizardTestCDI12;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	BeanValidationQuickFixTestCDI12.class,
	NamedComponentsSearchingTestCDI12.class,
	BeanInjectOpenOnTestCDI12.class,
	FindObserverEventTestCDI12.class,
	BeansXMLCompletionTestCDI12.class,
	BeansXMLBeansEditorTestCDI12.class,
	BeansXMLValidationTestCDI12.class,
	BeansXMLValidationQuickFixTestCDI12.class,
	CDIValidatorTestCDI12.class,
	CDIWebProjectWizardTestCDI12.class,
	BeansXMLDiscoveryModesTestCDI12.class
})
public class CDI12SmokeSuite {

}
