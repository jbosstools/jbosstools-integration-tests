package org.jboss.tools.openshift.reddeer.utils;

/**
 * Various labels for OpenShift tools plugin.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class OpenShiftLabel {

	/**
	 * Cartridges for application creation. Basic and downloadable.
	 */
	public static class Cartridge {
		// Basic cartridges
		public static final String DIY = "Do-It-Yourself 0.1";
		public static final String JENKINS = "Jenkins Server";
		public static final String JBOSS_AS = "JBoss Application Server 7";
		public static final String JBOSS_EAP = "JBoss Enterprise Application Platform 6";
		public static final String JBOSS_EWS = "Tomcat 7 (JBoss EWS 2.0)";
		public static final String PERL = "Perl 5.10";
		public static final String PHP = "PHP 5.3";
		public static final String PYTHON = "Python 2.6";
		public static final String RUBY_1_9 = "Ruby 1.9";
		
		// Quickstarts
		public static final String DJANGO = "Django (Quickstart)";
		
		// Downloadable cartridge
		public static final String DOWNLOADABLE_CARTRIDGE = "Code Anything";
	}

	/**
	 * Embeddable cartridges.
	 */
	public static class EmbeddableCartridge {
		public static final String CRON = "Cron 1.4 (cron-1.4)";
		public static final String JENKINS = "Jenkins Client (jenkins-client-1)";
		public static final String MY_SQL = "MySQL 5.5 (mysql-5.5)";
		public static final String MONGO_DB = "MongoDB 2.4 (mongodb-2.4)";
		public static final String PHP_MYADMIN = "phpMyAdmin 4.0 (phpmyadmin-4)";
		public static final String POSTGRE_SQL = "PostgreSQL 9.2 (postgresql-9.2)";
		public static final String ROCK_MONGO = "RockMongo 1.1 (rockmongo-1.1)";
		
		public static final String DOWNLOADABLE_CARTRIDGE = "Code Anything (Downloadable Cartridge)";
	}
	
	/**
	 * OpenShift explorer context menu labels.
	 */
	public static class ContextMenu {
		// General
		public static final String PROPERTIES = "Properties";
		public static final String REFRESH = "Refresh";
		public static final String[] SHOW_IN_WEB_CONSOLE = {"Show In", "Web Console"};
		public static final String[] SHOW_IN_WEB_BROWSER = {"Show In", "Web Browser"};
		
		// Connection related
		public static final String EDIT_CONNECTION = "Edit Connection...";
		public static final String MANAGE_SSH_KEYS = "Manage SSH Keys...";
		public static final String MANAGE_DOMAINS = "Manage Domains...";
		public static final String[] NEW_DOMAIN = {"New", "Domain..."};
		public static final String[] NEW_CONNECTION = {"New", "Connection..."};
		public static final String[] NEW_OS_PROJECT = {"New", "Project"};
		public static final String REMOVE_CONNECTION = "Remove Connection(s)...";
		
		// Domain related
		public static final String DELETE_DOMAIN = "Delete Domain...";
		public static final String EDIT_DOMAIN = "Edit Domain...";
		
		// Project related
		public static final String MANAGE_OS_PROJECTS = "Manage Projects...";
		public static final String DELETE_OS_PROJECT = "Delete Project";
		
		// Resource related
		public static final String CLONE_BUILD = "Clone Build...";
		public static final String DELETE_RESOURCE = "Delete Resource...";
		public static final String START_BUILD = "Start Build...";
		
		// Application related
		public static final String APPLICATION_DETAILS = "Details...";
		public static final String DELETE_APPLICATION = "Delete Application(s)...";
		public static final String EDIT_ENV_VARS = "Edit User Environment Variables...";
		public static final String EMBED_CARTRIDGE = "Edit Embedded Cartridges...";
		public static final String IMPORT_APPLICATION = "Import Application...";
		public static final String[] NEW_OS2_APPLICATION = {"New", "Application..."};
		public static final String[] NEW_OS3_APPLICATION = {"New", "Application"};
		public static final String[] NEW_SERVER_ADAPTER = {"New", "Server Adapter..."};		
		public static final String PORT_FORWARD = "Port Forwarding...";		
		public static final String RESTART_APPLICATION = "Restart Application";	
		public static final String[] RESTORE_SNAPSHOT = {"Snapshot", "Restore/Deploy..."};
		public static final String[] SAVE_SNAPSHOT = {"Snapshot", "Save..."}; 
		public static final String SHOW_ENV_VARS = "List All Environment Variables";
		public static final String[] SHOW_IN_BROWSER = {"Show In", "Web Browser"};
		public static final String TAIL_FILES = "Tail Files...";
		public static final String[] DEPLOY_PROJECT = {"Configure", "New/Import OpenShift Application"};
		
		// Server adapter related
		public static final String PUBLISH = "Publish";
		
		// Workspace project related
		public static final String[] CONFIGURE_MARKERS = {"OpenShift", "Configure Markers..."};
	}
		
	/**
	 * Shell title labels.
	 */
	public static class Shell {
		public static final String ACCEPT_HOST_KEY = "Question";
		public static final String ADAPTER = "New Server";
		public static final String ADD_CARTRIDGES = "Add Embedded Cartridges";
		public static final String ADD_CARTRIDGE_DIALOG = "Add Cartridges";
		public static final String APPLICATION_DETAILS = "Application Details";
		public static final String APPLICATION_SERVER_REMOVE = "Application and Server removal";
		public static final String COMMIT = "Commit Changes";
		public static final String DELETE_APP = "Application removal";
		public static final String DELETE_OS_PROJECT = "OpenShift project deletion";
		public static final String EDIT_CARTRIDGES = "Edit Embedded Cartridges";
		public static final String EDIT_ENV_VAR = "Edit Environment variable";
		public static final String EMBEDDED_CARTRIDGE = "Embedded Cartridges";
		public static final String ENV_VARS = "Create Environment Variable(s)";
		public static final String IMPORT_APPLICATION_WIZARD = "Import OpenShift Application ";
		public static final String IMPORT_APPLICATION = "Import OpenShift Application";
		public static final String MANAGE_ENV_VARS = "Manage Application Environment Variable(s) for application ";
		public static final String MARKERS = "Configure OpenShift Markers for project ";
		public static final String NEW_APP_WIZARD = "New OpenShift Application";
		public static final String PORTS_FORWARDING = "Application port forwarding";
		public static final String SAVE_SNAPSHOT = "Save Snapshot";
		public static final String SELECT_EXISTING_APPLICATION = "Select Existing Application";
		public static final String SELECT_EXISTING_PROJECT = "Select Existing Project";
		public static final String PREFERENCES = "Preferences";
		public static final String REMOVE_CONNECTION = "Remove connection";
		public static final String REMOVE_ENV_VAR = "Remove Environment Variable";
		public static final String REFRESH_ENV_VARS = "Refresh Environment Variables";
		public static final String RESTART_APPLICATION = "Restart Application";
		public static final String RESTORE_SNAPSHOT = "Restore/Deploy Snapshot";
		public static final String SECURE_STORAGE = "Secure Storage";
		public static final String SECURE_STORAGE_PASSWORD = "Secure Storage Password";
		public static final String TAIL_FILES = "Tail Files";
		
		// Domain related
		public static final String CREATE_DOMAIN = "Create Domain";
		public static final String DELETE_DOMAIN = "Domain deletion";
		public static final String EDIT_DOMAIN = "Edit domain";
		public static final String MANAGE_DOMAINS = "Domains";
		
		// SSH Key related
		public static final String ADD_SSH_KEY = "Add SSH Key";
		public static final String MANAGE_SSH_KEYS = "Manage SSH Keys";
		public static final String NEW_SSH_KEY = "New SSH Key";
		public static final String NO_SSH_KEY = "No SSH Keys";
		public static final String REMOVE_SSH_KEY = "Remove SSH Key";
		
		// Project related
		public static final String MANAGE_OS_PROJECTS = "OpenShift Projects";
		public static final String CREATE_OS_PROJECT = "Create OpenShift Project";
		
		// Server adapter related
		public static final String PUBLISH_CHANGES = "Publish Changes";
		
		// Application related
		public static final String APPLICATION_SUMMARY = "Create Application Summary";
		public static final String EDIT_TEMPLATE_PARAMETER = "Edit Template Parameter";
		public static final String REMOVE_LABEL = "Remove Label";
		public static final String RESOURCE_LABEL = "Resource Label";
		public static final String TEMPLATE_DETAILS = "Template Details";
		public static final String WEBHOOK_TRIGGERS = "Webhooks triggers";
		
		// Resources related
		public static final String DELETE_RESOURCE = "Delete Resource";
		
		// Others
		public static final String CHEATSHEET = "Found cheatsheet";
	}
	
	/**
	 * Button labels.
	 */
	public static class Button {
		public static final String APPLY = "Apply";
		public static final String ADD = "Add...";
		public static final String ADD_SSH_KEY = "Add Existing...";
		public static final String ADVANCED = " Advanced >> ";
		public static final String BROWSE = "Browse...";
		public static final String COMMIT_PUBLISH = "Commit and Publish";
		public static final String CREATE_DOMAIN = "New...";
		public static final String CREATE_SSH_KEY = "New...";
		public static final String DEFINED_RESOURCES = "Defined Resources...";
		public static final String DESELECT_ALL = "Deselect all";
		public static final String EDIT = "Edit...";
		public static final String EDIT_DOMAIN = "Edit...";
		public static final String ENV_VAR = "Environment Variables... ";
		public static final String NEW = "New...";
		public static final String REFRESH = "Refresh...";
		public static final String REMOVE = "Remove...";
		public static final String REMOVE_BASIC = "Remove...";
		public static final String RESET = "Reset";
		public static final String SELECT_ALL = "Select all";
		public static final String START_ALL = "Start All";
		public static final String STOP_ALL = "Stop All";
		public static final String WORKSPACE = "Workspace...";
	}
	
	public static class TextLabels {	
		// Connection related
		public static final String NEW_CONNECTION = "No connections are available. "
				+ "Create a new connection with the New Connection Wizard...";
		public static final String CONNECTION = "Connection:";
		public static final String PASSWORD = "Password:";
		public static final String SERVER = "Server:";
		public static final String USERNAME = "Username:";
		public static final String SERVER_TYPE = "Server type:";
		public static final String STORE_PASSWORD = "Save Password (could trigger secure storage login)";
		public static final String STORE_TOKEN= "Save Token (could trigger secure storage login)";
		public static final String CHECK_SERVER_TYPE = "Check Server Type";
		public static final String PROTOCOL = "Protocol:";
		public static final String TOKEN = "Token";
		public static final String RETRIEVAL_LINK = "retrieve";
		
		// Domain related
		public static final String DOMAIN_NAME = "Domain Name:";
	
		// Project related
		public static final String PROJECT_NAME = "Project Name:";
		public static final String PROJECT_DISPLAYED_NAME = "Display Name:";
		public static final String PROJECT_DESCRIPTION = "Description:";
		public static final String PROJECT = "Project: ";
		
		
		// SSH Key related
		public static final String NAME = "Name:";
		public static final String PRIVATE_NAME = "Private Key File Name:";
		public static final String PUBLIC_NAME = "Public Key File Name:";
		public static final String PUB_KEY = "Public Key:";
		
		// Wizard
		public static final String CARTRIDGE_URL = "Cartridge URL:";
		public static final String SOURCE_CODE = "Source code:";
		
		// Embeddable Cartridge
		public static final String EMBEDDED_CARTRIDGE_URL = "Cartridge URL:";
		
		// Application related
		public static final String LOCAL_TEMPLATE = "Local template";
		public static final String DESTINATION = "Destination:";
		public static final String SERVER_TEMPLATE = "Server templates";
		public static final String TAIL_OPTIONS = "Tail options:";	
		
		// Webhook
		public static final String GENERIC_WEBHOOK = "Generic webhook:";
		public static final String GITHUB_WEBHOOK = "GitHub webhook:";
	}
	
	/**
	 * Magic pond.
	 */
	public static class Others {
		public static final String CONNECT_TOOL_ITEM = "Connection...";
		public static final String EAP_TEMPLATE = "eap6-basic-sti";
		public static final String TOMCAT_TEMPLATE = "jws-tomcat7-basic-sti";
		public static final String JBOSS_CENTRAL = "JBoss Central";
		public static final String[] NEW_APP_MENU = {"File", "New", "OpenShift Application"};
		public static final String OPENSHIFT_APP = "OpenShift Application";
		public static final String OPENSHIFT_CENTRAL_SCRIPT = "$(\"#wizards\" ).find('a').filter(\""
				+ ":contains('OpenShift Application')\").click()";
		
	}
}
