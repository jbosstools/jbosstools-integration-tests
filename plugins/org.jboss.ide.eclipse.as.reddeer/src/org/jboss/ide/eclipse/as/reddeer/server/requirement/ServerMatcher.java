package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirementConfig.FamilyAS;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirementConfig.FamilyEAP;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirementConfig.ServerFamily;

/**
 * Provides methods for matching required server to configured server
 * 
 * @author Radoslav Rabara
 * 
 */
class ServerMatcher {

	static boolean matchServerType(ServerReqType serverType, ServerFamily configServerType) {
		return serverType == ServerReqType.ANY
				|| (serverType == ServerReqType.AS && configServerType instanceof FamilyAS)
				|| (serverType == ServerReqType.EAP && configServerType instanceof FamilyEAP)
				|| (serverType == ServerReqType.WILDFLY && configServerType instanceof FamilyAS);
	}

	static boolean matchServerVersion(String serverVersion, ServerReqOperator operator,
			String configVersion) {
		if (serverVersion == null || serverVersion.length() == 0)
			return true;
		
		int versionNum = parseRequiredServerVersion(serverVersion);
		int configVersionNum = parseConfigServerVersion(configVersion);

		switch (operator) {
		case EQUAL:
			return versionNum == configVersionNum;
		case NOT_EQUAL:
			return versionNum != configVersionNum;
		case GREATER:
			return configVersionNum > versionNum;
		case GREATER_OR_EQUAL:
			return configVersionNum >= versionNum;
		case LESS:
			return configVersionNum < versionNum;
		case LESS_OR_EQUAL:
			return configVersionNum <= versionNum;
		}

		throw new IllegalArgumentException("Operator " + operator
				+ " was not recognized!");
	}

	private static int parseServerVersionNumber(String version)
			throws NumberFormatException {
		version = version.replaceAll("\\.", "");
		int addZeros = 4 - version.length();
		while (addZeros > 0) {
			version += "0";
			addZeros--;
		}
		return Integer.parseInt(version);
	}

	private static final String versionFormatLogMessage = "Use format ##.## or ##.#.# (e.g. \"8.1\", \"5.2.2\")";

	private static int parseRequiredServerVersion(String version) {
		try {
			return parseServerVersionNumber(version);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Wrong format of server version in server requirement. "+versionFormatLogMessage);
		}
	}

	private static int parseConfigServerVersion(String configVersion) {
		try {
			return parseServerVersionNumber(configVersion);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Wrong format of server version in configuration file. "+versionFormatLogMessage);
		}
	}
}
