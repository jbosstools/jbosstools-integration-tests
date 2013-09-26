package org.jboss.tools.drools.ui.bot.test.functional;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.editor.TextEditor;
import org.jboss.tools.drools.reddeer.dialog.DslLineDialog;
import org.jboss.tools.drools.reddeer.dialog.DslLineDialog.Scope;
import org.jboss.tools.drools.reddeer.editor.DslEditor;
import org.jboss.tools.drools.reddeer.editor.DslEditor.DslLine;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultRuntime;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class DslEditorTest extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(DslEditorTest.class);

    @Before
    public void createDefaultDsl() {
        NewDslWizard wiz = new NewDslWizard();
        wiz.open();
        wiz.getFirstPage().setParentFolder(DEFAULT_PROJECT_NAME + "/" + RESOURCES_LOCATION);
        wiz.getFirstPage().setFileName(name.getMethodName());
        wiz.getSamplesPage().setAddSampleDsl(true);
        wiz.finish();
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testOpen() {
        Project project = new PackageExplorer().getProject(DEFAULT_PROJECT_NAME);
        project.getProjectItem(RESOURCES_LOCATION, name.getMethodName() + ".dsl").open();

        DslEditor editor = new DslEditor();
        Assert.assertNotSame("No DSL lines were generated", 0, editor.getDslLines().size());
        for (DslLine line : editor.getDslLines()) {
            LOGGER.info(line);
        }
    }

    @Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testAddExpression() {
        Project project = new PackageExplorer().getProject(DEFAULT_PROJECT_NAME);
        project.getProjectItem(RESOURCES_LOCATION, name.getMethodName() + ".dsl").open();

        DslEditor editor = new DslEditor();
        List<DslLine> origLines = editor.getDslLines();

        final String mapping = "System.out.println(\"Hello World!\");";
        final String expression = "Print Hello";

        // add a new line
        DslLineDialog dialog = editor.add();
        dialog.setRuleMapping(mapping);
        dialog.setLanguageExpression(expression);
        dialog.setScope(Scope.CONSEQUENCE);
        dialog.ok();

        // FIXME this is annoying - closing dialog makes editor loose focus and nothing works then
        project = new PackageExplorer().getProject(DEFAULT_PROJECT_NAME);
        project.getProjectItem(RESOURCES_LOCATION, name.getMethodName() + ".dsl").open();

        List<DslLine> newLines = editor.getDslLines();
        Assert.assertEquals("Line count is same!", origLines.size() + 1, newLines.size());

        DslLine line = newLines.get(newLines.size() - 1);
        Assert.assertEquals("Wrong mapping!", mapping, line.getMapping());
        Assert.assertEquals("Wrong expression!", expression, line.getExpression());
        Assert.assertEquals("Wrong scope!", Scope.CONSEQUENCE.toEditorString(), line.getScope());

        editor.close(true);
    }

    //@Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testUseDsl() {
        TextEditor txt = new TextEditor();
        txt.getText();
    }

    //@Test
    @UsePerspective(JavaPerspective.class) @UseDefaultRuntime @UseDefaultProject
    public void testRunDslRules() {
        
    }
}
