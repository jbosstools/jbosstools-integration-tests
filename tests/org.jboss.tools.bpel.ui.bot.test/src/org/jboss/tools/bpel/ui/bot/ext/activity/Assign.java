package org.jboss.tools.bpel.ui.bot.ext.activity;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Assign extends Activity {

	public static final String LABEL_FROM = "From:";
	public static final String LABEL_TO = "To:";

	public static final String VAR = "Variable";
	public static final String FIX = "Fixed Value";
	public static final String EXP = "Expression";

	public Assign(String name) {
		super(name, ASSIGN);
	}

	public Assign addFixToVar(String fixedValue, String variable) {
		return addFixToVar(fixedValue, new String[] { variable });
	}

	public Assign addFixToVar(String fixedValue, String[] variable) {
		return addAssign(FIX, new String[] { fixedValue }, VAR, variable);
	}

	public Assign addVarToExp(String variable, String expression) {
		return addVarToExp(new String[] { variable }, expression);
	}

	public Assign addFixToExp(String fixedValue, String exression) {
		return addAssign(FIX, new String[] { fixedValue }, EXP, new String[] { exression });
	}

	public Assign addVarToExp(String[] variable, String expression) {
		return addAssign(VAR, variable, EXP, new String[] { expression });
	}

	public Assign addExpToVar(String expression, String variable) {
		return addExpToVar(expression, new String[] { variable });
	}

	public Assign addExpToVar(String expression, String[] variable) {
		return addAssign(EXP, new String[] { expression }, VAR, variable);
	}

	public Assign addExpToExp(String expression1, String expression2) {
		return addAssign(EXP, new String[] { expression1 }, EXP, new String[] { expression2 });
	}

	public Assign addVarToVar(String[] variable1, String[] variable2) {
		return addAssign(VAR, variable1, VAR, variable2);
	}

	public Assign addAssign(String from, String[] valueFrom, String to, String[] valueTo) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.selectDetails();
		new PushButton("New").click();
		fillAssignement(LABEL_FROM, from, valueFrom);
		fillAssignement(LABEL_TO, to, valueTo);

		// Variable doesn't have initializer. Should it be generated?
		if (new DefaultShell().getText().equals("Initializer")) {
			new PushButton("Yes").click();
			bpelEditor.save();
		}
		return this;
	}

	private void fillAssignement(String label, String assignment, String... value) {
		new DefaultCombo(label).setSelection(assignment);
		if (assignment.equals(VAR)) {
			Bot.get().treeWithLabel(label).expandNode(value).select();
		}
		if (assignment.equals(FIX)) {
			new LabeledText(label).setText("'" + value[0] + "'");
		}
		if (assignment.equals(EXP)) {
			Bot.get().styledTextWithLabel(label).setText(value[0]);
		}
		bpelEditor.save();
	}

}
