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
package org.jboss.tools.batch.ui.bot.test.editor.jobxml;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * 
 * @author odockal
 *
 */
public class ContentAssistRestartAttributeTest extends AbstractJobXMLTest {

	public static final String BATCH_FILE = "job-level.xml";
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
		setJobXMLContentFromFile(BATCH_FILE, false);
	}
	
	@Test
	public void testRestartToJobLevel() {
		insertElementBefore("<stop on=\"FAILED\" restart=\"\" />", "</job>");
		List<String> proposals = getProposalsFromCursor("restart=\"");
		assertThat(proposals, hasItem("step-job-level-id"));
		assertThat(proposals, not(hasItem("step-inner-level-id")));
	}
	
	@Test
	public void testRestartToNestedLevel() {
		insertElementBefore("<stop on=\"FAILED\" restart=\"\" />", "</flow>");
		List<String> proposals = getProposalsFromCursor("restart=\"");
		assertThat(proposals, hasItem("step-job-level-id"));
		assertThat(proposals, not(hasItem("step-inner-level-id")));
	}
	
}
