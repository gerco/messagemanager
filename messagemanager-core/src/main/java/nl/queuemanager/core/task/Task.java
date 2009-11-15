/**

 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.queuemanager.core.task;

import java.util.Set;

import nl.queuemanager.core.events.AbstractEventSource;
import nl.queuemanager.core.events.EventListener;
import nl.queuemanager.core.task.TaskEvent.EVENT;
import nl.queuemanager.core.util.WeakHashSet;

/**
 * This class is the base class for all tasks to be executed on the TaskExecutor. 
 * When the execute() method throws an exception and the Task is used as a Runnable, 
 * the run() method will catch it and dispatch TASK_ERROR and clear the task queue.
 * 
 * @author gerco
 *
 */
public abstract class Task extends AbstractEventSource<TaskEvent> implements Runnable {

	private TaskExecutor executor;
	
	private DependencyRemover dependencyRemover = new DependencyRemover();
	
	/**
	 * The resource object that this tasks wants to be available. A Task may
	 * only have a single resource object or no resource object. Only a single
	 * Task is allowed to be active per resource object at any one time.
	 */
	private final Object resource;
	
	/**
	 * The set of other tasks (if any) this Task depends upon. This task will not
	 * start to run before all of it's dependencies have finished running.
	 */
	private final Set<Task> dependencies;
	
	/**
	 * This object will be notify()'d whenever a dependency has finished running.
	 */
	private final Object dependenciesLock = new Object();
	
	protected long startTime;

	/**
	 * Construct a Task with the specified object as it's resource, may be null.
	 * 
	 * @param resource
	 */
	@SuppressWarnings("unchecked")
	protected Task(Object resource) {
		this.resource = resource;
		
		/*
		 * This must be a Weak Set because when the queue is cleared by the Executors,
		 * any dependencies we were waiting on must be able to be garbage collected or
		 * the waiting will never stop!
		 */
		this.dependencies = new WeakHashSet();
	}
	
	TaskExecutor getExecutor() {
		return executor;
	}

	void setExecutor(TaskExecutor executor) {
		this.executor = executor;
	}
	
	/**
	 * Add a dependency to this Task. This task will not run until all of it's 
	 * dependencies have finished running.
	 * 
	 * @param task
	 */
	public void addDependency(Task task) {
		synchronized(dependenciesLock) {
			task.addListener(dependencyRemover);
			dependencies.add(task);
		}
	}

	/**
	 * Remove the specified task as a dependency of this task
	 * 
	 * @param task
	 */
	public boolean removeDependency(Task task) {
		synchronized(dependenciesLock) {
			boolean res = dependencies.remove(task);
			dependenciesLock.notify();
			return res;
		}
	}
	
	/**
	 * Return the number of dependencies for this Task.
	 * 
	 * @return
	 */
	public int getDependencyCount() {
		synchronized(dependenciesLock) {
			return dependencies.size();
		}
	}
		
	public final void run() {
		if(getDependencyCount() != 0)
			throw new IllegalStateException("Task started with non-zero dependency count!");
		
		startTime = System.currentTimeMillis();
		dispatchTaskStarted();
		
		try {
			execute();
		} catch (Exception e) {
			// Tell the executor to clear the task queue
			getExecutor().clearQueue();
			
			// Tell the application that there was an error
			dispatchTaskError(e);
		}
		
		dispatchTaskFinished();
		getExecutor().afterExecute(this);
	}

	protected void dispatchTaskWaiting() {
		dispatchEvent(new TaskEvent(EVENT.TASK_WAITING, getInfo(), this));
	}
	
	protected void dispatchTaskStarted() {
		dispatchEvent(new TaskEvent(EVENT.TASK_STARTED, getInfo(), this));
	}

	protected void dispatchTaskError(Exception e) {
		dispatchEvent(new TaskEvent(EVENT.TASK_ERROR, e, this));
	}
	
	protected void dispatchTaskFinished() {
		dispatchEvent(new TaskEvent(EVENT.TASK_FINISHED, getInfo(), this));
	}
	
	void dispatchTaskDiscarded() {
		dispatchEvent(new TaskEvent(EVENT.TASK_DISCARDED, null, this));
	}
	
	/**
	 * This method should perform the actual work. Any exceptions thrown will be converted
	 * into a TASK_ERROR event on the EventListener<TaskEvent>.
	 * 
	 * @throws Exception
	 */
	public abstract void execute() throws Exception;
	
	/**
	 * This method should return status information for the running task. It may be called
	 * one or more times when a task dispatches TASK_PROGRESS. It will not be called when
	 * TASK_PROGRESS is never dispatched.
	 * 
	 * @return The status of the running task
	 */
	public String getStatus() {
		return this.toString();
	}
	
	/**
	 * Return an Object describing the current status of the Task. This object is included in
	 * events published from the task (except TASK_PROGRESS and TASK_ERROR). The default 
	 * implementation returns null
	 * 
	 * @return
	 */
	protected Object getInfo() {
		return null;
	}
	
	/**
	 * The maximum progress value reported through TASK_PROGRESS events. When this value is
	 * reached, the task is considered complete. By default, this method returns 1.
	 * 
	 * @return The highest value to ever be reported through TASK_PROGRESS.
	 */
	public int getProgressMaximum() {
		return 1;
	}
		
	/**
	 * Raise the TASK_PROGRESS event with this task as the source and 'current' as the value
	 * 
	 * @param current The amount of progress that has been made (in total)
	 */
	protected void reportProgress(int current) {
		dispatchEvent(new TaskEvent(EVENT.TASK_PROGRESS, current, this));
	}
	
	/**
	 * When this Task is a true background task, the progress dialog will not
	 * pop up and allow the user to continue working while this task executes.
	 */
	public boolean isBackground() {
		return false;
	}

	/**
	 * Get this Tasks resource object, if any.
	 * 
	 * @return
	 */
	public Object getResource() {
		return resource;
	}
	
	/**
	 * When a Task that this Task depends on has sent it's TASK_FINISHED event. Remove
	 * that task as a dependency.
	 */
	private class DependencyRemover implements EventListener<TaskEvent> {
		public void processEvent(TaskEvent event) {
			if(event.getId() == EVENT.TASK_FINISHED) {
				Task.this.removeDependency((Task)event.getSource());
				event.getSource().removeListener(this);
			}
		}
	}
}