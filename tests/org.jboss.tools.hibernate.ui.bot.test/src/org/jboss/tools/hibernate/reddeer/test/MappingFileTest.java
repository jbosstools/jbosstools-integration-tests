package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.impl.tree.TreeItemNotFoundException;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingElementsSelectionPage2;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingFilePage;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingFileWizard;
import org.jboss.tools.hibernate.reddeer.wizard.NewHibernateMappingPreviewPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Hibernate mapping file (hbm.xml) test   
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
@CleanWorkspace
public class MappingFileTest extends HibernateRedDeerTest {
	
	public static final String PRJ = "java-hb-mapping-prj";
    
	@BeforeClass
	public static void prepare() {
		importProject(PRJ);
	}
	
	@Test()
	public void createMappingFileFromPackage() {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem item = new DefaultTreeItem(PRJ,"src","org.mapping.model.pkg");
		item.select();
		
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
		wizard.finish();
		
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
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		DefaultTreeItem item = new DefaultTreeItem(PRJ,"src","org.mapping.model.file","Owner.java");
		item.select();
		
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
		wizard.finish();
		
		pe.open();
		try {
			item = new DefaultTreeItem(PRJ,"src","org.mapping.model.file","Owner.hbm.xml");
		} catch (TreeItemNotFoundException e) {
			fail("https://issues.jboss.org/browse/JBIDE-18769");
		}

		item.doubleClick();
		new DefaultEditor("Dog.hbm.xml");
	}

		
	@AfterClass
	public static void cleanUp() {
	}
}
