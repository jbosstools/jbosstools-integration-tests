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
package org.jboss.tools.forge.ui.bot.console.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;
import org.jboss.tools.forge.ui.bot.test.util.ResourceUtils;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.ContentOutline;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.junit.Test;

@CleanWorkspace
public class EntityTest extends ForgeConsoleTestBase {

	private static final String ENTITY_CREATED = "Created @Entity [" + PACKAGE_NAME + "." + ENTITY_NAME + "]";
	private static final String FIELD_ADDED = "Added field to " + PACKAGE_NAME  + "." +
								ENTITY_NAME + ": @Column private String " + FIELD_NAME + ";";
	
	@Test
	public void newEntityTest(){			
		createProject();
		createPersistence();
		createEntity(ENTITY_NAME, PACKAGE_NAME);
		assertTrue(fView.getConsoleText().contains(ENTITY_CREATED));
		DefaultEditor editor = new DefaultEditor();
		assertTrue("Java editor is not active", editor.isActive());
		assertTrue(editor.getTitle().equals(ENTITY_NAME + ".java"));
		editor.close();
	}
	
	
	@Test
	public void newFieldTest(){
		
		createProject();
		createPersistence();
		createEntity();
		createStringField(FIELD_NAME);
		assertTrue(fView.getConsoleText().contains(FIELD_ADDED));
		
		String packagePath = PACKAGE_NAME.replace(".", "/");
		String entityFilePath = WORKSPACE + "/" + PROJECT_NAME + "/src/main/java/" +	
								packagePath + "/" + ENTITY_NAME + ".java";
		
		try {
			String entityContent = ResourceUtils.readFile(entityFilePath);	
			assertTrue(entityContent.contains("private String " + FIELD_NAME + ";"));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Attempt to read the '" + entityFilePath + "' failed!");
		}		
		
		ContentOutline oView = new ContentOutline();
		oView.open();
		Collection<TreeItem> items = oView.outlineElements();
		
		TreeItem entityItem = null;
		TreeItem fieldItem = null;
		
		for(TreeItem i : items){
			if(i.getText().equals(ENTITY_NAME)){
				entityItem = i;
				fieldItem = i.getItem(FIELD_NAME + " : String");
			}
		}
		
		assertNotNull(entityItem);
		assertNotNull(fieldItem);
		assertTrue(fieldItem.isSelected());
		
	}
	
}
