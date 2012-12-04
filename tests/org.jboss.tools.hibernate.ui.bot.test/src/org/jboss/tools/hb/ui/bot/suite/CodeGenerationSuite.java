package org.jboss.tools.hb.ui.bot.suite;

import org.jboss.tools.hb.ui.bot.test.generation.GenerateJPAHibernateAnnotationsContextMenu;
import org.jboss.tools.hb.ui.bot.test.generation.GenerateJPAHibernateAnnotationsMenuBar;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RequirementAwareSuite.class)
//@SuiteClasses({CreateCodeGenerationConfiguration35.class,CreateCodeGenerationConfiguration36.class,CreateCodeGenerationConfiguration40.class})
@SuiteClasses({GenerateJPAHibernateAnnotationsContextMenu.class, GenerateJPAHibernateAnnotationsMenuBar.class})
//@SuiteClasses({GenerateJPAHibernateAnnotationsMenuBar.class})
//@SuiteClasses({CreateCodeGenerationConfiguration40.class})
//@SuiteClasses({JPADDLGenerationTest.class})
//@SuiteClasses({JPAEntitiesGenerationTest.class})
//@SuiteClasses({RunSchemaExportTest.class})
//@SuiteClasses({CreateCodeGenerationConfiguration.class,JPADDLGenerationTest.class,JPAEntitiesGenerationTest.class,RunSchemaExportTest.class})
public class CodeGenerationSuite {

}
