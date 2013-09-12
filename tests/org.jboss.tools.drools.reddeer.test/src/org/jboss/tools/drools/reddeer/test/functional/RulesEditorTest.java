package org.jboss.tools.drools.reddeer.test.functional;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.ContentAssist;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.reddeer.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.reddeer.test.annotation.UsePerspective;
import org.jboss.tools.drools.reddeer.test.util.SmokeTest;
import org.jboss.tools.drools.reddeer.test.util.TestParent;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class RulesEditorTest extends TestParent {
    public static final String MESSAGE_TEXT = getTemplateText("MessageClass");
    public static final String RULE_RESOURCE_TEXT = getTemplateText("DummyRuleFile");

    @Before
    public void setUpDomainAndRule() {
        // create domain class
        NewJavaClassWizardDialog diag = new NewJavaClassWizardDialog();
        diag.open();
        diag.getFirstPage().setName("Message");
        diag.getFirstPage().setPackage("com.sample.domain");
        diag.finish();

        // FIXME RedDeerify when possible
        SWTBotEclipseEditor editor = new SWTWorkbenchBot().activeEditor().toTextEditor();
        editor.setText(MESSAGE_TEXT);
        editor.saveAndClose();

        // create RuleResource
        NewRuleResourceWizard wiz = new NewRuleResourceWizard();
        wiz.open();
        wiz.getFirstPage().setParentFolder(DEFAULT_RULES_PATH);
        wiz.getFirstPage().setFileName(name.getMethodName());
        wiz.getFirstPage().setRulePackageName("com.sample");
        wiz.finish();

        DrlEditor drlEditor = new DrlEditor();
        drlEditor.setText(RULE_RESOURCE_TEXT);
        drlEditor.save();
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testImportCodeCompletion() {
        DrlEditor editor = new DrlEditor();
        editor.setPosition(2, 0);

        ContentAssist assist = editor.createContentAssist();
        assist.selectItem("import");

        editor.writeText("message");
        assist = editor.createContentAssist();

        assist.selectItem("Message - com.sample.domain");

        Assert.assertEquals("Wrong line content", "import com.sample.domain.Message", editor.getTextOnCurrentLine());
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testRuleCodeCompletion() {
        DrlEditor editor = new DrlEditor();
        editor.setPosition(2, 0);

        editor.writeText("import com.sample.domain.Message\n\n");

        ContentAssist assist = editor.createContentAssist();
        assist.selectItem("rule");

        Assert.assertEquals("Line does not contain rule definition", "rule \"new rule\"", editor.getTextOnCurrentLine());

        editor.setPosition(2, 2);
        assist = editor.createContentAssist();
        assist.selectItem("Message");

        Assert.assertEquals("Line does not contain condition", "\t\tMessage(  )", editor.getTextOnCurrentLine());
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testShowReteTree() {
        DrlEditor rulesEditor = new DrlEditor();

        rulesEditor.showReteTree();
        SWTUtils.captureScreenshot("REVIEW-rete-tree.png");

        rulesEditor.close();
    }
}
