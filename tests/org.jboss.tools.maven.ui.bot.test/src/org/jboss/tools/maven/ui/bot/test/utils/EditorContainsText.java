package org.jboss.tools.maven.ui.bot.test.utils;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;

public class EditorContainsText extends AbstractWaitCondition{
	
	private TextEditor editor;
	
	public EditorContainsText(TextEditor editor){
		this.editor = editor;
	}

	@Override
	public boolean test() {
		return editor.getText().isEmpty();
	}

	@Override
	public String description() {
		return "Editor "+editor.getTitle()+" contains text";
	}

}
