package nl.queuemanager.ui.settings;

import com.google.inject.Inject;
import nl.queuemanager.core.configuration.CoreConfiguration;
import nl.queuemanager.ui.util.JIntegerField;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;

@SuppressWarnings("serial")
class GeneralSettingsPanel extends JPanel implements SettingsPanel {
	
	private final CoreConfiguration config;
	private JIntegerField autoRefreshIntervalField;
	private JIntegerField maxBufferedMessagesField;
	private JComboBox<LookAndFeelInfo> lafCombo;
	
	@Inject
	public GeneralSettingsPanel(CoreConfiguration config) {
		setAlignmentY(Component.TOP_ALIGNMENT);
		this.config = config;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel autoRefreshIntervalLabel = new JLabel("Message count refresh interval (ms)");
		autoRefreshIntervalLabel.setToolTipText("How often to refresh the number of messages on queues");
		GridBagConstraints gbc_autoRefreshIntervalLabel = new GridBagConstraints();
		gbc_autoRefreshIntervalLabel.anchor = GridBagConstraints.EAST;
		gbc_autoRefreshIntervalLabel.insets = new Insets(0, 0, 5, 5);
		gbc_autoRefreshIntervalLabel.gridx = 0;
		gbc_autoRefreshIntervalLabel.gridy = 0;
		add(autoRefreshIntervalLabel, gbc_autoRefreshIntervalLabel);
		
		autoRefreshIntervalField = new JIntegerField(10);
		autoRefreshIntervalLabel.setLabelFor(autoRefreshIntervalField);
		GridBagConstraints gbc_autoRefreshIntervalField = new GridBagConstraints();
		gbc_autoRefreshIntervalField.anchor = GridBagConstraints.WEST;
		gbc_autoRefreshIntervalField.insets = new Insets(0, 0, 5, 0);
		gbc_autoRefreshIntervalField.gridx = 1;
		gbc_autoRefreshIntervalField.gridy = 0;
		add(autoRefreshIntervalField, gbc_autoRefreshIntervalField);
		autoRefreshIntervalField.setColumns(10);
		
		JLabel maxBufferedMessagesLabel = new JLabel("Topic receiver buffer size");
		maxBufferedMessagesLabel.setToolTipText("How many messages to buffer for topic receivers");
		GridBagConstraints gbc_maxBufferedMessagesLabel = new GridBagConstraints();
		gbc_maxBufferedMessagesLabel.anchor = GridBagConstraints.EAST;
		gbc_maxBufferedMessagesLabel.insets = new Insets(0, 0, 5, 5);
		gbc_maxBufferedMessagesLabel.gridx = 0;
		gbc_maxBufferedMessagesLabel.gridy = 1;
		add(maxBufferedMessagesLabel, gbc_maxBufferedMessagesLabel);
		
		maxBufferedMessagesField = new JIntegerField(5);
		maxBufferedMessagesLabel.setLabelFor(maxBufferedMessagesField);
		GridBagConstraints gbc_maxBufferedMessagesField = new GridBagConstraints();
		gbc_maxBufferedMessagesField.insets = new Insets(0, 0, 5, 0);
		gbc_maxBufferedMessagesField.anchor = GridBagConstraints.WEST;
		gbc_maxBufferedMessagesField.gridx = 1;
		gbc_maxBufferedMessagesField.gridy = 1;
		add(maxBufferedMessagesField, gbc_maxBufferedMessagesField);
		maxBufferedMessagesField.setColumns(10);
		
		JLabel lblLookAndFeel = new JLabel("Look and feel (needs restart)");
		GridBagConstraints gbc_lblLookAndFeel = new GridBagConstraints();
		gbc_lblLookAndFeel.anchor = GridBagConstraints.EAST;
		gbc_lblLookAndFeel.insets = new Insets(0, 0, 0, 5);
		gbc_lblLookAndFeel.gridx = 0;
		gbc_lblLookAndFeel.gridy = 2;
		add(lblLookAndFeel, gbc_lblLookAndFeel);

		LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
		lafCombo = new JComboBox<>(lafs);
		lafCombo.setRenderer(new LAFRenderer());
		lblLookAndFeel.setLabelFor(lafCombo);
		GridBagConstraints gbc_lafCombo = new GridBagConstraints();
		gbc_lafCombo.fill = GridBagConstraints.HORIZONTAL;
		gbc_lafCombo.gridx = 1;
		gbc_lafCombo.gridy = 2;
		add(lafCombo, gbc_lafCombo);
	}
	
	private static class LAFRenderer extends DefaultListCellRenderer {
	    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        Object item = value;

	        if(item instanceof LookAndFeelInfo) {
	            item = ((LookAndFeelInfo)item).getName();
	        }
	        
	        return super.getListCellRendererComponent( list, item, index, isSelected, cellHasFocus);
	    }
	}
	
	public void readSettings() {
		autoRefreshIntervalField.setValue(Integer.parseInt(config.getUserPref(
				CoreConfiguration.PREF_AUTOREFRESH_INTERVAL, 
				CoreConfiguration.DEFAULT_AUTOREFRESH_INTERVAL)));
		maxBufferedMessagesField.setValue(Integer.parseInt(config.getUserPref(
				CoreConfiguration.PREF_MAX_BUFFERED_MSG,
				CoreConfiguration.DEFAULT_MAX_BUFFERED_MSG)));
		
		String lafClassName = config.getUserPref(
				CoreConfiguration.PREF_LOOK_AND_FEEL,
				UIManager.getSystemLookAndFeelClassName());
		if(!selectLafByClassName(lafClassName)) {
			// If we didn't get a match, select the default native LAF
			selectLafByClassName(UIManager.getSystemLookAndFeelClassName());
		}
	}
	
	private boolean selectLafByClassName(String className) {
		for(LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
			if(info.getClassName().equals(className)) {
				lafCombo.setSelectedItem(info);
				return true;
			}
		}
		return false;
	}
	
	public void saveSettings() {
		config.setUserPref(CoreConfiguration.PREF_AUTOREFRESH_INTERVAL, Integer.toString(autoRefreshIntervalField.getValue()));
		config.setUserPref(CoreConfiguration.PREF_MAX_BUFFERED_MSG, Integer.toString(maxBufferedMessagesField.getValue()));
		
		LookAndFeelInfo laf = (LookAndFeelInfo) lafCombo.getSelectedItem();
		if(laf != null) {
			config.setUserPref(CoreConfiguration.PREF_LOOK_AND_FEEL, laf.getClassName());
		}
	}

	public JComponent getUIPanel() {
		return this;
	}
	
}
