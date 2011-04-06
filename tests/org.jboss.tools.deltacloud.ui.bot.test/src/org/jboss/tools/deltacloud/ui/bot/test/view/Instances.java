package org.jboss.tools.deltacloud.ui.bot.test.view;

import org.apache.log4j.Logger;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View;
import org.jboss.tools.ui.bot.ext.view.ViewBase;

public class Instances extends ViewBase {

	Logger log = Logger.getLogger(Instances.class);

	public Instances() {
		this.viewObject = View.create("Deltacloud","Instances");
	}
	
}
