package nl.queuemanager.ui.message;

import nl.queuemanager.jms.JMSPart;

import javax.swing.*;

/**
 * Interface for components that can create a GUI to display a Message Part.
 * 
 * @author Gerco Dries <gerco@gdries.nl>
 */
public interface MessagePartContentViewer extends ContentViewer<JMSPart> {
	
	/**
	 * Examines the message and determines whether this Content Viewer supports 
	 * displaying the message part content.
	 * 
	 * @param message
	 * @return
	 */
	public boolean supports(JMSPart part);

	/**
	 * Create and return the user interface component for this message.
	 * 
	 * @return
	 */
	public JComponent createUI(JMSPart part);
	
}
