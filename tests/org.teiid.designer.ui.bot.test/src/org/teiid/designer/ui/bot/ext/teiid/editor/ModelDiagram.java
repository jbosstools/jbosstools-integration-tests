package org.teiid.designer.ui.bot.ext.teiid.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;

public class ModelDiagram {

	protected SWTBotGefEditPart editPart;

	public ModelDiagram(SWTBotGefEditPart editPart) {
		if(editPart.getClass().toString().equals("")) {
			
		}
		this.editPart = editPart;
	}

	public SWTBotGefEditPart getEditPart() {
		return editPart;
	}

	public void select() {
		editPart.select();
	}
	
	public List<String> getModelAttributes(SWTBotGefEditPart editPart) {
		List<String> modelAttributes = new ArrayList<String>();
		return modelAttributes;
	}
	
}
