 /*******************************************************************************
  * Copyright (c) 2007-2015 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jsf.ui.bot.test.cssdialog.jbide;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.handler.TreeItemHandler;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.jst.reddeer.wst.css.ui.wizard.NewCSSFileWizardPage;
import org.jboss.tools.jst.reddeer.wst.css.ui.wizard.NewCSSWizardDialog;
import org.jboss.tools.ui.bot.test.WidgetVariables;
import org.junit.Test;

public class CSSSelectorJBIDE3288 extends JSFAutoTestCase{

	private final static String CSS_FILE_NAME = "CSSSelectorJBIDE3288"; //$NON-NLS-1$
	private final static String CSS_CLASS1_NAME = ".cssclass1"; //$NON-NLS-1$
	private final static String CSS_CLASS2_NAME = ".cssclass2"; //$NON-NLS-1$
	private final static String css1Attrs = "\r\ncolor:red\r\n"; //$NON-NLS-1$
	private final static String css2Attrs = "\r\nbackground-color:green\r\n"; //$NON-NLS-1$
	@Test
	public void testCSSSelector(){
		createCSSPage();
		createCSSClass();
		openTestPage();
		linkCSSFile();
		selectTestElement();
		openCSSSelectorDialog();
		checkSelector();
		chooseStyleClass();
	}

	private void createCSSPage() {
		packageExplorer.open();
		try {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent", CSS_FILE_NAME + ".css")
					.open();
			new TextEditor(CSS_FILE_NAME + ".css").setText("@CHARSET \"UTF-8\";");
		} catch (EclipseLayerException ele) {
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).getProjectItem("WebContent").select();
			NewCSSWizardDialog newCSSWizardDialog = new NewCSSWizardDialog();
			newCSSWizardDialog.open();
			new NewCSSFileWizardPage().setFileName(CSS_FILE_NAME);
			newCSSWizardDialog.finish();
		}
		TextEditor editor = new TextEditor(CSS_FILE_NAME + ".css");
		editor.save();
	}
	
	private void createCSSClass(){
		TextEditor editor = new TextEditor(CSS_FILE_NAME + ".css");
		editor.activate();
		editor.insertText(0,0,CSS_CLASS1_NAME+"{"+css1Attrs+"}"+"\n"+CSS_CLASS2_NAME+"{"+css2Attrs+"}");
		editor.save();
	}
	
	private void linkCSSFile(){
		setEditor(new TextEditor(TEST_PAGE));
		setEditorText(getEditor().getText());
		getEditor().insertText(6,10,"<link href=\"/" + CSS_FILE_NAME + ".css\" type=\"text/css\"/>");
		getEditor().save();
	}
	
	private void selectTestElement(){
		getEditor().setCursorPosition(12, 19);
		getEditor().selectText("h:out");
		new WaitWhile(new JobIsRunning());
	}
	
	private void openCSSSelectorDialog(){
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		TreeItem tiStyleClass = propertiesView.getProperty("General","styleClass").getTreeItem();
		tiStyleClass.select();
		TreeItemHandler.getInstance().click(tiStyleClass.getSWTWidget()); 
		new PushButton("...").click();
	}
	
	private void checkSelector(){
		new DefaultShell(WidgetVariables.CSS_SELECTOR_DIALOG_TITLE);
		new DefaultTreeItem("/" + CSS_FILE_NAME+".css","cssclass1").select();
		assertEquals(".cssclass1 {\n\tcolor: red\n}", new DefaultStyledText().getText());
		new PushButton(new WithTooltipTextMatcher("Add CSS class")).click();
		new OkButton().click();
	}
	
	private void chooseStyleClass(){
		getEditor().activate();
		assertVisualEditorContainsString(new InternalBrowser(), 
			"\t\t<h1><h:outputText value=\"#{Message.header}\" styleClass=\"cssclass1\"/></h1>", 
			TEST_PAGE);
	}
	
}
