package org.jboss.tools.drools.reddeer.test.functional;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.reddeer.test.annotation.UsePerspective;
import org.jboss.tools.drools.reddeer.test.util.SmokeTest;
import org.jboss.tools.drools.reddeer.test.util.TestParent;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class RulesEditorTest extends TestParent {
    public static final String MESSAGE_TEXT = getTemplateText("MessageClass");
    public static final String RULE_RESOURCE_TEXT = getTemplateText("DummyRuleFile");

    // FIXME RedDeerify when possible
    private SWTBotEclipseEditor rulesEditor;

    @Before
    public void setUpDomainAndRule() {
        // create domain class
        NewJavaClassWizardDialog diag = new NewJavaClassWizardDialog();
        diag.open();
        diag.getFirstPage().setName("Message");
        diag.getFirstPage().setPackage("com.sample.domain");
        diag.finish();

        // FIXME RedDeerify when possible
        SWTBotEclipseEditor editor = Bot.get().activeEditor().toTextEditor();
        editor.setText(MESSAGE_TEXT);
        editor.saveAndClose();

        // create RuleResource
        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        wiz.getFirstPage().setParentFolder(DEFAULT_RULES_PATH);
        wiz.getFirstPage().setFileName(name.getMethodName());
        wiz.getFirstPage().setRulePackageName("com.sample");
        wiz.finish();

        // FIXME RedDeerify when possible
        rulesEditor = Bot.get().activeEditor().toTextEditor();
        rulesEditor.setText(RULE_RESOURCE_TEXT);
        rulesEditor.save();
    }

    @Ignore("RedDeer does not offer a reliable way to test autocompletion (yet)")
    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testImportCodeCompletion() {
        rulesEditor.navigateTo(2, 0);

        final long timeout = SWTBotPreferences.TIMEOUT;
        System.setProperty(SWTBotPreferences.KEY_TIMEOUT, "60000");
        try {
//        System.out.println("*** Auto-completion proposals: ***");
//        for (String proposal : rulesEditor.getAutoCompleteProposals("message")) {
//            System.out.println("*** '" + proposal + "'");
//        }
        rulesEditor.autoCompleteProposal("", "import ");
        rulesEditor.autoCompleteProposal("message", "com.sample.domain.Message");

        Assert.assertEquals("", "import com.sample.Message", rulesEditor.getTextOnCurrentLine());
        } finally {
            System.setProperty(SWTBotPreferences.KEY_TIMEOUT, Long.toString(timeout));
            SWTBotPreferences.TIMEOUT = timeout;
        }
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testShowReteTree() {
        // FIXME RedDeerify when possible
        rulesEditor.bot().cTabItem("Rete Tree").activate();
        SWTUtils.captureScreenshot("REVIEW-rete-tree.png");

        rulesEditor.close();
    }
}
