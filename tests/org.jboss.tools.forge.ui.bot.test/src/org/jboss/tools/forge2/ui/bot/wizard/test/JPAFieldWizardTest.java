/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.forge2.ui.bot.wizard.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.jboss.reddeer.common.wait.TimePeriod;
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
 * Class for new JPA field generation tests
 * @author Jan Richter
 *
 */
public class JPAFieldWizardTest extends WizardTestBase {

	private static final String FIELD_NAME = "name";
	private static final String ENTITY_NAME = "Customer";
	private static final String PACKAGE_NAME = GROUPID + ".model";
	
	@Before
	public void prepare() {
		newProject(PROJECT_NAME);
		persistenceSetup(PROJECT_NAME);
		newEntity();
	}
	
	/**
	 * Test creating new field for a JPA entity
	 */
	@Test
	public void testNewField() {
		newField();
		checkNewField(ENTITY_NAME, FIELD_NAME, String.class);
	}
	
	private void newEntity() {
		newJPAEntity(PROJECT_NAME, ENTITY_NAME, "", PACKAGE_NAME);
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
}
