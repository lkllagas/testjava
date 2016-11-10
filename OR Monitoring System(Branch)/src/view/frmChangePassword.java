package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import controller.sqliteConnection;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class frmChangePassword extends JFrame {

	private JPanel contentPane;
	private JPasswordField txtTempPassword;
	private JPasswordField txtNewPassword;
	private JPasswordField txtConfirmPassword;
	private String userID;
	Connection conn = sqliteConnection.dbConnector();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmChangePassword frame = new frmChangePassword();
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
	
	public frmChangePassword(String uid) {
		
		this.userID = uid;
		initialize();
		
	}
	
	public frmChangePassword() {
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
		
		initialize();
	}
	
	
	private void initialize() {
		
		setTitle("Change Password");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 436, 246);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 11, 410, 198);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("[For security purpose, kindly change your temporary password]");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 11, 390, 14);
		panel.add(lblNewLabel);
		
		JLabel lblYourTemporaryPassword = new JLabel("Your Temporary Password:");
		lblYourTemporaryPassword.setBounds(48, 57, 158, 14);
		panel.add(lblYourTemporaryPassword);
		
		txtTempPassword = new JPasswordField();
		txtTempPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				 if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				      txtNewPassword.requestFocus();
				   }
				 
			}
		});
		txtTempPassword.setBounds(206, 50, 152, 28);
		panel.add(txtTempPassword);
		
		JLabel lblYourNewPassword = new JLabel("Your New Password:");
		lblYourNewPassword.setBounds(48, 90, 158, 14);
		panel.add(lblYourNewPassword);
		
		txtNewPassword = new JPasswordField();
		txtNewPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				 if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				      txtConfirmPassword.requestFocus();
				   }
				
			}
		});
		txtNewPassword.setBounds(206, 83, 152, 28);
		panel.add(txtNewPassword);
		
		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.setBounds(206, 116, 152, 28);
		panel.add(txtConfirmPassword);
		
		JLabel lblConfirmNewPassword = new JLabel("Confirm New Password:");
		lblConfirmNewPassword.setBounds(48, 123, 158, 14);
		panel.add(lblConfirmNewPassword);
		
		JButton btnNewButton = new JButton("Change Password");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				validatePassword(userID, txtTempPassword.getText().trim());
				
			}
		});
		btnNewButton.setBounds(94, 156, 221, 28);
		panel.add(btnNewButton);
		this.setLocationRelativeTo(null);
	}
	
	
private boolean validatePassword(String username, String pwd) {
		
		try {
		
			
			String query = "select * from tblUser where userID=?";
	
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				
				if (rs.getString("Password").toUpperCase().equals(pwd.toUpperCase())) {
					
					
					if (validatePWD()) {
						
						changePassword(username, txtConfirmPassword.getText().trim());
						
						FrmLogin frame = new FrmLogin();
						frame.setVisible(true);
						dispose();
						
					}
					
					
				} else {
					
					JOptionPane.showMessageDialog(null, "Incorrect temporary password..");
					
				}
			
			}

			pst.close();
			rs.close();
	
			
		} catch (Exception ex) {
			
			JOptionPane.showMessageDialog(null, ex.toString());
			
		}
		
		return false;
		
	}


@SuppressWarnings("deprecation")
private boolean validatePWD() {
	
	if (txtNewPassword.getText().trim().length() < 5) {
		
		JOptionPane.showMessageDialog(null, "Password length must be 5 characters or more..","Change password..",JOptionPane.WARNING_MESSAGE);
		txtNewPassword.requestFocus();
		return false;
		
	} else if (!txtNewPassword.getText().trim().equals(txtConfirmPassword.getText().trim())) {
		
		JOptionPane.showMessageDialog(null, "Password doesn't match!","Change password..",JOptionPane.WARNING_MESSAGE);
		txtConfirmPassword.requestFocus();
		return false;
		
	}
	
	return true;
}

private void changePassword(String uName, String newPwd) {
	
	try {
		
		String query = "update tblUser set Password = ?,changedPassword = '0' where UserID = ?";
		PreparedStatement pst2 = conn.prepareStatement(query);
		pst2.setString(1, newPwd);
		pst2.setString(2, uName);
	
		pst2.executeUpdate();
		pst2.close();
		
		JOptionPane.showMessageDialog(null, "Password successfully changed!","Change password..",JOptionPane.INFORMATION_MESSAGE);
		
	} catch (Exception ex) {
		
		
	}
	
}

}
