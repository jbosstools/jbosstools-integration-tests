/*******************************************************************************
 * Copyright (c) 2010-2020 Red Hat, Inc.
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
import org.jboss.tools.cdi.bot.test.beans.bean.cdi12.BeanValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.bean.cdi20.BeanValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedComponentsSearchingTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.named.cdi12.NamedComponentsSearchingTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.named.cdi20.NamedComponentsSearchingTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.BeanInjectOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.FindObserverEventTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi12.BeanInjectOpenOnTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi12.FindObserverEventTestCDI12;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi20.BeanInjectOpenOnTestCDI20;
import org.jboss.tools.cdi.bot.test.beans.openon.cdi20.FindObserverEventTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLBeansEditorTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLBeansEditorTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLDiscoveryModesTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi12.BeansXMLValidationTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLBeansEditorTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLDiscoveryModesTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.cdi20.BeansXMLValidationTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi10.BeansXMLCompletionTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi12.BeansXMLCompletionTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi20.BeansXMLCompletionTestCDI20;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi12.BeansXMLValidationQuickFixTestCDI12;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi20.BeansXMLValidationQuickFixTestCDI20;
import org.jboss.tools.cdi.bot.test.validation.cdi10.CDIValidatorTestCDI10;
import org.jboss.tools.cdi.bot.test.validation.cdi12.CDIValidatorTestCDI12;
import org.jboss.tools.cdi.bot.test.validation.cdi20.CDIValidatorTestCDI20;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.CDIWebProjectWizardTestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi12.CDIWebProjectWizardTestCDI12;
import org.jboss.tools.cdi.bot.test.wizard.cdi20.CDIWebProjectWizardTestCDI20;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	BeanValidationQuickFixTestCDI10.class,
	BeanValidationQuickFixTestCDI12.class,
	BeanValidationQuickFixTestCDI20.class,
	NamedComponentsSearchingTestCDI10.class,
	NamedComponentsSearchingTestCDI12.class,
	NamedComponentsSearchingTestCDI20.class,
	BeanInjectOpenOnTestCDI10.class,
	BeanInjectOpenOnTestCDI12.class,
	BeanInjectOpenOnTestCDI20.class,
	FindObserverEventTestCDI10.class,
	FindObserverEventTestCDI12.class,
	FindObserverEventTestCDI20.class,
	BeansXMLCompletionTestCDI10.class,
	BeansXMLCompletionTestCDI12.class,
	BeansXMLCompletionTestCDI20.class,
	BeansXMLBeansEditorTestCDI10.class,
	BeansXMLBeansEditorTestCDI12.class,
	BeansXMLBeansEditorTestCDI20.class,
	BeansXMLValidationTestCDI10.class,
	BeansXMLValidationTestCDI12.class,
	BeansXMLValidationTestCDI20.class,
	BeansXMLValidationQuickFixTestCDI10.class,
	BeansXMLValidationQuickFixTestCDI12.class,
	BeansXMLValidationQuickFixTestCDI20.class,
	CDIValidatorTestCDI10.class,
	CDIValidatorTestCDI12.class,
	CDIValidatorTestCDI20.class,
	CDIWebProjectWizardTestCDI10.class,
	CDIWebProjectWizardTestCDI12.class,
	CDIWebProjectWizardTestCDI20.class,
	BeansXMLDiscoveryModesTestCDI12.class,
	BeansXMLDiscoveryModesTestCDI20.class
})
public class SmokeSuite {

}
