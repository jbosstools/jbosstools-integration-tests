package org.jboss.tools.batch.reddeer.editor;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.reddeer.core.matcher.EditorPartTitleMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.workbench.impl.editor.AbstractEditor;

public class MultiPageEditor extends AbstractEditor {

	public MultiPageEditor(String title) {
		this(new WithTextMatcher(title));
	}
	
	public MultiPageEditor(Matcher<String> titleMatcher) {
		super(createEditorMatchers(titleMatcher));
	}
	
	public void selectPage(String name) {
		new DefaultCTabItem(name).activate();
	}
	
	@SuppressWarnings("unchecked")
	private static Matcher<IEditorPart>[] createEditorMatchers(Matcher<String> titleMatcher){ 
		Matcher<IEditorPart>[] matchers = new Matcher[2];
		matchers[0] = new MultiPageEditorMatcher();
		matchers[1] = new EditorPartTitleMatcher(titleMatcher);
		return matchers;
	}
	
	private static class MultiPageEditorMatcher extends TypeSafeMatcher<IEditorPart>{

		@Override
		public void describeTo(Description description) {
			description.appendText("Editor is of type MultiPageEditor");
		}

		@Override
		protected boolean matchesSafely(IEditorPart item) {
			return (item instanceof MultiPageEditorPart); 
		}
	}
}
