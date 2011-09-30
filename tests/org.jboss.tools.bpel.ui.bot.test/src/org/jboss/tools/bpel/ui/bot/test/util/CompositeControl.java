package org.jboss.tools.bpel.ui.bot.test.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.hamcrest.SelfDescribing;

public class CompositeControl extends AbstractSWTBotControl<Composite> {
	
	public CompositeControl(Composite w, SelfDescribing description) throws WidgetNotFoundException {
		super(w);
	}
	
	public CompositeControl click() { 
	waitForEnabled();
	notify(SWT.MouseDown);
	notify(SWT.MouseUp);
	return this;
	}
}
