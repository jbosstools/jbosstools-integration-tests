package org.jboss.tools.bpel.reddeer.activity;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.hamcrest.Matcher;
import org.jboss.tools.bpel.reddeer.editor.BpelEditor;
import org.jboss.tools.bpel.reddeer.matcher.ActivityOfType;
import org.jboss.tools.bpel.reddeer.matcher.ActivityWithName;
import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

public class SequenceImpl {

	private BpelEditor bpelEditor;
	private SWTBotGefEditPart editPart;

	public SequenceImpl(String parent) {
		this(null, parent, 0);
	}

	public SequenceImpl(String name, String parent) {
		this(name, parent, 0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SequenceImpl(String name, String parent, int index) {
		bpelEditor = new BpelEditor();
		SWTBotGefEditPart parentEditPart = bpelEditor.getEditPart(parent);
		Matcher matcher = allOf(new ActivityOfType("Sequence"));
		if (name != null) {
			matcher = allOf(new ActivityOfType("Sequence"), new ActivityWithName(name));
		}
		editPart = bpelEditor.getEditPart(parentEditPart, matcher).get(index);
	}

	public void select() {
		bpelEditor.selectEditPart(editPart);
	}

	public void setName(String name) {
		select();

		BPELPropertiesView properties = new BPELPropertiesView();
		properties.setName(name);
	}
}
