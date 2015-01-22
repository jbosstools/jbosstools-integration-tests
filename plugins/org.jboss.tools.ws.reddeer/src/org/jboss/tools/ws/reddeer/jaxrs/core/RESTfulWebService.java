package org.jboss.tools.ws.reddeer.jaxrs.core;

import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;

/**
 * 
 * RESTful Web Service item under RESTful Web Services node in Project explorer.
 * 
 * @author mlabuda@redhat.com
 */
public class RESTfulWebService {

	private TreeViewerHandler handler = TreeViewerHandler.getInstance();
	
	private ProjectItem projectItem;
	
	private String httpMethod;
	private String path;
	
	public RESTfulWebService(ProjectItem projectItem) {
		this.projectItem = projectItem;
		
		TreeItem item = projectItem.getTreeItem();
		httpMethod = handler.getStyledTexts(item)[0];
		path = handler.getNonStyledText(item);
	}
	
	/**
	 * Selects project item representing RESTful web service.
	 */
	public void select() {
		projectItem.select();
	}
	
	/**
	 * Expands project item representing RESTful web service.
	 */
	public void expand() {
		projectItem.getTreeItem().expand();
	}
	
	/**
	 * Opens web service in text editor on the line with defined RESTful web service.
	 */
	public void open() {
		projectItem.open();
	}
	
	/**
	 * Gets HTTP web service method.
	 * 
	 * @return specific HTTP method of web service.
	 */
	public String getMethod() {
		return httpMethod;
	}
	
	/**
	 * Gets URI matching pattern for web service request path.
	 * 
	 * @return URI matching pattern of web service
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Gets class where RESTful web service is defined.
	 * 
	 * @return class where web service is located
	 */
	public String getJavaClassOfWebService() {
		return getClassAndMethod().split(".(")[0];
	}
	
	/**
	 * Gets java method related to RESTful web service.
	 * 
	 * @return java method of web service
	 */
	public String getJavaMethodOfWebService() {
		return getClassAndMethod().split(".(")[1];		
	}
	
	private String getClassAndMethod() {
		expand();
		for (TreeItem subItem: projectItem.getTreeItem().getItems()) {
			if (handler.getNonStyledText(subItem) == null) {
				return subItem.getText();
			}
		}
		throw new RESTfulException("RESTful web service does not contain class and method of its location.");
	}
	
	/**
	 * Gets content type of HTTP response.
	 * @return MIME type of response
	 */
	public String getProducingContentType() {
		return getContentType("produces:");
	}
	
	/**
	 * Gets content type of HTTP request.
	 * @return MIME type of request
	 */
	public String getConsumingContentType() {
		return getContentType("consumes:");
	}
	
	// Gets producing or consuming content type, depends on parameter
	private String getContentType(String process) {
		expand();
		for (TreeItem subItem: projectItem.getTreeItem().getItems()) {
			if (handler.getStyledTexts(subItem)[0].equals(process)) {
				return handler.getNonStyledText(subItem);
			}
		}
		throw new RESTfulException("RESTful web service does not contain processed content type.");
	}
}
