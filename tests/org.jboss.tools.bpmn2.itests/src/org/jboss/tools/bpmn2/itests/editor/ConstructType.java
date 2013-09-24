package org.jboss.tools.bpmn2.itests.editor;

/**
 * TBD: tool names and sections will be required.
 * 
 * @author mbaluch
 */
public enum ConstructType {

	PROCESS(null, null),
	
	AD_HOC_SUB_PROCESS("Activities", "Ad-Hoc Sub-Process"),
	SUB_PROCESS("Activities", "Sub-Process"),
	CALL_ACTIVITY("Activities", "Call Activity"),
	TASK("Activities", "Task"),
	MANUAL_TASK("Activities", "Manual Task"),
	USER_TASK("Activities", "User Task"),
	SCRIPT_TASK("Activities", "Script Task"),
	BUSINESS_RULE_TASK("Activities", "Business Rule Task"),
	SERVICE_TASK("Activities", "Service Task"),
	SEND_TASK("Activities", "Send Task"),
	RECEIVE_TASK("Activities", "Receive Task"),
//	TRANSACTION("TBD", "Transaction"),
	
	BOUNDARY_EVENT("Boundary Events", "Boundary Event"),
	CONDITIONAL_BOUNDARY_EVENT("Boundary Events", "Conditional"),
	ERROR_BOUNDARY_EVENT("Boundary Events", "Error"),
	ESCALATION_BOUNDARY_EVENT("Boundary Events", "Escalation"),
	MESSAGE_BOUNDARY_EVENT("Boundary Events", "Message"),
	SIGNAL_BOUNDARY_EVENT("Boundary Events", "Signal"),
	TIMER_BOUNDARY_EVENT("Boundary Events", "Timer"),
	
	COMPENSATION_START_EVENT("Start Events", "Compensation"),
	CONDITIONAL_START_EVENT("Start Events", "Conditional"),
	ERROR_START_EVENT("Start Events", "Error"),
	ESCALATION_START_EVENT("Start Events", "Escalation"),
	START_EVENT("Start Events", "Start Event"),
	MESSAGE_START_EVENT("Start Events", "Message"),
	SIGNAL_START_EVENT("Start Events", "Signal"),
	TIMER_START_EVENT("Start Events", "Timer"),
	
	CANCEL_END_EVENT("End Events", "Cancel"),
	COMPENSATION_END_EVENT("End Events", "Compensation"),
	END_EVENT("End Events", "End Event"),
	ERROR_END_EVENT("End Events", "Error"),
	ESCALATION_END_EVENT("End Events", "Escalation"),
	MESSAGE_END_EVENT("End Events", "Message"),
	SIGNAL_END_EVENT("End Events", "Signal"),
	TERMINATE_END_EVENT("End Events", "Terminate"),
	
	CONDITIONAL_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Conditional"),
	MESSAGE_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Message"),
	SIGNAL_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Signal"),
	TIMER_INTERMEDIATE_CATCH_EVENT("Intermediate Catch Events", "Timer"),
	
	COMPENSATION_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Compensation"),
	ESCALATION_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Escalation"),
	INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Throw Event"),
	MESSAGE_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Message"),
	SIGNAL_INTERMEDIATE_THROW_EVENT("Intermediate Throw Events", "Signal"),
	
//	COMPLEX_GATEWAY("Gateways", "Complex Gateway"),
	EXCLUSIVE_GATEWAY("Gateways", "Exclusive Gateway"),
	EVENT_BASED_GATEWAY("Gateways", "Event-Based Gateway"),
	INCLUSIVE_GATEWAY("Gateways", "Inclusive Gateway"),
	PARALLEL_GATEWAY("Gateways", "Parallel Gateway"),
	
	LANE("Swimlanes", "Lane"),

//	MESSAGE("Data Objects", "Message"),
//	DATA_INPUT("Data Objects", "Data Input"),
//	DATA_OUTPUT("Data Objects", "Data Output"),
	DATA_OBJECT("Data Objects", "Data Object"),
	
	SWITCHYARD_SERVICE_TASK("SwitchYard", "SwitchYard Service Task");

	private String sectionName;
	
	private String paletteToolName;
	
	/**
	 * 
	 * @param sectionName
	 * @param paletteToolName
	 */
	private ConstructType(String sectionName, String paletteToolName) {
		this.sectionName = sectionName;
		this.paletteToolName = paletteToolName;
	}
	
	/**
	 * Returns the name of the type. E.g. PARALLEL_GATEWAY -> Parallel Gateway. 
	 * 
	 * @return
	 */
	public String toToolName() {
		return paletteToolName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] toToolPath() {
		return new String[] {sectionName, paletteToolName};
	}

	/**
	 * 
	 * @return
	 */
	public String toId() {
		String name = null;
		if (sectionName.equals("End Events")) {
			name = "End Event";
		} else if (sectionName.equals("Start Events")) {
			name = "Start Event";
		} else if (sectionName.equals("Boundary Events")) {
			name = "Boundary Event";
		} else if (sectionName.equals("Intermediate Catch Events")) {
			name = "Intermediate Catch Event";
		} else if (sectionName.equals("Intermediate Throw Events")) {
			name = "Intermediate Throw Event";
		} else if (sectionName.equals("SwitchYard")){
            name = "Task";	
		} else {
			name = toToolName();
		}
		return name.replace(" ", "").replace("-", "");
	}
	
}
