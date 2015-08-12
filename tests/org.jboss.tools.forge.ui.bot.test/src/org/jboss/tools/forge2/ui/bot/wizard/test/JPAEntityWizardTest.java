package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Before;
import org.junit.Test;

/**
 * Class for testing JPA entity and field generation
 * @author Jan Richter
 *
 */
public class JPAEntityWizardTest extends WizardTestBase {

	private static final String ENTITY_NAME = "Customer";
	private static final String TABLE_NAME = "Customers";
	private static final String FIELD_NAME = "name";
	private static final String PACKAGE_NAME = GROUPID + ".model";

	@Before
	public void prepare() {
		newProject(PROJECT_NAME);
		persistenceSetup(PROJECT_NAME);
	}

	/**
	 * Test new JPA entity creation
	 */
	@Test
	public void testNewEntity() {
		newEntity();
		ProjectItem entityItem = new ProjectExplorer().getProject(PROJECT_NAME)
				.getProjectItem("Java Resources", "src/main/java", PACKAGE_NAME, ENTITY_NAME + ".java");
		assertTrue("New entity is not selected", entityItem.isSelected());
		
		TextEditor editor = new TextEditor();
		assertTrue("Java editor is not active", editor.isActive());
		assertTrue("Editor title does not match class name", editor.getTitle().equals(ENTITY_NAME + ".java"));
		
		String contents = editor.getText();
		assertTrue("Entity annotation missing", contents.contains("@Entity"));
		assertTrue("Table name annotation mismatch", contents.contains("@Table(name = \"" + TABLE_NAME + "\")"));
		assertTrue("Class definition mismatch", contents.contains("public class " + ENTITY_NAME));
		editor.close();
	}
	
	/**
	 * Test creating new field for a JPA entity
	 */
	@Test
	public void testNewField() {
		newEntity();
		newField();
		checkNewField(ENTITY_NAME, FIELD_NAME, String.class);
	}
	
	private void checkNewField(String entityName, String fieldName, Class<?> fieldType) {
		String type = fieldType.getSimpleName();
		
		TextEditor editor = new TextEditor();
		assertTrue("Java editor is not active", editor.isActive());
		assertTrue("Editor title does not match class name", editor.getTitle().equals(entityName + ".java"));
		
		for (int i = 0; i < editor.getNumberOfLines(); i++) {
			String line = editor.getTextAtLine(i);
			if(line.contains("private " + fieldType.getSimpleName() + " " + fieldName)) {
				String annotationLine = editor.getTextAtLine(i - 1);
				assertTrue("Missing column annotation", annotationLine.contains("@Column"));
				assertTrue("Missing nullable attribute", annotationLine.contains("nullable = false"));
				assertTrue("Missing insertable attribute", annotationLine.contains("insertable = false"));
				assertTrue("Missing updatable attribute", annotationLine.contains("updatable = false"));
			}
		}
		
		OutlineView oView = new OutlineView();
		oView.open();
		Collection<TreeItem> items = oView.outlineElements();
		
		TreeItem entityItem = null;
		TreeItem fieldItem = null;
		TreeItem fieldGetter = null;
		TreeItem fieldSetter = null;
		String methodName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
		
		for(TreeItem i : items){
			if(i.getText().equals(entityName)) {
				entityItem = i;
				fieldItem = i.getItem(fieldName + " : " + type);
				fieldGetter = i.getItem("get" + methodName + "() : " + type);
				fieldSetter = i.getItem("set" + methodName + "(" + type + ") : void");
			}
		}
		
		assertNotNull(entityItem);
		assertNotNull(fieldItem);
		assertNotNull(fieldGetter);
		assertNotNull(fieldSetter);
	}

	private void newEntity() {
		newEntity(ENTITY_NAME, TABLE_NAME);
	}
	
	private void newEntity(String name, String tableName) {
		new ProjectExplorer().selectProjects(PROJECT_NAME);
		WizardDialog dialog = getWizardDialog("JPA: New Entity", "(JPA: New Entity).*");
		new LabeledText("Package Name:").setText(GROUPID + ".model");
		new LabeledText("Type Name:").setText(name);
		new LabeledText("Table Name:").setText(tableName);
		dialog.finish(TimePeriod.LONG);		
	}
	
	private void newField() {
		newField(ENTITY_NAME, FIELD_NAME, String.class);
	}
	
	private void newField(String entityName, String name, Class<?> type) {
		new ProjectExplorer().getProject(PROJECT_NAME)
			.getProjectItem("Java Resources", "src/main/java", PACKAGE_NAME, entityName  + ".java").select();
		WizardDialog dialog = getWizardDialog("JPA: New Field", "(JPA: New Field).*");
		assertTrue(new DefaultCombo().getSelection().equals(PACKAGE_NAME + "." + entityName));
		
		new LabeledText("Field Name:").setText(name);
		new LabeledText("Field Type:").setText(type.getName());
		new CheckBox("Not Nullable").toggle(true);
		new CheckBox("Not Updatable").toggle(true);
		new CheckBox("Not Insertable").toggle(true);
		dialog.finish(TimePeriod.NORMAL);
	}
}
