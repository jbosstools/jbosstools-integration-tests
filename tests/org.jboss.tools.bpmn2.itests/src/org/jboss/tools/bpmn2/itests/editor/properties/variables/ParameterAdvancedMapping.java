package org.jboss.tools.bpmn2.itests.editor.properties.variables;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author mbaluch
 */
public class ParameterAdvancedMapping implements IMapping {

	String script;
	
	String[] fromExpression;
	
	String[] toExpression;
	
	public ParameterAdvancedMapping(String script, String[] fromExpression, String[] toExpression) {
		this.script = script;
		this.fromExpression = fromExpression;
		this.toExpression = toExpression;
	}
	
	public void add() {
		new RadioButton("Advanced Mapping").click();
		new DefaultCombo("Script Language").setSelection("XPath");
		new LabeledText("Script").setText(script);
		
		if (fromExpression.length != toExpression.length) {
			throw new RuntimeException("Length 'from=" + fromExpression.length + "' and 'to=" + toExpression.length + "' are not the same");
		}
		
		SWTBot bot = Bot.get();
		for (int i=0; i<fromExpression.length; i++) {
			bot.toolbarButtonWithTooltip("Add", 2).click();
			bot.comboBoxWithLabel("Script Language", 1).setSelection("XPath");
			bot.textWithLabel("Script", 1).setText(fromExpression[i]);
			bot.comboBoxWithLabel("Script Language", 2).setSelection("XPath");
			bot.textWithLabel("Script", 2).setText(toExpression[i]);
			bot.toolbarButtonWithTooltip("Close", 1).click();
		}
	}
}
