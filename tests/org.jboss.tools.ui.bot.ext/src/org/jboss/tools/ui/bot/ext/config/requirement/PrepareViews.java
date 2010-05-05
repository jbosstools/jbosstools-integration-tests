package org.jboss.tools.ui.bot.ext.config.requirement;

import java.util.List;
import java.util.Vector;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.IView;

/**
 * this is a special requirement which should run only once, main aim of this is  to prepare
 * and setup views behavior (e.g. force Console View not to steal focus) 
 * @author lzoubek@redhat.com
 *
 */
public class PrepareViews extends RequirementBase {

	@Override
	public boolean checkFulfilled() {
		return SWTTestExt.configuredState.isViewsPrepared();
	}

	@Override
	public void handle() {		
		SWTTestExt.open.viewClose(new IView(){
			public List<String> getGroupPath() {
				// TODO Auto-generated method stub
				return new Vector<String>();
			}
			public String getName() {
				// TODO Auto-generated method stub
				return "Welcome";
			}});
		
		// force console view not to steal focus when something happens
		try {
		SWTBotView consoleView = SWTTestExt.console.show();
		consoleView.toolbarToggleButton("Show Console When Standard Out Changes").deselect();
		consoleView.toolbarToggleButton("Show Console When Standard Error Changes").deselect();
		} catch (Exception ex) {
			// do nothing since buttons must not always be available 
		}
		SWTTestExt.configuredState.setViewsPrepared(true);

	}

}
