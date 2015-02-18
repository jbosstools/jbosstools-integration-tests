package org.jboss.tools.cdi.bot.test.extensions;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDI11TestBase;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.cdi.reddeer.uiutils.CDIWizardHelper;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.junit.Before;
import org.junit.Test;

//based on JBIDE-14765
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class WeldExclude extends CDI11TestBase{
	
	public static final String WELD_API_JAR=System.getProperty("jbosstools.test.weld-api.home");
	
	@Before
	public void prepareWorkspace1(){
		excludeInBeansXml();
		createBeans();
		
	}
	
	private void createBeans(){
		CDIWizardHelper wh = new CDIWizardHelper();
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean1", "exclude.p1", "@ApplicationScoped");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean2", "exclude.p1", "@ApplicationScoped");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean3", "exclude.p2", "@ApplicationScoped");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean4", "exclude.p2.p3", "@ApplicationScoped");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean5", "exclude.p4", "@ApplicationScoped");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean6", "exclude.p4.p5", "@ApplicationScoped");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean7", "exclude.p6", "@ApplicationScoped");
	}
	
	private void excludeInBeansXml() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateSourcePage();
		EditorResourceHelper eh = new EditorResourceHelper();
		try {
			eh.replaceClassContentByResource("beans.xml", new FileInputStream("resources/excluded/beans.xm_"), false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		beans.close(true);
	}
	
	@Test
	public void testExcludedBeans(){
		NewJavaClassWizardDialog jd = new NewJavaClassWizardDialog();
		jd.open();
		NewJavaClassWizardPage jp = jd.getFirstPage();
		jp.setName("TestExcluded");
		jp.setPackage("test");
		jd.finish();
		
		TextEditor ed = new TextEditor("TestExcluded.java");
		EditorResourceHelper eh = new EditorResourceHelper();
		try {
			eh.replaceClassContentByResource("TestExcluded.java", new FileInputStream("resources/excluded/TestExcluded.jav_"), false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ed.save();
		AbstractWait.sleep(TimePeriod.NORMAL);
		String warning = "org.eclipse.ui.workbench.texteditor.warning";
		String noInjection = "No bean is eligible for injection to the injection point [JSR-346 §5.2.2]";
		List<Marker> markers = ed.getMarkers();
		assertEquals(4, markers.size());
		
		for(Marker m: ed.getMarkers()){
			
			assertTrue((m.getLineNumber() == 14 && m.getType().equals(warning) && m.getText().equals(noInjection)) ||
					(m.getLineNumber() == 16 && m.getType().equals(warning) && m.getText().equals(noInjection)) ||
					(m.getLineNumber() == 18 && m.getType().equals(warning) && m.getText().equals(noInjection)) ||
					(m.getLineNumber() == 19 && m.getType().equals(warning) && m.getText().equals(noInjection)) );
		}
		
		checkProposal("bean2","Bean2");
		//checkProposal("bean4","Bean4");
		//checkProposal("bean7","Bean7");
		
	}
	
	private void checkProposal(String text, String proposal){
		TextEditor ed = new TextEditor("TestExcluded.java");
		ed.selectText(text);
		ContentAssistant ca = ed.openOpenOnAssistant();
		List<String> p = ca.getProposals();
		ca.close();
		assertTrue(p.contains("Open @Inject Bean "+proposal));
		
	}
	
	

}
