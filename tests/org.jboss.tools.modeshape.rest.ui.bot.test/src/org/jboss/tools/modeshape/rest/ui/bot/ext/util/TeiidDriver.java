package org.jboss.tools.modeshape.rest.ui.bot.ext.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

public class TeiidDriver implements Driver {
	private Driver driver;

	public TeiidDriver(Driver d) {
		this.driver = d;
	}

	public TeiidDriver(String path) {
		this(getDriver(path));
	}

	private static Driver getDriver(String path) {
		String classname = "org.teiid.jdbc.TeiidDriver";
		try {
			URL url = new URL("jar:file:" + path + "!/");
			URLClassLoader ucl = new URLClassLoader(new URL[] { url });
			return (Driver) Class.forName(classname, true, ucl).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Couldn't set Teiid Driver");
	}

	public boolean acceptsURL(String u) throws SQLException {
		return this.driver.acceptsURL(u);
	}

	public Connection connect(String u, Properties p) throws SQLException {
		return this.driver.connect(u, p);
	}

	public int getMajorVersion() {
		return this.driver.getMajorVersion();
	}

	public int getMinorVersion() {
		return this.driver.getMinorVersion();
	}

	public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
		return this.driver.getPropertyInfo(u, p);
	}

	public boolean jdbcCompliant() {
		return this.driver.jdbcCompliant();
	}
}