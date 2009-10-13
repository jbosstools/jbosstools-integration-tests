package org.jboss.tools.vpe.ui.bot.test;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.vpe.ui.bot.test.editor.BlockCommentTest;
import org.jboss.tools.vpe.ui.bot.test.editor.ToggleCommentTest;
import org.jboss.tools.vpe.ui.bot.test.editor.VerificationOfNameSpacesTest;
import org.jboss.tools.vpe.ui.bot.test.editor.pagedesign.AddSubstitutedELExpressionFolderScopeTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.AlwaysHideSelectionBarWithoutPromptTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.BorderForUnknownTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ChangeEditorTabForTheFirstOpenPageTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowNonVisualTagsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowResourceBundlesUsageasELexpressionsTest;
import org.jboss.tools.vpe.ui.bot.test.editor.preferences.ShowSelectionTagBarTest;
import org.jboss.tools.vpe.ui.bot.test.jbide.JBIDE4556Test;
import org.jboss.tools.vpe.ui.bot.test.palette.CancelTagLibDefenitionTest;
import org.jboss.tools.vpe.ui.bot.test.palette.ImportTagsFromTLDFileTest;

public class VPEAllBotTests extends SWTBotTestCase{
	public static Test suite(){
		TestSuite suite = new TestSuite("VPE All Tests");
		suite.addTestSuite(CancelTagLibDefenitionTest.class);
		suite.addTestSuite(ImportTagsFromTLDFileTest.class);
		suite.addTestSuite(ToggleCommentTest.class);
		suite.addTestSuite(BlockCommentTest.class);
		suite.addTestSuite(ChangeEditorTabForTheFirstOpenPageTest.class);
		suite.addTestSuite(JBIDE4556Test.class);
		suite.addTestSuite(VerificationOfNameSpacesTest.class);
		suite.addTestSuite(BorderForUnknownTagsTest.class);
		suite.addTestSuite(ShowResourceBundlesUsageasELexpressionsTest.class);
		suite.addTestSuite(ShowSelectionTagBarTest.class);
		suite.addTestSuite(AlwaysHideSelectionBarWithoutPromptTest.class);
		suite.addTestSuite(ShowNonVisualTagsTest.class);
		suite.addTestSuite(AddSubstitutedELExpressionFolderScopeTest.class);
		return new TestSetup(suite);
	}
}
