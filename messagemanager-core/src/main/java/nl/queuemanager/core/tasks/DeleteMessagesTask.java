package nl.queuemanager.core.tasks;

import java.util.List;

import javax.inject.Inject;
import javax.jms.Message;

import nl.queuemanager.core.jms.JMSDomain;
import nl.queuemanager.core.task.Task;
import nl.queuemanager.jms.JMSQueue;

import com.google.inject.assistedinject.Assisted;

public class DeleteMessagesTask extends Task {
	private final JMSDomain domain;
	private final JMSQueue queue;
	private final List<Message> messages;
	
	@Inject
	DeleteMessagesTask(JMSDomain domain, @Assisted JMSQueue queue, @Assisted List<Message> messages) {
		super(queue.getBroker());
		this.domain = domain;
		this.queue = queue;
		this.messages = messages;
	}

	@Override
	public void execute() throws Exception {
		domain.deleteMessages(queue, messages);
	}

	@Override
	public String toString() {
		return 
			"Deleting " + messages.size() + " messages from " + queue;
	}
}
