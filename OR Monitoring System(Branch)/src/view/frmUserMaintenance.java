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
import model.User;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class frmUserMaintenance extends JFrame {

	private JPanel contentPane;
	private JTextField txtFirstName;
	private JTextField txtLastName;
	private JTextField txtAnswer;
	private JTextField txtUserID;
	private JTextField txtMiddleName;
	private JPasswordField txtPassword;
	private JPasswordField txtConfirmPassword;
	private JComboBox cmbSecretQuestion;
	User user = new User();
	
	 Connection conn = sqliteConnection.dbConnector();
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmUserMaintenance frame = new frmUserMaintenance();
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
	public frmUserMaintenance() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				
				frmMainMenu frame = new frmMainMenu();
				frame.setVisible(true);
				dispose();
			}
		});
		setTitle("Branch User Creation");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 397, 348);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Creating User", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 370, 298);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setBounds(13, 58, 90, 14);
		panel.add(lblFirstName);
		
		txtFirstName = new JTextField();
		txtFirstName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				
				
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtMiddleName.requestFocus();
					 
				}
				
			}
		});
		txtFirstName.setBounds(113, 55, 112, 20);
		panel.add(txtFirstName);
		txtFirstName.setColumns(10);
		
		txtLastName = new JTextField();
		txtLastName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				generateUserID();
				
			}
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtPassword.requestFocus();
					 
				}
				
			}
		});
		txtLastName.setColumns(10);
		txtLastName.setBounds(113, 110, 112, 20);
		panel.add(txtLastName);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(13, 113, 90, 14);
		panel.add(lblLastName);
		txtFirstName.requestFocus();
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				onButtonPress();
				
			}
		});
		btnSave.setMnemonic('S');
		btnSave.setBounds(172, 257, 89, 23);
		panel.add(btnSave);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				clearFields();
				
			}
		});
		btnClear.setMnemonic('C');
		btnClear.setBounds(271, 257, 89, 23);
		panel.add(btnClear);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(13, 145, 90, 14);
		panel.add(lblPassword);
		
		JLabel lblConfirmPassword = new JLabel("Confirm PWD:");
		lblConfirmPassword.setBounds(13, 173, 120, 14);
		panel.add(lblConfirmPassword);
		
		JLabel label_1 = new JLabel("Secret Question:");
		label_1.setBounds(13, 201, 101, 14);
		panel.add(label_1);
		
		cmbSecretQuestion = new JComboBox();
		cmbSecretQuestion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtAnswer.requestFocus();
					 
				}
				
			}
		});
		cmbSecretQuestion.setModel(new DefaultComboBoxModel(new String[] {"What's your mother's maiden name?", "What is your father's first name?", "What is your favorite color?", "What was your childhood nickname?", "What was the name of your first pet?", "Who is your favorite superhero?", "In what province were you born?", "Where did you meet your spouse?"}));
		cmbSecretQuestion.setToolTipText("This is for resetting of password");
		cmbSecretQuestion.setBounds(113, 198, 247, 20);
		panel.add(cmbSecretQuestion);
		
		JLabel lblAnswer = new JLabel("Answer:");
		lblAnswer.setBounds(13, 229, 101, 14);
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
		txtAnswer.setBounds(113, 226, 247, 20);
		panel.add(txtAnswer);
		
		JLabel label = new JLabel("User ID:");
		label.setBounds(13, 30, 90, 14);
		panel.add(label);
		
		txtUserID = new JTextField();
		txtUserID.setEnabled(false);
		txtUserID.setColumns(10);
		txtUserID.setBounds(113, 27, 112, 20);
		panel.add(txtUserID);
		
		JLabel label_2 = new JLabel("Middle Name:");
		label_2.setBounds(13, 84, 90, 14);
		panel.add(label_2);
		
		txtMiddleName = new JTextField();
		txtMiddleName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtLastName.requestFocus();
					 
				}
				
			}
		});
		txtMiddleName.setColumns(10);
		txtMiddleName.setBounds(113, 81, 112, 20);
		panel.add(txtMiddleName);
		
		txtPassword = new JPasswordField();
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					txtConfirmPassword.requestFocus();
					 
				}
				
			}
		});
		txtPassword.setBounds(113, 142, 112, 20);
		panel.add(txtPassword);
		
		txtConfirmPassword = new JPasswordField();
		txtConfirmPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					cmbSecretQuestion.requestFocus();
					 
				}
				
			}
		});
		txtConfirmPassword.setBounds(113, 169, 112, 20);
		panel.add(txtConfirmPassword);
		
		this.setLocationRelativeTo(null);
	}
	
	private void generateUserID() {
		
		
		txtUserID.setText(txtFirstName.getText().substring(0,1).toUpperCase() +txtMiddleName.getText().substring(0,1).toUpperCase() + txtLastName.getText().toUpperCase());
		
	}
	
	
	private boolean validateFields() {
		
		if (txtFirstName.getText().trim().isEmpty()) {
				
				JOptionPane.showMessageDialog(null, "First name field must not be empty!","Blank First name",JOptionPane.WARNING_MESSAGE);
				txtFirstName.requestFocus();
				return false;
			
		} else if (txtMiddleName.getText().trim().isEmpty()) {
			
				JOptionPane.showMessageDialog(null, "Middle name field must not be empty!","Blank Middle name",JOptionPane.WARNING_MESSAGE);
				txtMiddleName.requestFocus();
				return false;
			
		}  else if (txtLastName.getText().trim().isEmpty()) {
			
				JOptionPane.showMessageDialog(null, "Last name field must not be empty!","Blank Last name",JOptionPane.WARNING_MESSAGE);
				txtLastName.requestFocus();
				return false;
			
		} 	else if (txtPassword.getText().trim().isEmpty()) {
					
				JOptionPane.showMessageDialog(null, "Password field must not be empty!","Blank Password",JOptionPane.WARNING_MESSAGE);
				txtPassword.requestFocus();
				return false;
	
		}	else if (txtConfirmPassword.getText().trim().isEmpty()) {
			
				JOptionPane.showMessageDialog(null, "Confirm Password field must not be empty!","Blank Confirm Password",JOptionPane.WARNING_MESSAGE);
				txtPassword.requestFocus();
				return false;

		}  else if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
			
				JOptionPane.showMessageDialog(null, "Password does not match the confirm password!","Password doesn't match",JOptionPane.WARNING_MESSAGE);
				txtConfirmPassword.requestFocus();
				return false;
				
		} 	else if (txtAnswer.getText().trim().isEmpty()) {
			
				JOptionPane.showMessageDialog(null, "Secret Question must be answered!","Blank Answer..",JOptionPane.WARNING_MESSAGE);
				txtAnswer.requestFocus();
				return false;
			
		} 	else if (txtConfirmPassword.getText().length() < 5) {
			
				JOptionPane.showMessageDialog(null, "Password must be 5 characters or more!","Password Length..",JOptionPane.WARNING_MESSAGE);
				txtPassword.requestFocus();
				return false;
			
		}

		return true;
	}
	
	private void assignFields(String userID, String fName, String mName, String lName, String question, String ans, String pWord) {

		user.setFirstName(fName);
		user.setMiddleName(mName);
		user.setLastName(lName);
		user.setSecretQuestion(question);
		user.setAnswer(ans);
		user.setpWord(pWord);
		user.setUserID(userID);
		
	}
	
	private void clearFields() {
		
		txtAnswer.setText(null);
		txtFirstName.setText(null);
		txtMiddleName.setText(null);
		txtLastName.setText(null);
		txtPassword.setText(null);
		txtConfirmPassword.setText(null);
		txtUserID.setText(null);
		txtFirstName.requestFocus();
		
	}
	
	private void onButtonPress() {
		
		if(validateFields()) {
			
			assignFields(txtUserID.getText().trim(), txtFirstName.getText().trim(), txtMiddleName.getText().trim(), txtLastName.getText().trim(), cmbSecretQuestion.getSelectedItem().toString(), txtAnswer.getText().trim(), txtConfirmPassword.getText().trim());
			
			validation vd = new validation();
			
			try {
				
				if (vd.validateUserID(user.getUserID())) {

	                String query2 = "insert into tblUser (userID, firstName, MiddleName, LastName, SecretQuestion, Answer, Password,changedPassword) values (?,?,?,?,?,?,?,?)";
					PreparedStatement pst2 = conn.prepareStatement(query2);
	            	pst2.setString(1, user.getUserID());
	                pst2.setString(2, user.getFirstName());
	                pst2.setString(3, user.getMiddleName());
	                pst2.setString(4, user.getLastName());
	                pst2.setString(5, user.getSecretQuestion());
	                pst2.setString(6, user.getAnswer());
	                pst2.setString(7, user.getpWord());
	                pst2.setString(8, "0");
	                pst2.execute();
	                
	                
	                
	                clearFields();
	                JOptionPane.showMessageDialog(null, "User " + user.getUserID() + " successfully added in the system!","Branch User Creation",JOptionPane.INFORMATION_MESSAGE);
	                
	                pst2.close();
	                
				} else {
	                
					clearFields();
					JOptionPane.showMessageDialog(null, "User ID [" +  user.getUserID() + "] already exists!","User ID Creation",JOptionPane.WARNING_MESSAGE);
					
				}
		            
			} catch (Exception ex) {
				
				JOptionPane.showMessageDialog(null, ex.toString());
				
			} 
			
//			finally {
//				
//				  if (conn != null) {
//					  
//					    try {
//					      conn.close(); // <-- This is important
//					    } catch (SQLException e) {
//					      /* handle exception */
//					    }
//				  }
//			}
			
            
		}
	}
	
	
}
