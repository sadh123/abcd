package librarysystem;


//Generated by GuiGenie - Copyright (c) 2004 Mario Awad.
//Home Page http://guigenie.cjb.net - Check often for new versions!

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.event.*;

import business.LoginException;
import business.SystemController;

public class frmLogin extends JPanel {
  private JButton jcomp1;
  private JLabel lblTitle;
  private JLabel lblUserName;
  private JLabel jcomp4;
  private JTextField jcomp5;
  private JPasswordField jcomp6;
  private JButton btnCancel;

  public frmLogin() {
      //construct components
      jcomp1 = new JButton ("Login");
      lblTitle = new JLabel ("Library System Login");
      lblUserName = new JLabel ("User Name");
      jcomp4 = new JLabel ("Password");
      jcomp5 = new JTextField (5);
      jcomp6 = new JPasswordField (5);
      btnCancel = new JButton ("Cancel");

      //adjust size and set layout
      setPreferredSize (new Dimension (341, 175));
      setLayout (null);

      //add components
      add (jcomp1);
      add (lblTitle);
      add (lblUserName);
      add (jcomp4);
      add (jcomp5);
      add (jcomp6);
      add (btnCancel);
Util.adjustLabelFont(lblTitle,Color.blue,true);
      //set component bounds (only needed by Absolute Positioning)
      jcomp1.setBounds (195, 115, 95, 25);
      lblTitle.setBounds (120, 5, 185, 25);
      lblUserName.setBounds (25, 50, 70, 25);
      jcomp4.setBounds (30, 80, 65, 25);
      jcomp5.setBounds (95, 50, 195, 25);
      jcomp6.setBounds (95, 80, 195, 25);
      btnCancel.setBounds (95, 115, 90, 25);
      jcomp1.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			SystemController systemController = new SystemController();
			boolean isValidUsername = false;
			boolean isValidPassword = false;
			
			try {
				
				if (isValidUsername(jcomp5.getText().toString())) {
    				isValidUsername = true;
				}else {
					//JOptionPane.showMessageDialog(this, "User should be small letter or number", TOOL_TIP_TEXT_KEY, ABORT);
					JOptionPane.showMessageDialog(jcomp1,"Username should be only small letter or number!","Error",JOptionPane.WARNING_MESSAGE);
					//JOptionPane.showMessageDialog(f,"Successfully Updated.","Alert",JOptionPane.WARNING_MESSAGE);
					System.out.println("Username should be only small letter or number!");
				}
    			
    			if (isValidUsername) {
					if (isValidPassword(jcomp6.getText().toString())) {
	    				isValidPassword = true;
					}else {
						JOptionPane.showMessageDialog(jcomp1,"Password should be only number or letter!");
	    				System.out.println("Password should be only number or letter!");
					}
				}
    			
    			
    			if (isValidUsername && isValidPassword) {
    				
    				systemController.login(jcomp5.getText().toString(), jcomp6.getText().toString());
    				
					if (SystemController.currentAuth != null) {
        				LibrarySystem.INSTANCE.setTitle("Library Application");
        				LibrarySystem.INSTANCE.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        				LibrarySystem.INSTANCE.init();
        				Util.centerFrameOnDesktop(LibrarySystem.INSTANCE);
        				LibrarySystem.INSTANCE.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(jcomp1,"Username or Password mismatch!");
					}
				}
    			
			} catch (LoginException ex) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(jcomp1,ex.getMessage());
			}
			
		}
	});
  }
  public static void centerFrameOnDesktop(Component f) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int height = toolkit.getScreenSize().height;
		int width = toolkit.getScreenSize().width;
		int frameHeight = f.getSize().height;
		int frameWidth = f.getSize().width;
		f.setLocation(((width - frameWidth) /2)-200, (height - frameHeight) / 3);
	}
  static boolean isValidUsername(String username) {
      String regex = "^[a-z0-9_]{3,16}$";
      Pattern pattern = Pattern.compile(regex);
      return pattern.matcher(username).matches();
  }
	
	public static boolean isValidPassword(String password) {
      String regex = "^[0-9a-zA-Z]+$";
      Pattern pattern = Pattern.compile(regex);
      return pattern.matcher(password).matches();
  }
  public static void main (String[] args) {
      JFrame frame = new JFrame ("Login");
      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add (new frmLogin());
      centerFrameOnDesktop(frame);
      frame.setSize	(300,200);
      frame.setResizable(false);
      frame.pack();
      frame.setVisible (true);
  }
}

