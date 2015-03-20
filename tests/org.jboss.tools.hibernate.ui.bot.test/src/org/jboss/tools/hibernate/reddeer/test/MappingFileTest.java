package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.impl.tree.TreeItemNotFoundException;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingElementsSelectionPage2;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingFilePage;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingFileWizard;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingPreviewPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Hibernate mapping file (hbm.xml) test   
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class MappingFileTest extends HibernateRedDeerTest {
	
	public static final String PRJ = "java-hb-mapping-prj";
	
	private Logger log = Logger.getLogger(this.getClass());
    
	@Before
	public void prepare() {
		log.step("Import test project");
		importProject(PRJ);
	}
	
	@Test()
	public void createMappingFileFromPackage() {
		
		log.step("Select package with POJO model classes");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem item = new DefaultTreeItem(PRJ,"src","org.mapping.model.pkg");
		item.select();
		
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
		
		try {
			item = new DefaultTreeItem(PRJ,"src","org.mapping.model.pkg","Dog.hbm.xml");
		} catch (TreeItemNotFoundException e) {
			fail("https://issues.jboss.org/browse/JBIDE-18769");
		}
		
		
		item.doubleClick();
		new DefaultEditor("Dog.hbm.xml");
		pe.open();
		item = new DefaultTreeItem(PRJ,"src","org.mapping.model.pkg","Owner.hbm.xml");
		item.doubleClick();		
		new DefaultEditor("Owner.hbm.xml");
	}
	
	@Test
	public void createMappingFileFromFile() {
		
		log.step("Select POJO model class");
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem item = new DefaultTreeItem(PRJ,"src","org.mapping.model.file","Owner.java");
		item.select();
		
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
		try {
			item = new DefaultTreeItem(PRJ,"src","org.mapping.model.file","Owner.hbm.xml");
		} catch (TreeItemNotFoundException e) {
			fail("https://issues.jboss.org/browse/JBIDE-18769");
		}

		item.doubleClick();
		new DefaultEditor("Owner.hbm.xml");
	}
}
