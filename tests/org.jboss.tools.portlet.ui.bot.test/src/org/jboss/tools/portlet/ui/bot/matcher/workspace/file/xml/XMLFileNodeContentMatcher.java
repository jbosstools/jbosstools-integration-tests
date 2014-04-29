package org.jboss.tools.portlet.ui.bot.matcher.workspace.file.xml;

import java.util.List;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.entity.XMLNode;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;

/**
 * Checks if the file contains specified nodes.
 * 
 * @author Lucia Jelinkova
 *
 */
public class XMLFileNodeContentMatcher extends JavaPerspectiveAbstractSWTMatcher<WorkspaceFile> {

	private List<XMLNode> nodes;
	
	public XMLFileNodeContentMatcher(List<XMLNode> nodes) {
		super();
		this.nodes = nodes;
	}
	
	@Override
	protected boolean matchesSafelyInJavaPerspective(WorkspaceFile file) {
		new PackageExplorer().getProject(file.getProject()).getProjectItem(file.getFilePathAsArray()).open();
		new DefaultEditor(file.getFileName());
		List<TreeItem> items = new DefaultTree().getAllItems();
		for(XMLNode node : nodes){
			int index = 0;
			String[] path = node.getPathAsArray();
			for(TreeItem item : items){
				if(item.getText().equals(path[0])){
					index++;
					while (index < path.length){
						item = item.getItem(path[index]);
						index++;
					}
					if(!item.getCell(1).equals(node.getContent())){
						return false;
					}
					break;
				}
			}
		}
		
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("contains XML nodes: " + nodes);
	}
}

