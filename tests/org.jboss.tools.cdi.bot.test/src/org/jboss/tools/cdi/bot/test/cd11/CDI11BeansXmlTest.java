package org.jboss.tools.cdi.bot.test.cd11;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.tools.cdi.bot.test.CDI11TestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.After;
import org.junit.Test;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class CDI11BeansXmlTest extends CDI11TestBase{
	
	@InjectRequirement
	protected static ServerRequirement sr;
	
	private static final Logger logger = Logger.getLogger(CDI11BeansXmlTest.class);
	
	private static final String PROJECT_NAME = "CDI11Beans";
	
	@Override
	public String getProjectName(){
		return PROJECT_NAME;
	}
	
	@After
	public void cleanup(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects();
	}
	
	@Test
	public void testBeanDiscoveryModes(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		assertTrue(beans.isBeanDiscoveryModeEnabled());
		assertEquals("annotated", beans.getBeanDiscoveryMode());
		List<String> modes = beans.getBeanDiscoveryModes();
		logger.debug("Discovery modes:");
		for(String mode: modes){
			logger.debug("  "+mode);
		}
		assertEquals(3, modes.size());
		assertTrue(modes.contains("all"));
		assertTrue(modes.contains("none"));
		assertTrue(modes.contains("annotated"));
	}
	
	@Test
	public void testBeanDiscoveryTreeEditing(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		beans.setBeanDiscoveryMode("non-existing-mode");
		//TODO check error marker
		setAndCheckTreeDiscoveryMode(beans, "all");
		setAndCheckTreeDiscoveryMode(beans, "none");
		setAndCheckTreeDiscoveryMode(beans, "annotated");
	}
	
	private void setAndCheckTreeDiscoveryMode(EditorPartWrapper beans, String mode){
		beans.activateTreePage();
		beans.selectBeanDiscoveryMode(mode);
		beans.activateSourcePage();
		DefaultStyledText st = new DefaultStyledText();
		st.getText().contains("bean-discovery-mode=\""+mode+"\"");
		DefaultEditor te = new DefaultEditor();
		assertEquals(0,te.getMarkers().size());
		beans.save();
		assertEquals(0,te.getMarkers().size());
	}
	
	private void setAndCheckSourceDiscoveryMode(EditorPartWrapper beans, String mode, int expectedNumberOfErrors, String type){
		beans.activateTreePage();
		String currentMode = beans.getBeanDiscoveryMode();
		beans.activateSourcePage();
		DefaultStyledText ds = new DefaultStyledText();
		String s = ds.getText().replace(currentMode, mode);
		ds.setText(s);
		Editor e = new DefaultEditor();
		e.save();
		new WaitUntil(new EditorHasValidationMarkers(e),TimePeriod.NORMAL,false);
		List<Marker> markers = e.getMarkers();
		assertEquals(expectedNumberOfErrors,markers.size());
		for(Marker m: markers){
			m.getType().equals(type);
		}
		beans.activateTreePage();
		assertEquals(mode,beans.getBeanDiscoveryMode());
		beans.save();
	}
	
	
	
	@Test
	public void testBeanDiscoverySourceEditing(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		setAndCheckSourceDiscoveryMode(beans, "non-existing-mode", 2, "org.eclipse.ui.workbench.texteditor.error");
		setAndCheckSourceDiscoveryMode(beans, "annotated", 0, null);
	}
	
	@Test
	public void testBeansVersion(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		assertEquals("1.1", beans.getVersion());
	}
	
	@Test
	public void testBeansName(){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).getProjectItem("WebContent","WEB-INF","beans.xml").open();
		EditorPartWrapper beans = new EditorPartWrapper();
		beans.activateTreePage();
		assertEquals("beans", beans.getName());
	}
	
	

}
