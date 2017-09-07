package org.jboss.tools.cdi.bot.test.beans.bean.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassCreationWizard;
import org.eclipse.reddeer.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.reddeer.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.reddeer.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.junit.Test;

//based on JBIDE-15381
//cdi1.1+
public class VetoedAnnotationTemplate extends CDITestBase{

	public static final String WELD_SE_JAR=System.getProperty("jbosstools.test.weld-se.home");
	
	@Test
	public void testVetoedAnnotation() throws FileNotFoundException{
		createBeans();
		
		NewClassCreationWizard jd = new NewClassCreationWizard();
		jd.open();
		NewClassWizardPage jp = new NewClassWizardPage(jd);
		jp.setName("Injector");
		jp.setPackage("test");
		jd.finish();
		EditorResourceHelper rhelper = new EditorResourceHelper();
		rhelper.replaceClassContentByResource("Injector.java",readFile("resources/cdi11/Injector.jav_"), false);
		TextEditor te = new TextEditor("Injector.java");
		AbstractWait.sleep(TimePeriod.DEFAULT);
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
		createBean("Bean1", "vetoed");
		createBean("Bean2", "vetoed");
		createBean("Bean3", "vetoed");
		createBean("Bean4", "vetoedpackage");
		createBean("Bean5", "vetoedpackage");
		createBean("Bean6", "vetoedpackage.pck");
		addVetoedAnnotation("Bean1");
		addVetoedAnnotation("Bean2");
		createVetoedPackage();
		TextEditor ed = new TextEditor("Bean6.java");
		ed.insertLine(2, "import javax.enterprise.inject.Vetoed;");
		ed.insertLine(12, "@Vetoed");
		ed.insertLine(13, "public class Bean7{}");
		ed.save();
	}
	
	private void createBean(String name, String pckg){
		beansHelper.createBean(name, pckg, false, false, false,false, false,false,false,null,
				"@ApplicationScoped");
	}
	
	private void addVetoedAnnotation(String bean){
		TextEditor ed = new TextEditor(bean+".java");
		ed.insertLine(2, "import javax.enterprise.inject.Vetoed;");
		ed.insertLine(5, "@Vetoed");
		ed.save();
	}
	
	private void createVetoedPackage(){
		BasicNewFileResourceWizard fd = new BasicNewFileResourceWizard();
		fd.open();
		WizardNewFileCreationPage fp = new WizardNewFileCreationPage(fd);
		fp.setFileName("package-info.java");
		fp.setFolderPath(PROJECT_NAME+"/src/vetoedpackage");
		fd.finish();
		TextEditor ed = new TextEditor("package-info.java");
		ed.insertLine(0, "@javax.enterprise.inject.Vetoed\n");
		ed.insertLine(1, "package vetoedpackage;");
		ed.save();
	}
	
	
	
	

}
