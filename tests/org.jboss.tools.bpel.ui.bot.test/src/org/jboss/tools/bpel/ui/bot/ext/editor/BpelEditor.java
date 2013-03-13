package org.jboss.tools.bpel.ui.bot.ext.editor;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpel.ui.bot.ext.matcher.ActivityOfType;
import org.jboss.tools.bpel.ui.bot.ext.matcher.ActivityWithName;

/**
 * 
 * @author apodhrad
 * 
 */
public class BpelEditor extends SWTBotGefEditor {

	private Logger log = Logger.getLogger(BpelEditor.class);

	public BpelEditor() {
		super(Bot.get().activeEditor().getReference(), Bot.get());
	}

	public BpelEditor(String title) {
		super(Bot.get().editorByTitle(title).getReference(), Bot.get());
	}

	public void selectActivity(String label) {
		log.info("Select activity '" + label + "'");
		SWTBotGefEditPart editPart = getEditPart(label);
		if (editPart == null) {
			throw new RuntimeException("Cannot find '" + label + "'");
		}
		selectEditPart(editPart);
	}

	public void selectActivity(Matcher<? extends EditPart> matcher, int index) {
		selectEditPart(getEditPart(matcher, index));
	}

	public void selectEditPart(SWTBotGefEditPart editPart) {
		setFocus();
		editPart.select();
	}

	public SWTBotGefEditPart getEditPart(Matcher<? extends EditPart> matcher, int index) {
		List<SWTBotGefEditPart> editParts = editParts(matcher);
		return editParts.get(index);
	}

	public SWTBotGefEditPart getEditPart(String label) {
		SWTBotGefEditPart editPart = super.getEditPart(label);
		if (editPart == null) {
			List<SWTBotGefEditPart> list = editParts(new ActivityWithName(label));
			if (!list.isEmpty()) {
				return list.get(0);
			}
		}
		return editPart;
	}

	public List<SWTBotGefEditPart> getEditPart(SWTBotGefEditPart editPart,
			Matcher<? extends EditPart> matcher) {
		return editPart.descendants(matcher);
	}

	public SWTBotGefEditPart getSelectedActivity() {
		List<SWTBotGefEditPart> selectedParts = selectedEditParts();
		if (!selectedParts.isEmpty()) {
			return selectedParts.get(0);
		}
		return null;
	}

	// public void add(Activity activity) {
	// clickContextMenu(activity.getMenu());
	// activity.select(this);
	// activity.fillProperties();
	// save();
	// }

	// public void add(String parent, Activity activity) {
	// selectActivity(parent);
	// add(activity);
	// }

	public void delete() {
		// we assume that an activity was selected
		clickContextMenu("Delete");
	}

	public void debug() {
		GraphicalEditPart gep = (GraphicalEditPart) mainEditPart().part();
		debug(gep.getFigure(), 0);
	}

	public void debug(IFigure fig, int count) {
		for (int i = 0; i < count; i++) {
			System.out.print("\t");
		}
		System.out.println(fig.getClass());
		if (fig instanceof Label) {
			System.out.println(((Label) fig).getText());
		}
		List children = fig.getChildren();
		for (Object child : children) {
			debug((IFigure) child, count + 1);
		}
	}

	public void debugBpelDiagram() {
		String result = debugBpelDiagram(mainEditPart(), 0);
		System.out.println(result);
	}

	public String debugBpelDiagram(SWTBotGefEditPart editPart, int count) {
		for (int i = 0; i < count; i++) {
			System.out.print("\t");
		}
		StringBuffer buf = new StringBuffer();
		EditPart ep = editPart.part();
		GraphicalEditPart gep = (GraphicalEditPart) ep;
		IFigure fig = gep.getFigure();
		String type = ActivityOfType.getActivityType(ep);
		String name = ActivityWithName.getActivityName(ep);
		String label = fig.getClass().toString();
		if (fig instanceof Label) {
			label = ((Label) fig).getText();
		}
		buf.append("[").append(type);
		buf.append("<").append(name).append(">");
		System.out.println(type + " <" + name + "> " + label);
		// debug(fig, count);
		List<SWTBotGefEditPart> children = editPart.children();
		for (SWTBotGefEditPart child : children) {
			buf.append(debugBpelDiagram(child, count + 1));
		}
		buf.append("]");
		return buf.toString();
	}

	private String getInterface(SWTBotGefEditPart editPart) {
		Class<?> clazz = editPart.part().getModel().getClass();
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces.length > 0) {
			return interfaces[0].getSimpleName();
		}
		return null;

	}

	private void clickContextMenu(String... menu) {
		for (int i = 0; i < menu.length; i++) {
			clickContextMenu(menu[i]);
		}
	}

	private void validateWithSource() {

	}

	private void validateWithOutline() {

	}

	public void addPartnerLink() {
	}

	public void removePartnerLink() {

	}

	public void addVariable() {
	}

	public void removeVariable() {

	}
}
