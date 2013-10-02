package org.jboss.tools.bpmn2.itests.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.IDataType;

public class DataType implements IDataType {

	private String name;
	
	public DataType(String name) {
		this.name = name;
	}
	
	public void add() {
		if (!selectType(name)) {
			new PushButton(0).click();
			new SWTBot().shell("Create New Data Type").activate();
			new LabeledText("Structure").setText(name);
			new PushButton("OK").click();
		}
		selectType(name);
	}
	
	private boolean selectType(String name) {
		// BZ-1008637
		try {
			new DefaultCombo("Data Type").setSelection(name);
		} catch (SWTLayerException e1) {
			try {
				new DefaultCombo("Item").setSelection(name);
			} catch (SWTLayerException e2) {
				return false;
			}
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
}
