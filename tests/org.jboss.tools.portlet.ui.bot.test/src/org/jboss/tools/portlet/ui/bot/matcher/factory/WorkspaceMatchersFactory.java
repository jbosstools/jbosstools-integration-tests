package org.jboss.tools.portlet.ui.bot.matcher.factory;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.entity.XMLNode;
import org.jboss.tools.portlet.ui.bot.matcher.SWTMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.workspace.ExistingProjectMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.workspace.ProjectFacetsMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.workspace.file.ExistingFileMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.workspace.file.ExistingFilesMatcher;
import org.jboss.tools.portlet.ui.bot.matcher.workspace.file.xml.XMLFileNodeContentMatcher;

/**
 * Factory for workspace specific matchers (projects, files..) 
 * 
 * @author Lucia Jelinkova
 *
 */
public class WorkspaceMatchersFactory {

	private WorkspaceMatchersFactory(){
		// not to be instantiated
	}
	
	public static SWTMatcher<String> isExistingProject(){
		return new ExistingProjectMatcher();
	}
	
	public static SWTMatcher<WorkspaceFile> exists(){
		return new ExistingFileMatcher();
	}
	
	public static SWTMatcher<List<WorkspaceFile>> exist(){
		return new ExistingFilesMatcher();
	}
	
	public static SWTMatcher<String> hasFacets(FacetDefinition... facets){
		return new ProjectFacetsMatcher(facets);
	}
	
	public static SWTMatcher<String> hasFacets(List<FacetDefinition> facets){
		return new ProjectFacetsMatcher(facets.toArray(new FacetDefinition[facets.size()]));
	}
	
	public static SWTMatcher<WorkspaceFile> containsNodes(XMLNode... nodes){
		return new XMLFileNodeContentMatcher(Arrays.asList(nodes));
	}
	
	public static SWTMatcher<WorkspaceFile> containsNodes(List<XMLNode> nodes){
		return new XMLFileNodeContentMatcher(Arrays.asList(nodes.toArray(new XMLNode[nodes.size()])));
	}
}
