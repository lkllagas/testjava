package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import controller.queryData;
import controller.sqliteConnection;
import controller.validation;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class frmResetPassword extends JFrame {

	private JPanel contentPane;
	private JTextField txtUserID;
	private JTextField txtUserName;
	private JTextField txtSecretQuestion;
	private JTextField txtAnswer;

	Connection conn = sqliteConnection.dbConnector();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmResetPassword frame = new frmResetPassword();
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
	public frmResetPassword() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				FrmLogin frame;
				try {
					frame = new FrmLogin();
					frame.setVisible(true);
					dispose();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		setResizable(false);
		setTitle("Resetting of Password");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 375, 259);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 348, 207);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblUserId = new JLabel("User ID:");
		lblUserId.setBounds(19, 12, 77, 16);
		panel.add(lblUserId);
		
		txtUserID = new JTextField();
		txtUserID.requestFocus();
		txtUserID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				validation vd = new validation();
				
				if (!vd.validateUserID(txtUserID.getText().trim())) {
					getUserDetails();
				} else {

					txtSecretQuestion.setText(null);
					txtUserName.setText(null);
				}
				
				
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtUserName.requestFocus();
					 
				}
				
			}
		});
		txtUserID.setBounds(120, 6, 122, 28);
		panel.add(txtUserID);
		txtUserID.setColumns(10);
		
		JLabel lblUserName = new JLabel("User Name:");
		lblUserName.setBounds(19, 45, 77, 16);
		panel.add(lblUserName);
		
		txtUserName = new JTextField();
		txtUserName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtSecretQuestion.requestFocus();
					 
				}
				
			}
		});
		txtUserName.setEditable(false);
		txtUserName.setColumns(10);
		txtUserName.setBounds(120, 39, 217, 28);
		panel.add(txtUserName);
		
		JLabel lblSecretQuestion = new JLabel("Secret Question:");
		lblSecretQuestion.setBounds(19, 79, 103, 16);
		panel.add(lblSecretQuestion);
		
		txtSecretQuestion = new JTextField();
		txtSecretQuestion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtAnswer.requestFocus();
					 
				}
			}
		});
		txtSecretQuestion.setEditable(false);
		txtSecretQuestion.setColumns(10);
		txtSecretQuestion.setBounds(120, 73, 217, 28);
		panel.add(txtSecretQuestion);
		
		JLabel lblAnswer = new JLabel("Answer:");
		lblAnswer.setBounds(19, 119, 55, 16);
		panel.add(lblAnswer);
		
		txtAnswer = new JTextField();
		txtAnswer.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					onButtonPress();
					 
				}
				
			}
		});
		txtAnswer.setColumns(10);
		txtAnswer.setBounds(120, 113, 217, 28);
		panel.add(txtAnswer);
		
		JButton btnResetPassword = new JButton("Reset Password");
		btnResetPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				onButtonPress();
				
			}
		});
		btnResetPassword.setIcon(new ImageIcon(this.getClass().getResource("/reset.png")));
		btnResetPassword.setMnemonic('R');
		btnResetPassword.setBounds(120, 163, 182, 28);
		panel.add(btnResetPassword);
		this.setLocationRelativeTo(null);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{txtUserID, txtAnswer, btnResetPassword, contentPane, panel, lblUserId, lblUserName, txtUserName, lblSecretQuestion, txtSecretQuestion, lblAnswer}));
	}
	
	private void getUserDetails() {
		
		try {
			
			String query = "select * from tblUser where userID=?";
	
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, txtUserID.getText().trim());
			ResultSet rst = pst.executeQuery();
	
			while (rst.next()) {
				txtSecretQuestion.setText(rst.getString("SecretQuestion"));
				txtUserName.setText(rst.getString("FirstName").toUpperCase() + " " + rst.getString("MiddleName").toUpperCase() + " " + rst.getString("LastName").toUpperCase());
				
			}
			

			pst.close();
			rst.close();
	
			
		} catch (Exception ex) {
			
			
			
		}
		
	}
	
	private boolean validateAnswer(String username, String ans) {
		
		try {

			String query = "select * from tblUser where userID=?";
	
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, username);
			ResultSet rst = pst.executeQuery();
			
			while (rst.next()) {
				
				if (rst.getString("Answer").toLowerCase().equals(ans.toLowerCase())) {
					
					resetPassword(username, randomString(5));
					
					dispose();
					FrmLogin frame = new FrmLogin();
					frame.setVisible(true);
					
				} else {
					
					JOptionPane.showMessageDialog(null, "Incorrect answer!");
					txtAnswer.requestFocus();
				}
				
			}
			
			pst.close();
			rst.close();
	
			
		} catch (Exception ex) {
			
			JOptionPane.showMessageDialog(null, ex.toString());
			
		}
		
		return false;
		
	}
	
	public void resetPassword(String uName, String newPassword) {
		
		try {
			
			String query = "update tblUser set Password = ?,changedPassword = '1' where UserID = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, newPassword);
			pst.setString(2, uName);
	
			pst.executeUpdate();
			pst.close();
			
			JOptionPane.showMessageDialog(null, "Temporary password has been generated: [" + newPassword + "]\nPlease take note as it will be used in your initial login.");
		
		} catch (Exception ex) {
			
			JOptionPane.showMessageDialog(null, ex.toString());
		}
	}
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
	
	private void onButtonPress() {
		
		if (txtSecretQuestion.getText().trim().isEmpty()) {
			
			JOptionPane.showMessageDialog(null, "Invalid User ID!","Invalid User ID",JOptionPane.WARNING_MESSAGE);
			txtUserID.requestFocus();
		
		} else if (txtAnswer.getText().trim().isEmpty()) {
			
			JOptionPane.showMessageDialog(null, "Answer must be provided!","Blank Answer..",JOptionPane.WARNING_MESSAGE);
			txtAnswer.requestFocus();
			
		} else {

			validateAnswer(txtUserID.getText().trim(),txtAnswer.getText().trim());
			
		}
		
	}
	
}
