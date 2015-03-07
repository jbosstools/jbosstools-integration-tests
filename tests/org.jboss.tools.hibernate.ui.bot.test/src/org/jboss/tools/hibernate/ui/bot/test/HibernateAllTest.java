package org.jboss.tools.hibernate.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.hibernate.reddeer.test.AntFileExportTest;
import org.jboss.tools.hibernate.reddeer.test.ConnectionProfileTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationFileTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationTest;
import org.jboss.tools.hibernate.reddeer.test.CreateJPAProjectTest;
import org.jboss.tools.hibernate.reddeer.test.CriteriaEditorTest;
import org.jboss.tools.hibernate.reddeer.test.EmptyTest;
import org.jboss.tools.hibernate.reddeer.test.HQLEditorTest;
import org.jboss.tools.hibernate.reddeer.test.HibernateUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.JBossDatasourceTest;
import org.jboss.tools.hibernate.reddeer.test.JPADetailsViewTest;
import org.jboss.tools.hibernate.reddeer.test.JPAEntityGenerationTest;
import org.jboss.tools.hibernate.reddeer.test.JPAFacetTest;
import org.jboss.tools.hibernate.reddeer.test.JpaAnnotationGenerationTest;
import org.jboss.tools.hibernate.reddeer.test.MappingDiagramTest;
import org.jboss.tools.hibernate.reddeer.test.MappingFileTest;
import org.jboss.tools.hibernate.reddeer.test.MavenizedProjectTest;
import org.jboss.tools.hibernate.reddeer.test.PersistenceXMLFileTest;
import org.jboss.tools.hibernate.reddeer.test.RevengFileTest;
import org.jboss.tools.hibernate.reddeer.test.TablesFromJPAEntitiesGeneration;
import org.jboss.tools.hibernate.reddeer.view.JPADetailsView;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	EmptyTest.class,
	AntFileExportTest.class,
	ConnectionProfileTest.class,
	ConsoleConfigurationFileTest.class,
	ConsoleConfigurationTest.class,
	CreateJPAProjectTest.class,
	CriteriaEditorTest.class,
	HibernateUIPartsTest.class,
	HQLEditorTest.class,
	JBossDatasourceTest.class,
	JpaAnnotationGenerationTest.class,
	JPADetailsViewTest.class,
	JPAEntityGenerationTest.class,
	JPAFacetTest.class,
	MappingDiagramTest.class,
	MappingFileTest.class,
	MavenizedProjectTest.class,
	PersistenceXMLFileTest.class,
	RevengFileTest.class,
	TablesFromJPAEntitiesGeneration.class	
})
public class HibernateAllTest {

}