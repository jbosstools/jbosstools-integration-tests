package org.jboss.tools.batch.reddeer.editor.jobxml;

import org.eclipse.ui.texteditor.ITextEditor;
import org.hamcrest.Matcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.part.MultiPageEditor;

/**
 * Represents the whole JobXMLEditor (all three tabs)
 * 
 * @author Lucia Jelinkova
 *
 */
public class JobXMLEditor extends MultiPageEditor {

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
		Object o = getSelectedPage();
		if (o instanceof ITextEditor){
			return new JobXMLEditorSourcePage((ITextEditor) o);
		} 
		throw new EclipseLayerException("Expected " + ITextEditor.class + 
				" but was " + o.getClass());
	}
}
