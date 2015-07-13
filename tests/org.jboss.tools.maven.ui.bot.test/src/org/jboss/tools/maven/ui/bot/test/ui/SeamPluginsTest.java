package org.jboss.tools.maven.ui.bot.test.ui;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;

public class SeamPluginsTest extends AbstractMavenSWTBotTest{
	
	@Test
	public void testSeamIsNotPresent(){
		new ShellMenu("Help","Installation Details").select();
		Shell installationDetails = new DefaultShell(new WithTextMatcher(new RegexMatcher(".*Installation Details")));
		new DefaultTabItem("Plug-ins").activate();
		new DefaultText().setText("seam");
		Table pluginsTable = new DefaultTable();
		List<TableItem> items = pluginsTable.getItems();
		int columntIndex = pluginsTable.getHeaderIndex("Plug-in Id");
		List<String> foundSeamPlugins = new ArrayList<String>();
		for(TableItem ti: items){
			String pluginId = ti.getText(columntIndex);
			if(pluginId.equals("org.jboss.tools.cdi.xml") ||
					pluginId.equals("org.jboss.tools.cdi.xml.ui") ||
					pluginId.equals("org.jboss.tools.cdi.seam.solder.core") ||
					pluginId.equals("org.jboss.tools.seam.reddeer")){
				//these seam plugins can exist in default installation (seam3 or reddeer)
			} else {
				foundSeamPlugins.add(pluginId);
			}
		}
		new PushButton("Close").click();
		new WaitWhile(new ShellIsActive(installationDetails));
		if(!foundSeamPlugins.isEmpty()){
			fail("seam plugins " + foundSeamPlugins+ " is/are present but all seam2 plugins should be removed");
		}
	}

}
