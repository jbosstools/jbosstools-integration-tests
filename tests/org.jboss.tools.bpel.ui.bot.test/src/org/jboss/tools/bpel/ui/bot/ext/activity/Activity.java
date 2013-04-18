package org.jboss.tools.bpel.ui.bot.ext.activity;

import static org.hamcrest.core.AllOf.allOf;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.hamcrest.Matcher;
import org.jboss.tools.bpel.ui.bot.ext.editor.BpelEditor;
import org.jboss.tools.bpel.ui.bot.ext.matcher.ActivityOfType;
import org.jboss.tools.bpel.ui.bot.ext.matcher.ActivityWithName;
import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Activity {

	public static final String ASSIGN = "Assign";
	public static final String COMPENSATE = "Compensate";
	public static final String COMPENSATE_SCOPE = "CompensateScope";
	public static final String EMPTY = "Empty";
	public static final String EXIT = "Exit";
	public static final String FLOW = "Flow";
	public static final String FOR_EACH = "ForEach";
	public static final String IF = "If";
	public static final String INVOKE = "Invoke";
	public static final String PICK = "Pick";
	public static final String RECEIVE = "Receive";
	public static final String REPEAT_UNTIL = "RepeatUntil";
	public static final String REPLY = "Reply";
	public static final String RETHROW = "Rethrow";
	public static final String SCOPE = "Scope";
	public static final String SEQUENCE = "Sequence";
	public static final String THROW = "Throw";
	public static final String VALIDATE = "Validate";
	public static final String WAIT = "Wait";
	public static final String WHILE = "While";

	protected String name;
	protected String type;

	protected BpelEditor bpelEditor;
	protected SWTBotGefEditPart editPart;

	protected Logger log = Logger.getLogger(this.getClass());

	public Activity(String name, String type) {
		this(name, type, null, 0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Activity(String name, String type, Activity parent, int index) {
		if (type == null) {
			throw new IllegalArgumentException("Activity type must be specified!");
		}
		this.bpelEditor = new BpelEditor();
		this.name = name;
		this.type = type;

		List<Matcher<?>> matchers = new ArrayList<Matcher<?>>();
		matchers.add(new ActivityOfType(type));
		if (name != null) {
			matchers.add(new ActivityWithName(name));
		}
		Matcher matcher = allOf(matchers);

		if (parent != null) {
			List<SWTBotGefEditPart> editParts = bpelEditor.getEditPart(parent.editPart, matcher);
			if (editParts.isEmpty()) {
				throw new RuntimeException("Could not find an activity of type " + type + "'");
			}
			editPart = editParts.get(index);
		} else if (name != null) {
			editPart = bpelEditor.getEditPart(name);
		} else {
			editPart = bpelEditor.getEditPart(matcher, index);
		}
		select();
	}

	public void select() {
		bpelEditor.selectEditPart(editPart);
	}

	public void delete() {
		menu("Delete");
	}

	public Activity setName(String name) {
		select();
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.setName(name);
		bpelEditor.save();
		return this;
	}

	protected void add(String type) {
		menu("Add", type);
	}

	protected void add(String type, String name) {
		log.info("Add activity '" + name + "' of type <" + type + ">");
		add(type);
		if (name != null) {
			// we expect that each activity has its default name as its type
			new Activity(type, type, this, 0).setName(name);
			bpelEditor.save();
		}
	}

	protected void menu(String... menu) {
		select();
		for (int i = 0; i < menu.length; i++) {
			bpelEditor.clickContextMenu(menu[i]);
		}
		bpelEditor.save();
	}

	public boolean validate(String text) {
		throw new UnsupportedOperationException();
	}

}
