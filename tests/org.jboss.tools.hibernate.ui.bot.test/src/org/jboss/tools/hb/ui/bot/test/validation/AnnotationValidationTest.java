package org.jboss.tools.hb.ui.bot.test.validation;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Hibernate annotation validation test
 * - Hibernate annotation is properly validated in Problems View 
 * @author jpeterka
 * 
 */

public class AnnotationValidationTest extends HibernateRedDeerTest {

	final String prj = "jpatest40";
	final String pkg = "org.validation";
	
	@Test
	public void annotationValidationTest() {
		importProject("/resources/prj/hibernatelib");
		importProject("/resources/prj/jpatest40");
		
		checkGenericGeneratorValidation();
	}

	private void checkGenericGeneratorValidation() {
		String resource = "GeneratorValidationEntity.java";
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new DefaultTreeItem(prj, "src", pkg, resource);
		
		/*
		String desc = "No generator named \"mygen\" is defined in the persistence unit";
		String path = "/" + prj + "/src/org/validation";
		String type = "JPA Problem";
		*/
		
		new TextEditor(resource);
	}
}