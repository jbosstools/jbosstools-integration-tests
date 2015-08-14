package org.jboss.tools.batch.reddeer.wizard;

/**
 * The list of supported batch artifacts. 
 * 
 * @author Lucia Jelinkova
 *
 */
public enum BatchArtifacts {

	BATCHLET("Batchlet"),	

	DECIDER("Decider", false),

	ITEM_READER("Item Reader"),
	
	ITEM_WRITER("Item Writer"),
	
	ITEM_PROCESSOR("Item Processor", false),
	
	CHECKPOINT_ALGORITHM("Checkpoint Algorithm"),
	
	PARTITION_MAPPER("Partition Mapper", false),
	
	PARTITION_REDUCER("Partition Reducer"), 
	
	PARTITION_COLLECTOR("Partition Collector", false),
	
	PARTITION_ANALYZER("Partition Analyzer"),
	
	JOB_LISTENER("Job Listener"),
	
	STEP_LISTENER("Step Listener"),
	
	CHUNK_LISTENER("Chunk Listener"),
	
	ITEM_READ_LISTENER("Item Read Listener"),
	
	ITEM_PROCESS_LISTENER("Item Process Listener"),
	
	ITEM_WRITE_LISTENER("Item Write Listener"),
	
	SKIP_READ_LISTENER("Skip Read Listener", false),
	
	SKIP_PROCESS_LISTENER("Skip Process Listener", false),
	
	SKIP_WRITE_LISTENER("Skip Write Listener", false),
	
	RETRY_READ_LISTENER("Retry Read Listener", false),
	
	RETRY_PROCESS_LISTENER("Retry Process Listener", false),
	
	RETRY_WRITE_LISTENER("Retry Write Listener", false);
	
	private String name;
	
	private boolean hasAbstractClass;
	
	private BatchArtifacts(String name) {
		this.name = name;
		this.hasAbstractClass = true;
	}
	
	private BatchArtifacts(String name, boolean hasAbstractClass) {
		this.name = name;
		this.hasAbstractClass = hasAbstractClass;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean hasAbstractClass(){
		return hasAbstractClass;
	}
}
