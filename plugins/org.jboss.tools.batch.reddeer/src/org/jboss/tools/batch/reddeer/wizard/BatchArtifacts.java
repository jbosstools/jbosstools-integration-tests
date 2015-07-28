package org.jboss.tools.batch.reddeer.wizard;

/**
 * The list of supported batch artifacts. 
 * 
 * @author Lucia Jelinkova
 *
 */
public enum BatchArtifacts {

	BATCHLET("Batchlet"),	

	DECIDER("Decider"),

	ITEM_READER("Item Reader"),
	
	ITEM_WRITER("Item Writer"),
	
	ITEM_PROCESSOR("Item Processor"),
	
	CHECKPOINT_ALGORITHM("Checkpoint Algorithm"),
	
	PARTITION_MAPPER("Partition Mapper"),
	
	PARTITION_REDUCER("Partition Reducer"), 
	
	PARTITION_COLLECTOR("Partition Collector"),
	
	PARTITION_ANALYZER("Partition Analyzer"),
	
	JOB_LISTENER("Job Listener"),
	
	STEP_LISTENER("Step Listener"),
	
	CHUNK_LISTENER("Chunk Listener"),
	
	ITEM_READ_LISTENER("Item Read Listener"),
	
	ITEM_PROCESS_LISTENER("Item Process Listener"),
	
	ITEM_WRITE_LISTENER("Item Write Listener"),
	
	SKIP_READ_LISTENER("Skip Read Listener"),
	
	SKIP_PROCESS_LISTENER("Skip Process Listener"),
	
	SKIP_WRITE_LISTENER("Skip Write Listener"),
	
	RETRY_READ_LISTENER("Retry Read Listener"),
	
	RETRY_PROCESS_LISTENER("Retry Process Listener"),
	
	RETRY_WRITE_LISTENER("Retry Write Listener");
	
	private String name;
	
	private BatchArtifacts(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
