package org.jboss.tools.cdi.bot.test.beansxml.template;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.junit.After;
import org.junit.Test;

public class BeansXMLUITemplate extends CDITestBase{
	
	private static final Logger logger = Logger.getLogger(BeansXMLUITemplate.class);
	protected String CDIVersion = null;
	
	@After
	public void cleanup(){
		deleteAllProjects();
	}
	
	@Test
	public void testBeanDiscoveryModes(){
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
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
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		beans.setBeanDiscoveryMode("non-existing-mode");
		//TODO check error marker
		setAndCheckTreeDiscoveryMode(beans, "all");
		setAndCheckTreeDiscoveryMode(beans, "none");
		setAndCheckTreeDiscoveryMode(beans, "annotated");
	}
	
	@Test
	public void testBeanDiscoverySourceEditing(){
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		setAndCheckSourceDiscoveryMode(beans, "non-existing-mode", 2, "org.eclipse.ui.workbench.texteditor.error");
		setAndCheckSourceDiscoveryMode(beans, "annotated", 0, null);
	}
	
	@Test
	public void testBeansVersion(){
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		assertEquals(CDIVersion, beans.getVersion());
	}
	
	@Test
	public void testBeansName(){
		EditorPartWrapper beans = beansXMLHelper.openBeansXml(PROJECT_NAME);
		beans.activateTreePage();
		assertEquals("beans", beans.getName());
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
		new WaitUntil(new EditorHasValidationMarkers(e),TimePeriod.DEFAULT,false);
		List<Marker> markers = e.getMarkers();
		assertEquals(expectedNumberOfErrors,markers.size());
		for(Marker m: markers){
			m.getType().equals(type);
		}
		beans.activateTreePage();
		assertEquals(mode,beans.getBeanDiscoveryMode());
		beans.save();
	}
	

}
