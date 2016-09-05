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
package org.jboss.tools.freemarker.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.freemarker.ui.bot.test.editor.FreeMarkerCodeAssistTest;
import org.jboss.tools.freemarker.ui.bot.test.editor.FreemarkerDataModelTest;
import org.jboss.tools.freemarker.ui.bot.test.editor.FreeMarkerBaseEditorTest;
import org.jboss.tools.freemarker.ui.bot.test.editor.FreemarkerDirectiveTest;
import org.jboss.tools.freemarker.ui.bot.test.editor.FreemarkerPreferencePageTest;
import org.jboss.tools.freemarker.ui.bot.test.editor.FreemarkerRunDirectiveTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	FreemarkerPreferencePageTest.class,
	FreeMarkerBaseEditorTest.class,
	FreemarkerDataModelTest.class,
	FreemarkerDirectiveTest.class,
	FreemarkerRunDirectiveTest.class,
	FreeMarkerCodeAssistTest.class
})
public class AllSuite {

}
