package org.jboss.tools.openshift.ui.bot.util;

public class OpenShiftLabel {

	public static class AppType {
		public static final String DIY = "Do-It-Yourself 0.1" + " " + "diy-0.1";
		public static final String JBOSS_AS = "JBoss Application Server 7.1" + " " + "jbossas-7.1";
		public static final String JBOSS_EAP = "JBoss Enterprise Application Platform 6" + " " + "jbosseap-6";
		public static final String JBOSS_EWS = "Tomcat 7 (JBoss EWS 2.0)" + " " + "jbossews-2.0";
		public static final String JENKINS = "Jenkins Server" + " " + "jenkins-1";
		public static final String PERL = "Perl 5.10" + " " + "perl-5.10";
		public static final String PHP = "PHP 5.3" + " " + "php-5.3";
		public static final String PYTHON = "Python 2.6" + " " + "python-2.6";
		public static final String RUBY_1_9 = "Ruby 1.9" + " " + "ruby-1.9";
		
		// Bcs. in explorer tree are with parenthesis
		public static final String DIY_TREE = "Do-It-Yourself 0.1" + " (" + "diy-0.1" + ")";
		public static final String JBOSS_AS_TREE = "JBoss Application Server 7.1" + " (" + "jbossas-7.1" + ")";
		public static final String JBOSS_EAP_TREE = "JBoss Enterprise Application Platform 6" + " (" + "jbosseap-6" + ")";
		public static final String JBOSS_EWS_TREE = "Tomcat 7 (JBoss EWS 2.0)" + " (" + "jbossews-2.0" + ")";
		public static final String JENKINS_TREE = "Jenkins Server" + " (" + "jenkins-1" + ")";
		public static final String PERL_TREE = "Perl 5.10" + " (" + "perl-5.10" + ")";
		public static final String PHP_TREE = "PHP 5.3" + " (" + "php-5.3" + ")";
		public static final String PYTHON_TREE = "Python 2.6" + " (" + "python-2.6" + ")";
		public static final String RUBY_1_9_TREE = "Ruby 1.9" + " (" + "ruby-1.9" + ")";
		}
	

	public static class Cartridge {
		public static final String JENKINS = "Jenkins Client 1.4 (jenkins-client-1.4)";
		public static final String CRON = "Cron 1.4 (cron-1.4)";
		public static final String MYSQL = "MySQL 5.1 (mysql-5.1)";
		public static final String POSTGRESQL = "PostgreSQL 8.4 (postgresql-8.4)";
		public static final String MONGODB = "MongoDB 2.2 (mongodb-2.2)";
		public static final String ROCKMONGO = "RockMongo 1.1 (rockmongo-1.1)";
		public static final String PHPMYADMIN = "phpMyAdmin 3.4 (phpmyadmin-3.4)";
		public static final String SWITCHYARD = "SwitchYard 0.6 (switchyard-0.6)";
	}
	
	public static class Labels {
		// Connection
		public static final String CONNECT_TO_OPENSHIFT = "Connect to OpenShift";
		
		// Connection context menu
		public static final String MANAGE_SSH_KEYS = "Manage SSH Keys...";
		public static final String MANAGE_DOMAINS = "Manage Domains...";
		public static final String REFRESH = "Refresh";

		// Application context menu
		public static final String EDIT_CARTRIDGES = "Edit Embedded Cartridges...";
		public static final String EXPLORER_IMPORT_APP = "Import Application...";
		public static final String EXPLORER_TAIL_FILES = "Tail files...";
		public static final String EXPLORER_ENV_VAR = "Environment Variables";
		public static final String EXPLORER_PORTS = "Port forwarding...";
		public static final String BROWSER = "Web Browser";
		public static final String EXPLORER_ADAPTER = "Create a Server Adapter...";
		public static final String EXPLORER_RESTART_APP = "Restart Application";
		public static final String DELETE_APP = "Delete Application";
	}
	
	public static class Shell {
		public static final String DELETE_APP = "Application deletion";
		public static final String TAIL_FILES = "Tail Files";
		public static final String PORTS = "Application port forwarding";
		public static final String ADAPTER = "New Server";
		public static final String ADD_CARTRIDGES = "Add Embedded Cartridges";
		public static final String EDIT_CARTRIDGES = "Edit Embedded Cartridges";
		public static final String NEW_APP_WIZARD = "New OpenShift Application";
		public static final String ENV_VARS = "Create Environment Variable(s)";
		public static final String EDIT_ENV_VAR = "Edit Environment variable";
	}
	
	public class Button {
		public static final String ADD = "Add...";
		public static final String ENV_VAR = "Environment Variables...";
		public static final String NEXT = "Next >";
		public static final String BACK = "< Back";
		public static final String FINISH = "Finish";
		public static final String CANCEL = "Cancel";
		public static final String OK = "OK";
		public static final String YES = "Yes";
		public static final String NO = "No";
		public static final String ADVANCED = " Advanced >> ";
		public static final String BROWSE = "Browse...";
	}
	
}
