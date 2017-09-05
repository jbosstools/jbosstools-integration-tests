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
package org.jboss.tools.batch.reddeer.editor.jobxml;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.hamcrest.Matcher;

/**
 * Represents the whole JobXMLEditor (all three tabs)
 * 
 * @author Lucia Jelinkova
 *
 */
public class JobXMLEditor extends MultiPageEditorPart {

	public JobXMLEditor(String title) {
		this(new WithTextMatcher(title));
	}
	
	@SuppressWarnings("unchecked")
	public JobXMLEditor(Matcher<String> titleMatcher) {
		super(titleMatcher, "org.jboss.tools.batch.ui.editor.internal.model.JobXMLEditor");
	}
	
	/**
	 * Select the design page tab
	 */
	public void selectDesignPage(){
		super.selectPage("Design");
	}

	/**
	 * Select the diagram page tab
	 */
	public void selectDiagramPage(){
		super.selectPage("Diagram");
	}
	
	/**
	 * Select the source page tab
	 */
	public void selectSourcePage(){
		super.selectPage("Source");
	}
	
	/**
	 * Return object for working with design page
	 * @return page that enables to work with XML in tree format
	 */
	public JobXMLEditorDesignPage getDesignPage(){
		selectDesignPage();
		return new JobXMLEditorDesignPage();
	}
	
	/**
	 * Return object for working with diagram page
	 * @return page that enables to work with XML in tree format
	 */
	public JobXMLEditorDiagramPage getDiagramPage(){
		selectDiagramPage();
		return new JobXMLEditorDiagramPage();
	}
	
	/**
	 * Return object for working with source page
	 * @return page that enables to work with XML in text format
	 */
	public JobXMLEditorSourcePage getSourcePage(){
		selectSourcePage();
		StructuredTextEditor textEditor = Display.syncExec(new ResultRunnable<StructuredTextEditor>() {

			@Override
			public StructuredTextEditor run() {
				return ((org.jboss.tools.batch.ui.editor.internal.model.JobXMLEditor) getEditorPart()).getSourceEditor();
			}
		});
		return new JobXMLEditorSourcePage(textEditor);
	}

}
