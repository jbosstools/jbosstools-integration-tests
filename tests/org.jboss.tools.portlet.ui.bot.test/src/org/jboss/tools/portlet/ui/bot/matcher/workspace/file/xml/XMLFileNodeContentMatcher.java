package org.jboss.tools.portlet.ui.bot.matcher.workspace.file.xml;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.entity.XMLNode;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.parts.SWTBotEditorExt;

/**
 * Checks if the file contains specified nodes.
 * 
 * @author Lucia Jelinkova
 *
 */
public class XMLFileNodeContentMatcher extends AbstractSWTMatcher<WorkspaceFile> {

	private List<XMLNode> nodes;
	
	public XMLFileNodeContentMatcher(List<XMLNode> nodes) {
		super();
		this.nodes = nodes;
	}
	
	@Override
	public boolean matchesSafely(WorkspaceFile file) {
		SWTBotFactory.getPackageexplorer().openFile(file.getProject(), file.getFilePathAsArray());
		SWTBotEditorExt editor = SWTBotFactory.getBot().swtBotEditorExtByTitle(file.getFileName());
		SWTBotTree tree = editor.bot().tree();
		
		for (XMLNode node : nodes){
			if (!containsNode(editor.bot(), tree, node)){
				return false;
			}
		}
		return true;
	}
	
	private boolean containsNode(SWTBot bot, SWTBotTree tree, XMLNode node) {
		SWTBotTreeItem item = SWTEclipseExt.getTreeItemOnPathStartsWith(bot, tree, 0, node.getNodeName(), getNodePath(node));
		return item.cell(1).contains(node.getContent());
	}

	private String[] getNodePath(XMLNode node) {
		String[] path = node.getPathAsArray();
		if (path.length <= 1){
			return new String[0];
		}
		return Arrays.copyOfRange(path, 0, path.length - 1);
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("file containing nodes: " + nodes);
	}
}

