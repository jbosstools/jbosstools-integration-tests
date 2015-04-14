package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;

public class EditorContainsText implements WaitCondition{
	
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
