package org.jboss.tools.bpel.ui.bot.ext.widgets;

import java.util.List;

import org.eclipse.bpel.model.FromPart;
import org.eclipse.bpel.model.OnAlarm;
import org.eclipse.bpel.model.OnMessage;
import org.eclipse.bpel.model.impl.ActivityImpl;
import org.eclipse.bpel.model.impl.ElseImpl;
import org.eclipse.bpel.model.impl.IfImpl;
import org.eclipse.bpel.model.impl.OnAlarmImpl;
import org.eclipse.bpel.model.impl.OnMessageImpl;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.AssertionFailedException;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.part.MultiPageEditorPart;

import org.jboss.tools.ui.bot.ext.widgets.SWTBotMultiPageEditor;


public class SWTBotBPELEditor extends SWTBotMultiPageEditor {

	private SWTBotGefEditor gEditor;
	
	private SWTBotPropertiesView propsView;
	
	
	public SWTBotBPELEditor(SWTBotGefEditor editor, SWTWorkbenchBot bot) {
		super(editor.getReference(), bot);
		gEditor = editor;
		propsView = new SWTBotPropertiesView(bot);
		propsView.show();
	}
	
	public void showPropertiesView() {
		propsView.show();
	}
	
	/** TODO: replace with simple parameters "partnerLink", "operation" when JBIDE-7861 is fixed. */
	// DONE !
	public SWTBotGefEditPart addInvoke(SWTBotGefEditPart toPart, String name, String in, String out, String[] operationInfo) {
		appendActivity(toPart, "Invoke", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		propsBot.tree().expandNode(operationInfo).select();
		propsBot.text(2).setText(in);
		propsBot.text(3).setText(out);
		// Save changes to update process model
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
//		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}	
	
	/** TODO: see {@link #addInvoke(SWTBotGefEditPart, String, String, String, String...)} */
	// DONE !
	public SWTBotGefEditPart addReceive(SWTBotGefEditPart toPart, String name, String var, String[] operationInfo) {
		appendActivity(toPart, "Receive", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		propsBot.tree().expandNode(operationInfo).select();
		propsBot.text(2).setText(var);
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
//		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	/** 
	 * TODO: see {@link #addInvoke(SWTBotGefEditPart, String, String, String, String...)}
	 *  - add faultName 
	 * 
	 */
	// DONE !
	public SWTBotGefEditPart addReply(SWTBotGefEditPart toPart, String name, String var, String[] operationInfo) {
		appendActivity(toPart, "Reply", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		propsBot.tree().expandNode(operationInfo).select();
//		propsBot.text(2).setText("FaultName");
		propsBot.text(3).setText(var);
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
//		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	// DONE!
	public SWTBotGefEditPart addEmpty(SWTBotGefEditPart toPart, String name) {
		appendActivity(toPart, "Empty", name);
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	
	// DONE!
	public SWTBotGefEditPart addValidate(SWTBotGefEditPart toPart, String name, String ... variables) {
		appendActivity(toPart, "Validate", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		propsBot.button("Add").click();
		
		SWTBotShell shell = bot.shell("Select Variable").activate();
		SWTBot viewBot = shell.bot();
		SWTBotTable table = viewBot.table();
		table.select(variables);
		viewBot.button("OK").click();
		
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
//		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	
	// DONE!
	public SWTBotGefEditPart addIf(SWTBotGefEditPart toPart, String name, String condition) {
		appendActivity(toPart, "If", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		propsBot.button("Create a New Condition").click();
		propsBot.styledText().setText(condition);
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}

	/**
	 * TODO: Change the implementation of this method. At the moment
	 *       this method expects a concrete order of child elements !!! 
	 * @param ifPart
	 * @return
	 */
	public SWTBotGefEditPart addElse(SWTBotGefEditPart ifPart) {
		setFocus(ifPart);
		gEditor.clickContextMenu("Add Else");
		save();
		// get ElsePart
		List<SWTBotGefEditPart> children = ifPart.children(); 
		SWTBotGefEditPart added = children.get(children.size() - 1);
		log.info("Added [part=" + added + ", name=Unnamed]");
		return added;
	}
	
	/**
	 * TODO: See {@link #addElse(SWTBotGefEditPart)}
	 * @param ifPart
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public SWTBotGefEditPart addElseIf(SWTBotGefEditPart ifPart, String condition) throws Exception {
		setFocus(ifPart);
		gEditor.clickContextMenu("Add ElseIf");
		
		// get the new ElseIfPart
		List<SWTBotGefEditPart> children = ifPart.children();
		SWTBotGefEditPart elseIfPart = children.get(children.size() - 1);
		// test the part
		if(elseIfPart.part().getModel() instanceof ElseImpl) {
			elseIfPart = children.get(children.size() - 2);
		}
		setFocus(elseIfPart);
		// setup properties
		propsView.selectTab(0);
		SWTBot propsBot = propsView.bot();
		propsBot.button("Create a New Condition").click();
		propsBot.styledText().setText(condition);
		save();
		log.info("Added [part=" + elseIfPart + ", name=Unnamed]");
		return elseIfPart;
	}
	
	// DONE !
	public SWTBotGefEditPart addPick(SWTBotGefEditPart toPart, String name, boolean createInstance) {
		appendActivity(toPart, "Pick", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		if(createInstance) {
			propsBot.checkBox().select();
		}
		
		SWTBotGefEditPart pickPart = getEditPart(toPart, name);
		SWTBotGefEditPart onMessagePart = pickPart.children().get(0);
		setFocus(onMessagePart);
		// remove OnMessage branch to make the methods coherent
		gEditor.clickContextMenu("Delete");
		setFocus(pickPart);
		save();
		
		log.info("Added [part=" + pickPart + ", name=" + pickPart + "]");
		return pickPart;
	}
	
	public SWTBotGefEditPart addPickOnMessage(SWTBotGefEditPart pickPart, String in, String[] operationInfo) {
		setFocus(pickPart);
		gEditor.clickContextMenu("Add OnMessage");
		save();

		// get the new ElseIfPart
		List<SWTBotGefEditPart> children = pickPart.children();
		SWTBotGefEditPart onMessagePart = children.get(children.size() - 1);
		// test the part
		if(!(onMessagePart.part().getModel() instanceof OnMessage)) {
			onMessagePart = children.get(children.size() - 2);
		}
		
		setFocus(onMessagePart);
		propsView.selectTab(0);
		SWTBot propsBot = propsView.bot();
		propsBot.tree().expandNode(operationInfo).select();
		propsBot.text(2).setText(in);
		save();
		
		log.info("Added [part=" + onMessagePart + ", name=Unnamed]");
		
		return onMessagePart;
	}
	
	public SWTBotGefEditPart addPickOnAlarm(SWTBotGefEditPart pickPart, String expression) {
		setFocus(pickPart);
		gEditor.clickContextMenu("Add OnAlarm");
		save();
		
		// get the new ElseIfPart
		List<SWTBotGefEditPart> children = pickPart.children();
		SWTBotGefEditPart onAlaramPart = children.get(children.size() - 1);
		// test the part
		if(!(onAlaramPart.part().getModel() instanceof OnAlarm)) {
			onAlaramPart = children.get(children.size() - 2);
		}
		setFocus(onAlaramPart);
		SWTBot propsBot = propsView.bot();
		propsBot.button("Create a New Condition").click();
		propsBot.comboBox(1).setSelection("Text");
		propsBot.styledText().setText(expression);
		save();
		
		log.info("Added [part=" + onAlaramPart + ", name=Unnamed]");
		
		// if onAlarm contains scope then return the scope. onAlarm otherwise
		return (onAlaramPart.children().size() == 1) ? 
				onAlaramPart.children().get(0): onAlaramPart;
		
	}
	
	public SWTBotGefEditPart addAssign(SWTBotGefEditPart toPart, String name) {
		appendActivity(toPart, "Assign", name);
		propsView.selectTab(1);
		SWTBot propsBot = propsView.bot();
		try {
			propsBot.list().select("? to ?");
			propsBot.button("Delete").click();
		} catch (AssertionFailedException e) {
			log.info(e.getMessage());
		}
		save();
		SWTBotGefEditPart assign = getEditPart(toPart, name);
		log.info("Added [part=" + assign + ", name=" + name + "]");
		return assign;
	}

	public SWTBotGefEditPart copyVarToVar(SWTBotGefEditPart assignPart, String[] from, String[] to) {
		setFocus(assignPart);
		propsView.selectTab(1);
		SWTBot propsBot = propsView.bot();
		propsBot.button("New").click();
		propsBot.tree(0).expandNode(from).select();
		propsBot.tree(1).expandNode(to).select();
		save();
		// Initializer
		try {
			log.info("Initializer view was opend ... clicking YES.");
			bot.shell("Initializer").bot().button("Yes").click();
		} catch (Exception e) {
			log.warn(e.getMessage());
			log.info("Initializer view was not opened.");
		}
		return assignPart;
	}
	
	// TODO - return just the new <copy></copy> element
	public SWTBotGefEditPart copyVarToExpresion(SWTBotGefEditPart assignPart, String[] from, String exp) {
		setFocus(assignPart);
		propsView.selectTab(1);
		SWTBot propsBot = propsView.bot();
		propsBot.button("New").click();
		propsBot.comboBox(1).setSelection("Expression");
		propsBot.tree().expandNode(from).select();
		propsBot.styledText().setText(exp);
		save();

		return assignPart;
	}

	public SWTBotGefEditPart copyExpressionToExpression(SWTBotGefEditPart assignPart, String from, String to) {
		setFocus(assignPart);
		propsView.selectTab(1);
		SWTBot propsBot = propsView.bot();
		propsBot.button("New").click();
		propsBot.comboBox(0).setSelection("Expression");
		propsBot.styledText(0).setText(from);
		
		propsBot.comboBox(2).setSelection("Expression");
		propsBot.styledText(1).setText(to);
		save();
		
		return assignPart;
	}
	

	public SWTBotGefEditPart copyFixedToExpression(SWTBotGefEditPart assignPart, String from, String to) {
		setFocus(assignPart);
		propsView.selectTab(1);
		SWTBot propsBot = propsView.bot();
		propsBot.button("New").click();
		propsBot.comboBox(0).setSelection("Fixed Value");
		propsBot.comboBox(1).setSelection("Expression");
		
		propsBot.text().setText(from);
		propsBot.styledText().setText(to);
		save();
		
		return assignPart;
	}

	public SWTBotGefEditPart copyFixedToVar(SWTBotGefEditPart assignPart, String exp, String[] to) {
		setFocus(assignPart);
		propsView.selectTab(1);
		SWTBot propsBot = propsView.bot();
		propsBot.button("New").click();
		propsBot.comboBox(0).setSelection("Fixed Value");
		propsBot.text().setText(exp);
		propsBot.tree().expandNode(to).select();
		save();
		// Initializer
		try {
			log.info("Initializer view was opend ... clicking YES.");
			bot.shell("Initializer").bot().button("Yes").click();
		} catch (Exception e) {
			log.warn(e.getMessage());
			log.info("Initializer view was not opened.");
		}
		return assignPart;
	}
	
	// DONE !
	public SWTBotGefEditPart addWhile(SWTBotGefEditPart toPart, String name, String condition) {
		appendActivity(toPart, "While", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		propsBot.button("Create a New Condition").click();
		propsBot.styledText().setText(condition);
		
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	public SWTBotGefEditPart addForEach(SWTBotGefEditPart toPart, String name, String startExpression, String finalExpression) throws Exception {
		appendActivity(toPart, "ForEach", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(2);
		
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
		
		// TODO: maybe delete the scope element
		SWTBotGefEditPart added = getEditPart(toPart, name);
		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	public SWTBotGefEditPart addRepeatUntil(SWTBotGefEditPart toPart, String name, String condition) {
		appendActivity(toPart, "RepeatUntil", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		propsBot.button("Create a New Condition").click();
		propsBot.styledText().setText(condition);
		
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	public SWTBotGefEditPart addWait(SWTBotGefEditPart toPart, String name, String expression) {
		appendActivity(toPart, "Wait", name);
		SWTBot propsBot = propsView.bot();
		propsView.selectTab(1);
		propsBot.button("Create a New Condition").click();
		propsBot.comboBox(1).setSelection("Text");
		propsBot.styledText().setText(expression);
		
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	public SWTBotGefEditPart addSequence(SWTBotGefEditPart toPart, String name) {
		appendActivity(toPart, "Sequence", name);
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	public SWTBotGefEditPart addFlow(SWTBotGefEditPart toPart, String name) {
		appendActivity(toPart, "Flow", name);
		save();
		SWTBotGefEditPart added = getEditPart(toPart, name);
		log.info("Added [part=" + added + ", name=" + name + "]");
		return added;
	}
	
	public SWTBotGefEditPart addScope(SWTBotGefEditPart toPart, String name, boolean isoalated) {
		appendActivity(toPart, "Scope", name);
		propsView.selectTab(1);
		if(isoalated) {
			propsView.bot().checkBox().select();
		}
		SWTBotGefEditPart scope = getEditPart(toPart, name);
		log.info("Added [part=" + scope + ", name=" + name + "]");
		return scope;
	}

	// TODO: add properties and test
	public SWTBotGefEditPart addFaultHandler(SWTBotGefEditPart part) {
		setFocus(part);
		gEditor.clickContextMenu("Add Fault Handler");
		save();
		
		printChildren(part);
		
		return null;
	}
	
	// test me!!!
	public SWTBotGefEditPart addExit(SWTBotGefEditPart toPart, String name) {
		appendActivity(toPart, "Exit", name);
		SWTBotGefEditPart exit = getEditPart(toPart, name);
		log.info("Added [part=" + exit + ", name=" + name + "]");
		return exit;
	}
	
	public SWTBotGefEditPart addThrow() { throw new UnsupportedOperationException(); }
	
	public SWTBotGefEditPart addRethrow() { throw new UnsupportedOperationException(); }
	
	public SWTBotGefEditPart addCompensate() { throw new UnsupportedOperationException(); }
	
	public SWTBotGefEditPart addCompensateScope() { throw new UnsupportedOperationException(); }
	
	public void appendActivity(SWTBotGefEditPart toPart, String activity, String name) {
		setFocus(toPart);
		gEditor.clickContextMenu("Add").clickContextMenu(activity);
		propsView.selectTab(0);
		SWTBot propsBot = propsView.bot();
		propsBot.text(0).setText(name);
	}
	
	// TODO: - test me
	//       - How to validate this???
	public void delete(SWTBotGefEditPart part) {
		gEditor.setFocus();
		part.select();
		gEditor.clickContextMenu("Delete");
	}

	public void save() {
		gEditor.save();
	}
	
	public SWTBotGefEditPart getEditPart(SWTBotGefEditPart fromPart, String label) {
		Object model = fromPart.part().getModel();
//		System.out.println("PART: " + fromPart.part());
		if(model instanceof ActivityImpl) {
			ActivityImpl activity = (ActivityImpl) model;
//			System.out.println("ACTIVITY NAME: " + activity.getName() + ", " + activity);
			if(fromPart.children().isEmpty() && label.equals(activity.getName())) {
				return fromPart;
			}
		}
		List<SWTBotGefEditPart> allEditParts = fromPart.children();
		allEditParts.addAll(fromPart.sourceConnections());
		// ElseIf and Else must use element name !!!
		// ElseIf must use also coordinates to verify that it's the last one.
		return getEditPart(allEditParts, label);
	}
	
	protected SWTBotGefEditPart getEditPart(List<SWTBotGefEditPart> allEditParts, String label) {
		for (SWTBotGefEditPart child : allEditParts) {
			Object model = child.part().getModel();
			if(model instanceof ActivityImpl) {
//				System.out.println("PART: " + child.part());
				ActivityImpl activity = (ActivityImpl) child.part().getModel();
//				System.out.println("ACTIVITY NAME: " + activity.getName() + ", " + activity);
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
	
	public void setFocus(SWTBotGefEditPart part) {
		gEditor.setFocus();
		part.select();
	}

	public void printControls(Composite composite) {
		printControls("", composite);
	}
	
	private void printControls(String prefix, Composite composite) {
		Control[] children = composite.getChildren();
		for(Control ctrl : children) {
			System.out.println(prefix + " " + ctrl.getClass().getName());
			if(ctrl instanceof Composite) {
				Composite c = (Composite) ctrl;
				if(c.getChildren().length > 0) {
					printControls(prefix + "\t",  c);
				}
			}
		}
	}
	
	public void printChildren(SWTBotGefEditPart part) {
		printChildren("", part);
	}
	
	private void printChildren(String prefix, SWTBotGefEditPart part) {
		List<SWTBotGefEditPart> children = part.children();
		for(SWTBotGefEditPart c : children) {
			System.out.println(prefix + " " + c.part());
			if(c.children().size() > 0) {
				printChildren(prefix + "\t",  c);
			}
		}
	}
	
}
