package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.db.DatabaseConfiguration;
import org.jboss.reddeer.requirements.db.DatabaseRequirement;
import org.jboss.reddeer.requirements.db.DatabaseRequirement.Database;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.impl.tree.TreeItemNotFoundException;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.hibernate.reddeer.factory.ConnectionProfileFactory;
import org.jboss.tools.hibernate.reddeer.factory.DriverDefinitionFactory;
import org.jboss.tools.hibernate.reddeer.factory.EntityGenerationFactory;
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

	@Before
	public void testConnectionProfile() {
		importProject(PRJ);
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		DriverDefinitionFactory.createDatabaseDriverDefinition(cfg);
		ConnectionProfileFactory.createConnectionProfile(cfg);
		ProjectConfigurationFactory.convertProjectToFacetsForm(PRJ);
		ProjectConfigurationFactory.setProjectFacetForDB(PRJ, cfg);
	}

	@Test
	public void testJPADetailView() {

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PRJ);
		try {
			new DefaultTreeItem(PRJ, "src/main/java", "org.gen", "Actor.java")
					.doubleClick();
		} catch (TreeItemNotFoundException e) {
			fail("Entities not generated, possible cause https://issues.jboss.org/browse/JBIDE-19175");
		}
		new DefaultEditor("Actor.java");

		JPADetailsView jpaDetailsView = new JPADetailsView();
		jpaDetailsView.open();

		try {
			new DefaultLabel(
					"JPA Details are not available for the current selection.");
			fail("JPA details should be available - known issue - https://issues.jboss.org/browse/JBIDE-17940");
		} catch (SWTLayerException e) {
			// do nothing
		}
	}

	@After
	public void cleanUp() {
		DatabaseConfiguration cfg = dbRequirement.getConfiguration();
		ConnectionProfileFactory.deleteConnectionProfile(cfg.getProfileName());
	}
}