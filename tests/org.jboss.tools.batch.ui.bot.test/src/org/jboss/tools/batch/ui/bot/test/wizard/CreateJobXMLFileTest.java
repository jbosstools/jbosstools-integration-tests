/*******************************************************************************
 * Copyright (c) 2016-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.wizard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditor;
import org.jboss.tools.batch.reddeer.editor.jobxml.JobXMLEditorDesignPage;
import org.jboss.tools.batch.ui.bot.test.AbstractBatchTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@OpenPerspective(JavaPerspective.class)
public class CreateJobXMLFileTest extends AbstractBatchTest {
	
	private static final Logger log = Logger.getLogger(CreateJobXMLFileTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() {
		String suffix = JAVA_VERSION > 1.8 ? "-11.zip" : ".zip";
		initTestResources(log, "projects/" + getProjectName() + suffix);
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		removeProject(log, getProjectName());
	}
	
	@Test
	public void testCreateJobXMLFile(){
		assertFileExists();
		assertJobID();
		assertNoProblems();
	}
	
	private void assertFileExists() {
		assertTrue(getProject().containsResource(JOB_XML_FILE_FULL_PATH));
	}

	private void assertJobID() {
		getProject().getProjectItem(JOB_XML_FILE_FULL_PATH).open();
		JobXMLEditor editor = new JobXMLEditor(JOB_XML_FILE);
		editor.activate();
		
		JobXMLEditorDesignPage tab = editor.getDesignPage();
		tab.selectJob();
		
		assertThat(tab.getJobID(), is(JOB_ID));
	}

	@Override
	protected String getPackage() {
		// TODO Auto-generated method stub
		return null;
	}
}
