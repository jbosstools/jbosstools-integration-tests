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

	AntFileExportTest.class, //35
	CodeGenerationConfigurationTest.class, //650s //400
	ConnectionProfileTest.class, //6
	ConsoleConfigurationFileTest.class, //200s
	ConsoleConfigurationTest.class, //60
	CreateJPAProjectTest.class, //24
	CriteriaEditorTest.class, //500s //240 
	CriteriaEditorCodeAssistTest.class, //470s
	EntityValidationTest.class,
	JPADetailsViewTest.class, //55
	HibernateUIPartsTest.class,
	JPAEntityGenerationTest.class, //550s
	
	JPAFacetTest.class, //25
	JPAUIPartsTest.class, //4
	HQLEditorTest.class, //920s //157
	HQLEditorCodeAssistTest.class, //300s //160
	JBossDatasourceTest.class,  //12
	JpaAnnotationGenerationTest.class, //30
	MappingDiagramTest.class, //350s //200
	MappingFileTest.class, //40
	MavenizedProjectTest.class, //15
	PersistenceXMLFileTest.class, //25
	RevengFileTest.class, //15
	TablesFromJPAEntitiesGeneration.class	 //900s //440

	

})
public class HibernateAllTest {

}
