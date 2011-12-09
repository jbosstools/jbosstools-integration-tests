package org.jboss.tools.vpe.ui.bot.test.editor.preferences;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.parts.SWTBotTableExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.JobName;
import org.jboss.tools.vpe.ui.bot.test.VPEAutoTestCase;
import org.jboss.tools.vpe.ui.bot.test.editor.VPEEditorTestCase;
import org.jboss.tools.vpe.ui.bot.test.tools.SWTBotWebBrowser;
/**
 * Tests Global EL Variables Definition
 * @author vpakan
 *
 */
public class GlobalELVariablesTest extends VPEEditorTestCase{
	
  private static final String elName = "user.name";
  private static final String elValue = "!!TestELValue!!";
  private static String FACELET_PROJECT_XHTML_FILE_NAME = "globalELVariablesTest.xhtml";
  private SWTBotExt botExt = null;
  
  public GlobalELVariablesTest() {
    super();
    botExt = new SWTBotExt();
  }
  
	public void testGlobalELVariables() throws Throwable{
	  open.preferenceOpen(ActionItem.Preference.JBossTools.LABEL);
	  SWTBotTree preferenceTree = this.bot.tree();
    preferenceTree
      .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS)
      .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB)
      .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_EXPRESSION_LANGUAGE)
      .select(IDELabel.PreferencesDialog.JBOSS_TOOLS_VARIABLES);
    bot.button(IDELabel.Button.ADD_WITHOUT_DOTS).click();
    bot.shell(IDELabel.Shell.ADD_EL_REFERENCE).activate();
    bot.textWithLabel(IDELabel.AddELReferenceDialog.EL_NAME).setText(GlobalELVariablesTest.elName);
    bot.textWithLabel(IDELabel.AddELReferenceDialog.VALUE).setText(GlobalELVariablesTest.elValue);
    bot.button(IDELabel.Button.FINISH).click();
    bot.shell(IDELabel.Shell.PREFERENCES).activate();
    bot.button(IDELabel.Button.OK).click();
	  openPage(VPEAutoTestCase.TEST_PAGE);
	  // Create XHTML File in Facelet Project
	  SWTBotTree tree = packageExplorer.show().bot().tree();
    tree.expandNode(VPEAutoTestCase.FACELETS_TEST_PROJECT_NAME)
      .expandNode("WebContent")
      .getNode("pages")
      .select();
    // add JSP
    open.newObject(ActionItem.NewObject.JBossToolsWebXHTMLFile.LABEL);
    bot.shell(IDELabel.Shell.NEW_XHTML_FILE).activate(); //$NON-NLS-1$
    bot.textWithLabel(ActionItem.NewObject.JBossToolsWebXHTMLFile.TEXT_FILE_NAME)
      .setText(GlobalELVariablesTest.FACELET_PROJECT_XHTML_FILE_NAME); //$NON-NLS-1$
    bot.button(IDELabel.Button.FINISH).click(); //$NON-NLS-1$
    bot.sleep(Timing.time3S());
    util.waitForJobs(JobName.BUILDING_WS);
    SWTBotEditor xhtmlEditor = bot.editorByTitle(GlobalELVariablesTest.FACELET_PROJECT_XHTML_FILE_NAME);
    xhtmlEditor.toTextEditor().setText("<html xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
      "           xmlns:h=\"http://java.sun.com/jsf/html\">\n" + 
      "  <body>\n" +
      "    <h:outputText value=\"#{user.name}\"/>\n" + 
      "  </body>\n" +
      "</html>");
    xhtmlEditor.save();
    bot.sleep(Timing.time5S());
    assertVisualEditorContainsNodeWithValue(new SWTBotWebBrowser(GlobalELVariablesTest.FACELET_PROJECT_XHTML_FILE_NAME, botExt),
        GlobalELVariablesTest.elValue,
        GlobalELVariablesTest.FACELET_PROJECT_XHTML_FILE_NAME);
    xhtmlEditor.close();
    SWTBotEditor jspEditor = bot.editorByTitle(VPEAutoTestCase.TEST_PAGE);
    jspEditor.show();
    bot.sleep(Timing.time5S());
    assertVisualEditorContains(new SWTBotWebBrowser(VPEAutoTestCase.TEST_PAGE, botExt),
        "INPUT",
        new String[] {"value"},
        new String[] {GlobalELVariablesTest.elValue},
        VPEAutoTestCase.TEST_PAGE);
    jspEditor.close();
    
	}

  @Override
public void tearDown() throws Exception {
    open.preferenceOpen(ActionItem.Preference.JBossTools.LABEL);
    SWTBotTree preferenceTree = this.bot.tree();
    preferenceTree.expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS)
      .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_WEB)
      .expandNode(IDELabel.PreferencesDialog.JBOSS_TOOLS_EXPRESSION_LANGUAGE)
      .select(IDELabel.PreferencesDialog.JBOSS_TOOLS_VARIABLES);;
    
    SWTBotTableItem tiEL = new SWTBotTableExt(bot.table()).getTableItem(new String[] {"Global",
        GlobalELVariablesTest.elName,  
      GlobalELVariablesTest.elValue});
    if (tiEL != null){
      tiEL.select();
      bot.sleep(Timing.time2S());
      bot.button(IDELabel.Button.REMOVE).click();      
    }
    bot.button(IDELabel.Button.OK).click();
    super.tearDown();
  }
}
