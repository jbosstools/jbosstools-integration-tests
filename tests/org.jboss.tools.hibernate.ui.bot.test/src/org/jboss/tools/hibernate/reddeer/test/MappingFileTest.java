package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.dialogs.ExplorerItemPropertyDialog;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.hibernate.reddeer.common.XPathHelper;
import org.jboss.tools.hibernate.reddeer.editor.Hibernate3CompoundEditor;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingElementsSelectionPage2;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingFilePage;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingFileWizard;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingPreviewPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Hibernate mapping file (hbm.xml) test   
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@Database(name="testdb")
public class MappingFileTest extends HibernateRedDeerTest {
	
	public static final String PRJ = "java-hb-mapping-prj";
	
	private static final Logger log = Logger.getLogger(MappingFileTest.class);
    
	@Before
	public void prepare() {
		log.step("Import test project");
		importProject(PRJ, null);
	}
	
	@After
	public void clean(){
		deleteAllProjects();
	}
	
	@Test
	public void createMappingFileFromPackage() {
		
		log.step("Select package with POJO model classes");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PRJ).getProjectItem("src","org.mapping.model.pkg").select();
		
		log.step("Open Hibernate mapping file wizard and go thorugh it");
		NewHibernateMappingFileWizard wizard = new NewHibernateMappingFileWizard();
		wizard.open();
		NewHibernateMappingElementsSelectionPage2 selPage = new NewHibernateMappingElementsSelectionPage2();
		selPage.selectItem("org.mapping.model.pkg");
		wizard.next();
		NewHibernateMappingFilePage files = new NewHibernateMappingFilePage();
		files.selectClasses("Owner");
		wizard.next();
		NewHibernateMappingPreviewPage preview = new NewHibernateMappingPreviewPage();
		assertTrue("Preview text cannot be empty", !preview.getPreviewText().equals(""));
		log.step("Finish wizard to create mapping files");
		wizard.finish();
		
		log.step("Check the files");
		pe.open();
		
		String clazz = "org.mapping.model.pkg";
		
		assertTrue("Hbm.xml not generated: Known issue(s): JBIDE-18769, JBIDE-20042",
				pe.getProject(PRJ).containsItem("src",clazz,"Dog.hbm.xml"));
		
		pe.getProject(PRJ).getProjectItem("src",clazz,"Dog.hbm.xml").open();
		Hibernate3CompoundEditor hme = new Hibernate3CompoundEditor("Dog.hbm.xml");
		hme.activateSourceTab();
		String sourceText = hme.getSourceText();

		
		XPathHelper xph = XPathHelper.getInstance();
		String table = xph.getMappingFileTable(clazz + ".Dog", sourceText);
		assertTrue(table.equals("DOG"));
		
		pe.open();	
		pe.getProject(PRJ).getProjectItem("src",clazz,"Owner.hbm.xml").open();

		hme = new Hibernate3CompoundEditor("Owner.hbm.xml");
		hme.activateSourceTab();
		sourceText = hme.getSourceText();

		table = xph.getMappingFileTable(clazz + ".Owner", sourceText);
		assertEquals("OWNER", table);
	}
	
	@Test
	public void createMappingFileFromFile() {
		
		log.step("Select POJO model class");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(PRJ).getProjectItem("src","org.mapping.model.file","Owner.java").select();
		
		log.step("Open Hibernate mapping file wizard and go thorugh it");
		NewHibernateMappingFileWizard wizard = new NewHibernateMappingFileWizard();
		wizard.open();
		NewHibernateMappingElementsSelectionPage2 selPage = new NewHibernateMappingElementsSelectionPage2();
		selPage.selectItem("Owner");
		wizard.next();
		NewHibernateMappingFilePage files = new NewHibernateMappingFilePage();
		files.selectClasses("Owner");
		wizard.next();
		NewHibernateMappingPreviewPage preview = new NewHibernateMappingPreviewPage();
		assertTrue("Preview text cannot be empty", !preview.getPreviewText().equals(""));
		log.step("Finish wizard to create mapping file");
		wizard.finish();
		
		log.step("Check the file");
		pe.open();
		String clazz = "org.mapping.model.file";
		
		assertTrue("Hbm.xml not generated: Known issue(s): JBIDE-18769, JBIDE-20042",
				pe.getProject(PRJ).containsItem("src",clazz,"Owner.hbm.xml"));

		pe.getProject(PRJ).getProjectItem("src",clazz,"Owner.hbm.xml").open();
		
		String fileName = "Owner.hbm.xml";
		Hibernate3CompoundEditor hme = new Hibernate3CompoundEditor(fileName);
		hme.activateSourceTab();
		String sourceText = hme.getSourceText();
		
		XPathHelper xph = XPathHelper.getInstance();
		String table = xph.getMappingFileTable(clazz + ".Owner", sourceText);
		assertEquals("OWNER", table);
	}
	
	//JBIDE-21766
	@Test
	public void createMappingFilePackageWithNoConfig(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		ExplorerItemPropertyDialog pd = new ExplorerItemPropertyDialog(pe.getProject(PRJ));
		pd.open();
		new DefaultTreeItem("Hibernate Settings").select();
		new DefaultCombo().setSelection("<None>");
		pd.ok();
		createMappingFileFromPackage();
	}
	
	//JBIDE-21766
	@Test
	public void createMappingFileWithNoConfig(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		ExplorerItemPropertyDialog pd = new ExplorerItemPropertyDialog(pe.getProject(PRJ));
		pd.open();
		new DefaultTreeItem("Hibernate Settings").select();
		new DefaultCombo().setSelection("<None>");
		pd.ok();
		createMappingFileFromFile();
	}
	

}
