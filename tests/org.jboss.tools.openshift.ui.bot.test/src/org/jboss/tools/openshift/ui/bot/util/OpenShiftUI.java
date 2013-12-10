package org.jboss.tools.openshift.ui.bot.util;

import java.util.List;
import java.util.Vector;

import org.jboss.tools.ui.bot.ext.gen.INewObject;
import org.jboss.tools.ui.bot.ext.gen.IView;

/**
 * 
 * Wrapper class for OpenShift UI tooling.
 * 
 * @author sbunciak
 * 
 */
public class OpenShiftUI {

	/**
	 * 
	 * Class representing OpenShift Console View
	 * 
	 */
	public static class Explorer {

		public static final IView iView = new IView() {
			@Override
			public String getName() {
				return "OpenShift Explorer";
			}

			@Override
			public List<String> getGroupPath() {
				List<String> l = new Vector<String>();
				l.add("JBoss Tools");
				return l;
			}
		};
	}

	/**
	 * 
	 * Class representing OpenShift Console View
	 * 
	 */
	public static class WebBrowser {

		public static final IView iView = new IView() {
			@Override
			public String getName() {
				return "Internal Web Browser";
			}

			@Override
			public List<String> getGroupPath() {
				List<String> l = new Vector<String>();
				l.add("General");
				return l;
			}
		};
	}

	/**
	 * 
	 * Class representing "navigation" to new OpenShift Application
	 * 
	 */
	public static class NewApplication {
		public static final INewObject iNewObject = new INewObject() {
			@Override
			public String getName() {
				return "OpenShift Application";
			}

			@Override
			public List<String> getGroupPath() {
				List<String> l = new Vector<String>();
				l.add("OpenShift");
				return l;
			}
		};
	}

	/**
	 * List of available application type labels
	 * 
	 * @author sbunciak, mlabuda
	 * 
	 */
	public static class AppType {
		public static final String JBOSS = "JBoss Application Server 7.1" + " (" + AppTypeOld.JBOSS + ")";
		public static final String JBOSS_EAP = "JBoss Enterprise Application Platform 6.0" + " (" + AppTypeOld.JBOSS_EAP + ")";
		public static final String JBOSS_EWS = "Tomcat 7 (JBoss EWS 2.0)" + " (" + AppTypeOld.JBOSS_EWS + ")";
		public static final String JENKINS = "Jenkins Server 1.4" + " (" + AppTypeOld.JENKINS + ")";
		public static final String PERL = "Perl 5.10" + " (" + AppTypeOld.PERL + ")";
		public static final String PHP = "PHP 5.3" + " (" + AppTypeOld.PHP + ")";
		public static final String PYTHON = "Python 2.6" + " (" + AppTypeOld.PYTHON + ")";
		public static final String DIY = "Do-It-Yourself 0.1" + " (" + AppTypeOld.DIY + ")";
		public static final String RUBY_1_9 = "Ruby 1.9" + " (" + AppTypeOld.RUBY_1_9 + ")";
	}

	// TODO:
	// TEMPORARY
	public static class AppTypeOld {

		public static final String JBOSS = "jbossas-7.1";
		public static final String JBOSS_EAP = "jbosseap-6.0";
		public static final String JBOSS_EWS = "jbossews-2.0";
		public static final String JENKINS = "jenkins-1.4";
		public static final String PERL = "perl-5.10";
		public static final String PHP = "php-5.3";
		public static final String PYTHON = "python-2.6";
		public static final String DIY = "diy-0.1";
		public static final String RUBY_1_9 = "ruby-1.9";
	}

	/**
	 * List of available cartridge labels
	 * 
	 * @author sbunciak
	 * 
	 */
	public static class Cartridge {

		public static final String JENKINS = "Jenkins Client 1.4 (jenkins-client-1.4)";
		public static final String CRON = "Cron 1.4 (cron-1.4)";
		public static final String MYSQL = "MySQL Database 5.1 (mysql-5.1)";
		public static final String POSTGRESQL_8_4 = "PostgreSQL Database 8.4 (postgresql-8.4)";
		public static final String POSTGRESQL = "PostgreSQL 9.2 (postgresql-9.2)";
		public static final String SWITCHYARD = "SwitchYard 0.6 (switchyard-0.6)";
		public static final String MONGODB = "MongoDB 2.2 (mongodb-2.2)";
		public static final String PHPMYADMIN = "phpMyAdmin 3.4 (phpmyadmin-3.4)";
		public static final String ROCKMONGO = "RockMongo 1.1 (rockmongo-1.1)";
	}

	public static class Labels {

		public static final String CONNECT_TO_OPENSHIFT = "Connect to OpenShift";
		public static final String EXPLORER_NEW_APP = "New OpenShift Application...";
		public static final String EXPLORER_CREATE_EDIT_DOMAIN = "Create or Edit Domain...";
		public static final String EXPLORER_DELETE_DOMAIN = "Delete Domain";
		public static final String EXPLORER_CREATE_SERVER = "Create a Server Adapter";
		public static final String EXPLORER_DELETE_APP = "Delete Application";
		public static final String EXPLORER_MANAGE_SSH = "Manage SSH &Keys...";
		public static final String EDIT_CARTRIDGES = "Edit Embedded Cartridges...";
		public static final String REFRESH = "Refresh";
		public static final String EXPLORER_IMPORT_APP = "Import Application...";
		public static final String EXPLORER_TAIL_FILES = "Tail files...";
		public static final String EXPLORER_ENV_VAR = "All Environment Variables";
		public static final String EXPLORER_PORTS = "Port forwarding...";
		public static final String BROWSER = "Web Browser";
		public static final String EXPLORER_ADAPTER = "Create a Server Adapter...";
		public static final String EXPLORER_RESTART_APP = "Restart Application";
		
		public static final String MANAGE_DOMAINS = "Manage Domains...";
		public static final String MANAGE_SSH_KEYS = "Manage SSH Keys...";

	}

	public static class Shell {

		public static final String NO_TITLE = "";
		public static final String NEW_APP = "New OpenShift Application";
		public static final String CREATE_DOMAIN = "Create Domain";
		public static final String EDIT_DOMAIN = "Edit Domain";
		public static final String CREDENTIALS = "";
		public static final String SSH_WIZARD = "Manage SSH Keys";
		public static final String NEW_SSH = "New SSH Key";
		public static final String DELETE_APP = "Application deletion";
		public static final String IMPORT_APP = "Import OpenShift Application";
		public static final String TAIL_FILES = "Tail Files";
		public static final String PORTS = "";
		public static final String ADAPTER = "New Server";
		public static final String EDIT_CARTRIDGES = "Edit Embedded Cartridges";

	}
}