package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.ProjectConfigurationFactory;
import org.jboss.tools.hibernate.reddeer.view.JPADetailsView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests JPA Details view
 * 
 * @author Jiri Peterka
 */
@RunWith(RedDeerSuite.class)
@Database(name = "testdb")
public class JPADetailsViewTest extends HibernateRedDeerTest {

	private final String PRJ = "mvn-hibernate43-ent";
	@InjectRequirement
	private DatabaseRequirement dbRequirement;
	
	private static final Logger log = Logger.getLogger(JPADetailsViewTest.class);

	@Before
	public void testConnectionProfile() {
		log.step("Import test project");
		importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		log.step("Create database driver definition");
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		log.step("Create database connection profile");
		ConnectionProfileFactory.createConnectionProfile(cfg);
		log.step("Convert project to faceted form");
		ProjectConfigurationFactory.convertProjectToFacetsForm(PRJ);
		log.step("Set project facet for JPA");
		ProjectConfigurationFactory.setProjectFacetForDB(PRJ, cfg);
	}

	@Test
	public void testJPADetailView() {

		log.step("Open entity");
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PRJ);
		try {
			new DefaultTreeItem(PRJ, "src/main/java", "org.gen", "Actor.java")
					.doubleClick();
		} catch (SWTLayerException e) {
			fail("Entities not generated, possible cause https://issues.jboss.org/browse/JBIDE-19175");
		}
		TextEditor textEditor = new TextEditor("Actor.java");
		textEditor.setCursorPosition(20, 1);		

		log.step("Open JPA view and check content");
		JPADetailsView jpaDetailsView = new JPADetailsView();
		jpaDetailsView.open();
					
		try {
			new DefaultStyledText("Type 'Actor' is mapped as entity.");
		} catch (SWTLayerException e) {
			fail("JPA details should be available - known issue - https://issues.jboss.org/browse/JBIDE-17940");
		}
	}

	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}
}