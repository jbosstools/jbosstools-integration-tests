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

package org.jboss.tools.jsf.ui.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.jsf.ui.test.project.CreateJSFProjectTest;
import org.jboss.tools.jsf.ui.test.project.ExportImportWARTest;
import org.jboss.tools.jsf.ui.test.project.RunJSFProjectTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
    CreateJSFProjectTest.class,
    RunJSFProjectTest.class,
    ExportImportWARTest.class
    })
public class AllTestsSuite {

}
