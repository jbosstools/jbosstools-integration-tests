package org.jboss.tools.hibernate.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.hibernate.reddeer.test.AntFileExportTest;
import org.jboss.tools.hibernate.reddeer.test.CodeGenerationConfigurationTest;
import org.jboss.tools.hibernate.reddeer.test.ConnectionProfileTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationFileTest;
import org.jboss.tools.hibernate.reddeer.test.ConsoleConfigurationTest;
import org.jboss.tools.hibernate.reddeer.test.CreateJPAProjectTest;
import org.jboss.tools.hibernate.reddeer.test.CriteriaEditorCodeAssistTest;
import org.jboss.tools.hibernate.reddeer.test.CriteriaEditorTest;
import org.jboss.tools.hibernate.reddeer.test.EntityValidationTest;
import org.jboss.tools.hibernate.reddeer.test.HQLEditorCodeAssistTest;
import org.jboss.tools.hibernate.reddeer.test.HQLEditorTest;
import org.jboss.tools.hibernate.reddeer.test.HibernateUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.JBossDatasourceTest;
import org.jboss.tools.hibernate.reddeer.test.JPADetailsViewTest;
import org.jboss.tools.hibernate.reddeer.test.JPAEntityGenerationTest;
import org.jboss.tools.hibernate.reddeer.test.JPAFacetTest;
import org.jboss.tools.hibernate.reddeer.test.JPAUIPartsTest;
import org.jboss.tools.hibernate.reddeer.test.JpaAnnotationGenerationTest;
import org.jboss.tools.hibernate.reddeer.test.MappingDiagramTest;
import org.jboss.tools.hibernate.reddeer.test.MappingFileTest;
import org.jboss.tools.hibernate.reddeer.test.MavenizedProjectTest;
import org.jboss.tools.hibernate.reddeer.test.PersistenceXMLFileTest;
import org.jboss.tools.hibernate.reddeer.test.RevengFileTest;
import org.jboss.tools.hibernate.reddeer.test.TablesFromJPAEntitiesGeneration;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({

	AntFileExportTest.class,
	CodeGenerationConfigurationTest.class, //650s TODO
	ConnectionProfileTest.class,
	ConsoleConfigurationFileTest.class, //200s
	ConsoleConfigurationTest.class,
	CreateJPAProjectTest.class,
	CriteriaEditorTest.class, //500s
	CriteriaEditorCodeAssistTest.class, //470s
	EntityValidationTest.class,
	HibernateUIPartsTest.class,
	JPADetailsViewTest.class,
	JPAEntityGenerationTest.class, //550s
	JPAFacetTest.class,
	JPAUIPartsTest.class,
	/*
	HQLEditorTest.class, //920s
	HQLEditorCodeAssistTest.class, //300s
	JBossDatasourceTest.class,
	JpaAnnotationGenerationTest.class,
	MappingDiagramTest.class, //350s
	MappingFileTest.class,
	MavenizedProjectTest.class,
	PersistenceXMLFileTest.class,
	RevengFileTest.class,
	TablesFromJPAEntitiesGeneration.class	 //900s
*/
	

})
public class HibernateAllTest {

}
