package org.jboss.tools.teiid.reddeer.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRadio;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.teiid.reddeer.shell.FunctionExpressionBuilder;

public class CriteriaBuilder {
	
	private SWTBotShell shell;
	//private InnerButtonWithToolTipMatcher matcher;
	
	private List<Button> columnButtons = new ArrayList<Button>();
	private List<Button> constantButtons = new ArrayList<Button>();
	private List<Button> functionButtons = new ArrayList<Button>();
	
	public CriteriaBuilder(SWTBotShell shell) {
		this.shell = shell;
		new SWTBot().widgets(new InnerButtonWithToolTipMatcher());//fill lists of column/constant/function buttons
	}
	
	public static class RadioButtonType{
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		
		public static final String COLUMN = "columnRadioButton";
		public static final String CONSTANT = "constantRadioButton";
		public static final String FUNCTION = "functionRadioButton";	
	}
	
	public static class OperatorType{
		public static final int EQUALS = 0;//=
		public static final int NOT_EQUAL = 1;//<>
		public static final int LT = 2;//<
		public static final int GT = 3;//>
		public static final int LESS_OR_EQUAL = 4;//<=
		public static final int GREATER_OR_EQUAL = 5;//>=
		public static final int IS_NULL = 6;//IS NULL
		public static final int LIKE = 7;//LIKE
		public static final int IN = 8;//IN
	}
	
	public List<Button> getColumnButtons() {
		return columnButtons;
	}

	public List<Button> getConstantButtons() {
		return constantButtons;
	}

	public List<Button> getFunctionButtons() {
		return functionButtons;
	}
	
	/**
	 * Just for relational view models from JDBC, not for sources with XML
	 * @param table
	 * @param attribute
	 */
	public void selectLeftAttribute(String table, String attribute){
		shell.bot().tree(1).setFocus();
		shell.bot().tree(1).expandNode(table).select(getAttributeName(table, attribute));
	}
	
	public void selectLeftAttribute(String document, String element, boolean isXMLSource){
		if (isXMLSource){
			shell.bot().tree(1).setFocus();
			shell.bot().tree(1).expandNode(document).select(element);
		} else {
			selectLeftAttribute(document, element);
		}
		
	}
	
	/**
	 * Just for relational view models from JDBC, not for sources with XML
	 * @param table
	 * @param attribute
	 */
	public void selectRightAttribute(String table, String attribute){
		shell.bot().tree(2).setFocus();
		shell.bot().tree(2).expandNode(table).select(getAttributeName(table, attribute));
	}
	
	public void selectRightAttribute(String document, String element, boolean isXMLSource){
		if (isXMLSource){
			shell.bot().tree(2).setFocus();
			shell.bot().tree(2).expandNode(document).select(element);
		} else {
			selectLeftAttribute(document, element);
		}
	}
	
	public void apply(){
		shell.bot().button("Apply").click();
	}
	
	public void finish(){
		new PushButton("OK").click();
	}
	
	private String getAttributeName(String table, String attribute){
		return table + "." + attribute;
	}
	
	/**
	 * 
	 * @param type of radio button (RadioButtonType.COLUMN|CONSTANT|FUNCTION) 
	 * @param leftRight (RadioButtonType.LEFT|RIGHT)
	 */
	public void selectRadioButton(String type, int leftRight){
		//left - 0, right - 1
		if (type.equals(RadioButtonType.COLUMN)){
			new SWTBotRadio(getColumnButtons().get(leftRight)).click();
		}
		if (type.equals(RadioButtonType.CONSTANT)){
			new SWTBotRadio(getConstantButtons().get(leftRight)).click();
		}
		if (type.equals(RadioButtonType.FUNCTION)){
			new SWTBotRadio(getFunctionButtons().get(leftRight)).click();
		}
	}
	
	/**
	 * 
	 * @param operatorType OperatorType.EQUALS|NOT_EQUAL|LT|GT|...
	 */
	public void selectOperator(int operatorType){
		if (operatorType > 8){
			return;
		}
		new DefaultCombo(0).setSelection(operatorType);
	}
	
	/**
	 * problematic if constant = constant... but this shouldn't occur...
	 * @param text
	 */
	public void setConstant(String text){
		new DefaultText(0).setText(text);
	}
	
	
	public void close(){
		new PushButton("OK").click();
	}
	
	public void selectInList(){
		new RadioButton(3).click();
	}
	
	public void selectInSubQuery(){
		new RadioButton(4).click();
	}
	
	public void addConstantsToList(String... constants){
		//new PushButton("Add...").click();
		//FunctionExpressionBuilder feb = new FunctionExpressionBuilder(Bot.get().activeShell());
		for (String constant : constants){
			new PushButton("Add...").click();
			FunctionExpressionBuilder feb = new FunctionExpressionBuilder(Bot.get().activeShell());
			feb.setRadioButtonType(RadioButtonType.CONSTANT);
			new DefaultText(0).setText(constant);
			feb.apply();
			feb.close();
		}
	}
	
	/**
	 * This class is created for efficiency reason -- inner matcher goes through all widgets on page once (instead of calling e.g.
	 * ButtonWithToolTip thrice with different tooltips for column, constant, function)
	 * @author lfabriko
	 *
	 */
	private class InnerButtonWithToolTipMatcher extends BaseMatcher {
		
		private String columnToolTip = "Show the Column Editor";
		private String constantToolTip = "Show the Constant Editor";
		private String functionToolTip = "Show the Function Display Editor";
		
		@Override
		public boolean matches(Object o) {
			if (o instanceof Button){
				Button but = (Button)o;
				if (but.getToolTipText().equals(this.columnToolTip)){
					columnButtons.add(but);
					return true;
				}
				if (but.getToolTipText().equals(this.constantToolTip)){
					constantButtons.add(but);
					return true;
				}
				if (but.getToolTipText().equals(this.functionToolTip)){
					functionButtons.add(but);
					return true;
				}
				
			}
			return false;
		}

		@Override
		public void describeTo(Description arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
