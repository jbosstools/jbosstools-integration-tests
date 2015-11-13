package org.jboss.tools.batch.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignBatchletStepElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignChunkStepElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignDecisionElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignFlowElementTest;
import org.jboss.tools.batch.ui.bot.test.editor.design.DesignSplitElementTest;
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
	DesignSplitElementTest.class
	})
public class SmokeSuite {

}
