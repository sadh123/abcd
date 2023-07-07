package librarysystem;

import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import business.SystemController;
import dataaccess.Auth;

public class HomePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private String pathToImage;
	Image img=null;
	private LibrarySystem librarySystem;
	
	HomePanel(LibrarySystem ls) {
		super(new GridBagLayout());
		initValue();
		initMenuPanel();
		
		this.librarySystem = ls;
	}
	
	private void initValue() {
		String currDirectory = System.getProperty("user.dir");
		pathToImage = currDirectory + "/src/images";
	}
	
	private void initMenuPanel() {
		ImageIcon imageIcon = new ImageIcon(pathToImage+"/bgg.jpg");
		JPanel gridPanel = new JPanel() {
			
			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);

		        // Draw the background image at (0, 0)
		        g.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
		    }
		};
				
		
		

        // Create a JLabel with the image
        JLabel label = new JLabel(imageIcon);
//		gridPanel.add(createButton(MenuType.CHECKOUT.getText(), "/ic_checkout.png", null));
//		gridPanel.add(createButton(MenuType.OVERDUE.getText(), "/ic_due_date.png", null));
//		gridPanel.add(createButton(MenuType.BOOK.getText(), "/ic_book.png", null));
//		gridPanel.add(createButton(MenuType.MEMBER.getText(), "/ic_member.png", null));
//		gridPanel.setPreferredSize(new Dimension(200, 200));
		gridPanel.add(label);
		add(gridPanel);
	}
	
    private JButton createButton(String name, String iconName, MouseListener listener) {
    	ImageIcon icon = Util.loadImage(pathToImage + "/" + iconName, 45, 45);
    	JButton button = new JButton(icon);    
    	 
    	button.setName(name);
    	button.addActionListener(onClickedListener);
    	
    	return button;
    }
    
    private ActionListener onClickedListener = e -> {
    	JButton button =  (JButton) e.getSource();
    	this.librarySystem.changeSelectMenu(button.getName());
    };

}
