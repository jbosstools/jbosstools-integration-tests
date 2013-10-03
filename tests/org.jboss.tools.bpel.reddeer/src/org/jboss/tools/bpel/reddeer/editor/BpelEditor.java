package org.jboss.tools.bpel.reddeer.editor;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.gef.EditPart;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.hamcrest.Matcher;
import org.jboss.tools.bpel.reddeer.matcher.ActivityWithName;

/**
 * 
 * @author apodhrad
 * 
 */
public class BpelEditor extends SWTBotGefEditor {

	private static SWTWorkbenchBot bot = new SWTWorkbenchBot();
	
	private Logger log = Logger.getLogger(BpelEditor.class);

	public BpelEditor() {
		super(bot.activeEditor().getReference(), bot);
	}

	public BpelEditor(String title) {
		super(bot.editorByTitle(title).getReference(), bot);
	}

	public String getSource() {
		return toTextEditor().getText();
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

	public List<SWTBotGefEditPart> getEditPart(SWTBotGefEditPart editPart, Matcher<? extends EditPart> matcher) {
		return editPart.descendants(matcher);
	}

	public SWTBotGefEditPart getSelectedActivity() {
		List<SWTBotGefEditPart> selectedParts = selectedEditParts();
		if (!selectedParts.isEmpty()) {
			return selectedParts.get(0);
		}
		return null;
	}

	public void delete() {
		// we assume that an activity was selected
		clickContextMenu("Delete");
	}

	@Override
	public void saveAndClose() {
		super.saveAndClose();
	}

	public void addPartnerLink() {
		throw new UnsupportedOperationException();
	}

	public void removePartnerLink() {
		throw new UnsupportedOperationException();

	}

	public void addVariable() {
		throw new UnsupportedOperationException();
	}

	public void removeVariable() {
		throw new UnsupportedOperationException();

	}
}
