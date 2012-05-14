package com.vgs.dashboard.model.bo;


/**
 * An interface that describes the operations performed to provide an
 * <code>Instrument</code> with a value or status.
 * 
 * @author dromoli
 * 
 */
public interface Task<T> {

	/**
	 * Performs the task.
	 * 
	 * @return the value calculated by this <code>Task</code>
	 */
	T performTask();

}
