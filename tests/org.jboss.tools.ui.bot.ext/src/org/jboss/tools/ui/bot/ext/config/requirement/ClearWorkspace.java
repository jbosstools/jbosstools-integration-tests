package org.jboss.tools.ui.bot.ext.config.requirement;
import java.util.List;
import java.util.Vector;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.gen.IView;

public class ClearWorkspace extends RequirementBase {
	
	@Override
	public boolean checkFulfilled() {
		try {
		return SWTTestExt.bot.shells().length==2 && SWTTestExt.bot.editors().isEmpty() && !SWTTestExt.configuredState.isWelcomeViewVisible();
		}
		catch (Exception ex) {
			log.error("Cannot determine, if all editors and shells are closed", ex);
			return false;
		}
	}

	@Override
	public void handle() {
		SWTTestExt.bot.closeAllShells();
		SWTTestExt.bot.closeAllEditors();
		SWTTestExt.open.viewClose(new IView(){
			public List<String> getGroupPath() {
				// TODO Auto-generated method stub
				return new Vector<String>();
			}
			public String getName() {
				// TODO Auto-generated method stub
				return "Welcome";
			}});
		SWTTestExt.configuredState.setWelcomeViewVisible(false);

	}

}
