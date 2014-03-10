package org.jboss.tools.openshift.ui.bot.util;

public class OpenShiftLabel {

	public static class AppType {
		// Pure OpenShift Enterprise applications labels
		public static final String JBOSS = "JBoss Application Server 7.1" + " (" + "jbossas-7.1" + ")";
		public static final String JBOSS_EAP = "JBoss Enterprise Application Platform 6.0" + " (" + "jbosseap-6.0" + ")";
		public static final String JENKINS = "Jenkins Server 1.4" + " (" + "jenkins-1.4" + ")";
			
		// OpenShift Online applications labels
		public static final String JBOSS_EAP_ONLINE = "JBoss Enterprise Application Platform 6" + " (" + "jbosseap-6" + ")";
		
		// Shared applications labels (both OpenShift Online and OpenShift Enterprise)
		public static final String DIY = "Do-It-Yourself 0.1" + " (" + "diy-0.1" + ")";
		public static final String JBOSS_EWS = "Tomcat 7 (JBoss EWS 2.0)" + " (" + "jbossews-2.0" + ")";
		public static final String PERL = "Perl 5.10" + " (" + "perl-5.10" + ")";
		public static final String PHP = "PHP 5.3" + " (" + "php-5.3" + ")";
		public static final String PYTHON = "Python 2.6" + " (" + "python-2.6" + ")";
		public static final String RUBY_1_9 = "Ruby 1.9" + " (" + "ruby-1.9" + ")";
	}

	public static class Cartridge {
		// OpenShift Online and OpenShift Enterprise
		public static final String JENKINS = "Jenkins Client 1.4 (jenkins-client-1.4)";
		public static final String CRON = "Cron 1.4 (cron-1.4)";
		public static final String MYSQL = "MySQL Database 5.1 (mysql-5.1)";
		public static final String POSTGRESQL = "PostgreSQL Database 8.4 (postgresql-8.4)";
		public static final String MONGODB = "MongoDB NoSQL Database 2.2 (mongodb-2.2)";
		public static final String ROCKMONGO = "RockMongo 1.1 (rockmongo-1.1)";
		public static final String PHPMYADMIN = "phpMyAdmin 3.4 (phpmyadmin-3.4)";
		public static final String SWITCHYARD = "SwitchYard 0.6 (switchyard-0.6)";
		
		// Those are for OpenShift Online
		public static final String CRON_ONLINE = "Cron 1.4 (cron-1.4)";
		public static final String MYSQL_ONLINE = "MySQL 5.1 (mysql-5.1)";
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
		public static final String EXPLORER_ENV_VAR = "All Environment Variables";
		public static final String EXPLORER_PORTS = "Port forwarding...";
		public static final String BROWSER = "Web Browser";
		public static final String EXPLORER_ADAPTER = "Create a Server Adapter...";
		public static final String EXPLORER_RESTART_APP = "Restart Application";
		public static final String DELETE_APP = "Delete Application";
	}
	
	public static class Shell {
		public static final String DELETE_APP = "Application deletion";
		public static final String TAIL_FILES = "Tail Files";
		public static final String PORTS = "";
		public static final String ADAPTER = "New Server";
		public static final String EDIT_CARTRIDGES = "Edit Embedded Cartridges";
	}
	
	public class Button {
		public static final String NEXT = "Next >";
		public static final String BACK = "< Back";
		public static final String FINISH = "Finish";
		public static final String OK = "OK";
		public static final String YES = "Yes";
		public static final String ADVANCED = " Advanced >> ";
	}
	
}
