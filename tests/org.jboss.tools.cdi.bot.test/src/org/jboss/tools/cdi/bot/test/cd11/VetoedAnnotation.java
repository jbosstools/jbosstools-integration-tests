package org.jboss.tools.cdi.bot.test.cd11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.reddeer.eclipse.ui.ide.NewFileCreationWizardDialog;
import org.jboss.reddeer.eclipse.ui.ide.NewFileCreationWizardPage;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.Marker;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDI11TestBase;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.uiutils.CDIWizardHelper;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.junit.Test;

//based on JBIDE-15381
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.WILDFLY8x)
public class VetoedAnnotation extends CDI11TestBase{
	
	private static final String PROJECT_NAME = "CDIVetoedProject";
	public static final String WELD_SE_JAR=System.getProperty("jbosstools.test.weld-se.home");
	
	@Override
	public String getProjectName(){
		return PROJECT_NAME;
	}
	
	@Test
	public void testVetoedAnnotation() throws FileNotFoundException{
		createBeans();
		
		NewJavaClassWizardDialog jd = new NewJavaClassWizardDialog();
		jd.open();
		NewJavaClassWizardPage jp = jd.getFirstPage();
		jp.setName("Injector");
		jp.setPackage("test");
		jd.finish();
		EditorResourceHelper rhelper = new EditorResourceHelper();
		rhelper.replaceClassContentByResource("Injector.java", new FileInputStream("resources/cdi11/Injector.jav_"), false);
		TextEditor te = new TextEditor("Injector.java");
		AbstractWait.sleep(TimePeriod.NORMAL);
		String warning = "org.eclipse.ui.workbench.texteditor.warning";
		String noInjection = "No bean is eligible for injection to the injection point [JSR-346 §5.2.2]";
		List<Marker> markers = te.getMarkers();
		assertEquals(5, markers.size());
		for(Marker m: te.getMarkers()){
			assertTrue((m.getLineNumber() == 17 && m.getType().equals(warning) && m.getText().equals(noInjection)) ||
					(m.getLineNumber() == 20 && m.getType().equals(warning) && m.getText().equals(noInjection)) ||
					(m.getLineNumber() == 26 && m.getType().equals(warning) && m.getText().equals(noInjection)) ||
					(m.getLineNumber() == 29 && m.getType().equals(warning) && m.getText().equals(noInjection)) ||
					(m.getLineNumber() == 35 && m.getType().equals(warning) && m.getText().equals(noInjection)));
		}
	}
	
	private void createBeans(){
		CDIWizardHelper wh = new CDIWizardHelper();
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean1", "vetoed", "@ApplicationScoped");
		addVetoedAnnotation("Bean1");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean2", "vetoed", "@ApplicationScoped");
		addVetoedAnnotation("Bean2");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean3", "vetoed", "@ApplicationScoped");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean4", "vetoedpackage", "@ApplicationScoped");
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean5", "vetoedpackage", "@ApplicationScoped");
		createVetoedPackage();
		wh.createCDIComponent(CDIWizardType.BEAN, "Bean6", "vetoedpackage.pck", "@ApplicationScoped");
		TextEditor ed = new TextEditor("Bean6.java");
		ed.insertLine(2, "import javax.enterprise.inject.Vetoed;");
		ed.insertLine(12, "@Vetoed");
		ed.insertLine(13, "public class Bean7{}");
		ed.save();
	}
	
	private void addVetoedAnnotation(String bean){
		TextEditor ed = new TextEditor(bean+".java");
		ed.insertLine(2, "import javax.enterprise.inject.Vetoed;");
		ed.insertLine(5, "@Vetoed");
		ed.save();
	}
	
	private void createVetoedPackage(){
		NewFileCreationWizardDialog fd = new NewFileCreationWizardDialog();
		fd.open();
		NewFileCreationWizardPage fp = fd.getFirstPage();
		fp.setFileName("package-info.java");
		fp.setFolderPath("CDIVetoedProject/src/vetoedpackage");
		fd.finish();
		TextEditor ed = new TextEditor("package-info.java");
		ed.insertLine(0, "@javax.enterprise.inject.Vetoed\n");
		ed.insertLine(1, "package vetoedpackage;");
		ed.save();
	}
	
	
	
	

}
