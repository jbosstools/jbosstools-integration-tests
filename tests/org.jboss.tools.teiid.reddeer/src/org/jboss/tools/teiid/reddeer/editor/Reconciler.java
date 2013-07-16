package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;

public class Reconciler {

	private SWTBotShell shell;
	
	public static class ExpressionBuilder {
		public static final String KEEP_VIRTUAL_TARGET = "Convert all source SQL symbol datatypes";
		public static final String KEEP_SQL_SYMBOLS = "Change all target column datatypes";
	}
	
	
	public Reconciler(SWTBotShell shell) {
		this.shell = shell;
	}
	
	/**
	 * 
	 * @param left -- virtual target column
	 * @param right -- unmatched SQL symbol
	 */
	public void bindAttributes(String left, String right){
		new DefaultTable().select(left);//left  -- virtual target column
		new DefaultTable(1).select(right);//right -- unmatched SQL symbol
		new PushButton("< Bind").click();
	}
	
	public void addNewVirtualTargetAttribute(String unmatchedSQLSymbol){
		new DefaultTable(1).select(unmatchedSQLSymbol);
		new PushButton("< New").click();
	}
	
	public void expressionBuilderConstant(String virtualTargetColumn, String constantType, String value){
		new DefaultTable().select(virtualTargetColumn);
		shell.bot().buttonWithTooltip("Create Expression").click();
		new RadioButton("Constant").click();
		new DefaultCombo(0).setSelection(constantType);
		new DefaultText().setText(value);
		new PushButton("Apply").click();
		new PushButton("OK").click();
	}
	
	public void clearRemainingUnmatchedSymbols(){
		new PushButton("Clear").click();
	}
	
	/**
	 * Closes and saves the reconciler
	 */
	public void close(){
		new PushButton("OK").click();
	}
	
	/**
	 * 
	 * @param resolveType constant from ExpressionBuilder - e.g. ExpressionBuilder.KEEP_VIRTUAL_TARGET
	 */
	public void resolveTypes(String resolveType){
		new PushButton("Type Resolver...").click();
		new PushButton(resolveType).click();
		new PushButton("OK").click();
	}
}
