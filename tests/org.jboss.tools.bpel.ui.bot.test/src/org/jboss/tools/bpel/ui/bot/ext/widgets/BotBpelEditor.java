package org.jboss.tools.bpel.ui.bot.ext.widgets;

import java.io.IOError;
import java.util.List;

import javax.swing.KeyStroke;

import org.eclipse.bpel.model.Else;
import org.eclipse.bpel.model.If;
import org.eclipse.bpel.model.OnAlarm;
import org.eclipse.bpel.model.Pick;
import org.eclipse.bpel.model.impl.ActivityImpl;
import org.eclipse.bpel.model.impl.CatchImpl;
import org.eclipse.bpel.model.impl.CompensationHandlerImpl;
import org.eclipse.bpel.model.impl.FaultHandlerImpl;
import org.eclipse.bpel.model.impl.OnMessageImpl;
import org.eclipse.bpel.model.impl.ScopeImpl;
import org.eclipse.bpel.model.impl.SequenceImpl;

import org.eclipse.gef.EditPart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.AssertionFailedException;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;

import org.jboss.tools.ui.bot.ext.widgets.SWTBotMultiPageEditor;
import org.junit.Assert;

/**
 * TODO: - add support for catchAll to fault handlers
 *        
 * @author mbaluch
 *
 */
public class BotBpelEditor extends SWTBotMultiPageEditor {

	SWTBotGefEditor gefEditor;
	
	SWTBotPropertiesView propertiesView;
	
	SWTBotGefEditPart selectedPart;
	
	BPELEditPartListener listener;
	
	/**
	 * Creates a new instance of SWTBotBPELEditor
	 * 
	 * @param gefEditor
	 * @param bot
	 */
	public BotBpelEditor(SWTBotGefEditor gefEditor, SWTWorkbenchBot bot) {
		super(gefEditor.getReference(), bot);
		
		this.gefEditor = gefEditor;
		this.propertiesView = new SWTBotPropertiesView(bot);
		this.listener = new BPELEditPartListener();
		
		EditPartEventLogger eventLogger = new EditPartEventLogger();
		SWTBotGefEditPart mainSequencePart = gefEditor
			.mainEditPart().children().get(1);
		mainSequencePart.part().addEditPartListener(eventLogger);
		select(mainSequencePart);
	}
	
	/**
	 * TODO: the line on which the variable is declared needs to be selected (caret needs to present) otherwise
	 * 		 propertries will not change and type selection will not work
	 * @param name
	 * @param type
	 */
	public void addVariable(String name, String type) {
//		activatePage("Source");
//		SWTBotEclipseEditor textEditor = gefEditor.toTextEditor();
//		int line = getLine("(.)*<variables>(.)*");
//		textEditor.insertText(line, textEditor.getLines().get(line).length(), "\n");
//		line++;
//		textEditor.insertText(line, 0, "\t\t<variable name=\"" + name + "\"/>");
//		textEditor.setFocus();
//		textEditor.navigateTo(line, 0);
//		textEditor.selectLine(line);
//		textEditor.contextMenu(text);
//		propertiesView.selectTab(1);
//		SWTBot propsBot = propertiesView.bot();
//		propsBot.button("Browse...");
//		
//		SWTBot viewBot = bot.shell("Choose type of variable").bot();
//		try {
//			viewBot.radio("From Imports").click();
//			viewBot.table().select(type);
//		} catch (Exception e) {
//			e.printStackTrace();
//			viewBot.radio("From Project").click();
//			viewBot.table().select(type);
//		}
//		activatePage("Design");
		throw new UnsupportedOperationException();
	}
	
	public void addCorrelationSet(String name) {
		SWTBotEclipseEditor textEditor = gefEditor.toTextEditor();
		int correlationSetsLine = getLine("(.)*<correlationSets(.)*");
		if(correlationSetsLine == -1) {
			int pl = getLine("(.)*</partnerLinks>(.)*");
			textEditor.insertText(pl + 1, 0, "\t<correlationSets>\n\n\t</correlationSets>");
			correlationSetsLine = pl + 2;
		} else {
			textEditor.insertText(correlationSetsLine, textEditor.getLines().get(correlationSetsLine).length(), "\n");
			correlationSetsLine++;
		}
		textEditor.insertText(correlationSetsLine, 0, "\t\t<correlationSet name=\"" + name + "\"></bpel:correlationSet>");
		textEditor.selectLine(correlationSetsLine);
	}

	public void addCorrelationProperty(String correlationSet, String name) {
//		activatePage("Source");
//		SWTBotEclipseEditor textEditor = gefEditor.toTextEditor();
//		// select correlation set
//		int line = -1;
//		List<String> lines = textEditor.getLines();
//		for(int i=0; i<lines.size(); i++) {
//			if(lines.get(i).matches("(.)*correlationSet(.)*name=\"" + correlationSet + "\"(.)*")) {
//				line = i;
//			}
//			System.out.println(i + ") '" + lines.get(i) + "'");
//		}
//		System.out.println("Selecting line=" + line);
//		textEditor.selectLine(line);
//		activatePage("Design");
//
//		// setup properties
//		propertiesView.selectTab(1);
//		SWTBot propsBot = propertiesView.bot();
//		propsBot.button("Add...").click();
//		
//		SWTBot propsViewBot = bot.shell("Select a Property").bot();
//		propsViewBot.button("New...").click();
//		
//		SWTBot newPropViewBot = bot.shell("Create Message Property").bot();
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Generate code:
	 * 
	 * {@code
	 *  <bpel:assign name="${label}">
	 *  </bpel:assign>
	 * }
	 * 
	 * @param name
	 * @return
	 */
	protected BotBpelEditor addAssign(String name) {
		appendActivity("Assign", name);
		propertiesView.selectTab(1);
		SWTBot propertiesBot = propertiesView.bot();
		try {
			propertiesBot.list().select("? to ?");
			propertiesBot.button("Delete").click();
		} catch (AssertionFailedException e) {
			log.info(e.getMessage());
		}
		save();
		return this;
	}
	
//	public SWTBotGefEditPart addCopyVarToVar(SWTBotGefEditPart assignPart, String[] from, String[] to) {
//		setFocus(assignPart);
//		propsView.selectTab(1);
//		SWTBot propsBot = propsView.bot();
//		propsBot.button("New").click();
//		propsBot.tree(0).expandNode(from).select();
//		propsBot.tree(1).expandNode(to).select();
//		save();
//		// Initializer
//		try {
//			log.info("Initializer view was opened ... clicking YES.");
//			bot.shell("Initializer").bot().button("Yes").click();
//		} catch (Exception e) {
//			log.warn(e.getMessage());
//			log.info("Initializer view was not opened.");
//		}
//		return assignPart;
//	}
//	
//	// TODO - return just the new <copy></copy> element
//	public SWTBotGefEditPart addCopyVarToExpresion(SWTBotGefEditPart assignPart, String[] from, String exp) {
//		setFocus(assignPart);
//		propsView.selectTab(1);
//		SWTBot propsBot = propsView.bot();
//		propsBot.button("New").click();
//		propsBot.comboBox(1).setSelection("Expression");
//		propsBot.tree().expandNode(from).select();
//		propsBot.styledText().setText(exp);
//		save();
//
//		return assignPart;
//	}
//
//	public SWTBotGefEditPart addCopyExpressionToExpression(SWTBotGefEditPart assignPart, String from, String to) {
//		setFocus(assignPart);
//		propsView.selectTab(1);
//		SWTBot propsBot = propsView.bot();
//		propsBot.button("New").click();
//		propsBot.comboBox(0).setSelection("Expression");
//		propsBot.styledText(0).setText(from);
//		
//		propsBot.comboBox(2).setSelection("Expression");
//		propsBot.styledText(1).setText(to);
//		save();
//		
//		return assignPart;
//	}
//	
//
//	public SWTBotGefEditPart addCopyFixedToExpression(SWTBotGefEditPart assignPart, String from, String to) {
//		setFocus(assignPart);
//		propsView.selectTab(1);
//		SWTBot propsBot = propsView.bot();
//		propsBot.button("New").click();
//		propsBot.comboBox(0).setSelection("Fixed Value");
//		propsBot.comboBox(1).setSelection("Expression");
//		
//		propsBot.text().setText(from);
//		propsBot.styledText().setText(to);
//		save();
//		
//		return assignPart;
//	}
//
//	public SWTBotGefEditPart addCopyFixedToVar(SWTBotGefEditPart assignPart, String exp, String[] to) {
//		setFocus(assignPart);
//		propsView.selectTab(1);
//		SWTBot propsBot = propsView.bot();
//		propsBot.button("New").click();
//		propsBot.comboBox(0).setSelection("Fixed Value");
//		propsBot.text().setText(exp);
//		propsBot.tree().expandNode(to).select();
//		save();
//		// Initializer
//		try {
//			log.info("Initializer view was opened ... clicking YES.");
//			bot.shell("Initializer").bot().button("Yes").click();
//		} catch (Exception e) {
//			log.warn(e.getMessage());
//			log.info("Initializer view was not opened.");
//		}
//		return assignPart;
//	}
	
	public BotBpelEditor addAssignVarToVar(String label, String[] from, String[] to) {
		addAssign(label);
		propertiesView.selectTab(1);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.button("New").click();
		propertiesBot.tree(0).expandNode(from).select();
		propertiesBot.tree(1).expandNode(to).select();
		save();
		// Initializer
		try {
			log.info("Initializer view was opened ... choosing Yes.");
			bot.shell("Initializer").bot().button("Yes").click();
		} catch (Exception e) {
			log.warn(e.getMessage());
			log.info("Initializer view was not opened.");
		}

		fireEditFinished(selectedPart.part(), new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addAssignVarToExpression(String label, String[] from, String exp) {
		addAssign(label);
		propertiesView.selectTab(1);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.button("New").click();
		propertiesBot.comboBox(1).setSelection("Expression");
		propertiesBot.tree().expandNode(from).select();
		propertiesBot.styledText().setText(exp);
		save();

		fireEditFinished(selectedPart.part(), new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addAssignExpressionToExpression(String label, String from, String to) {
		addAssign(label);
		propertiesView.selectTab(1);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.button("New").click();
		propertiesBot.comboBox(0).setSelection("Expression");
		propertiesBot.styledText(0).setText(from);
		
		propertiesBot.comboBox(2).setSelection("Expression");
		propertiesBot.styledText(1).setText(to);
		save();
		
		fireEditFinished(selectedPart.part(), new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addAssignFixedToExpression(String label, String fixed, String expression) {
		addAssign(label);
		propertiesView.selectTab(1);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.button("New").click();
		propertiesBot.comboBox(0).setSelection("Fixed Value");
		propertiesBot.comboBox(1).setSelection("Expression");
		
		propertiesBot.text().setText(fixed);
		propertiesBot.styledText().setText(expression);
		
		fireEditFinished(selectedPart.part(), new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		save();
		return this;
	}
	
	// TODO - Test me!
	public BotBpelEditor addAssignFixedToVar(String label, String exp, String[] to) {
		addAssign(label);
		propertiesView.selectTab(1);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.button("New").click();
		propertiesBot.comboBox(0).setSelection("Fixed Value");
		propertiesBot.text().setText(exp);
		propertiesBot.tree().expandNode(to).select();
		save();
		// Initializer
		try {
			log.info("Initializer view was opened ... clicking YES.");
			bot.shell("Initializer").bot().button("Yes").click();
		} catch (Exception e) {
			log.warn(e.getMessage());
			log.info("Initializer view was not opened.");
		}

		fireEditFinished(selectedPart.part(), new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addInvoke(String label, String in, String out, String partnerLink, String operation) {
		appendActivity("Invoke", label);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propertiesBot.text(0).setText(partnerLink).pressShortcut(Keystrokes.LF);
		propertiesBot.text(1).setText(operation).pressShortcut(Keystrokes.LF);
		propertiesBot.text(2).setText(in);
		propertiesBot.text(3).setText(out);
		// Save changes to update process model
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name", "partnerLink", "operation", "inputVariable", "outputVariable"}, 
				new String[] {label, partnerLink, operation, in, out});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addInvoke(String label, String in, String out, String[] operationInfo) {
		appendActivity("Invoke", label);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propertiesBot.tree().expandNode(operationInfo).select();
		propertiesBot.text(2).setText(in);
		propertiesBot.text(3).setText(out);
		// Save changes to update process model
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name", "partnerLink", "operation", "inputVariable", "outputVariable"}, 
				new String[] {label, operationInfo[0], operationInfo[2], in, out});
		select(selectedPart.parent());
		return this;
	}	
	
	public BotBpelEditor addReceive(String label, String var, String partnerLink, String operation, boolean createInstance) {
		appendActivity("Receive", label);
		propertiesView.selectTab(1);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.text(0).setText(partnerLink).pressShortcut(Keystrokes.LF);
		propertiesBot.text(1).setText(operation).pressShortcut(Keystrokes.LF);
		if(createInstance) {
			propertiesBot.checkBox(1).select();
		}
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name", "partnerLink", "operation", "variable", "createInstance"}, 
				new String[] {label, partnerLink, operation, var, Boolean.toString(createInstance)});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addReceive(String label, String var, String[] operationInfo, boolean createInstance) {
		appendActivity("Receive", label);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propertiesBot.tree().expandNode(operationInfo).select();
		propertiesBot.text(2).setText(var);
		if(createInstance) {
//			propertiesBot.checkBox("Create a new Process instance if one does not already exist").select();
			propertiesBot.checkBox(1).select();
		}
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name", "partnerLink", "operation", "variable", "createInstance"}, 
				new String[] {label, operationInfo[0], operationInfo[2], var, Boolean.toString(createInstance)});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addReply(String label, String var, String partnerLink, String operation, String fault) {
		appendActivity("Reply", label);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propertiesBot.text(0).setText(partnerLink).pressShortcut(Keystrokes.LF);
		propertiesBot.text(1).setText(operation).pressShortcut(Keystrokes.LF);
		propertiesBot.text(2).setText(fault != null ? fault : "");
		propertiesBot.text(3).setText(var);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		// TODO add fault
		fireEditFinished(part.part(), new String[] {"name", "partnerLink", "operation", "variable"}, 
				new String[] {label, partnerLink, operation, var});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addReply(String label, String var, String fault, String[] operationInfo) {
		appendActivity("Reply", label);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propertiesBot.tree().expandNode(operationInfo).select();
		propertiesBot.text(2).setText(fault != null ? fault : "");
		propertiesBot.text(3).setText(var);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		// TODO add createInstance, fault
		fireEditFinished(part.part(), new String[] {"name", "partnerLink", "operation", "variable"}, 
				new String[] {label, operationInfo[0], operationInfo[2], var});
		select(selectedPart.parent());
		return this;
	}
	
	/**
	 * Generate code:
	 * 
	 * {@code
	 *  <bpel:if name="${label}">
     *      <bpel:condition><![CDATA[${expression}]]></bpel:condition>
     *  </bpel:if>
	 * }
	 * 
	 * @param label name of the activity
	 * @param expression condition expression
	 * @return
	 */
	public BotBpelEditor addIf(String label, String condition) {
		appendActivity("If", label);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propertiesBot.button("Create a New Condition").click();
		propertiesBot.styledText().setText(condition);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name", "condition"}, new String[] {label, condition});
		select(part);
		return this;
	}
	
	public BotBpelEditor addElseIf(String ifLabel, String condition) {
		SWTBotGefEditPart ifPart = getEditPart(selectedPart, ifLabel);
		if(ifPart == null || !(ifPart.part().getModel() instanceof If)) {
			throw new RuntimeException("Pick not found: " + ifLabel);
		}
		select(ifPart);
		gefEditor.clickContextMenu("Add ElseIf");
		// get elseIf container
		SWTBotGefEditPart elseIf = null;
		List<SWTBotGefEditPart> children = ifPart.children();
		elseIf = children.get(children.size() - 1);
		if(elseIf.part().getModel() instanceof Else) {
			elseIf = children.get(children.size() - 2);
		}
		select(elseIf);
		// setup properties
		propertiesView.selectTab(0);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.button("Create a New Condition").click();
		propertiesBot.styledText().setText(condition);
		save();
		fireEditFinished(elseIf.part(), new String[] {"condition"}, new String[] {condition});
		return this;
	}
	
	public BotBpelEditor addElse(String ifLabel) {
		SWTBotGefEditPart ifPart = getEditPart(selectedPart, ifLabel);
		if(ifPart == null || !(ifPart.part().getModel() instanceof If)) {
			throw new RuntimeException("Pick not found: " + ifLabel);
		}
		select(ifPart);
		gefEditor.clickContextMenu("Add Else");
		// get elseIf container
		SWTBotGefEditPart elsePart = null;
		List<SWTBotGefEditPart> children = ifPart.children();
		elsePart = children.get(children.size() - 1);
		select(elsePart);
		save();
		fireEditFinished(elsePart.part(), new String[] {}, new String[] {});
		return this;
	}
	
	public BotBpelEditor addValidate(String label, String ... variables) {
		appendActivity("Validate", label);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propertiesBot.button("Add").click();
		
		SWTBotShell shell = bot.shell("Select Variable").activate();
		SWTBot viewBot = shell.bot();
		SWTBotTable table = viewBot.table();
		table.select(variables);
		viewBot.button("OK").click();
		save();

		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		StringBuilder varsb = new StringBuilder();
		for(String s : variables) {
			varsb.append(s + " ");
		}
		fireEditFinished(part.part(), new String[] {"name", "variables"}, new String[] {label, varsb.toString().trim()});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addEmpty(String label) {
		appendActivity("Empty", label);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		return this;
	}
	
	/**
	 * Generate code:
	 * 
	 * {@code
	 *  <bpel:pick name="${label}" createInstance="${createInstance}">
     *      <bpel:onMessage partnerLink="client" operation="calculateDiscriminant" variable="DiscriminantRequest"></bpel:onMessage>
     *  </bpel:pick>
	 * }
	 * @param label
	 * @param createInstance
	 * @param in
	 * @param operationInfo
	 * @return
	 */
	public BotBpelEditor addPick(String label, boolean createInstance, String in, String[] operationInfo) {
		appendActivity("Pick", label);
		// setup pick properties
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		if(createInstance) {
			propertiesBot.checkBox().select();
		}
		
		SWTBotGefEditPart pickPart = selectedPart;
		SWTBotGefEditPart onMessagePart = getEditPartByClass(pickPart, OnMessageImpl.class);
		save();
		// TODO add "createInstance" = "yes" if createInstance is true
		fireEditFinished(pickPart.part(), new String[] {"name"}, new String[] {label});
		// setup onMessage properties
		select(onMessagePart);
		propertiesBot = propertiesView.bot();
		propertiesBot.tree().expandNode(operationInfo).select();
		propertiesBot.text(2).setText(in);
		save();
		fireEditFinished(onMessagePart.part(), new String[] {"variable", "partnerLink", "operation"}, 
				new String[] {in, operationInfo[0], operationInfo[2]});
		return this;
	}
	
	public BotBpelEditor addOnMessage(String pickLabel, String in, String[] operationInfo) {
		SWTBotGefEditPart pick = getEditPart(selectedPart, pickLabel);
		if(pick == null || !(pick.part().getModel() instanceof Pick)) {
			throw new RuntimeException("Pick not found: " + pickLabel);
		}
		select(pick);
		gefEditor.clickContextMenu("Add OnMessage");
		
		// get onMessage container
		SWTBotGefEditPart onMessage = null;
		List<SWTBotGefEditPart> children = pick.children();
		onMessage = children.get(children.size() - 1);
		if(onMessage.part().getModel() instanceof OnAlarm) {
			onMessage = children.get(children.size() - 2);
		}
		select(onMessage);
		// setup properties
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.tree().expandNode(operationInfo).select();
		propertiesBot.text(2).setText(in);
		save();
		fireEditFinished(onMessage.part(), new String[] {"variable", "partnerLink", "operation"}, new String[] {in, operationInfo[0], operationInfo[2]});
		return this;
	}
	
	public BotBpelEditor addOnAlarm(String pickLabel, String condition) {
		SWTBotGefEditPart pick = getEditPart(selectedPart, pickLabel);
		if(pick == null || !(pick.part().getModel() instanceof Pick)) {
			throw new RuntimeException("Pick not found: " + pickLabel);
		}
		select(pick);
		gefEditor.clickContextMenu("Add OnAlarm");
		
		// get onAlarm container
		List<SWTBotGefEditPart> children = pick.children();
		SWTBotGefEditPart onAlarm = children.get(children.size() - 1);
		select(onAlarm);
		// setup properties
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.button("Create a New Condition").click();
		propertiesBot.comboBox(1).setSelection("Text");
		propertiesBot.styledText().setText(condition);
		save();
		fireEditFinished(onAlarm.part(), new String[] {"for"}, new String[] {condition});
		// TODO: select scope -- required if we wan't to add a compensation handler
		return this;
	}
	
	public BotBpelEditor addWhile(String label, String condition) {
		appendActivity("While", label);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propertiesBot.button("Create a New Condition").click();
		propertiesBot.styledText().setText(condition);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name", "condition"}, new String[] {label, condition});
		select(part);
		return this;
	}
	
	public BotBpelEditor addForEach(String label, String startExpression, String finalExpression) throws Exception {
		appendActivity("ForEach", label);
		SWTBot propsBot = propertiesView.bot();
		propertiesView.selectTab(2);
		
		SWTBotButton leftButton = propsBot.button(0);
		SWTBotButton rightButton = propsBot.button(1);
		
		leftButton.click();
		rightButton.click();
		propsBot.styledText(0).setText(startExpression);
		// Previous change must be saved otherwise we will see an ugly NPE.
		// This issue seems to be caused by SWTBot since the exception cannot
		// be seen by clicking the steps manually.
		save();
		propsBot.styledText(1).setText(finalExpression);
		save();
		// validate
		fireEditFinished(selectedPart.part(),
				new String[] {"name", "startCounterValue", "finalCounterValue"}, new String[] {label, startExpression, finalExpression});
		// select scope which is a child activity of forEach (that one will be probably modeled next)
		select(getEditPartByClass(selectedPart, ScopeImpl.class));
		return this;
	}
	
	public BotBpelEditor addRepeatUntil(String label, String condition) {
		appendActivity("RepeatUntil", label);
		SWTBot propsBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propsBot.button("Create a New Condition").click();
		propsBot.styledText().setText(condition);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name", "condition"}, new String[] {label, condition});
		return this;
	}
	
	public BotBpelEditor addWait(String label, String condition) {
		appendActivity("Wait", label);
		SWTBot propsBot = propertiesView.bot();
		propertiesView.selectTab(1);
		propsBot.button("Create a New Condition").click();
		propsBot.comboBox(1).setSelection("Text");
		propsBot.styledText().setText(condition);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name", "for"}, new String[] {label, condition});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addSequence(String label) {
		appendActivity("Sequence", label);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name"}, new String[] {label});
		select(getEditPart(selectedPart, label));
		return this;
	}
	
	public BotBpelEditor addFlow(String label) {
		appendActivity("Flow", label);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label); 
		fireEditFinished(part.part(), new String[] {"name"}, new String[] {label});
		select(part);
		return this;
	}
	
	public BotBpelEditor addScope(String label, boolean isoalated) {
		appendActivity("Scope", label);
		propertiesView.selectTab(1);
		if(isoalated) {
			propertiesView.bot().checkBox().select();
		}
		save();
		// TODO add isolated=yes if isolated==true
		SWTBotGefEditPart part = getEditPart(selectedPart, label);
		fireEditFinished(part.part(), new String[] {"name"}, new String[] {label});
		select(part);
		return this;
	}

	public BotBpelEditor addCompensationHandler() {
		// add the fault handler to selectedPart
		gefEditor.clickContextMenu("Add Compensation Handler");
		save();

		SWTBotGefEditPart handlerPart = getEditPartByClass(selectedPart, CompensationHandlerImpl.class);
		select(handlerPart);
		SWTBotGefEditPart sequencePart = getEditPartByClass(selectedPart, SequenceImpl.class);
		select(sequencePart);
		// Delete sequence (We need to allow to add other activities ... e.g. flow)
		gefEditor.clickContextMenu("Delete");
		
		save();
		select(handlerPart);
		fireEditFinished(handlerPart.part(), new String[] {}, new String[] {});
		return this;
	}
	
	/**
	 * TODO: there may be the need to set also the faultMessageType field.
	 * NOTE: faultMessageType attribute is not mandatory according to BPEL 2.0 specs.
	 * 
	 * @param faultName
	 * @param var
	 * @return
	 */
	public BotBpelEditor addFaultHandler(String faultName, String var) {
		// add the fult handler to selectedPart
		gefEditor.clickContextMenu("Add Fault Handler");
		save();
		// setup properties
		SWTBotGefEditPart handlerPart = getEditPartByClass(selectedPart, FaultHandlerImpl.class);
		select(handlerPart);
		SWTBotGefEditPart catchPart = getEditPartByClass(selectedPart, CatchImpl.class);
		select(catchPart);
		propertiesView.selectTab(0);

		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.radio("User-defined").click();
		propertiesBot.ccomboBox(0).setSelection(faultName);
		propertiesBot.text().setText(var);
		save();
		
		// Delete default activities
		SWTBotGefEditPart sequencePart = getEditPartByClass(selectedPart, SequenceImpl.class);
		select(sequencePart);
		gefEditor.clickContextMenu("Delete");
		
		// Select catch
		select(catchPart);
		fireEditFinished(handlerPart.part(), new String[] {"faultName", "faultVariable"}, 
				new String[] {faultName, var});
		
		return this;
	}
	
	public BotBpelEditor addExit(String label) {
		appendActivity("Exit", label);
		save();
		SWTBotGefEditPart part = getEditPart(selectedPart, label);
		fireEditFinished(part.part(), new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addThrow(String label, String faultName) {
		appendActivity("Throw", label);
		propertiesView.selectTab(1);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.radio("User-defined").click();
		propertiesBot.text().setText(faultName);
		save();
		fireEditFinished(getEditPart(selectedPart, label).part(), 
				new String[] {"name", "faultName"}, new String[] {label, faultName});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addRethrow(String label) {
		appendActivity("Rethrow", label);
		save();
		fireEditFinished(getEditPart(selectedPart, label).part(), 
				new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addCompensate(String label) {
		appendActivity("Compensate", label);
		save();
		fireEditFinished(selectedPart.part(), new String[] {"name"}, new String[] {label});
		select(selectedPart.parent());
		return this;
	}
	
	public BotBpelEditor addCompensateScope(String label, String target) {
		appendActivity("CompensateScope", label); 
		propertiesView.selectTab(1);
		SWTBot bot = propertiesView.bot();
		bot.ccomboBox().setSelection(target);
		save();
		fireEditFinished(selectedPart.part(), new String[] {"name", "target"}, new String[] {label, target});
		select(selectedPart.parent());
		return this;
		
	}
	
	/**
	 * Create a new activity as child of part returned by {@link #getSelectedPart()} and select it.
	 * 
	 * @param activity activity type
	 * @param label    activity name
	 * @return
	 */
	public SWTBotGefEditPart appendActivity(String activity, String label) {
		// make sure to focus the right part
		select(selectedPart);
		gefEditor.clickContextMenu("Add").clickContextMenu(activity);
		propertiesView.selectTab(0);
		SWTBot propertiesBot = propertiesView.bot();
		propertiesBot.text(0).setText(label);
		save();
		// newly added activity is selected by default
		selectedPart = getEditPart(selectedPart, label);
		return selectedPart;
	}
	
	/**
	 * Select the first non-leaf activity in the parent activity
	 * 
	 * @param  parentPart non-leaf parent part 
	 * @param  branch     the number of the branch. this is most usefull when dealing with pick and if activities
	 * @return container part like sequence, scope, for, while ...
	 */
	public SWTBotGefEditPart getContainerChildPart(SWTBotGefEditPart parentPart, int branch) {
		throw new UnsupportedOperationException();
	}
	
	public SWTBotGefEditPart getSelectedPart() {
		return selectedPart;
	}
	
	public void selectChildPart(SWTBotGefEditPart parentPart, int position) {
		select(getEditPart(parentPart, position));
	}
	
	public void select(SWTBotGefEditPart part) {
		gefEditor.setFocus();
		part.select();
		selectedPart = part;
	}
	
	public SWTBotGefEditPart getEditPart(SWTBotGefEditPart fromPart, int index) {
		List<SWTBotGefEditPart> children = fromPart.children();
		if(children.size() <= index) {
			throw new IndexOutOfBoundsException("The part has only " + children.size() + " children.");
		}
		return fromPart.children().get(index);
	}
	
	public SWTBotGefEditPart getEditPart(SWTBotGefEditPart fromPart, String label) {
		Object model = fromPart.part().getModel();
		if(model instanceof ActivityImpl) {
			ActivityImpl activity = (ActivityImpl) model;
			if(fromPart.children().isEmpty() && label.equals(activity.getName())) {
				return fromPart;
			}
		}
		List<SWTBotGefEditPart> allEditParts = fromPart.children();
		allEditParts.addAll(fromPart.sourceConnections());
		return getEditPart(allEditParts, label);
	}
	
	protected SWTBotGefEditPart getEditPart(List<SWTBotGefEditPart> allEditParts, String label) {
		for (SWTBotGefEditPart child : allEditParts) {
			Object model = child.part().getModel();
			if(model instanceof ActivityImpl) {
				ActivityImpl activity = (ActivityImpl) child.part().getModel();
				if (label.equals(activity.getName())) {
					return child;
				}
		
				SWTBotGefEditPart childEditPart = getEditPart(child, label);
				if (childEditPart != null) {
					return childEditPart;
				}
			}
		}
		return null;
	}
	
	protected void fireEditFinished(EditPart editpart, String[] attributes, String[] values) {
		listener.editFinished(new BPELEditPartEvent(editpart, attributes, values));
	}
	
	private SWTBotGefEditPart getEditPartByClass(final SWTBotGefEditPart parent, Class<?> modelClass) {
		for(SWTBotGefEditPart part : parent.children()) {
			if(modelClass.isInstance(part.part().getModel())) {
				return part;
			}
			
			if(part.children().size() > 0) {
				SWTBotGefEditPart found = getEditPartByClass(part, modelClass);
				if(found != null) {
					return found;
				}
			}
		}
		return null;
	}
	
	private boolean editPartInstanceOf(final SWTBotGefEditPart part, Class<?> modelClass) {
		return modelClass.isInstance(part.part().getModel());
	}
	
	private void printControls(Composite composite) {
		printControls("", composite);
	}
	
	private void printControls(String prefix, Composite composite) {
		Control[] children = composite.getChildren();
		for(Control ctrl : children) {
			if(ctrl instanceof Composite) {
				Composite c = (Composite) ctrl;
				if(c.getChildren().length > 0) {
					printControls(prefix + "\t",  c);
				}
			}
		}
	}
	
	private int getLine(String regex) {
//		if(!getActivePage().equals("Source")) {
//			activatePage("Source");
//		}
		SWTBotEclipseEditor textEditor = toTextEditor();
		int line = -1;
		List<String> lines = textEditor.getLines();
		for(int i=0; i<lines.size(); i++) {
			if(lines.get(i).matches(regex)) {
				line = i;
			}
		}
		return line;
	}
	
}
