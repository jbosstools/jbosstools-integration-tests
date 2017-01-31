/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignBatchletStepElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignChunkStepElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignDecisionElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignFlowElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignSplitElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.features.RenameTest;
import org.jboss.tools.batch.ui.bot.test.editor.jobxml.ValidateSourceBatchPropertyTest;
import org.jboss.tools.batch.ui.bot.test.editor.jobxml.ValidateSourceClassAttributeTest;
import org.jboss.tools.batch.ui.bot.test.editor.jobxml.ValidateSourceElementLoopingTest;
import org.jboss.tools.batch.ui.bot.test.editor.jobxml.ValidateSourceLevelReferenctingTest;
import org.jboss.tools.batch.ui.bot.test.editor.jobxml.ValidateSourceRefAttributeTest;
import org.jboss.tools.batch.ui.bot.test.editor.jobxml.ValidateSourceRestartAttributeTest;
import org.jboss.tools.batch.ui.bot.test.editor.jobxml.ValidateSourceUniqueIDTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateArtifactWithPropertiesTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateBatchXMLArtifactTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateClassloaderArtifactTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateJobXMLFileTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateNamedArtifactTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	CreateJobXMLFileTest.class, 
	CreateBatchXMLArtifactTest.class,
	CreateNamedArtifactTest.class,
	CreateClassloaderArtifactTest.class, 
	CreateArtifactWithPropertiesTest.class,
	DesignBatchletStepElementTest.class,
	DesignChunkStepElementTest.class,
	DesignDecisionElementTest.class,
	DesignFlowElementTest.class,
	DesignSplitElementTest.class,
	RenameTest.class,
	ValidateSourceUniqueIDTest.class,
	ValidateSourceBatchPropertyTest.class,
	ValidateSourceRefAttributeTest.class,
	ValidateSourceClassAttributeTest.class,
	ValidateSourceElementLoopingTest.class,
	ValidateSourceLevelReferenctingTest.class,
	ValidateSourceRestartAttributeTest.class
	})
public class SmokeTestsSuite {

}
