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
package org.jboss.tools.freemarker.ui.bot.test.editor;

import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker UI Editor test
 * @author Jiri Peterka, Ondrej Dockal
 *
 */
@RunWith(RedDeerSuite.class)
public class FreeMarkerBaseEditorTest extends AbstractFreemarkerTest {
	
	private static final Logger log = Logger.getLogger(FreeMarkerBaseEditorTest.class);

	@Test
	public void freeMarkerTest() {
		log.step("Open ftl file in freemarker editor");
		openFTLFileInEditor();
		log.step("Run FM-Test java class to check resulting output");
		checkFreemMarkerOutput(true, 
				"resources/results/fm-output.txt", 
				"Bunny does 6 little hops :)", 
				new String[]{"src", projectName, "FMTest.java"});
		checkErrorLog();
	}	

	private void openFTLFileInEditor() {
		
		PackageExplorer explorer = new PackageExplorer();
		Project project = explorer.getProject(projectName);
		project.expand();
		project.refresh();
		ProjectItem item = project.getProjectItem(parentFolder, "welcome.ftl");
		item.open();
		
		new TextEditor("welcome.ftl");
		
		log.step("Open outline view and check freemarker elements there");
		OutlineView ov = new OutlineView();
		ov.open();
		
		Collection<TreeItem> outlineElements = ov.outlineElements();
		
		List<String> list = new ArrayList<String>();
		for (TreeItem i : outlineElements) {
			list.add(i.getText());
		}
		
		assertTrue(list.contains("user"));
		assertTrue(list.contains("latestProduct.name"));		
		assertTrue("Should contain latestProduct.url + see JBIDE-11287", list.contains("latestProduct.url"));		
	}
	

}
