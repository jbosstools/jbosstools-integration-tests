package org.jboss.tools.archives.ui.bot.test;

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
		createJavaProject(project);
	}
	
	@Test
	public void testArchiveSupport() {
		testAddArchiveSupport();
		testRemoveArchiveSupport();
	}
	
	private void testAddArchiveSupport() {
		
		addArchivesSupport(project);
		
		assertTrue("Project archives support was not set on project '" + project + "'",
				archiveExplorerExists(project));
	}
	
	private void testRemoveArchiveSupport() {
		
		removeArchivesSupport(project);
		
		assertFalse("Project archives support is still set on project '" + project + "'",
				archiveExplorerExists(project));
		
	}
	
}
