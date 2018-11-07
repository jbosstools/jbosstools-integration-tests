/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
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
import org.jboss.tools.cdi.bot.test.beans.bean.cdi10.BeanValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi11.BeanValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedComponentsSearchingTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.named.cdi11.NamedComponentsSearchingTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.BeanInjectOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.FindObserverEventTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi11.BeanInjectOpenOnTestCDI11;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi11.FindObserverEventTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLBeansEditorTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLBeansEditorTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLDiscoveryModesTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.cdi11.BeansXMLValidationTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi10.BeansXMLCompletionTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi11.BeansXMLCompletionTestCDI11;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi11.BeansXMLValidationQuickFixTestCDI11;
import org.jboss.tools.cdi.bot.test.validation.cdi10.CDIValidatorTestCDI10;
import org.jboss.tools.cdi.bot.test.validation.cdi11.CDIValidatorTestCDI11;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.CDIWebProjectWizardTestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi11.CDIWebProjectWizardTestCDI11;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	BeanValidationQuickFixTestCDI10.class,
	BeanValidationQuickFixTestCDI11.class,
	NamedComponentsSearchingTestCDI10.class,
	NamedComponentsSearchingTestCDI11.class,
	BeanInjectOpenOnTestCDI10.class,
	BeanInjectOpenOnTestCDI11.class,
	FindObserverEventTestCDI10.class,
	FindObserverEventTestCDI11.class,
	BeansXMLCompletionTestCDI10.class,
	BeansXMLCompletionTestCDI11.class,
	BeansXMLBeansEditorTestCDI10.class,
	BeansXMLBeansEditorTestCDI11.class,
	BeansXMLValidationTestCDI10.class,
	BeansXMLValidationTestCDI11.class,
	BeansXMLValidationQuickFixTestCDI10.class,
	BeansXMLValidationQuickFixTestCDI11.class,
	CDIValidatorTestCDI10.class,
	CDIValidatorTestCDI11.class,
	CDIWebProjectWizardTestCDI10.class,
	CDIWebProjectWizardTestCDI11.class,
	BeansXMLDiscoveryModesTestCDI11.class
})
public class SmokeSuite {

}
