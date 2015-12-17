/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.bot.test.smoke;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.ui.bot.ext.helper.ContentAssistHelper;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.junit.Test;
/** * Test Code Completion functionality of JSF components within xhtml page
 * @author Vladimir Pakan
 *
 */
public class CodeCompletionTest extends JSFAutoTestCase{
  private String compositeComponentDefEditorText;
  private String origCompositeComponentContainerEditorText;
  private TextEditor compositeComponentContainerEditor;
  private TextEditor compositeComponentDefEditor;
  /**
   * Test Code Completion functionality for managed bean
   */
	@Test
	public void testCodeCompletionOfManagedBean() {
		initFaceletsPageTest();
		List<String> expectedProposals = new LinkedList<String>();
		expectedProposals.add("msg");
		expectedProposals.add("person : Person");
		// Check content assist for #{ prefix
		ContentAssistHelper.checkContentAssistContent(getEditor(), 19, 79, expectedProposals, true);
		// Check content assist for #{msg. prefix
		expectedProposals.clear();
		expectedProposals.add("name : String - Person");
		ContentAssistHelper.checkContentAssistContent(getEditor(), 19, 86, expectedProposals, true);
	}
	/**
	 * Test Code Completion functionality for resource
	 */
	@Test
	public void testCodeCompletionOfResource() {
		initFaceletsPageTest();
		// Check content assist for ${. prefix
		getEditor().setCursorPosition(18, 7);
		ContentAssistant contentAssistant = getEditor().openContentAssistant();
		ContentAssistHelper.assertContentAssistantContains(contentAssistant, "msg", false);
		ContentAssistHelper.assertContentAssistantContains(contentAssistant, "person : Person", false);
		contentAssistant.close();
		// Check content assist for ${msg. prefix
		getEditor().setCursorPosition(18, 11);
		contentAssistant = getEditor().openContentAssistant();
		ContentAssistHelper.assertContentAssistantContains(contentAssistant, "greeting", false);
		ContentAssistHelper.assertContentAssistantContains(contentAssistant, "prompt", false);
		contentAssistant.close();
	}
	/**
	 * Test Code Completion functionality of <input> tag attributes within xhtml
	 * page
	 */
	@Test
	public void testCodeCompletionOfInputTagAttributes() {
		initFaceletsPageTest();
		// Check content assist for Input tag attributes
		ContentAssistHelper.checkContentAssistContent(getEditor(), 21, 6, getInputTagProposalList(), true);
		// Check content assist insertion
		String contentAssistToUse = "maxlength";
		ContentAssistHelper.assertContentAssistantContains(getEditor().openContentAssistant(), contentAssistToUse,
				true);
		getEditor().save();
		String expectedInsertedText = contentAssistToUse + "=\"\"";
		assertTrue("Editor has to contain text '" + expectedInsertedText + "' but it doesn't\n" + "Editor Text is\n"
				+ getEditor().getText(), getEditor().getText().contains(expectedInsertedText));
	}
  /**
   * Test Code Completion functionality of <input> tag for jsfc attribute within xhtml page
   */
	@Test
	public void testCodeCompletionOfInputTagForJsfcAttribute() {
		initFaceletsPageTest();
		// check jsfc attribute insertion via Content Assist
		String textToInsert = "<input  />";
		getEditor().insertText(19, 0, textToInsert);
		getEditor().setCursorPosition(19, 7);
		String contentAssistToUse = "jsfc";
		ContentAssistHelper.assertContentAssistantContains(getEditor().openContentAssistant(), contentAssistToUse,
				true);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ESC);
		String expectedInsertedText = "<input " + contentAssistToUse + "=\"\"";
		assertTrue("Editor has to contain text '" + expectedInsertedText + "' but it doesn't\n" + "Editor Text is\n"
				+ getEditor().getText(), getEditor().getText().contains(expectedInsertedText));
		// check jsfc attribute value Content Assist menu Content
		ContentAssistHelper.checkContentAssistContent(getEditor(), 19, 13, getJsfcAttributeValueProposalList(), true);
		// check jsfc attribute value insertion via Content Assist
		contentAssistToUse = "h:inputText";
		ContentAssistHelper.assertContentAssistantContains(getEditor().openContentAssistant(), contentAssistToUse,
				true);
		expectedInsertedText = "<input jsfc=\"" + contentAssistToUse + "\"";
		assertTrue("Editor has to contain text '" + expectedInsertedText + "' but it doesn't\n" + "Editor Text is\n"
				+ getEditor().getText(), getEditor().getText().contains(expectedInsertedText));
		getEditor().save();
		// check Content Assist content of jsfc attribute attribute
		ContentAssistHelper.checkContentAssistContent(getEditor(), 19, 26,
				getJsfcAttributeValueAttributesProposalList(), true);
		// check jsfc attribute value attribute insertion via Content Assist
		String contentAssistAttributeToUse = "accept";
		ContentAssistHelper.assertContentAssistantContains(getEditor().openContentAssistant(), contentAssistAttributeToUse,
				true);
		expectedInsertedText = "<input jsfc=\"" + contentAssistToUse + "\" " + contentAssistAttributeToUse + "=\"\"";
		assertTrue("Editor has to contain text '" + expectedInsertedText + "' but it doesn't\n" + "Editor Text is\n"
				+ getEditor().getText(), getEditor().getText().contains(expectedInsertedText));
		getEditor().save();
	}
	  /**
   * Test Code Completion functionality for Composite Component
   */
	@Test
	public void testCodeCompletionOfCompositeComponent() {
		initJSF2PageTest();
		String textToInsert = "<ez:";
		compositeComponentContainerEditor.insertText(15, 0, textToInsert);
		compositeComponentContainerEditor.setCursorPosition(15, textToInsert.length());
		compositeComponentContainerEditor.save();
		new WaitWhile(new JobIsRunning());
		// Check content assist menu content for "<ez:""ez:input"
		ContentAssistHelper.assertContentAssistantContains(compositeComponentContainerEditor.openContentAssistant(), "ez:input", true);
		String currentLineText = compositeComponentContainerEditor.getTextAtLine(15);
		String expectedInsertedText = "<ez:input value=\"\" action=\"\"></ez:input>";
		if (!currentLineText.toLowerCase().contains(expectedInsertedText.toLowerCase())) {
			expectedInsertedText = "<ez:input action=\"\" value=\"\"></ez:input>";
			assertTrue(
					"Inserted text should be " + expectedInsertedText + " but is not.\n" + "Current line text is "
							+ currentLineText,
					currentLineText.toLowerCase().contains(expectedInsertedText.toLowerCase()));
		}
		// Check content assist menu content for Composite Components attributes
		ContentAssistHelper.checkContentAssistContent(compositeComponentContainerEditor, 15, 10,
				getCompositeComponentsAttributesProposalList(), true);
		// Open Composite Component definition file
		String compositeComponentFileName = "input.xhtml";
		OpenOnHelper.checkOpenOnFileIsOpened(compositeComponentContainerEditor, 15, 5, compositeComponentFileName);
		compositeComponentDefEditor = new TextEditor(compositeComponentFileName);
		compositeComponentDefEditorText = compositeComponentDefEditor.getText();
		textToInsert = "<h:commandButton action=\"";
		compositeComponentDefEditor.insertText(18, 0, textToInsert + "\"/> "); // add closing "/>
		compositeComponentDefEditor.setCursorPosition(18, textToInsert.length());
		// Check content assist menu content for ""<h:commandButton action=""/>"
		ContentAssistHelper.assertContentAssistantContains(compositeComponentDefEditor.openContentAssistant(),
				"cc.attrs", true);
		compositeComponentDefEditor.save();
		currentLineText = compositeComponentDefEditor.getTextAtLine(18);
		expectedInsertedText = "#{cc.attrs}";
		assertTrue("Inserted text should be " + expectedInsertedText + " but is not.\n" + "Current line text is "
				+ currentLineText, currentLineText.toLowerCase().contains(expectedInsertedText.toLowerCase()));
		KeyboardFactory.getKeyboard().type(".");
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ARROW_LEFT);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ESC);
		// Check content assist menu content for Composite Components attributes
		ContentAssistHelper.checkContentAssistContent(compositeComponentDefEditor, 18, 36,
				getCompositeComponentsAttributeDefProposalList(), true);
		// check inserting of "submitlabel" content assist
		String contentAssistToUse = "submitlabel";
		ContentAssistHelper.assertContentAssistantContains(compositeComponentDefEditor.openContentAssistant(),
				contentAssistToUse, true);
		expectedInsertedText = "<h:commandButton action=\"#{cc.attrs." + contentAssistToUse + "}\"";
		assertTrue(
				"Editor has to contain text '" + expectedInsertedText + "' but it doesn't\n" + "Editor Text is\n"
						+ compositeComponentDefEditor.getText(),
				compositeComponentDefEditor.getText().toLowerCase().contains(expectedInsertedText.toLowerCase()));
		compositeComponentDefEditor.save();
	}
  /**
   * Test Code Completion functionality for Managed Bean 
   * referenced via @ManagedBean annotation  
   */
  @Test
  public void testCodeCompletionOfReferencedManagedBean(){
    initJSF2PageTest();
    List<String> expectedProposals = new LinkedList<String>();
    expectedProposals.add("msgs");
    expectedProposals.add("user : User");
    // Check content assist for #{ prefix
    ContentAssistHelper.checkContentAssistContent(compositeComponentContainerEditor,15, 60,expectedProposals,false);
    // Check content assist for #{user. prefix
    expectedProposals.clear();
    expectedProposals.add("name : String - User");
    expectedProposals.add("sayHello() : String - User");
    ContentAssistHelper.checkContentAssistContent(compositeComponentContainerEditor, 15,65,expectedProposals,false);
  }
  
  /**
   * Test Code Completion functionality for msgs[
   */
  @Test
  public void testCodeCompletionOfMsgsWithBrackets(){
    initFaceletsPageTest();
    getEditor().insertText(16,0,"\n");
    final String textToInsert = "<h:outputText value=\"#{msg";
    getEditor().insertText(16,0,textToInsert);
    getEditor().save();
    getEditor().setCursorPosition(16, textToInsert.length());
    // Check Content Assist invoked by typing
    ContentAssistant contentAssistant = getEditor().getAutoContentAssistant(new Runnable() {
		@Override
		public void run() {
			KeyboardFactory.getKeyboard().type("[");
		}
	});
    assertNotNull(contentAssistant);
    String useCodeAssist = "['greeting']";
    ContentAssistHelper.assertContentAssistantContains(contentAssistant,useCodeAssist, false);
    useCodeAssist = "['prompt']";
    ContentAssistHelper.assertContentAssistantContains(contentAssistant,useCodeAssist, false);
    contentAssistant.close();
    // Check Content Assist invoked by Ctrl-Space
    useCodeAssist = "['greeting']";
    contentAssistant = getEditor().openContentAssistant();
    ContentAssistHelper.assertContentAssistantContains(contentAssistant,useCodeAssist, false);
    useCodeAssist = "['prompt']";
    ContentAssistHelper.assertContentAssistantContains(contentAssistant,useCodeAssist, true);
    getEditor().setCursorPosition(16, getEditor().getTextAtLine(16).length());
    final String textToInsertAtEnd = "/>";
    KeyboardFactory.getKeyboard().type(textToInsertAtEnd);
    getEditor().save();
    assertTrue(getEditor().getText().contains(textToInsert + useCodeAssist + "}\"" + textToInsertAtEnd));
  }
  
  /**
   * Test Code Completion functionality of src attribute for tags <link>, <h:link> and <a:loadStyle>
   */
  @Test
  public void testCodeCompletionOfSrcAttribute(){
    initJSF2PageTest();
    addRichFacesToProjectClassPath(JSF2_TEST_PROJECT_NAME);
    compositeComponentContainerEditor.insertLine(2, "xmlns:a4j=\"http://richfaces.org/a4j\"");
    final String linkTag = "<link href=\"";
    compositeComponentContainerEditor.insertLine(14,linkTag + "\"/>\n");
    final String hLinkTag = "<h:link value=\"";
    compositeComponentContainerEditor.insertLine(14,hLinkTag + "\"/>\n");
    compositeComponentContainerEditor.save();
    checkCodeCompletionOfSourceAttribute(linkTag);
    checkCodeCompletionOfSourceAttribute(hLinkTag);
  }
	/**
	 * Initialize test which are using facelets test page
	 */
	private void initFaceletsPageTest() {
		EditorHandler.getInstance().closeAll(true);
		openPage(FACELETS_TEST_PAGE, FACELETS_TEST_PROJECT_NAME);
		TextEditor editor = new TextEditor(FACELETS_TEST_PAGE);
		setEditor(editor);
		setEditorText(editor.getText());
		setProjectName(FACELETS_TEST_PROJECT_NAME);
	}
	
  /**
   * Initialize test which are using JSF2 test page
   */
  private void initJSF2PageTest() {
	  EditorHandler.getInstance().closeAll(true);
	  createJSF2Project(JSF2_TEST_PROJECT_NAME);
	  openPage(JSF2_TEST_PAGE, JSF2_TEST_PROJECT_NAME);
	  compositeComponentContainerEditor = new TextEditor(JSF2_TEST_PAGE);
	  origCompositeComponentContainerEditorText = compositeComponentContainerEditor.getText();
	  setProjectName(JSF2_TEST_PROJECT_NAME);
  } 
	/**
	 * Returns list of expected Content Assist proposals for Input tag
	 * @return
	 */
  private static List<String> getInputTagProposalList(){
    LinkedList<String> result = new LinkedList<String>();
    
    result.add("accept");
    result.add("accesskey");
    result.add("align=\"top\" ");
    result.add("alt");
    result.add("checked=\"checked\" ");
    result.add("class");
    result.add("dir=\"ltr\" ");
    result.add("disabled=\"disabled\" ");
    result.add("lang");
    result.add("maxlength");
    result.add("name");
    /*
    result.add("ng-app - ng");
    result.add("ng-bind - ng");
    result.add("ng-bind-html - ng");
    result.add("ng-bind-template - ng");
    result.add("ng-blur - ng");
    result.add("ng-change - ng");
    result.add("ng-checked - ng");
    result.add("ng-class - ng");
    result.add("ng-class-even - ng");
    result.add("ng-class-odd - ng");
    result.add("ng-click - ng");
    result.add("ng-cloak - ng");
    result.add("ng-controller - ng");
    result.add("ng-copy - ng");
    result.add("ng-csp - ng");
    result.add("ng-cut - ng");
    result.add("ng-dblclick - ng");
    result.add("ng-disabled - ng");
    result.add("ng-focus - ng");
    result.add("ng-form - ng");
    result.add("ng-hide - ng");
    result.add("ng-if - ng");
    result.add("ng-include - ng");
    result.add("ng-init - ng");
    result.add("ng-keydown - ng");
    result.add("ng-keypress - ng");
    result.add("ng-keyup - ng");
    result.add("ng-list - ng");
    result.add("ng-model - ng");
    result.add("ng-mousedown - ng");
    result.add("ng-mouseenter - ng");
    result.add("ng-mouseleave - ng");
    result.add("ng-mousemove - ng");
    result.add("ng-mouseover - ng");
    result.add("ng-mouseup - ng");
    result.add("ng-non-bindable - ng");
    result.add("ng-open - ng");
    result.add("ng-paste - ng");
    result.add("ng-pluralize - ng");
    result.add("ng-readonly - ng");
    result.add("ng-repeat - ng");
    result.add("ng-show - ng");
    result.add("ng-style - ng");
    result.add("ng-swipe-left - ngTouch");
    result.add("ng-swipe-right - ngTouch");
    result.add("ng-switch - ng");
    result.add("ng-switch-default - ng");
    result.add("ng-switch-when - ng");
    result.add("ng-transclude - ng");
    result.add("ng-value - ng");
    result.add("ng-view - ngRoute");
    */
    result.add("onblur");
    result.add("onchange");
    result.add("onclick");
    result.add("ondblclick");
    result.add("onfocus");
    result.add("onkeydown");
    result.add("onkeypress");
    result.add("onkeyup");
    result.add("onmousedown");
    result.add("onmousemove");
    result.add("onmouseout");
    result.add("onmouseover");
    result.add("onmouseup");
    result.add("onselect");
    result.add("readonly=\"readonly\" ");
    result.add("size");
    result.add("src");
    result.add("style");
    result.add("tabindex");
    result.add("title");
    result.add("usemap");
    result.add("xml:lang");
    result.add("actionListener");
    result.add("binding");
    result.add("image");
    result.add("immediate");
    result.add("label");
    result.add("list");
    result.add("rendered");
    result.add("styleClass");
        
    return result;
  }
  /**
   * Returns list of expected Content Assist proposals for Jsfc attribute value
   * @return
   */
  private static List<String> getJsfcAttributeValueProposalList(){
    LinkedList<String> result = new LinkedList<String>();
    
    result.add("c:catch");
    result.add("c:choose");
    result.add("c:forEach");
    result.add("c:forTokens");
    result.add("c:if");
    result.add("c:import");
    result.add("c:otherwise");
    result.add("c:out");
    result.add("c:param");
    result.add("c:redirect");
    result.add("c:remove");
    result.add("c:set");
    result.add("c:url");
    result.add("c:when");
    result.add("f:actionListener");
    result.add("f:attribute");
    result.add("f:convertDateTime");
    result.add("f:convertNumber");
    result.add("f:converter");
    result.add("f:facet");
    result.add("f:loadBundle");
    result.add("f:param");
    result.add("f:phaseListener");
    result.add("f:selectItem");
    result.add("f:selectItems");
    result.add("f:setPropertyActionListener");
    result.add("f:subview");
    result.add("f:validateDoubleRange");
    result.add("f:validateLength");
    result.add("f:validateLongRange");
    result.add("f:validator");
    result.add("f:valueChangeListener");
    result.add("f:verbatim");
    result.add("f:view");
    result.add("h:column");
    result.add("h:commandButton");
    result.add("h:commandLink");
    result.add("h:dataTable");
    result.add("h:form");
    result.add("h:graphicImage");
    result.add("h:inputHidden");
    result.add("h:inputSecret");
    result.add("h:inputText");
    result.add("h:inputTextarea");
    result.add("h:message");
    result.add("h:messages");
    result.add("h:outputFormat");
    result.add("h:outputLabel");
    result.add("h:outputLink");
    result.add("h:outputText");
    result.add("h:panelGrid");
    result.add("h:panelGroup");
    result.add("h:selectBooleanCheckbox");
    result.add("h:selectManyCheckbox");
    result.add("h:selectManyListbox");
    result.add("h:selectManyMenu");
    result.add("h:selectOneListbox");
    result.add("h:selectOneMenu");
    result.add("h:selectOneRadio");
    result.add("msg");
    result.add("person : Person");
    return result;
  }
  	@Override
	public void tearDown() throws Exception {
		if (compositeComponentDefEditor != null) {
			compositeComponentDefEditor.setText(compositeComponentDefEditorText);
			compositeComponentDefEditor.save();
			util.waitForNonIgnoredJobs();
			compositeComponentDefEditor.close();
		}
		if (compositeComponentContainerEditor != null) {
			compositeComponentContainerEditor.setText(origCompositeComponentContainerEditorText);
			compositeComponentContainerEditor.save();
			compositeComponentContainerEditor.close();
		}
		new WaitWhile(new JobIsRunning());

		removeRichFacesFromProjectClassPath(JSF2_TEST_PROJECT_NAME);

		super.tearDown();
	}

	  /**
   * Returns list of expected Content Assist proposals for Jsfc attribute value attributes
   * @return
   */
  private static List<String> getJsfcAttributeValueAttributesProposalList(){
    LinkedList<String> result = new LinkedList<String>();
    
    result.add("accept");
    result.add("accesskey");
    result.add("align=\"top\"");
    result.add("alt");
    result.add("checked=\"checked\"");
    result.add("class");
    result.add("dir=\"ltr\"");
    result.add("disabled=\"disabled\"");
    result.add("id");
    result.add("lang");
    result.add("maxlength");
    result.add("name");
    /*
    result.add("ng-app - ng");
    result.add("ng-bind - ng");
    result.add("ng-bind-html - ng");
    result.add("ng-bind-template - ng");
    result.add("ng-blur - ng");
    result.add("ng-change - ng");
    result.add("ng-checked - ng");
    result.add("ng-class - ng");
    result.add("ng-class-even - ng");
    result.add("ng-class-odd - ng");
    result.add("ng-click - ng");
    result.add("ng-cloak - ng");
    result.add("ng-controller - ng");
    result.add("ng-copy - ng");
    result.add("ng-csp - ng");
    result.add("ng-cut - ng");
    result.add("ng-dblclick - ng");
    result.add("ng-disabled - ng");
    result.add("ng-focus - ng");
    result.add("ng-form - ng");
    result.add("ng-hide - ng");
    result.add("ng-if - ng");
    result.add("ng-include - ng");
    result.add("ng-init - ng");
    result.add("ng-keydown - ng");
    result.add("ng-keypress - ng");
    result.add("ng-keyup - ng");
    result.add("ng-list - ng");
    result.add("ng-model - ng");
    result.add("ng-mousedown - ng");
    result.add("ng-mouseenter - ng");
    result.add("ng-mouseleave - ng");
    result.add("ng-mousemove - ng");
    result.add("ng-mouseover - ng");
    result.add("ng-mouseup - ng");
    result.add("ng-non-bindable - ng");
    result.add("ng-open - ng");
    result.add("ng-paste - ng");
    result.add("ng-pluralize - ng");
    result.add("ng-readonly - ng");
    result.add("ng-repeat - ng");
    result.add("ng-show - ng");
    result.add("ng-style - ng");
    result.add("ng-swipe-left - ngTouch");
    result.add("ng-swipe-right - ngTouch");
    result.add("ng-switch - ng");
    result.add("ng-switch-default - ng");
    result.add("ng-switch-when - ng");
    result.add("ng-transclude - ng");
    result.add("ng-value - ng");
    result.add("ng-view - ngRoute");
*/
    result.add("onblur");
    result.add("onchange");
    result.add("onclick");
    result.add("ondblclick");
    result.add("onfocus");
    result.add("onkeydown");
    result.add("onkeypress");
    result.add("onkeyup");
    result.add("onmousedown");
    result.add("onmousemove");
    result.add("onmouseout");
    result.add("onmouseover");
    result.add("onmouseup");
    result.add("onselect");
    result.add("readonly=\"readonly\"");
    result.add("size");
    result.add("src");
    result.add("style");
    result.add("tabindex");
    result.add("title");
    result.add("type=\"text\"");
    result.add("usemap");
    result.add("value");
    result.add("xml:lang");
    result.add("autocomplete");
    result.add("binding");
    result.add("converter");
    result.add("converterMessage");
    result.add("immediate");
    result.add("label");
    result.add("list");
    result.add("rendered");
    result.add("required");
    result.add("requiredMessage");
    result.add("styleClass");
    result.add("validator");
    result.add("validatorMessage");
    result.add("valueChangeListener");
    return result;
  }
  /**
   * Returns list of expected Content Assist proposals for Composite Component Attributes
   * @return
   */
  private static List<String> getCompositeComponentsAttributesProposalList(){
    LinkedList<String> result = new LinkedList<String>();
  /*  
    result.add("ng-app - ng");
    result.add("ng-bind - ng");
    result.add("ng-bind-html - ng");
    result.add("ng-bind-template - ng");
    result.add("ng-class - ng");
    result.add("ng-class-even - ng");
    result.add("ng-class-odd - ng");
    result.add("ng-click - ng");
    result.add("ng-cloak - ng");
    result.add("ng-controller - ng");
    result.add("ng-csp - ng");
    result.add("ng-dblclick - ng");
    result.add("ng-form - ng");
    result.add("ng-hide - ng");
    result.add("ng-if - ng");
    result.add("ng-include - ng");
    result.add("ng-init - ng");
    result.add("ng-keydown - ng");
    result.add("ng-keypress - ng");
    result.add("ng-keyup - ng");
    result.add("ng-mousedown - ng");
    result.add("ng-mouseenter - ng");
    result.add("ng-mouseleave - ng");
    result.add("ng-mousemove - ng");
    result.add("ng-mouseover - ng");
    result.add("ng-mouseup - ng");
    result.add("ng-non-bindable - ng");
    result.add("ng-open - ng");
    result.add("ng-pluralize - ng");
    result.add("ng-repeat - ng");
    result.add("ng-show - ng");
    result.add("ng-style - ng");
    result.add("ng-swipe-left - ngTouch");
    result.add("ng-swipe-right - ngTouch");
    result.add("ng-switch - ng");
    result.add("ng-switch-default - ng");
    result.add("ng-switch-when - ng");
    result.add("ng-transclude - ng");
    result.add("ng-view - ngRoute");
    */
    result.add("id");
    result.add("label");
    result.add("rendered");
    result.add("submitlabel");    
    return result;
  }
  
  /**
   * Returns list of expected Content Assist proposals for Composite Component Attributes
   * within file containing Composite Component definition
   * @return
   */
  private static List<String> getCompositeComponentsAttributeDefProposalList(){
    LinkedList<String> result = new LinkedList<String>();
    
    result.add("action");
    result.add("label");
    result.add("onclick");
    result.add("ondblclick");
    result.add("onkeydown");
    result.add("onkeypress");
    result.add("onkeyup");
    result.add("onmousedown");
    result.add("onmousemove");
    result.add("onmouseout");
    result.add("onmouseover");
    result.add("onmouseup");
    result.add("submitlabel");
    result.add("value");
        
    return result;
  }

	/**
	 * Check Code Completion of src attribute of tagToCheck tag
	 * 
	 * @param tagToCheck
	 */
	private void checkCodeCompletionOfSourceAttribute(String tagToCheck) {
		compositeComponentContainerEditor.setCursorPosition(
			compositeComponentContainerEditor.getPositionOfText(tagToCheck) + tagToCheck.length());
		ContentAssistant contentAssistant = compositeComponentContainerEditor.openContentAssistant();
 		ContentAssistHelper.assertContentAssistantContains(contentAssistant, "/pages", false);
		ContentAssistHelper.assertContentAssistantContains(contentAssistant, "/resources", false);
		ContentAssistHelper.assertContentAssistantContains(contentAssistant, "/templates", false);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.ESC);
	}
}