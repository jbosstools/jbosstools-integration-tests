/*******************************************************************************
 * Copyright (c) 2007-2017 Red Hat, Inc.
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
import org.jboss.tools.cdi.bot.test.beans.named.cdi10.NamedComponentsSearchingTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.BeanInjectOpenOnTestCDI10;
import org.jboss.tools.cdi.bot.test.beans.openon.cd10.FindObserverEventTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLBeansEditorTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.cdi10.BeansXMLValidationTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.completion.cdi10.BeansXMLCompletionTestCDI10;
import org.jboss.tools.cdi.bot.test.beansxml.validation.cdi10.BeansXMLValidationQuickFixTestCDI10;
import org.jboss.tools.cdi.bot.test.validation.cdi10.CDIValidatorTestCDI10;
import org.jboss.tools.cdi.bot.test.wizard.cdi10.CDIWebProjectWizardTestCDI10;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({	
	BeanValidationQuickFixTestCDI10.class,
	NamedComponentsSearchingTestCDI10.class,
	BeanInjectOpenOnTestCDI10.class,
	FindObserverEventTestCDI10.class,
	BeansXMLCompletionTestCDI10.class,
	BeansXMLBeansEditorTestCDI10.class,
	BeansXMLValidationTestCDI10.class,
	BeansXMLValidationQuickFixTestCDI10.class,
	CDIValidatorTestCDI10.class,
	CDIWebProjectWizardTestCDI10.class,
})
public class CDI10SmokeSuite {

}
