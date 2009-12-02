 /*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.ui.bot.ext.types;

/**
 * Base label constants for all widgets. Naming convention is (except buttons
 * and menus) based on Eclipse platform class names of each part (e.g.
 * NewJavaProjectWizardPageOne)
 * 
 * @author jpeterka 
 */
public class IDELabel {
	public class Menu {
		public static final String FILE = "File";
		public static final String NEW = "New";
		public static final String PROJECT = "Project";
		public static final String OTHER = "Other...";
		public static final String WINDOW = "Window";
		public static final String SHOW_VIEW = "Show View";
		public static final String OPEN_PERSPECTIVE = "Open Perspective";
		public static final String OPEN_WITH =  "Open With";
		public static final String TEXT_EDITOR = "Text Editor";
		public static final String EDIT = "Edit";
		public static final String SELECT_ALL = "Select All";
		public static final String CLOSE = "Close";
		public static final String OPEN = "Open";
		public static final String RENAME = "Rename";
		public static final String JSP_FILE = "JSP...";
		public static final String PROPERTIES = "Properties";
		public static final String XHTML_FILE = "XHTML...";
	}

	public class Button {
		public static final String NEXT = "Next >";
		public static final String BACK = "< Back";
		public static final String CANCEL = "Cancel";
		public static final String FINISH = "Finish";
		public static final String OK = "OK";
		public static final String YES = "Yes";
		public static final String NO = "No";
		public static final String CLOSE = "Close";
	}

	public class Shell {
		public static final String NEW_JAVA_PROJECT = "New Java Project";
		public static final String NEW_JAVA_CLASS = "New Java Class";
		public static final String NEW_HIBERNATE_MAPPING_FILE = "New Hibernate XML Mapping file (hbm.xml)";
		public static final String NEW = "New";
		public static final String SAVE_RESOURCE = "Save";
		public static final String RENAME_RESOURCE = "Rename";
		public static final String NEW_JSP_FILE = "New JSP File";
		public static final String PROPERTIES = "Properties";
		public static final String NEW_XHTML_FILE = "New File XHTML";
	}

	public class EntityGroup {
		public static final String HIBERNATE = "Hibernate";
		public static final String JAVA = "Java";
		public static final String SEAM = "Seam";
	}
	
	public class EntityLabel {
		public static final String HIBERNATE_MAPPING_FILE = "Hibernate XML Mapping file (hbm.xml)";
		public static final String HIBERNATE_REVERSE_FILE = "Hibernate Reverse Engineering File(reveng.xml)";
		public static final String HIBERNATE_CONSOLE = "Hibernate Console Configuration";
		public static final String JAVA_CLASS = "Class";
		public static final String JAVA_PROJECT =  "Java Project";
		public static final String SEAM_PROJECT = "Seam Web Project";
		public static final String HIBERNATE_CONFIGURATION_FILE = "Hibernate Configuration File (cfg.xml)";
	}

	public class JavaProjectWizard {
		public static final String PROJECT_NAME = "Project name:";
	}

	public class NewClassCreationWizard {
		public static final String CLASS_NAME = "Name:";
		public static final String PACKAGE_NAME = "Package:";
	}

	public class ShowViewDialog {
		public static final String JAVA_GROUP = "Java";
		public static final String PROJECT_EXPLORER = "Project Explorer";

	}

	public class View {
		public static final String WELCOME = "Welcome";
		public static final String PROJECT_EXPLORER = "Project Explorer";
		public static final String PACKAGE_EXPLORER = "Package Explorer";
	}
	
	public class ViewGroup {
		public static final String GENERAL = "General";
		public static final String JAVA = "Java";
	}

	public class SelectPerspectiveDialog {
		public static final String JAVA = "Java";
		public static final String HIBERNATE = "Hibernate";

	}
	/**
	 * Hibernate Console Wizard (ConsoleConfigurationCreationWizard) Labels (
	 * @author jpeterka
	 *
	 */
	public class HBConsoleWizard {
		public static final String MAIN_TAB = "Main";
		public static final String OPTIONS_TAB = "Options";
		public static final String CLASSPATH_TAB = "Classpath";
		public static final String MAPPINGS_TAB = "Mappings";
		public static final String COMMON_TAB = "Common";
		public static final String PROJECT_GROUP = "Project:";
		public static final String CONFIGURATION_FILE_GROUP = "Configuration file:";
		public static final String SETUP_BUTTON = "Setup...";
		public static final String CREATE_NEW_BUTTON = "Create new...";
		public static final String USE_EXISTING_BUTTON = "Use existing...";
		public static final String DATABASE_DIALECT = "Database dialect:";
		public static final String DRIVER_CLASS = "Driver class:";
		public static final String CONNECTION_URL = "Connection URL:";
		public static final String USERNAME = "Username:";
		public static final String CREATE_CONSOLE_CONFIGURATION = "Create a console configuration";
	}
	
	public class HBConfigurationWizard {
		public static final String FILE_NAME = "File name:";
	}

	public static class RenameResourceDialog {

		public static final String NEW_NAME = "Rename Resource";
		
	}
	
	public static class WebProjectsTree {

		public static final String WEB_CONTENT = "WebContent";
		public static final String CONFIGURATION = "Configuration";
		public static final String WEB_XML = "web.xml";
		public static final String CONTEXT_PARAMS = "Context Params";
		public static final String JAVAX_FACES_CONFIG_FILES = "javax.faces.CONFIG_FILES";
		
	}
	
	public static class NewJSPFileDialog {

		public static final String NAME = "New File JSP";
		public static final String TEMPLATE = "Template";
		public static final String TEMPLATE_JSF_BASE_PAGE = "JSFBasePage";
		
	}
	
	public static class PropertiesDialog {

		public static final String PARAM_VALUE = "Param-Value";
		
	}

	public static final class NewXHTMLFileDialog {

		public static final String NAME = "New XHTML";
		public static final String TEMPLATE = "Template";
		public static final String TEMPLATE_FACELET_FORM_XHTML = "FaceletForm.xhtml";
		
	}
}
