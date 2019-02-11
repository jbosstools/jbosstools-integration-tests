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
package org.jboss.tools.cdi.bot.test.beansxml.annotation.cdi11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerTypeMatcher;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.jre.JRERequirement.JRE;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Covers JBIDE-23727
 * @author odockal
 *
 */
@JRE(cleanup=true)
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerRequirementState.PRESENT, cleanup=false)
public class BeanParametersAnnotationTest extends CDITestBase {

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly());
	}
	
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
		ed.insertLine(6, "@Inject public void injectParameters(CdiBean1 bean1){}");
		ed.save();
	}
	
	@After
	public void clean() {
		deleteAllProjects();
	}
	
	@Test
	public void testParametersAnnotationvalidation() {
		prepareBeanXml("annotated", false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem(CDIConstants.JAVA_RESOURCES, CDIConstants.SRC, "test", "CdiBean1.java").open();
		TextEditor ed = new TextEditor("CdiBean2.java");
		try{
			new WaitUntil(new EditorHasValidationMarkers(ed));
		} catch (WaitTimeoutExpiredException ex){
			fail("There is supposed to be warning present");
		}
		
		ProblemsView pv = new ProblemsView();
		pv.open();
		List<Problem> problems = pv.getProblems(ProblemType.ALL, new MarkerTypeMatcher("CDI Problem"));
		assertEquals(1, problems.size());
		Problem validation = problems.get(0);
		assertTrue(validation.getLocation().contains("7"));
		assertTrue(validation.getDescription().contains("No bean is eligible for injection"));
	}

}
