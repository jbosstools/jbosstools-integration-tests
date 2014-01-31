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
		if (serverVersion != null && serverVersion.length() > 0) {
			int versionNum;
			int configVersionNum;
			try {
				versionNum = parseServerVersionNumber(serverVersion);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Wrong format of server version in server requirement.");
			}
			try {
				configVersionNum = parseServerVersionNumber(configVersion);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Wrong format of server version in configuration file.");
			}

			switch (operator) {
			case EQUALS:
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
		return true;
	}

	private static int parseServerVersionNumber(String version)
			throws NumberFormatException {
		version = version.replaceAll("\\.", "");
		int addZeros = 4 - version.length();
		if (addZeros > 0) {
			while (addZeros > 0) {
				version += "0";
				addZeros--;
			}
		}
		return Integer.parseInt(version);
	}
}
