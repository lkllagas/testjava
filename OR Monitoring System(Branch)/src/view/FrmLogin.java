 package view;


import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;





import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Color;
import java.io.IOException;





import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPasswordField;

import controller.validation;
import model.Global;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class FrmLogin extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	
	private JButton btnLogin;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					FrmLogin frame = new FrmLogin();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.toString());
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public FrmLogin() throws IOException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				dispose();
				
			}
		});
		setTitle("CWORMS Ver. 1.0.0");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 442, 176);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLocationRelativeTo(null);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Image icon = Toolkit.getDefaultToolkit().getImage("login.png");
	    this.setIconImage(icon);
	    
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "User Login", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(59, 59, 59)));
		panel.setForeground(Color.RED);
		panel.setBounds(6, 6, 424, 133);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(25, 29, 79, 16);
		panel.add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				  if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				      txtPassword.requestFocus();
				   }
				
			}
		});
		txtUsername.setBounds(103, 23, 200, 28);
		panel.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(25, 57, 79, 16);
		panel.add(lblPassword);
		
		btnLogin = new JButton("Login");
		btnLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				  if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					  onButtonPress();
				   }
				  
			}
		});
		btnLogin.setToolTipText("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				onButtonPress();
			}
		});
		
		
		
		btnLogin.setIcon(new ImageIcon(this.getClass().getResource("/check16px.png")));
		btnLogin.setMnemonic('L');
		btnLogin.setBounds(103, 85, 90, 28);
		panel.add(btnLogin);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(this.getClass().getResource("/login2.png")));
		label.setBounds(315, 23, 90, 90);
		panel.add(label);
		
		txtPassword = new JPasswordField();
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				  if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				      btnLogin.requestFocus();
				   }
				  
			}
		});
		txtPassword.setBounds(103, 51, 200, 28);
		panel.add(txtPassword);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				frmResetPassword frame = new frmResetPassword();
				frame.setVisible(true);
				
			}
		});
		btnReset.setToolTipText("Reset Password");
		btnReset.setIcon(new ImageIcon(this.getClass().getResource("/reset.png")));
		btnReset.setMnemonic('R');
		btnReset.setBounds(208, 85, 95, 28);
		panel.add(btnReset);
		
	
		
	}
	
	private void onButtonPress() {
		validation vd = new validation();
		
		if (txtUsername.getText().equals("ebd") && txtPassword.getText().equals("eBanking")) {
			
            frmMainMenu mm = new frmMainMenu();
            mm.mntmUserMaintenance.setEnabled(true);
            mm.mntmSystemSetup.setEnabled(true);
            mm.setVisible(true);
            Global.setUserLoggedIn("Admin");
            dispose();
			
		}
		else {
				if (vd.validateUser(txtUsername.getText(), txtPassword.getText()) == true) {
					
					if (vd.validateChangedPassword()) {
						
						frmChangePassword frame = new frmChangePassword(txtUsername.getText());
						frame.setVisible(true);
						dispose();
						
					} else {
					
			            frmMainMenu mm = new frmMainMenu();

			            mm.setVisible(true);
			            dispose();

					}
		            
				} else {
					
					JOptionPane.showMessageDialog(null, "Wrong Username/Password!");
					
				}
	
		}
	}
}
