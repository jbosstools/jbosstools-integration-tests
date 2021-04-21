/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beansxml.annotation.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerTypeMatcher;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author zcervink@redhat.com
 *
 */
public class BeanParametersAnnotationTemplate extends CDITestBase {

	@Before
	public void setupProjectAccordingToGivenValues() {
		valuesToInsertDuringProjectSetup.put(1, "import javax.inject.Inject;");
		valuesToInsertDuringProjectSetup.put(2, "import javax.enterprise.context.RequestScoped;");
		valuesToInsertDuringProjectSetup.put(4, "@RequestScoped");
		valuesToInsertDuringProjectSetup.put(6, "@Inject public void injectParameters(CdiBean1 bean1){}");
		super.setupProject(valuesToInsertDuringProjectSetup);
	}

	@After
	public void clean() {
		cleanUp();
	}

	@Test
	public void testParametersAnnotationvalidation() {
		prepareBeanXml("annotated", false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("src", "main", "java", "test",
				CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION).open();
		TextEditor ed = new TextEditor(CDI_BEAN_2_JAVA_FILE_NAME + JAVA_FILE_EXTENSION);
		try {
			new WaitUntil(new EditorHasValidationMarkers(ed));
		} catch (WaitTimeoutExpiredException ex) {
			fail("There is supposed to be warning present");
		}

		ProblemsView pv = new ProblemsView();
		pv.open();
		List<Problem> problems = pv.getProblems(ProblemType.ALL, new MarkerTypeMatcher("CDI Problem"));
		assertEquals(1, problems.size());
		Problem validation = problems.get(0);
		assertTrue(validation.getLocation().contains("7"));
		
		if ("2.0".equals(CDIVersion)) {
			assertTrue(validation.getDescription()
					.contains("No bean is eligible for injection to the injection point [JSR-365 §5.2.2]"));
		}
		// JBIDE-26664
		else if ("1.2".equals(CDIVersion)) {
			assertTrue(validation.getDescription()
					.contains("No bean is eligible for injection to the injection point [JSR-365 §5.2.2]"));
		}
		else {
			assertTrue(validation.getDescription()
					.contains("No bean is eligible for injection to the injection point [JSR-346 §5.2.2]"));
		}
	}
}
