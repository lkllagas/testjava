package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import model.Global;
import controller.queryData;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class frmSystemSetup extends JFrame {

	private JPanel contentPane;
	private JTextField txtBranchCode;
	private JTextField txtUploadFolder;
	private JTextField txtDownloadFolder;
	private final JFileChooser db = new JFileChooser();
	private JTextField txtBranchName;
	private JRadioButton rdbtnEnable;
	private JRadioButton rdbtnDisable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmSystemSetup frame = new frmSystemSetup();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public frmSystemSetup() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
			
				
			}
		});
		setFont(new Font("SansSerif", Font.BOLD, 14));
		setTitle("System Setup");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 570, 311);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		panel.setToolTipText("System Setup");
		panel.setBounds(10, 11, 548, 266);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblReleasingBranchCode = new JLabel("Releasing Branch Code");
		lblReleasingBranchCode.setBounds(22, 27, 132, 16);
		panel.add(lblReleasingBranchCode);
		
		txtBranchCode = new JTextField();
		txtBranchCode.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtBranchName.requestFocus();
					 
				}
				
			}
		});
		txtBranchCode.setBounds(164, 21, 96, 28);
		panel.add(txtBranchCode);
		txtBranchCode.setColumns(7);
		
		JLabel lblUploadFolder = new JLabel("Upload Folder");
		lblUploadFolder.setBounds(22, 95, 132, 16);
		panel.add(lblUploadFolder);
		
		txtUploadFolder = new JTextField();
		txtUploadFolder.setEnabled(false);
		txtUploadFolder.setColumns(10);
		txtUploadFolder.setBounds(164, 89, 274, 28);
		panel.add(txtUploadFolder);
		
		JLabel lblDownloadFolder = new JLabel("Download Folder");
		lblDownloadFolder.setBounds(22, 129, 132, 16);
		panel.add(lblDownloadFolder);
		
		txtDownloadFolder = new JTextField();
		txtDownloadFolder.setEnabled(false);
		txtDownloadFolder.setColumns(10);
		txtDownloadFolder.setBounds(164, 123, 274, 28);
		panel.add(txtDownloadFolder);
		txtBranchCode.requestFocus();
		JButton button = new JButton("Browse");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String path = "";
				int searchSlash = 0;
				
				File file2 = new File("untitled.txt");
				db.setSelectedFile(file2);
				int returnVal = db.showOpenDialog(null);
				
				
				if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {

					java.io.File file = db.getSelectedFile();

					path = file.toString();
					
					searchSlash = path.lastIndexOf('\\');
					
					path = path.substring(0, searchSlash + 1);
					
					txtUploadFolder.setText(path);

				}
				
			}
		});
		button.setBounds(445, 89, 90, 28);
		panel.add(button);
		
		JButton button_1 = new JButton("Browse");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String path = "";
				int searchSlash = 0;
				
				File file2 = new File("untitled.txt");
				db.setSelectedFile(file2);
				int returnVal = db.showOpenDialog(null);
				
				
				if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {

					java.io.File file = db.getSelectedFile();

					path = file.toString();
					
					searchSlash = path.lastIndexOf('\\');
					
					path = path.substring(0, searchSlash + 1);
					
					txtDownloadFolder.setText(path);

				}
				
			}
		});
		button_1.setBounds(445, 123, 90, 28);
		panel.add(button_1);
		
		JButton btnsave = new JButton("Save");
		btnsave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				onButtonPress();
				
			}
		});
		btnsave.setMnemonic('S');
		btnsave.setBounds(348, 224, 90, 28);
		panel.add(btnsave);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				dispose();
				
				frmMainMenu frame = new frmMainMenu();
				
				frame.setVisible(true);
				
			}
		});
		btnClose.setMnemonic('C');
		btnClose.setBounds(445, 224, 90, 28);
		panel.add(btnClose);
		

		JLabel lblReleasingBranchName = new JLabel("Branch Name");
		lblReleasingBranchName.setBounds(22, 61, 132, 16);
		panel.add(lblReleasingBranchName);
		
		txtBranchName = new JTextField();
		txtBranchName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					onButtonPress();
					 
				}
				
			}
		});
		txtBranchName.setText("");
		txtBranchName.setColumns(7);
		txtBranchName.setBounds(164, 55, 274, 28);
		panel.add(txtBranchName);
		
		//Calling the SystemSetup Global Vars
		
		queryData qd = new queryData();
		
		qd.getCompanySetup();
		
		txtBranchCode.setText(Global.getBranchCode().toString());
		txtUploadFolder.setText(Global.getUploadPath());
		txtDownloadFolder.setText(Global.getDownloadPath());
		txtBranchName.setText(Global.getBranchName().toString());
		
		

		
		db.setBounds(34, 212, 16, 31);
		panel.add(db);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Import Function in Claiming Checks", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(164, 163, 274, 57);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		rdbtnEnable = new JRadioButton("Enable");
		rdbtnEnable.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (rdbtnEnable.isSelected()) rdbtnDisable.setSelected(false);
				
			}
		});
		rdbtnEnable.setBounds(22, 22, 115, 18);
		panel_1.add(rdbtnEnable);
		
		rdbtnDisable = new JRadioButton("Disable");
		rdbtnDisable.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
				if (rdbtnDisable.isSelected()) rdbtnEnable.setSelected(false);
			}
		});
		rdbtnDisable.setBounds(149, 22, 115, 18);
		panel_1.add(rdbtnDisable);
		
		if (Global.isEnableImport()) {
			
			
			rdbtnEnable.setSelected(true);
			rdbtnDisable.setSelected(false);
			
		} else {
			
			rdbtnDisable.setSelected(true);
			rdbtnEnable.setSelected(false);
		}
		
	}
	
	private void onButtonPress() {
		String bCode = Global.getBranchCode();
		
		boolean whatIsSelected;
		
		if (rdbtnEnable.isSelected()) whatIsSelected = true;
		else whatIsSelected = false;
		
		queryData qd = new queryData();
		
		qd.updateCompanySetup(txtBranchCode.getText(), txtDownloadFolder.getText(), txtUploadFolder.getText(), bCode,txtBranchName.getText(),whatIsSelected);
		
	}
}
