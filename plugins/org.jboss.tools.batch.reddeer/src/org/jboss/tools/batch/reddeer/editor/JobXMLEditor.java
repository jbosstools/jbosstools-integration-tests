package org.jboss.tools.batch.reddeer.editor;

import org.eclipse.ui.IEditorPart;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.reddeer.core.matcher.EditorPartTitleMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;

/**
 * Represents the whole JobXMLEditor (all three tabs)
 * 
 * @author Lucia Jelinkova
 *
 */
public class JobXMLEditor extends AbstractEditor {

	public JobXMLEditor(String title) {
		this(new WithTextMatcher(title));
	}
	
	public JobXMLEditor(Matcher<String> titleMatcher) {
		super(createEditorMatchers(titleMatcher));
	}
	
	@SuppressWarnings("unchecked")
	private static Matcher<IEditorPart>[] createEditorMatchers(Matcher<String> titleMatcher){ 
		Matcher<IEditorPart>[] matchers = new Matcher[2];
		matchers[0] = new JobXMLEditorMatcher();
		matchers[1] = new EditorPartTitleMatcher(titleMatcher);
		return matchers;
	}
	
	public JobXMLEditorDesignTab openDesignTab(){
		new DefaultCTabItem("Design").activate();
		return new JobXMLEditorDesignTab();
	}
	
	public JobXMLEditorDiagramTab openDiagramTab(){
		new DefaultCTabItem("Diagram").activate();
		return new JobXMLEditorDiagramTab();
	}
	
	public JobXMLEditorSourceTab openSourceTab(){
		new DefaultCTabItem("Source").activate();
		return new JobXMLEditorSourceTab();
	}
	
	private static class JobXMLEditorMatcher extends TypeSafeMatcher<IEditorPart>{

		@Override
		public void describeTo(Description description) {
			description.appendText("Editor is of type JobXMLEditor");
		}

		@Override
		protected boolean matchesSafely(IEditorPart item) {
			return (item instanceof org.jboss.tools.batch.ui.editor.internal.model.JobXMLEditor); 
		}
		
	}
}
