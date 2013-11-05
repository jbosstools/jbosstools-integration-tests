package org.jboss.tools.hibernate.reddeer.console;

/**
 * Hibernate Configuration
 * @author jpeterka
 *
 */
public class HibernateConfiguration {

	private String project;
	private String name;
	private String databaseConnection;
	private String configurationFile;
	private String persistenceUnit;
	
	public class DatabaseConnection {
		public static final String hibernateConfiguredConection = "[Hibernate configured connection]";
		public static final String jpaProjectConfiguredConnetion = "[JPA Project Configured Connection]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatabaseConnection() {
		return databaseConnection;
	}

	public void setDatabaseConnection(String databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

	public String getConfigurationFile() {
		return configurationFile;
	}

	public void setConfigurationFile(String configurationFile) {
		this.configurationFile = configurationFile;
	}

	public String getPersistenceUnit() {
		return persistenceUnit;
	}

	public void setPersistenceUnit(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

}
