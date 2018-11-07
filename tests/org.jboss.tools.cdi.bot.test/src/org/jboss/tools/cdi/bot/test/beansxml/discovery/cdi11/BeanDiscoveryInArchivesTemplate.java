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
package org.jboss.tools.cdi.bot.test.beansxml.discovery.cdi11;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.junit.After;
import org.junit.Before;

/**
 * Tempalte class for tests covering JBIDE-23725
 * @author odockal
 *
 */
public class BeanDiscoveryInArchivesTemplate extends CDITestBase {

	@Before
	public void setupProject() {
		createClass(PROJECT_NAME, "test", "CdiBean1");
		createClass(PROJECT_NAME, "test", "CdiBean2");
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC, "test", "CdiBean2.java").open();
		TextEditor ed = new TextEditor("CdiBean2.java");
		ed.insertLine(1, "import javax.inject.Inject;");
		ed.insertLine(2, "import javax.enterprise.context.RequestScoped;");
		ed.insertLine(4, "@RequestScoped");
		ed.insertLine(6, "@Inject CdiBean1 bean1;");
		ed.save();
	}
	
	@After
	public void clean() {
		deleteAllProjects();
	}
	
}
