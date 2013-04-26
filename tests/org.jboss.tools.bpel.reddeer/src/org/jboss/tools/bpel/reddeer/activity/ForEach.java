package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class ForEach extends Activity {

	public ForEach(String name) {
		super(name, FOR_EACH);
	}

	public void addScope(String name) {
		add(SCOPE, name);
	}

	public Scope getScope() {
		return new Scope(null, this);
	}

	public ForEach setCounterValue(String startExpression, String finalExpression) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.selectTab("Counter Values");
		Bot.get().styledText(0).setText(startExpression);
		Bot.get().styledText(1).setText(finalExpression);
		bpelEditor.save();
		return this;
	}
}
