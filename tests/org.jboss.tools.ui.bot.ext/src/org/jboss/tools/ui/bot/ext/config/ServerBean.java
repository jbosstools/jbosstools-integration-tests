package org.jboss.tools.ui.bot.ext.config;

/**
 * 
 * @author lzoubek
 *
 */
public class ServerBean {

	public String version;
	public String runtimeHome;
	public String withJavaVersion;
	public ServerType type;
	
	public enum ServerType {
		EAP, 
		JBOSS_AS
	}
	/**
	 * creates bean instance from property string
	 * @param propValue property value
	 * @return
	 * @throws Exception
	 */
	public static ServerBean fromString(String propValue) throws Exception {
		try {
		String[] serverParams = propValue.split(",");
		ServerBean bean = new ServerBean();
		bean.withJavaVersion = serverParams[2];		
		bean.runtimeHome=serverParams[3];
		bean.version=serverParams[1];
		bean.type = Enum.valueOf(ServerType.class, serverParams[0]);
		return bean;
		}
		catch (Exception ex) {
			throw new Exception("Cannot parse SERVER property line",ex);
		}
	}
	
}
