package nl.queuemanager.ui.message;

import javax.swing.*;

class StringContentViewer implements ContentViewer<String> {

	public JComponent createUI(String str) {
		return new JLabel(str);
	}

	public boolean supports(String str) {
		return str != null;
	}

	public String getDescription(String object) {
		return null;
	}		
}
