package org.jboss.tools.portlet.ui.bot.test.matcher.workspace.file.xml;

import java.util.Arrays;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.test.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;

/**
 * Checks if the file contains specified nodes.
 * 
 * @author Lucia Jelinkova
 *
 */
public class XMLFileNodeContentMatcher extends AbstractSWTMatcher<XMLNode[]> {

	private String project;
	
	private String file;
	
	public XMLFileNodeContentMatcher(String project, String file) {
		super();
		this.project = project;
		this.file = file;
	}
	
	@Override
	public boolean matchesSafely(XMLNode[] nodes) {
		String[] filePath = file.split("/");
		SWTBotFactory.getPackageexplorer().openFile(project, filePath);
		SWTBotEditorExt editor = SWTBotFactory.getBot().swtBotEditorExtByTitle(filePath[filePath.length - 1]);
		SWTBotTree tree = editor.bot().tree();
		
		for (XMLNode node : nodes){
			if (!containsNode(editor.bot(), tree, node)){
				return false;
			}
		}
		return true;
	}
	
	private boolean containsNode(SWTBot bot, SWTBotTree tree, XMLNode node) {
		for (String s : getNodePath(node)){
			System.out.println("Path element: " + s);
		}
		System.out.println("Name: " + getNodeName(node));
		SWTBotTreeItem item = SWTEclipseExt.getTreeItemOnPathStartsWith(bot, tree, 0, getNodeName(node), getNodePath(node));
		return item.cell(1).contains(node.getContent());
	}

	private String[] getNodePath(XMLNode node) {
		String[] path = node.getPath().split("/");
		if (path.length <= 1){
			return new String[0];
		}
		return Arrays.copyOfRange(path, 0, path.length - 1);
	}
	
	private String getNodeName(XMLNode node) {
		String[] path = node.getPath().split("/");
		return path[path.length - 1];
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("file " + project + "/" + file + " contains the node(s)");
	}
}

