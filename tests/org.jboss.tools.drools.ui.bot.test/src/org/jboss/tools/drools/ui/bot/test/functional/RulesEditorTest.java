package org.jboss.tools.drools.ui.bot.test.functional;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.drools.reddeer.editor.ContentAssist;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.wizard.NewRuleResourceWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

/*
 * TODO star imports (com.sample.domain.*)
 * TODO function imports
 * TODO globals
 * TODO attributes
 * TODO declares
 * TODO queries
 * TODO advanced stuff (inline cast, group accessors)
 */
@RunWith(RedDeerSuite.class)
public class RulesEditorTest extends TestParent {
    public static final String MESSAGE_TEXT = getTemplateText("MessageClass");
    public static final String RULE_RESOURCE_TEXT = getTemplateText("DummyRuleFile");

    private int errors, warnings;

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

        ProblemsView problems = new ProblemsView();
        problems.open();
        errors = problems.getAllErrors().size();
        warnings = problems.getAllWarnings().size();

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
        Assert.assertTrue("Import is not in content assist!", assist.getItems().contains("import"));
        assist.selectItem("import");

        editor.writeText("message");
        assist = editor.createContentAssist();

        Assert.assertTrue("Message class is not in content assist!", assist.getItems().contains("Message - com.sample.domain"));
        assist.selectItem("Message - com.sample.domain");

        Assert.assertEquals("Wrong line content", "import com.sample.domain.Message", editor.getTextOnCurrentLine());

        editor.save();
        ProblemsView problems = new ProblemsView();
        problems.open();
        Assert.assertEquals("New errors occured!", errors, problems.getAllErrors().size());
        Assert.assertEquals("New errors occured!", warnings, problems.getAllWarnings().size());
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testFactTypeCompletion() {
        DrlEditor editor = new DrlEditor();
        editor.setPosition(2, 0);

        editor.writeText("import com.sample.domain.Message\n\n");

        ContentAssist assist = editor.createContentAssist();
        Assert.assertTrue("Rule is not in content assist!", assist.getItems().contains("rule"));
        assist.selectItem("rule");

        Assert.assertEquals("Line does not contain rule definition", "rule \"new rule\"", editor.getTextOnCurrentLine());

        editor.setPosition(6, 2);
        assist = editor.createContentAssist();
        Assert.assertTrue("Message is not in content assist!", assist.getItems().contains("Message"));
        assist.selectItem("Message");

        Assert.assertEquals("Line does not contain condition", "\t\tMessage(  )", editor.getTextOnCurrentLine());

        editor.save();
        ProblemsView problems = new ProblemsView();
        problems.open();
        Assert.assertEquals("New errors occured!", errors, problems.getAllErrors().size());
        Assert.assertEquals("New errors occured!", warnings, problems.getAllWarnings().size());
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testConstraintsCompletion() {
        DrlEditor editor = new DrlEditor();
        editor.setPosition(2, 0);
        editor.writeText("import com.sample.domain.Message\n\nrule newRule\n\twhen\n\t\tMessage()\n\tthen\nend\n");

        editor.setPosition(6, 10);

        ContentAssist assist = editor.createContentAssist();
        List<String> items = assist.getItems();
        Assert.assertTrue("Text field is not available", items.contains("text"));
        Assert.assertTrue("Parameter field is not available", items.contains("parameter"));
        Assert.assertTrue("Parameterized field is not available", items.contains("parameterized"));

        assist.selectItem("text");
        Assert.assertEquals("Wrong text inserted", "\t\tMessage( text  )", editor.getTextOnCurrentLine());

        // finish the constraint
        editor.writeText("!= null, ");

        assist = editor.createContentAssist();
        items = assist.getItems();
        Assert.assertTrue("Text field is not available as second constraint", items.contains("text"));
        Assert.assertTrue("Parameter field is not available as second constraint", items.contains("parameter"));
        Assert.assertTrue("Parameterized field is not available as second constraint", items.contains("parameterized"));
        assist.close();

        editor.writeText("$var: ");
        assist = editor.createContentAssist();
        items = assist.getItems();
        Assert.assertTrue("Text field is not available for variable assignment", items.contains("text"));
        Assert.assertTrue("Parameter field is not available for variable assignment", items.contains("parameter"));
        Assert.assertTrue("Parameterized field is not available for variable assignment", items.contains("parameterized"));

        editor.save();
        ProblemsView problems = new ProblemsView();
        problems.open();
        Assert.assertEquals("New errors occured!", errors, problems.getAllErrors().size());
        Assert.assertEquals("New errors occured!", warnings, problems.getAllWarnings().size());
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testConsequencesCompletion() {
        DrlEditor editor = new DrlEditor();
        editor.setPosition(2, 2);
        editor.writeText("import com.sample.domain.Message\n\nrule newRule\n\twhen\n\t\t$msg: Message()\n\tthen\n\t\t\nend\n");
        editor.setPosition(8, 2);

        ContentAssist assist = editor.createContentAssist();
        List<String> items = assist.getItems();
        Assert.assertTrue("Variable kcontext is unavailable", items.contains("kcontext : RuleContext"));
        Assert.assertTrue("Variable $msg is unavailable", items.contains("$msg : Message"));

        assist.selectItem("kcontext : RuleContext");
        editor.writeText(".");
        assist = editor.createContentAssist();
        items = assist.getItems();
        // 3*wait + notify + notifyAll + toString + hashCode + equals + getClass
        Assert.assertTrue("kcontext methods are not available", items.size() > 9);
        Assert.assertTrue("kcontext is missing method getRule()", items.contains("getRule() : Rule - RuleContext"));
        assist.selectItem("getRule() : Rule - RuleContext");
        Assert.assertEquals("Wrong text", "kcontext.getRule()", editor.getTextOnCurrentLine());

        editor.writeText(";\n");
        assist = editor.createContentAssist();
        items = assist.getItems();
        Assert.assertTrue("Variable kcontext is unavailable", items.contains("kcontext : RuleContext"));
        Assert.assertTrue("Variable $msg is unavailable", items.contains("$msg : Message"));
        assist.selectItem("$msg : Message");
        editor.writeText(".");
        assist = editor.createContentAssist();
        items = assist.getItems();
        Assert.assertEquals("Some Message methods are missing", 15, items.size());
        Assert.assertTrue("Method setParameter() is unavailable", items.contains("setParameter(Object parameter) : void - Message"));
        assist.selectItem("setParameter(Object parameter) : void - Message");

        editor.writeText("Message.");
        assist = editor.createContentAssist();
        items = assist.getItems();
        Assert.assertEquals("Some Message methods are missing", 3, items.size());
        Assert.assertTrue("Constant NO_PARAMETER is missing", items.contains("NO_PARAMETER : Object - Message"));
        assist.selectItem("NO_PARAMETER : Object - Message");
        Assert.assertEquals("Wrong text", "$msg.setParameter(Message.NO_PARAMETER);", editor.getTextOnCurrentLine());

        editor.save();
        ProblemsView problems = new ProblemsView();
        problems.open();
        Assert.assertEquals("New errors occured!", errors, problems.getAllErrors().size());
        Assert.assertEquals("New errors occured!", warnings, problems.getAllWarnings().size());
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
