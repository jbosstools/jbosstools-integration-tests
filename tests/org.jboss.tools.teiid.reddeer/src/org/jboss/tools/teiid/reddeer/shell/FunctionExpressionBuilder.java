package org.jboss.tools.teiid.reddeer.shell;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder.RadioButtonType;

public class FunctionExpressionBuilder{

	private SWTBotShell shell;
	private List<Combo> combos = new ArrayList<Combo>();
	private CriteriaBuilder cb;
	
	public FunctionExpressionBuilder(SWTBotShell sh){
		this.shell = sh;
		Matcher m = allOf(widgetOfType(Combo.class));
		this.combos = new SWTBot().widgets(m);
	}
	
	
	public void setCategory(String category){
		new SWTBotCombo(combos.get(0)).setSelection(category);
	}

	public void setFunction(String function){
		new SWTBotCombo(combos.get(1)).setSelection(function);
	}
	
	public void apply(){
		new PushButton("Apply").click();
	}
	
	public void close(){
		new PushButton("OK").click();
	}
	
	public void setRadioButtonType(String typeOfRadioButton){
		this.cb = new CriteriaBuilder(this.shell);
		cb.selectRadioButton(typeOfRadioButton, 0);
	}
	
	public void setColumn(String document, String element){
		cb.selectLeftAttribute(document, element, true);
	}
	
}
