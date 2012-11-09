package com.gl.couchbase.test;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.spy.memcached.CASValue;

import com.couchbase.client.CouchbaseClient;
import javax.swing.border.TitledBorder;
import javax.swing.JSplitPane;

public class CouchbaseSDKTestUI {

	private JFrame frame;
	private JTextField txtConnection;
	private JTextField txtBucket;
	private JTextField txtKey;
	private JTextField txtExpTime;
	private static final String EMPTY = "";
	private static String selectedOpp ="Add";	
	private CouchbaseClient client = null;
	private JTextField txtCasValue;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CouchbaseSDKTestUI window = new CouchbaseSDKTestUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CouchbaseSDKTestUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 736, 494);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JPanel pnlConnection = new JPanel();
		pnlConnection.setBorder(new TitledBorder(null, "Couchbase SDK Tester", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frame.getContentPane().add(pnlConnection);
		GridBagLayout gbl_pnlConnection = new GridBagLayout();
		gbl_pnlConnection.columnWidths = new int[]{40, 126, 334, 0};
		gbl_pnlConnection.rowHeights = new int[]{25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pnlConnection.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_pnlConnection.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlConnection.setLayout(gbl_pnlConnection);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		pnlConnection.add(panel, gbc_panel);
		panel.setForeground(Color.PINK);

		JLabel lblConnectionUrl = new JLabel("Connection URL:");
		panel.add(lblConnectionUrl);

		txtConnection = new JTextField();
		GridBagConstraints gbc_txtConnection = new GridBagConstraints();
		gbc_txtConnection.insets = new Insets(0, 0, 5, 0);
		gbc_txtConnection.anchor = GridBagConstraints.WEST;
		gbc_txtConnection.gridx = 2;
		gbc_txtConnection.gridy = 0;
		pnlConnection.add(txtConnection, gbc_txtConnection);
		txtConnection.setColumns(30);

		JPanel pnlBucket = new JPanel();
		GridBagConstraints gbc_pnlBucket = new GridBagConstraints();
		gbc_pnlBucket.anchor = GridBagConstraints.WEST;
		gbc_pnlBucket.insets = new Insets(0, 0, 5, 5);
		gbc_pnlBucket.gridx = 1;
		gbc_pnlBucket.gridy = 1;
		pnlConnection.add(pnlBucket, gbc_pnlBucket);

		JLabel lblBucketName = new JLabel("Bucket Name:");
		pnlBucket.add(lblBucketName);

		txtBucket = new JTextField();
		GridBagConstraints gbc_txtBucket = new GridBagConstraints();
		gbc_txtBucket.insets = new Insets(0, 0, 5, 0);
		gbc_txtBucket.anchor = GridBagConstraints.WEST;
		gbc_txtBucket.gridx = 2;
		gbc_txtBucket.gridy = 1;
		pnlConnection.add(txtBucket, gbc_txtBucket);
		txtBucket.setColumns(15);


		GridBagConstraints gbc_comboBoxOper = new GridBagConstraints();
		gbc_comboBoxOper.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxOper.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxOper.gridx = 1;
		gbc_comboBoxOper.gridy = 4;



		final JLabel lblKey = new JLabel("Key:");
		lblKey.setEnabled(false);
		GridBagConstraints gbc_lblKey = new GridBagConstraints();
		gbc_lblKey.anchor = GridBagConstraints.WEST;
		gbc_lblKey.insets = new Insets(0, 0, 5, 5);
		gbc_lblKey.gridx = 1;
		gbc_lblKey.gridy = 4;
		pnlConnection.add(lblKey, gbc_lblKey);

		txtKey = new JTextField();
		txtKey.setEnabled(false);
		GridBagConstraints gbc_txtKey = new GridBagConstraints();
		gbc_txtKey.anchor = GridBagConstraints.WEST;
		gbc_txtKey.insets = new Insets(0, 0, 5, 0);
		gbc_txtKey.gridx = 2;
		gbc_txtKey.gridy = 4;
		pnlConnection.add(txtKey, gbc_txtKey);
		txtKey.setColumns(15);

		final JLabel lblValue = new JLabel("Value:");
		lblValue.setEnabled(false);
		GridBagConstraints gbc_lblValue = new GridBagConstraints();
		gbc_lblValue.anchor = GridBagConstraints.WEST;
		gbc_lblValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblValue.gridx = 1;
		gbc_lblValue.gridy = 5;
		pnlConnection.add(lblValue, gbc_lblValue);

		final JTextArea txtValue = new JTextArea();
		GridBagConstraints gbc_txtValue = new GridBagConstraints();
		gbc_txtValue.insets = new Insets(0, 0, 5, 0);
		gbc_txtValue.fill = GridBagConstraints.BOTH;
		gbc_txtValue.gridx = 2;
		gbc_txtValue.gridy = 5;
		pnlConnection.add(txtValue, gbc_txtValue);

		final JLabel lblExpTime = new JLabel("Exp Time:");
		lblExpTime.setEnabled(false);
		GridBagConstraints gbc_lblExpTime = new GridBagConstraints();
		gbc_lblExpTime.anchor = GridBagConstraints.WEST;
		gbc_lblExpTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblExpTime.gridx = 1;
		gbc_lblExpTime.gridy = 6;
		pnlConnection.add(lblExpTime, gbc_lblExpTime);

		txtExpTime = new JTextField();
		txtExpTime.setEnabled(false);
		GridBagConstraints gbc_txtExpTime = new GridBagConstraints();
		gbc_txtExpTime.insets = new Insets(0, 0, 5, 0);
		gbc_txtExpTime.anchor = GridBagConstraints.WEST;
		gbc_txtExpTime.gridx = 2;
		gbc_txtExpTime.gridy = 6;
		pnlConnection.add(txtExpTime, gbc_txtExpTime);
		txtExpTime.setColumns(15);

		final JLabel lblValueToAppend = new JLabel("Value To Append:");
		lblValueToAppend.setEnabled(false);
		GridBagConstraints gbc_lblValueToAppend = new GridBagConstraints();
		gbc_lblValueToAppend.anchor = GridBagConstraints.WEST;
		gbc_lblValueToAppend.insets = new Insets(0, 0, 5, 5);
		gbc_lblValueToAppend.gridx = 1;
		gbc_lblValueToAppend.gridy = 7;
		pnlConnection.add(lblValueToAppend, gbc_lblValueToAppend);

		final JTextArea txtValueToAppend = new JTextArea();
		txtValueToAppend.setEnabled(false);
		GridBagConstraints gbc_txtValueToAppend = new GridBagConstraints();
		gbc_txtValueToAppend.insets = new Insets(0, 0, 5, 0);
		gbc_txtValueToAppend.fill = GridBagConstraints.BOTH;
		gbc_txtValueToAppend.gridx = 2;
		gbc_txtValueToAppend.gridy = 7;
		pnlConnection.add(txtValueToAppend, gbc_txtValueToAppend);		

		final JLabel lblCasValue = new JLabel("CAS Value:");
		lblCasValue.setEnabled(false);
		GridBagConstraints gbc_lblCasValue = new GridBagConstraints();
		gbc_lblCasValue.anchor = GridBagConstraints.WEST;
		gbc_lblCasValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblCasValue.gridx = 1;
		gbc_lblCasValue.gridy = 8;
		pnlConnection.add(lblCasValue, gbc_lblCasValue);

		txtCasValue = new JTextField();
		txtCasValue.setEnabled(false);
		GridBagConstraints gbc_txtCasValue = new GridBagConstraints();
		gbc_txtCasValue.insets = new Insets(0, 0, 5, 0);
		gbc_txtCasValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCasValue.gridx = 2;
		gbc_txtCasValue.gridy = 8;
		pnlConnection.add(txtCasValue, gbc_txtCasValue);
		txtCasValue.setColumns(10);

		final JComboBox comboBoxOper = new JComboBox();
		comboBoxOper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				selectedOpp = comboBoxOper.getSelectedItem().toString();
				txtExpTime.setText(EMPTY);
				txtValue.setText(EMPTY);
				txtKey.setText(EMPTY);
				txtValueToAppend.setText(EMPTY);
				txtCasValue.setText(EMPTY);

				if(selectedOpp.equals("Remove")){					
					txtExpTime.setEnabled(false);
					lblValue.setEnabled(false);
					txtValue.setEnabled(false);
					lblExpTime.setEnabled(false);	
					txtValueToAppend.setEnabled(false);
					lblValueToAppend.setEnabled(false);					
				}	else if (selectedOpp.equals("Append") || selectedOpp.equals("Prepend")){	
					txtValueToAppend.setEnabled(true);
					lblValueToAppend.setEnabled(true);
					txtExpTime.setEnabled(false);
					lblValue.setEnabled(false);
					txtValue.setEnabled(false);
					lblExpTime.setEnabled(false);
					txtCasValue.setEnabled(false);
					lblCasValue.setEnabled(false);
				} else if (selectedOpp.equals("Add") || selectedOpp.equals("Set")){					
					txtValueToAppend.setEnabled(false);
					lblValueToAppend.setEnabled(false);
					txtExpTime.setEnabled(true);
					lblValue.setEnabled(true);
					txtValue.setEnabled(true);
					lblExpTime.setEnabled(true);
					txtCasValue.setEnabled(false);
					lblCasValue.setEnabled(false);
				} else if(selectedOpp.equals("Get")){					
					txtValueToAppend.setEnabled(false);
					lblValueToAppend.setEnabled(false);
					txtExpTime.setEnabled(false);
					lblValue.setEnabled(true);
					txtValue.setEnabled(true);
					lblExpTime.setEnabled(false);	
					txtCasValue.setEnabled(false);
					lblCasValue.setEnabled(false);
				} else if(selectedOpp.equals("Get with CAS")){					
					txtValueToAppend.setEnabled(false);
					lblValueToAppend.setEnabled(false);
					txtExpTime.setEnabled(false);
					lblValue.setEnabled(true);					
					txtValue.setEnabled(true);
					txtCasValue.setEnabled(true);
					lblCasValue.setEnabled(true);
					lblExpTime.setEnabled(false);					
				}else if(selectedOpp.equals("Set with CAS")){					
					txtValueToAppend.setEnabled(false);
					lblValueToAppend.setEnabled(false);
					txtExpTime.setEnabled(true);
					lblValue.setEnabled(true);					
					txtValue.setEnabled(true);
					txtCasValue.setEnabled(true);
					lblCasValue.setEnabled(true);
					lblExpTime.setEnabled(true);					
				}	
				else if(selectedOpp.equals("Delete & Persist") || selectedOpp.equals("DPR")){					
					txtValueToAppend.setEnabled(false);
					lblValueToAppend.setEnabled(false);
					txtExpTime.setEnabled(false);
					lblValue.setEnabled(false);					
					txtValue.setEnabled(false);
					txtCasValue.setEnabled(false);
					lblCasValue.setEnabled(false);
					lblExpTime.setEnabled(false);					
				}	
			}
		});		
		comboBoxOper.setEnabled(false);
		comboBoxOper.setModel(new DefaultComboBoxModel(new String[] {"Add", "Remove", "Set", "Get", "Append", "Prepend", "Get with CAS", "Set with CAS", "Delete & Persist", "DPR", "Set & Persist", "SPR"}));
		gbc_comboBoxOper.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxOper.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxOper.gridx = 1;
		gbc_comboBoxOper.gridy = 3;
		pnlConnection.add(comboBoxOper, gbc_comboBoxOper);
		
		JSplitPane splitPane = new JSplitPane();
		GridBagConstraints gbc_splitPane = new GridBagConstraints();
		gbc_splitPane.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane.gridx = 2;
		gbc_splitPane.gridy = 9;
		pnlConnection.add(splitPane, gbc_splitPane);
		
				final JButton btnSubmit = new JButton("Submit");
				splitPane.setLeftComponent(btnSubmit);
				btnSubmit.setEnabled(false);
				
				final JButton btnClose = new JButton("Close");
				btnClose.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {		
						client.shutdown();
						frame.dispose();
					}
				});
				btnClose.setEnabled(false);
				splitPane.setRightComponent(btnClose);
				btnSubmit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {	
						OperationAttributes opAttr = new OperationAttributes();
						String expTime = txtExpTime.getText();
						if(expTime==null||expTime.length()==0){
							expTime="0";
						}
						opAttr.setExpTime(Integer.parseInt(expTime));
						opAttr.setKey(txtKey.getText());
						opAttr.setOperationName(selectedOpp);
						opAttr.setValue(txtValue.getText());
						opAttr.setValueToAppend(txtValueToAppend.getText());
						String casValue = txtCasValue.getText();
						if(!casValue.isEmpty())
							opAttr.setCasValue(Long.valueOf(casValue));
						ConnectionAttributes conAttr = new ConnectionAttributes();
						conAttr.setConnectionUrl(txtConnection.getText());
						conAttr.setBucketName(txtBucket.getText());
						OperationBase operationBase = new OperationBase(opAttr);									
						Object obj = operationBase.executeOperation(client);
						String displayableException = operationBase.getDisplayableException();
						String displayableMessage = operationBase.getDisplayableMessage();
						if(null!=displayableMessage && !displayableMessage.isEmpty()){
							JOptionPane.showMessageDialog(null, displayableMessage,null, JOptionPane.OK_OPTION);
						}
						else if (null!=displayableException && !displayableException.isEmpty()) {
							JOptionPane.showMessageDialog(null, displayableException, null, JOptionPane.ERROR_MESSAGE);
						}
						if(obj!=null){
							if(obj instanceof CASValue<?>){
								txtValue.setText(((CASValue)obj).getValue().toString());
								txtCasValue.setText(String.valueOf(((CASValue)obj).getCas()));
							}else
								txtValue.setText((String) obj);
						}				
					}
				});		

		JButton btnConnectAndExecute = new JButton("Connect and Execute Operations");
		btnConnectAndExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConnectionAttributes conAttr = new ConnectionAttributes();
				conAttr.setConnectionUrl(txtConnection.getText());
				conAttr.setBucketName(txtBucket.getText());
				OperationBase operationBase = new OperationBase(conAttr);
				client = operationBase.connect();
				comboBoxOper.setEnabled(true);
				lblKey.setEnabled(true);
				txtKey.setEnabled(true);
				lblValue.setEnabled(true);
				txtValue.setEnabled(true);
				lblExpTime.setEnabled(true);
				txtExpTime.setEnabled(true);		
				btnSubmit.setEnabled(true);
				btnClose.setEnabled(true);
			}
		});
		GridBagConstraints gbc_btnConnectAndExecute = new GridBagConstraints();
		gbc_btnConnectAndExecute.insets = new Insets(0, 0, 5, 0);
		gbc_btnConnectAndExecute.gridx = 2;
		gbc_btnConnectAndExecute.gridy = 2;
		pnlConnection.add(btnConnectAndExecute, gbc_btnConnectAndExecute);
	}
}
