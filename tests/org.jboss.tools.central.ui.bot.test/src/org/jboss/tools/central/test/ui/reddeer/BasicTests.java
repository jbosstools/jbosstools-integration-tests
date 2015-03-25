package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.jboss.reddeer.eclipse.ui.browser.BrowserEditor;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.menu.ToolItemMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.swt.lookup.WidgetLookup;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.uiforms.api.FormText;
import org.jboss.reddeer.uiforms.api.Section;
import org.jboss.reddeer.uiforms.impl.formtext.DefaultFormText;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.central.editors.xpl.TextSearchControl;
import org.junit.Before;
import org.junit.Test;

public class BasicTests {

	@Before
	public void setup() {
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		// activate central editor
		new DefaultEditor("JBoss Central");
	}
	
	@Test
	public void documentationTest() {
		checkDocumentationLink("JBoss developer website",
				"JBoss Developer");
		checkDocumentationLink("User Forum", "Space: JBoss Tools | Community");
		checkDocumentationLink("Developer Forum",
				"Space: JBoss Tools Development | Community");
		checkDocumentationLink("Product documentation",
				"JBoss Tools - Documentation");
		checkDocumentationLink("Videos", "JBoss Tools - Video Tutorials");
		KeyboardFactory.getKeyboard().type('\r');
	}

	@Test
	public void jBossToolsHomeToolItemTest() {
		new DefaultToolItem("JBoss Tools Home").click();
		checkBrowserOpenedAndLoaded("JBoss Tools - Home");
	}
	
	@Test
	public void favoriteToolItemTest() {
		new DefaultToolItem(new WithTooltipTextMatcher(new RegexMatcher("Favorite this.*"))).click();
		checkBrowserOpenedAndLoaded(".*Eclipse Marketplace.*");
	}
	
	@Test
	public void newToolItemTest(){
		DefaultToolItem item = new DefaultToolItem("New");
		item.click();
		new DefaultShell("New").close();
		
		new ToolItemMenu(item, "Other...").select();
		new DefaultShell("New").close();
	}

	@Test
	public void projectExamplesToolItemTest(){
		new DefaultToolItem("Project Examples Wizard").click();
		new DefaultShell("New Project Example").close();
	}
	
	@Test
	public void runtimeDetectionToolItemTest(){
		new DefaultToolItem("JBoss Runtime Detection").click();
		DefaultShell defaultShell = new DefaultShell("Preferences");
		new DefaultCLabel("JBoss Runtime Detection");
		defaultShell.close();
	}
	
	@Test
	public void wtpRuntimeToolItemTest(){
		new DefaultToolItem("WTP Runtime Preferences").click();
		DefaultShell defaultShell = new DefaultShell("Preferences");
		new DefaultCLabel("Server Runtime Environments");
		defaultShell.close();
	}
	
	@Test
	public void refreshToolItemTest(){
		new DefaultToolItem(0, "Refresh").click();
		new DefaultToolItem(1, "Refresh").click();
		new WaitWhile(new JobIsRunning());
	}
	
	@Test
	public void buzzToolItemTest(){
		new DefaultToolItem("JBoss Buzz").click();
		checkBrowserOpenedAndLoaded("JBoss Buzz.*");
	}
	
	@Test
	public void twitterToolItemTest(){
		new DefaultToolItem("Twitter"); //do not click -> it opens external browser
	}
	
	@Test
	public void buzzFeedIsNotEmptyTest(){
		DefaultSection buzzSection = new DefaultSection("JBoss Buzz");
		List<FormText> texts = getAllFormTextForSection(buzzSection);
		assertNotEquals(texts.size(), 0);
	}
	
	@Test
	public void ticketMonsterLinkTest(){
		DefaultSection tutorialSection = new DefaultSection("Tutorial");
		DefaultHyperlink link = new DefaultHyperlink(tutorialSection);
		link.activate();
		checkBrowserOpenedAndLoaded("Ticket Monster tutorial");
	}
	
	@Test
	public void searchTest(){
		TextSearchControlComposite searchComposite = new TextSearchControlComposite();
		Text text = new DefaultText(searchComposite);
		text.setText("Pokus");
		KeyboardFactory.getKeyboard().type(SWT.CR);
		checkBrowserOpenedAndLoaded("Pokus.*");
	}
	
	private void checkDocumentationLink(String linkText, String title) {
		new DefaultHyperlink(linkText).activate();
		checkBrowserOpenedAndLoaded(title);
	}
	
	private void checkBrowserOpenedAndLoaded(String title){
		new WaitWhile(new JobIsRunning());
		BrowserEditor browser = new BrowserEditor(new RegexMatcher(title));
		browser.close();
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CTRL, SWT.SPACE);
	}
	
	private List<FormText> getAllFormTextForSection(Section section) {
		List<FormText> list = new ArrayList<FormText>();
		boolean found = true;
		int index = 0;
		do {
			try {
				list.add(new DefaultFormText(section, index++));
			} catch (SWTLayerException e) {
				found = false;
			}
		} while (found);
		return list;
	}
	
	private class TextSearchControlComposite implements ReferencedComposite{
		
		TextSearchControl widget;
		
		public TextSearchControlComposite() {
			try{
				widget = WidgetLookup.getInstance().activeWidget(new WorkbenchShell(), TextSearchControl.class, 0);
			}catch(SWTLayerException e){
				DefaultEditor defaultEditor = new DefaultEditor();
				widget = WidgetLookup.getInstance().activeWidget(new WorkbenchShell(), TextSearchControl.class, 0);
				defaultEditor.maximize();
			}
		}
		
		@Override
		public Control getControl() {
			return widget;
		}
	}
}
