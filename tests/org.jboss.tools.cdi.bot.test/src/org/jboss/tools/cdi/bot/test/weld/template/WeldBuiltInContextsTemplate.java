package org.jboss.tools.cdi.bot.test.weld.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.jboss.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Before;
import org.junit.Test;

//based on JBIDE-12645
public class WeldBuiltInContextsTemplate extends CDITestBase{
	
	public static final String WELD_API_JAR=System.getProperty("jbosstools.test.weld-api.home");
	protected String CDIVersion;
	
	@Before
	public void addLibs(){
		disableSourceLookup();
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if(pe.getProject(PROJECT_NAME).containsResource("Java Resources","src","test","BuiltInContexts.java")){
			return;
		}
		NewClassCreationWizard jd = new NewClassCreationWizard();
		jd.open();
		NewClassWizardPage jp = new NewClassWizardPage();
		jp.setPackage("test");
		jp.setName("BuiltInContexts");
		jd.finish();
		projectHelper.addLibrariesIntoProject(PROJECT_NAME, WELD_API_JAR);
		editResourceUtil.replaceClassContentByResource("BuiltInContexts.java", readFile("resources/cdi11/BuiltInContexts.jav_"), false);
	}
	
	@Test
	public void testBuildInContextsWarnings(){
		TextEditor te = new TextEditor("BuiltInContexts.java");
		new WaitUntil(new EditorHasValidationMarkers(te));
		List<Marker> markers = te.getMarkers();
		assertEquals(3,markers.size());
		for(Marker m: te.getMarkers()){
			assertEquals("org.eclipse.ui.workbench.texteditor.warning",m.getType());
			String JSRspec = "[JSR-346 §5.2.2]";
			if("1.0".equals(CDIVersion)){
				JSRspec = "[JSR-299 §5.2.1]";
			}
			assertEquals("Multiple beans are eligible for injection to the injection point "+JSRspec, m.getText());
			assertTrue(m.getLineNumber() == 70 || m.getLineNumber() == 73 || m.getLineNumber() == 76);
		}
	}
	
	@Test
	public void testBuildInContextsOpenOns(){
		openOn("dependentContext", "DependentContext");
		openOn("applicationContext", "ApplicationContext");
		openOn("singletonContext", "SingletonContext");
		openOn("unboundRequestContext", "RequestContext");
		openOn("bboundRequestContext", "BoundRequestContext");
		openOn("bboundRequestContext2", "BoundRequestContext");
		openOn("httpRequestContext", "HttpRequestContext");
		openOn("httpRequestContext2", "HttpRequestContext");
		openOn("ejbRequestContext", "EjbRequestContext");
		openOn("ejbRequestContext2", "EjbRequestContext");
		
		openOn("boundConversationContext", "BoundConversationContext");
		openOn("boundConversationContext2", "BoundConversationContext");
		openOn("httpConversationContext", "HttpConversationContext");
		openOn("httpConversationContext2", "HttpConversationContext");
		openOn("boundSessionContext", "BoundSessionContext");
		openOn("boundSessionContext2", "BoundSessionContext");
		
		openOn("httpSessionContext", "HttpSessionContext");
		openOn("httpSessionContext2", "HttpSessionContext");
	}
	
	private void openOn(String text, String proposal){
		TextEditor te = new TextEditor("BuiltInContexts.java");
		te.selectText(text);
		te.openOpenOnAssistant().chooseProposal("Open @Inject Bean "+proposal);
		new DefaultEditor(proposal+".class");
	}
	
	
}
