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
package org.jboss.tools.batch.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignBatchletStepElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignChunkStepElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignDecisionElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignFlowElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignSplitElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.features.CheckExistingReferenceTest;
import org.jboss.tools.batch.ui.bot.test.validation.XSDValidationTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateAllArtifactsFromAbstractClassTest;
import org.jboss.tools.batch.ui.bot.test.wizard.CreateAllArtifactsFromInterfaceTest;
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
	CreateAllArtifactsFromInterfaceTest.class, 
	CreateAllArtifactsFromAbstractClassTest.class,
	CreateBatchXMLArtifactTest.class,
	CreateNamedArtifactTest.class,
	CreateClassloaderArtifactTest.class, 
	CreateArtifactWithPropertiesTest.class,
	DesignBatchletStepElementTest.class,
	DesignChunkStepElementTest.class,
	DesignDecisionElementTest.class,
	DesignFlowElementTest.class,
	DesignSplitElementTest.class,
	CheckExistingReferenceTest.class,
	XSDValidationTest.class
	})
public class AllTestsSuite {

}
