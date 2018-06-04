package org.jboss.tools.maven.ui.bot.test.ui;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Test;

public class SeamPluginsTest extends AbstractMavenSWTBotTest{
	
	@Test
	public void testSeamIsNotPresent(){
		new ShellMenuItem("Help","About Red Hat Developer Studio").select();
		new PushButton("Installation Details").click();
		new DefaultShell(new WithTextMatcher(new RegexMatcher(".*Installation Details")));
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
		new PushButton("Close").click();
		if(!foundSeamPlugins.isEmpty()){
			fail("seam plugins " + foundSeamPlugins+ " is/are present but all seam2 plugins should be removed");
		}
	}

}
