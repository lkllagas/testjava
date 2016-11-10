package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.toedter.calendar.JDateChooser;

import controller.sqliteConnection;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class frmClaimTagging extends JFrame {

	private JPanel contentPane;
	private JTextField txtCheckNumber;
	private JTextField txtPayeeName;
	private JTextField txtORNo;
	private String checkNum;
	private String payee;
	private String checkDate;
	private String checkAmount;
	private JTextField txtClaimedBy;
	JDateChooser dateChooser = new JDateChooser();
	/**
	 * Launch the application.
	 */
	Connection conn = sqliteConnection.dbConnector();
	private JTextField txtIDPresented;
	private JTextField txtCheckDate;
	private JTextField txtAmount;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmClaimTagging frame = new frmClaimTagging();
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
	 private Date DateNow(String dateFormat) {
		   
		    Calendar cal = Calendar.getInstance();
		    return (Date)(cal.getTime());

	  }
	
	 private static String now(String dateFormat) {
		   
		    Calendar cal = Calendar.getInstance();
		    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		    return sdf.format(cal.getTime());

		  }
	 
	public frmClaimTagging() {
		setTitle("Tagging of Claiming Checks");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		initialize();
	}
	
	public frmClaimTagging(String checkNumber, String payeeName, String checkDate, String checkAmt) throws ParseException {
		
		this.setCheckNum(checkNumber);
		this.setPayee(payeeName);
		this.setCheckDate(checkDate);
		this.setCheckAmount(checkAmt);
		
		initialize();

		txtCheckNumber.setText(getCheckNum());
		txtPayeeName.setText(getPayee());
		txtCheckDate.setText(getCheckDate());
		txtAmount.setText(getCheckAmount());
		
		dateChooser.setDate(this.DateNow("MMM dd, yyyy"));
	
	}
	
	public String getCheckNum() {
		return this.checkNum;
	}

	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}

	public String getPayee() {
		return this.payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}
	
	private void initialize() {
		setResizable(false);
		setBounds(100, 100, 450, 371);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(6, 6, 432, 328);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblCheckNumber = new JLabel("Check Number:");
		lblCheckNumber.setBounds(25, 49, 97, 16);
		panel.add(lblCheckNumber);
		
		txtCheckNumber = new JTextField();
		txtCheckNumber.setEditable(false);
		txtCheckNumber.setBounds(123, 43, 162, 28);
		panel.add(txtCheckNumber);
		txtCheckNumber.setColumns(10);
		
		JLabel lblPayeeName = new JLabel("Payee Name:");
		lblPayeeName.setBounds(25, 15, 86, 16);
		panel.add(lblPayeeName);
		
		txtPayeeName = new JTextField();
		txtPayeeName.setEditable(false);
		txtPayeeName.setColumns(10);
		txtPayeeName.setBounds(123, 9, 288, 28);
		panel.add(txtPayeeName);
		
		JLabel lblOrNo = new JLabel("OR No.:");
		lblOrNo.setBounds(25, 151, 86, 16);
		panel.add(lblOrNo);
		
		txtORNo = new JTextField();
		txtORNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
			
				 if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 txtClaimedBy.requestFocus();
				   }
			}
		});
		txtORNo.setColumns(10);
		txtORNo.setBounds(123, 145, 162, 28);
		panel.add(txtORNo);
		txtORNo.setVisible(true);
		
		JLabel lblClaimedBy = new JLabel("Claimed By:");
		lblClaimedBy.setBounds(25, 185, 86, 16);
		panel.add(lblClaimedBy);
		
		txtClaimedBy = new JTextField();
		txtClaimedBy.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				 if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 txtIDPresented.requestFocus();
				   }
				
			}
		});
		txtClaimedBy.setColumns(10);
		txtClaimedBy.setBounds(123, 179, 162, 28);
		panel.add(txtClaimedBy);
		dateChooser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
		});
		dateChooser.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				 if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 onButtonPress();
				   }
				 
			}
		});
		
		
		dateChooser.getDateEditor().addPropertyChangeListener(
			    new PropertyChangeListener() {
			        public void propertyChange(PropertyChangeEvent e) {
			            if ("date".equals(e.getPropertyName())) {
			            	
			            	Calendar today = Calendar.getInstance();
			            	Date todaysDate;
			            	Date validateDate;
			            	
			            	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			        		String todayDate = sdf.format(today.getTime());
			        		String objDate = sdf.format(e.getNewValue());
			            	
			        		
			        	
			        	    DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
			        	    
			        	   
			        	    try {
								todaysDate = df.parse(todayDate);
								validateDate = df.parse(objDate);
								  if (validateDate.after(todaysDate)) {
					        	    	
					        	    	JOptionPane.showMessageDialog(null, "Invalid Claimed Date!");
					        	    	dateChooser.setDate(new Date());
					        	    	return;
					        	    	
					        	    }
								
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			        	    
		        	        
			        	  
			         
			            }
			        }

					
			    });
		
		
		dateChooser.setBounds(123, 247, 162, 28);
		panel.add(dateChooser);
		
		JLabel lblClaimedDate = new JLabel("Claimed Date:");
		lblClaimedDate.setBounds(25, 249, 86, 16);
		panel.add(lblClaimedDate);
		
		txtPayeeName.requestFocus();
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				onButtonPress();
				
			}
		});
		btnSave.setMnemonic('S');
		btnSave.setBounds(123, 287, 73, 28);
		panel.add(btnSave);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				frmChecksforClaiming frame = new frmChecksforClaiming();
				frame.setVisible(true);
				
				dispose();
				
			}
		});
		btnClose.setMnemonic('C');
		btnClose.setBounds(212, 287, 73, 28);
		panel.add(btnClose);
		
		JLabel lblIdPresented = new JLabel("ID Presented:");
		lblIdPresented.setBounds(25, 219, 86, 16);
		panel.add(lblIdPresented);
		
		txtIDPresented = new JTextField();
		
		this.setLocationRelativeTo(null);
		
		 AutoSuggestor autoSuggestor = new AutoSuggestor(txtIDPresented, this, null, Color.WHITE.brighter(), Color.BLACK, Color.RED, 0.75f) {
	            @Override
	            boolean wordTyped(String typedWord) {

	                //create list for dictionary this in your case might be done via calling a method which queries db and returns results as arraylist
	                ArrayList<String> words = new ArrayList<>();
	                words.add("Passport");
	                words.add("Driver's License");
	                words.add("PRC ID");
	                words.add("NBI Clearance");
	                words.add("Police Clearance");
	                words.add("Postal ID");
	                words.add("Voter's ID");
	                words.add("Barangay Certificate");
	                words.add("GSIS E-Card");
	                words.add("SSS Card");
	                words.add("Senior Citizen Card");
	                words.add("OWWA ID");
	                words.add("OFW ID");
	                words.add("Seaman's Book");
	                words.add("Alien Certification of Registration");
	                words.add("Immigrant Certificate of Registration");
	                words.add("AFP ID");
	                words.add("HDMF ID");
	                words.add("NCWDP Certificate");
	                words.add("DSWD Certificate");
	                words.add("Integrated Bar of the Philippines ID");
	                words.add("Company ID");
	                
	                setDictionary(words);
	                //addToDictionary("bye");//adds a single word

	                return super.wordTyped(typedWord);//now call super to check for any matches against newest dictionary
	            }
	        };
		
		txtIDPresented.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
				 if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					 dateChooser.requestFocus();
				   }
				
			}
		});
		txtIDPresented.setColumns(10);
		txtIDPresented.setBounds(123, 213, 162, 28);
		panel.add(txtIDPresented);
		
		JLabel lblCheckDate = new JLabel("Check Date:");
		lblCheckDate.setBounds(25, 83, 97, 16);
		panel.add(lblCheckDate);
		
		txtCheckDate = new JTextField();
		txtCheckDate.setEditable(false);
		txtCheckDate.setColumns(10);
		txtCheckDate.setBounds(123, 77, 162, 28);
		panel.add(txtCheckDate);
		
		JLabel lblCheckAmount = new JLabel("Check Amount:");
		lblCheckAmount.setBounds(25, 117, 97, 16);
		panel.add(lblCheckAmount);
		
		txtAmount = new JTextField();
		txtAmount.setEditable(false);
		txtAmount.setColumns(10);
		txtAmount.setBounds(123, 111, 162, 28);
		panel.add(txtAmount);
	
		
	}
	
	//set focus
	@Override
	public void setVisible(boolean value) {
	    super.setVisible(value);
	    txtORNo.requestFocusInWindow();
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckAmount() {
		return checkAmount;
	}

	public void setCheckAmount(String checkAmount) {
		this.checkAmount = checkAmount;
	}
	
	private void onButtonPress() {
		

		try {
			
			if (txtORNo.getText().trim().isEmpty()) {
				
				JOptionPane.showMessageDialog(null, "OR No. Field must not be empty!","OR No.",JOptionPane.WARNING_MESSAGE);
				txtORNo.requestFocus();
				
				
			} else if (txtClaimedBy.getText().trim().isEmpty()) {
				
				JOptionPane.showMessageDialog(null, "Claimed By Field must not be empty!","Claimed By",JOptionPane.WARNING_MESSAGE);
				txtClaimedBy.requestFocus();
			
			} else if (txtIDPresented.getText().trim().isEmpty()) {
				
				JOptionPane.showMessageDialog(null, "ID Presented Field must not be empty!","ID Presented",JOptionPane.WARNING_MESSAGE);
				txtIDPresented.requestFocus();
				
			} else if (dateChooser.getDate() == null) {
					
					JOptionPane.showMessageDialog(null, "Claimed date must not be empty!","Claimed Date",JOptionPane.WARNING_MESSAGE);
					dateChooser.requestFocus();
					
			} else {
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				String formattedDate = sdf.format(dateChooser.getDate());
				
				
				String query = "update tblcheck set ORNo = ?,ClaimedBy = ?,ClaimedDate = ?,ClaimedStamp = ?,ClaimedID = ?,Status = 'Claimed' where CheckNumber = ? and PayeeName = ? and CheckAmount = ?";
				PreparedStatement pst = conn.prepareStatement(query);
				pst.setString(1, txtORNo.getText());
				pst.setString(2, txtClaimedBy.getText());
				pst.setString(3, formattedDate);
				pst.setString(4, now("MM/dd/yyyy hh:mm:ss"));
				pst.setString(5, txtIDPresented.getText());
				pst.setString(6, txtCheckNumber.getText());
				pst.setString(7, txtPayeeName.getText());
				pst.setString(8, txtAmount.getText());
				pst.executeUpdate();
				
				pst.close();
				
				frmChecksforClaiming frame = new frmChecksforClaiming();
				frame.setVisible(true);
				
				dispose();
			
			}
		
		}  catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex.toString(),"Error Updating Database..",JOptionPane.WARNING_MESSAGE);
		}

		
	}
	
	
	class AutoSuggestor {

	    private final JTextField textField;
	    private final Window container;
	    private JPanel suggestionsPanel;
	    private JWindow autoSuggestionPopUpWindow;
	    private String typedWord;
	    private final ArrayList<String> dictionary = new ArrayList<>();
	    private int currentIndexOfSpace, tW, tH;
	    private DocumentListener documentListener = new DocumentListener() {
	        @Override
	        public void insertUpdate(DocumentEvent de) {
	            checkForAndShowSuggestions();
	        }

	        @Override
	        public void removeUpdate(DocumentEvent de) {
	            checkForAndShowSuggestions();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent de) {
	            checkForAndShowSuggestions();
	        }
	    };
	    private final Color suggestionsTextColor;
	    private final Color suggestionFocusedColor;

	    public AutoSuggestor(JTextField textField, Window mainWindow, ArrayList<String> words, Color popUpBackground, Color textColor, Color suggestionFocusedColor, float opacity) {
	        this.textField = textField;
	        this.suggestionsTextColor = textColor;
	        this.container = mainWindow;
	        this.suggestionFocusedColor = suggestionFocusedColor;
	        this.textField.getDocument().addDocumentListener(documentListener);

	        setDictionary(words);

	        typedWord = "";
	        currentIndexOfSpace = 0;
	        tW = 0;
	        tH = 0;

	        autoSuggestionPopUpWindow = new JWindow(mainWindow);
	        autoSuggestionPopUpWindow.setOpacity(opacity);

	        suggestionsPanel = new JPanel();
	        suggestionsPanel.setLayout(new GridLayout(0, 1));
	        suggestionsPanel.setBackground(popUpBackground);

	        addKeyBindingToRequestFocusInPopUpWindow();
	    }

	    private void addKeyBindingToRequestFocusInPopUpWindow() {
	        textField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
	        textField.getActionMap().put("Down released", new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent ae) {//focuses the first label on popwindow
	                for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
	                    if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
	                        ((SuggestionLabel) suggestionsPanel.getComponent(i)).setFocused(true);
	                        autoSuggestionPopUpWindow.toFront();
	                        autoSuggestionPopUpWindow.requestFocusInWindow();
	                        suggestionsPanel.requestFocusInWindow();
	                        suggestionsPanel.getComponent(i).requestFocusInWindow();
	                        break;
	                    }
	                }
	            }
	        });
	        suggestionsPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down released");
	        suggestionsPanel.getActionMap().put("Down released", new AbstractAction() {
	            int lastFocusableIndex = 0;

	            @Override
	            public void actionPerformed(ActionEvent ae) {//allows scrolling of labels in pop window (I know very hacky for now :))

	                ArrayList<SuggestionLabel> sls = getAddedSuggestionLabels();
	                int max = sls.size();

	                if (max > 1) {//more than 1 suggestion
	                    for (int i = 0; i < max; i++) {
	                        SuggestionLabel sl = sls.get(i);
	                        if (sl.isFocused()) {
	                            if (lastFocusableIndex == max - 1) {
	                                lastFocusableIndex = 0;
	                                sl.setFocused(false);
	                                autoSuggestionPopUpWindow.setVisible(false);
	                                setFocusToTextField();
	                                checkForAndShowSuggestions();//fire method as if document listener change occured and fired it

	                            } else {
	                                sl.setFocused(false);
	                                lastFocusableIndex = i;
	                            }
	                        } else if (lastFocusableIndex <= i) {
	                            if (i < max) {
	                                sl.setFocused(true);
	                                autoSuggestionPopUpWindow.toFront();
	                                autoSuggestionPopUpWindow.requestFocusInWindow();
	                                suggestionsPanel.requestFocusInWindow();
	                                suggestionsPanel.getComponent(i).requestFocusInWindow();
	                                lastFocusableIndex = i;
	                                break;
	                            }
	                        }
	                    }
	                } else {//only a single suggestion was given
	                    autoSuggestionPopUpWindow.setVisible(false);
	                    setFocusToTextField();
	                    checkForAndShowSuggestions();//fire method as if document listener change occured and fired it
	                }
	            }
	        });
	    }

	    private void setFocusToTextField() {
	        container.toFront();
	        container.requestFocusInWindow();
	        textField.requestFocusInWindow();
	    }

	    public ArrayList<SuggestionLabel> getAddedSuggestionLabels() {
	        ArrayList<SuggestionLabel> sls = new ArrayList<>();
	        for (int i = 0; i < suggestionsPanel.getComponentCount(); i++) {
	            if (suggestionsPanel.getComponent(i) instanceof SuggestionLabel) {
	                SuggestionLabel sl = (SuggestionLabel) suggestionsPanel.getComponent(i);
	                sls.add(sl);
	            }
	        }
	        return sls;
	    }

	    private void checkForAndShowSuggestions() {
	        typedWord = getCurrentlyTypedWord();

	        suggestionsPanel.removeAll();//remove previos words/jlabels that were added

	        //used to calcualte size of JWindow as new Jlabels are added
	        tW = 0;
	        tH = 0;

	        boolean added = wordTyped(typedWord);

	        if (!added) {
	            if (autoSuggestionPopUpWindow.isVisible()) {
	                autoSuggestionPopUpWindow.setVisible(false);
	            }
	        } else {
	            showPopUpWindow();
	            setFocusToTextField();
	        }
	    }

	    protected void addWordToSuggestions(String word) {
	        SuggestionLabel suggestionLabel = new SuggestionLabel(word, suggestionFocusedColor, suggestionsTextColor, this);

	        calculatePopUpWindowSize(suggestionLabel);

	        suggestionsPanel.add(suggestionLabel);
	    }

	    public String getCurrentlyTypedWord() {//get newest word after last white spaceif any or the first word if no white spaces
	        String text = textField.getText();
	        String wordBeingTyped = "";
	        if (text.contains(" ")) {
	            int tmp = text.lastIndexOf(" ");
	            if (tmp >= currentIndexOfSpace) {
	                currentIndexOfSpace = tmp;
	                wordBeingTyped = text.substring(text.lastIndexOf(" "));
	            }
	        } else {
	            wordBeingTyped = text;
	        }
	        return wordBeingTyped.trim();
	    }

	    private void calculatePopUpWindowSize(JLabel label) {
	        //so we can size the JWindow correctly
	        if (tW < label.getPreferredSize().width) {
	            tW = label.getPreferredSize().width;
	        }
	        tH += label.getPreferredSize().height;
	    }

	    private void showPopUpWindow() {
	        autoSuggestionPopUpWindow.getContentPane().add(suggestionsPanel);
	        autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textField.getWidth(), 30));
	        autoSuggestionPopUpWindow.setSize(tW, tH);
	        autoSuggestionPopUpWindow.setVisible(true);

	        int windowX = 0;
	        int windowY = 0;

	        windowX = container.getX() + textField.getX() + 5;
	        if (suggestionsPanel.getHeight() > autoSuggestionPopUpWindow.getMinimumSize().height) {
	            windowY = container.getY() + textField.getY() + textField.getHeight() + autoSuggestionPopUpWindow.getMinimumSize().height;
	        } else {
	            windowY = container.getY() + textField.getY() + textField.getHeight() + autoSuggestionPopUpWindow.getHeight();
	        }

	        autoSuggestionPopUpWindow.setLocation(windowX, windowY);
	        autoSuggestionPopUpWindow.setMinimumSize(new Dimension(textField.getWidth(), 30));
	        autoSuggestionPopUpWindow.revalidate();
	        autoSuggestionPopUpWindow.repaint();

	    }

	    public void setDictionary(ArrayList<String> words) {
	        dictionary.clear();
	        if (words == null) {
	            return;//so we can call constructor with null value for dictionary without exception thrown
	        }
	        for (String word : words) {
	            dictionary.add(word);
	        }
	    }

	    public JWindow getAutoSuggestionPopUpWindow() {
	        return autoSuggestionPopUpWindow;
	    }

	    public Window getContainer() {
	        return container;
	    }

	    public JTextField getTextField() {
	        return textField;
	    }

	    public void addToDictionary(String word) {
	        dictionary.add(word);
	    }

	    boolean wordTyped(String typedWord) {

	        if (typedWord.isEmpty()) {
	            return false;
	        }
	        //System.out.println("Typed word: " + typedWord);

	        boolean suggestionAdded = false;

	        for (String word : dictionary) {//get words in the dictionary which we added
	            boolean fullymatches = true;
	            for (int i = 0; i < typedWord.length(); i++) {//each string in the word
	                if (!typedWord.toLowerCase().startsWith(String.valueOf(word.toLowerCase().charAt(i)), i)) {//check for match
	                    fullymatches = false;
	                    break;
	                }
	            }
	            if (fullymatches) {
	                addWordToSuggestions(word);
	                suggestionAdded = true;
	            }
	        }
	        return suggestionAdded;
	    }
	}
	
	class SuggestionLabel extends JLabel {

	    private boolean focused = false;
	    private final JWindow autoSuggestionsPopUpWindow;
	    private final JTextField textField;
	    private final AutoSuggestor autoSuggestor;
	    private Color suggestionsTextColor, suggestionBorderColor;

	    public SuggestionLabel(String string, final Color borderColor, Color suggestionsTextColor, AutoSuggestor autoSuggestor) {
	        super(string);

	        this.suggestionsTextColor = suggestionsTextColor;
	        this.autoSuggestor = autoSuggestor;
	        this.textField = autoSuggestor.getTextField();
	        this.suggestionBorderColor = borderColor;
	        this.autoSuggestionsPopUpWindow = autoSuggestor.getAutoSuggestionPopUpWindow();

	        initComponent();
	    }

	    private void initComponent() {
	        setFocusable(true);
	        setForeground(suggestionsTextColor);

	        addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent me) {
	                super.mouseClicked(me);

	                replaceWithSuggestedText();

	                autoSuggestionsPopUpWindow.setVisible(false);
	            }
	        });

	        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "Enter released");
	        getActionMap().put("Enter released", new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent ae) {
	                replaceWithSuggestedText();
	                autoSuggestionsPopUpWindow.setVisible(false);
	            }
	        });
	    }

	    public void setFocused(boolean focused) {
	        if (focused) {
	            setBorder(new LineBorder(suggestionBorderColor));
	        } else {
	            setBorder(null);
	        }
	        repaint();
	        this.focused = focused;
	    }

	    public boolean isFocused() {
	        return focused;
	    }

	    private void replaceWithSuggestedText() {
	        String suggestedWord = getText();
	        String text = textField.getText();
	        String typedWord = autoSuggestor.getCurrentlyTypedWord();
	        String t = text.substring(0, text.lastIndexOf(typedWord));
	        String tmp = t + text.substring(text.lastIndexOf(typedWord)).replace(typedWord, suggestedWord);
	        textField.setText(tmp + " ");
	    }
	}

	}




