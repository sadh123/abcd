package librarysystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;

import business.ControllerInterface;
import business.LoginException;
import business.SystemController;
import dataaccess.User;


public class LoginWindow extends JFrame implements LibWindow {
    public static final LoginWindow INSTANCE = new LoginWindow();
	
	private boolean isInitialized = false;
	
	private JPanel mainPanel;
	private JPanel upperHalf;
	private JPanel middleHalf;
//	private JPanel lowerHalf;
	private JPanel container;
	
	private JPanel topPanel;
	private JPanel middlePanel;
	private JPanel lowerPanel;
	private JPanel leftTextPanel;
	private JPanel rightTextPanel;
	
	private JTextField username;
	private JTextField password;
	private JLabel label;
	private JButton loginButton;
	private JButton logoutButton;
	
	
	
	
	public boolean isInitialized() {
		return isInitialized;
	}
	public void isInitialized(boolean val) {
		isInitialized = val;
	}
	private JTextField messageBar = new JTextField();
	public void clear() {
		messageBar.setText("");
	}
	
	/* This class is a singleton */
    private LoginWindow () {}
    
    public void init() {     		
    		mainPanel = new JPanel();
    		defineUpperHalf();
    		defineMiddleHalf();
//    		defineLowerHalf();
    		BorderLayout bl = new BorderLayout();
    		bl.setVgap(30);
    		mainPanel.setLayout(bl);
    					
    		mainPanel.add(upperHalf, BorderLayout.NORTH);
    		mainPanel.add(middleHalf, BorderLayout.CENTER);
//    		mainPanel.add(lowerHalf, BorderLayout.SOUTH);
    		getContentPane().add(mainPanel);
    		isInitialized(true);
    		pack();
    		//setSize(660, 500);

    	
    }
    private void defineUpperHalf() {
    		
    		upperHalf = new JPanel();
    		upperHalf.setLayout(new BorderLayout());
    		defineTopPanel();
    		defineMiddlePanel();
    		defineLowerPanel();
    		upperHalf.add(topPanel, BorderLayout.NORTH);
    		upperHalf.add(middlePanel, BorderLayout.CENTER);
    		upperHalf.add(lowerPanel, BorderLayout.SOUTH);
    		
    	}
    	private void defineMiddleHalf() {
    		middleHalf = new JPanel();
    		middleHalf.setLayout(new BorderLayout());
    		JSeparator s = new JSeparator();
    		s.setOrientation(SwingConstants.HORIZONTAL);
    		//middleHalf.add(Box.createRigidArea(new Dimension(0,50)));
    		middleHalf.add(s, BorderLayout.SOUTH);
    		
    	}
//    	private void defineLowerHalf() {
//
//    		lowerHalf = new JPanel();
//    		lowerHalf.setLayout(new FlowLayout(FlowLayout.LEFT));
//    		
//    		JButton backButton = new JButton("<= Back to Main");
//    		addBackButtonListener(backButton);
//    		lowerHalf.add(backButton);
//    		
//    	}
    	private void defineTopPanel() {
    		topPanel = new JPanel();
    		JPanel intPanel = new JPanel(new BorderLayout());
    		intPanel.add(Box.createRigidArea(new Dimension(0,20)), BorderLayout.NORTH);
    		JLabel loginLabel = new JLabel("Login");
    		Util.adjustLabelFont(loginLabel, Color.BLUE.darker(), true);
    		intPanel.add(loginLabel, BorderLayout.CENTER);
    		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    		topPanel.add(intPanel);
    		
    	}
    	
    	
    	
    	private void defineMiddlePanel() {
    		middlePanel=new JPanel();
    		middlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    		defineLeftTextPanel();
    		defineRightTextPanel();
    		middlePanel.add(leftTextPanel);
    		middlePanel.add(rightTextPanel);
    	}
    	private void defineLowerPanel() {
    		lowerPanel = new JPanel();
    		loginButton = new JButton("Login");
    		addLoginButtonListener(loginButton);
    		lowerPanel.add(loginButton);
    	}

    	private void defineLeftTextPanel() {
    		
    		JPanel topText = new JPanel();
    		JPanel bottomText = new JPanel();
    		topText.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
    		bottomText.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));		
    		
    		username = new JTextField(10);
    		label = new JLabel("ID");
    		label.setFont(Util.makeSmallFont(label.getFont()));
    		topText.add(username);
    		bottomText.add(label);
    		
    		leftTextPanel = new JPanel();
    		leftTextPanel.setLayout(new BorderLayout());
    		leftTextPanel.add(topText,BorderLayout.NORTH);
    		leftTextPanel.add(bottomText,BorderLayout.CENTER);
    	}
    	private void defineRightTextPanel() {
    		
    		JPanel topText = new JPanel();
    		JPanel bottomText = new JPanel();
    		topText.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
    		bottomText.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));		
    		
    		password = new JPasswordField(10);
    		label = new JLabel("Password");
    		label.setFont(Util.makeSmallFont(label.getFont()));
    		topText.add(password);
    		bottomText.add(label);
    		
    		rightTextPanel = new JPanel();
    		rightTextPanel.setLayout(new BorderLayout());
    		rightTextPanel.add(topText,BorderLayout.NORTH);
    		rightTextPanel.add(bottomText,BorderLayout.CENTER);
    	}
    	
//    	private void addBackButtonListener(JButton butn) {
//    		butn.addActionListener(evt -> {
//    			LibrarySystem.hideAllWindows();
//    			LibrarySystem.INSTANCE.setVisible(true);
//    		});
//    	}
    	
    	private void addLoginButtonListener(JButton butn) {
    		butn.addActionListener(evt -> {
    			SystemController systemController = new SystemController();
    			boolean isValidUsername = false;
    			boolean isValidPassword = false;
    			
    			try {
    				
    				if (isValidUsername(username.getText().toString())) {
        				isValidUsername = true;
    				}else {
    					JOptionPane.showMessageDialog(this,"Username should be only small letter or number!");
        				System.out.println("Username should be only small letter or number!");
    				}
        			
        			if (isValidUsername) {
    					if (isValidPassword(password.getText().toString())) {
    	    				isValidPassword = true;
    					}else {
    						JOptionPane.showMessageDialog(this,"Password should be only number or letter!");
    	    				System.out.println("Password should be only number or letter!");
    					}
    				}
        			
        			
        			if (isValidUsername && isValidPassword) {
        				
        				systemController.login(username.getText().toString(), password.getText().toString());
        				
    					if (SystemController.currentAuth != null) {
	        				LibrarySystem.INSTANCE.setTitle("Group 7 Library Application");
	        				LibrarySystem.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        				LibrarySystem.INSTANCE.init();
	        				Util.centerFrameOnDesktop(LibrarySystem.INSTANCE);
	        				LibrarySystem.INSTANCE.setVisible(true);
    					}else {
    						JOptionPane.showMessageDialog(this,"Username or Password mismatch!");
    					}
    				}
        			
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(this,e.getMessage());
				}
    			
    		});
    	}
	
    	public static boolean isValidUsername(String username) {
            String regex = "^[a-z0-9_]{3,16}$";
            Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(username).matches();
        }
    	
    	public static boolean isValidPassword(String password) {
            String regex = "^[0-9a-zA-Z]+$";
            Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(password).matches();
        }
    	

    
}
