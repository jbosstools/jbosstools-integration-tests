package org.jboss.ide.eclipse.as.reddeer.server.requirement;

import org.jboss.ide.eclipse.as.reddeer.server.family.FamilyAS;
import org.jboss.ide.eclipse.as.reddeer.server.family.FamilyEAP;
import org.jboss.ide.eclipse.as.reddeer.server.family.FamilyWildFly;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType.ServerReqFamily;
import org.jboss.reddeer.requirements.server.IServerFamily;

/**
 * Provides methods for matching required server to configured server
 * 
 * @author Radoslav Rabara
 * 
 */
class ServerMatcher {

	static boolean matchServerFamily(ServerReqFamily serverFamily, IServerFamily configServerType) {
		return serverFamily == ServerReqFamily.ANY
				|| (serverFamily == ServerReqFamily.AS && configServerType instanceof FamilyAS)
				|| (serverFamily == ServerReqFamily.EAP && configServerType instanceof FamilyEAP)
				|| (serverFamily == ServerReqFamily.WILDFLY && configServerType instanceof FamilyWildFly);
	}

	static boolean matchServerVersion(String requiredVersion, ServerReqVersion versionMatcher,
			String configVersion) {
		if (requiredVersion == null || requiredVersion.length() == 0) {
			return true;
		}
		
		int versionNum = parseServerVersionNumber(requiredVersion);
		int configVersionNum = parseServerVersionNumber(configVersion);

		switch (versionMatcher) {
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

		throw new IllegalArgumentException("Version matcher " + versionMatcher
				+ " was not recognized!");
	}

	/**
	 * Parse version number.
	 * Distinct only major and minor versions. Minor version can be just single digit.
	 * 
	 * @param version version number in format ## (just major version) or ##.# (major and minor version)
	 * @return version number in format MAJOR_VERSION*10+MINOR_VERSION
	 * 
	 */
	private static int parseServerVersionNumber(String version) {
		version = version.replaceAll("[^0-9.]", "");
		int idx = version.indexOf(".");
		boolean onlyMajorVersion = idx == 0;
		boolean onlyMajorVersionEndingWithDot = idx+1 == version.length(); //e.g. 6.x -> 6.
		if(onlyMajorVersion || onlyMajorVersionEndingWithDot) { //only major version
			return Integer.parseInt(version.replaceAll("[.]", ""))*10;
		}
		
		//distinct major and minor version
		String majorVersion = version.substring(0, idx);
		String minorVersion = version.substring(idx+1, version.length());
		if (minorVersion.length() > 1) {
			throw new IllegalArgumentException("Version '" + version
						+ "' must have format ## or ##.#");
		}
		return Integer.parseInt(majorVersion) * 10
				+ Integer.parseInt(minorVersion);
	}
}
