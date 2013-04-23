package org.jboss.tools.bpmn2.itests.editor;

/**
 * 
 * @author mbaluch
 */
public enum ConstructType {

	PROCESS,
	
	AD_HOC_SUB_PROCESS,
	BUSINESS_RULE_TASK,
	CALL_ACTIVITY,
	MANUAL_TASK,
	RECEIVE_TASK,
	SCRIPT_TASK,
	SEND_TASK,
	SERVICE_TASK,
	SUB_PROCESS_TASK,
	TASK,
	TRANSACTION,
	USER_TASK,
	
	START_EVENT,
	
	END_EVENT,
	INTERMEDIATE_CATCH_EVENT,
	INTERMEDIATE_THROW_EVENT,
	
	COMPLEX_GATEWAY,
	EVENT_BASED_GATEWAY,
	EXCLUSIVE_GATEWAY,
	INCLUSIVE_GATEWAY,
	PARALLEL_GATEWAY,
	
	LANE,
	
	MESSAGE,
	DATA_OBJECT,
	DATA_INPUT,
	DATA_OUTPUT;
	
	public String toName() {
		StringBuilder r = new StringBuilder();
		for (String w : name().split("_")) {
			r.append(w.charAt(0) + w.substring(1).toLowerCase());
			r.append(" ");
		}
		
		String menuName = r.toString().trim();
		if (menuName.startsWith("Ad Hoc")) {
			menuName = menuName.replace("Ad Hoc", "AdHoc");
		}
		
		return menuName;
	}
	
	public String toToolName() {
		return toName()
				.replace("Ad Hoc", "Ad-Hoc")
				.replace("Sub Process", "Sub-Process");
	}

	public String toId() {
		return toName()
				.replace(" ", "");
	}
	
	public String toId(int sequenceNumber) {
		return toId() + "_" + sequenceNumber;
	}
	
}
