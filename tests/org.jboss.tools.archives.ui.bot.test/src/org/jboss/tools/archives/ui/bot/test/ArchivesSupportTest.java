package org.jboss.tools.archives.ui.bot.test;

import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.ui.bot.ext.entity.JavaProjectEntity;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class ArchivesSupportTest extends ArchivesTestBase {

	private static final String project = "prj";
	
	@BeforeClass
	public static void setup() {
		JavaProjectEntity jpe = new JavaProjectEntity();
		jpe.setProjectName(project);
		eclipse.createJavaProject(jpe);
	}
	
	@Test
	public void testAddArchiveSupport() {
		
		addArchivesSupport(project);
		
		assertTrue("Project archives support was not set on project '" + project + "'",
				archiveExplorerExists(project));
	}
	
	@Test
	public void testRemoveArchiveSupport() {
		
		removeArchivesSupport(project);
		
		assertFalse("Project archives support is still set on project '" + project + "'",
				archiveExplorerExists(project));
		
	}
	
	@Test
	public void testReAddingArchiveSupport() {
		
		String project = "pr2";
		
		/* prepare project with existing archive in it */
		importProjectWithoutRuntime(project);
		ProjectArchivesExplorer explorer = explorerForProject(project);
		assertItemExistsInExplorer(explorer, project + ".jar" + " [/" + project + "]");
		
		/* re-add archive support into project */
		removeArchivesSupport(project);
		addArchivesSupport(project);
		
		assertItemExistsInExplorer(explorer, project + ".jar" + " [/" + project + "]");
	}
	
}
