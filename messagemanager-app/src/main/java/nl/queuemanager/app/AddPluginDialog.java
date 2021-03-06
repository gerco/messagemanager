package nl.queuemanager.app;

import nl.queuemanager.ui.util.TableColumnAdjuster;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class AddPluginDialog extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTable installedPluginsTable;
	private JTable availablePluginsTable;
	private final Action selectPluginAction = new SelectPluginAction();
	
	private PluginDescriptor selectedPlugin;

	/**
	 * Create the dialog.
	 */
	public AddPluginDialog(PluginManager pluginManager) {
		setTitle("Add plugin");
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel installedPluginsPanel = new JPanel();
				tabbedPane.addTab("Installed plugins", null, installedPluginsPanel, null);
				installedPluginsPanel.setLayout(new BorderLayout(0, 0));
				{
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					installedPluginsPanel.add(scrollPane);
					{
						installedPluginsTable = new JTable();
						PluginListTableModel model = new PluginListTableModel();
						model.setData(pluginManager.getInstalledPlugins());
						installedPluginsTable.setModel(model);
						TableColumnAdjuster adjuster = new TableColumnAdjuster(installedPluginsTable, 15);
						adjuster.adjustColumns();
						scrollPane.setViewportView(installedPluginsTable);
						// TODO Install double-click handler
					}
				}
				{
					JButton selectPluginButton = new JButton("Select plugin");
					selectPluginButton.setAction(selectPluginAction);
					installedPluginsPanel.add(selectPluginButton, BorderLayout.SOUTH);
				}
			}
			{
				JPanel availablePluginsPanel = new JPanel();
				tabbedPane.addTab("Available plugins", null, availablePluginsPanel, null);
				availablePluginsPanel.setLayout(new BorderLayout(0, 0));
				{
					JScrollPane scrollPane = new JScrollPane();
					availablePluginsPanel.add(scrollPane, BorderLayout.CENTER);
					{
						availablePluginsTable = new JTable();
						scrollPane.setViewportView(availablePluginsTable);
					}
				}
				{
					JButton installPluginButton = new JButton("Install plugin");
					availablePluginsPanel.add(installPluginButton, BorderLayout.SOUTH);
				}
			}
		}
	}
	
	public PluginDescriptor selectPlugin() {
		setVisible(true);
		return selectedPlugin;
	}

	private class SelectPluginAction extends AbstractAction {
		public SelectPluginAction() {
			putValue(NAME, "Select Plugin");
			putValue(SHORT_DESCRIPTION, "Select Plugin");
		}
		public void actionPerformed(ActionEvent e) {
			int row = installedPluginsTable.getSelectedRow();
			if(row != -1) {
				selectedPlugin = ((PluginListTableModel)installedPluginsTable.getModel()).getRowItem(row);
				setVisible(false);
			}
		}
	}
	
}
