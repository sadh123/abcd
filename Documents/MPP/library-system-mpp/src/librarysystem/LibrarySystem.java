package librarysystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import business.ControllerInterface;
import business.SystemController;
import dataaccess.Auth;


public class LibrarySystem extends JFrame implements LibWindow {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static LibrarySystem INSTANCE =new LibrarySystem();
	Image img=null;
	JPanel mainPanel, menuPanel, cardPanel;

	JMenuBar menuBar;
    JMenuItem login, allBookIds, allMemberIds; 
    List<JLabel> menus;
    
    String pathToImage;
    
	ControllerInterface ci = new SystemController();
    
    private boolean isInitialized = false;
    
    private static LibWindow[] allWindows = { 
    	LibrarySystem.INSTANCE,
		LoginWindow.INSTANCE,
		AllMemberIdsWindow.INSTANCE,	
		AllBookIdsWindow.INSTANCE
	};

	public static void hideAllWindows() {
		for(LibWindow frame: allWindows) {
			frame.setVisible(false);
		}
	}

	private LibrarySystem() {}

	public void init() {
		initValue();
		initComponent();
		isInitialized = true;
	}

	private void initValue() {
		initPathToImage();
	}

	private void initComponent() {
		img = Toolkit.getDefaultToolkit().createImage(System.getProperty("user.dir") +"/src/images/bg.jpg" );
		initContentPane();
		initMenuPanel();
		initCardLayout();
		
		setSize(1000,800);
	}

	private void initPathToImage() {
		String currDirectory = System.getProperty("user.dir");
		pathToImage = currDirectory + "/src/images";
	}

	private void initContentPane() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		getContentPane().add(mainPanel);
	}

	private void initMenuPanel() {
		menus = new ArrayList<JLabel>();

		menuPanel = new JPanel(new GridLayout(1, 0));
		//menus.add(createButton(MenuType.HOME.getText(),
		menus.add(createButton(MenuType.HOME.getText(), "/ic_menu.png", new librarysystem.LibrarySystem.CustomMenuListener()));
		menus.add(createButton(MenuType.MEMBER.getText(), "/ic_member.png", new librarysystem.LibrarySystem.CustomMenuListener()));
		menus.add(createButton(MenuType.BOOK.getText(), "/ic_book.png", new librarysystem.LibrarySystem.CustomMenuListener()));
		
		menus.add(createButton(MenuType.CHECKOUT.getText(), "/ic_checkout.png", new librarysystem.LibrarySystem.CustomMenuListener()));
		menus.add(createButton(MenuType.OVERDUE.getText(), "/ic_due_date.png", new librarysystem.LibrarySystem.CustomMenuListener()));
		
		for (JLabel menu : menus) {
			menuPanel.add(menu);
		}

		menuPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.decode("#CCCCCC")));
    	mainPanel.add(menuPanel, BorderLayout.NORTH);
    }
	
    private void initCardLayout() {
    	cardPanel = new JPanel(new CardLayout()){
    	     @Override
    	     protected void paintComponent(Graphics g)
    	     {
    	        super.paintComponent(g);
    	        g.drawImage(img, 0, 0, null);
    	     }
    	  };

    	cardPanel.add(new HomePanel(this), MenuType.HOME.getText());
    	cardPanel.add(CheckOutPanel.getInstance(), MenuType.CHECKOUT.getText());
    	cardPanel.add(OverDuePanel.getInstance(), MenuType.OVERDUE.getText());
		cardPanel.add(BookPanel.getInstance(), MenuType.BOOK.getText());
    	cardPanel.add(LibraryMemberPanel.getInstance(), MenuType.MEMBER.getText());
    	
    	mainPanel.add(cardPanel, BorderLayout.CENTER);
    }
    
    private JLabel createButton(String title, String iconName, MouseListener listener) {
    	JLabel label = new JLabel(title);    
    	ImageIcon icon = Util.loadImage(pathToImage + "/" + iconName, 30, 30); 
    	
    	label.setIcon(icon);
    	label.addMouseListener(listener);
    	label.setOpaque(true);
    	label.setBorder(new EmptyBorder(10 , 10, 10, 10));
    	
    	if (title == MenuType.HOME.getText()) {
    		label.setBackground(Colors.primary);
    	}
    	
    	return label;
    }

	@Override
	public boolean isInitialized() {
		return isInitialized;
	}


	@Override
	public void isInitialized(boolean val) {
		isInitialized =val;

	}
	
	public void changeSelectMenu(String menuName) {
		
		MenuType mt = MenuType.getMenuType(menuName);

		if (SystemController.currentAuth.equals(Auth.ADMIN)) {
			if (mt == MenuType.CHECKOUT) {
				JOptionPane.showMessageDialog(this, "You  Role is " + SystemController.currentAuth + ". Sorry you are not allow to access!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if (SystemController.currentAuth.equals(Auth.LIBRARIAN)) {
			if (mt == MenuType.BOOK) {
				JOptionPane.showMessageDialog(this, "Your Role is " + SystemController.currentAuth + " Sorry you are not allow to access!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		for (JLabel menu : menus) {
			
			if (menu.getText() != menuName) {
				menu.setBackground(mainPanel.getBackground());
			} else {
				menu.setBackground(Colors.primary);
			}

			CardLayout cl = (CardLayout) (cardPanel.getLayout());
			cl.show(cardPanel, menuName);

		}

		CardLayout cl = (CardLayout) (cardPanel.getLayout());
		cl.show(cardPanel, menuName);
		
		switch (mt) {
			case CHECKOUT:
				CheckOutPanel.getInstance().reset();
				break;
			case OVERDUE:
				OverDuePanel.getInstance().reset();
				break;
			case MEMBER:
				LibraryMemberPanel.getInstance().init();
				break;
			case BOOK:
				BookPanel.getInstance().clear();
				BookPanel.getInstance().clearAuthorTextField();
				break;
		}
	}
	
    class CustomMenuListener implements MouseListener {

	  @Override
	  public void mouseClicked(MouseEvent e) {
		JLabel clickedMenu = (JLabel) e.getSource();
		changeSelectMenu(clickedMenu.getText());
	  }
	
	  @Override
	  public void mousePressed(MouseEvent e) {}
	
	  @Override
	  public void mouseReleased(MouseEvent e) {}
	
	  @Override
	  public void mouseEntered(MouseEvent e) {
		  JLabel label = (JLabel) e.getSource();
		  label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	  }
	
	  @Override
	  public void mouseExited(MouseEvent e) {
		  JLabel label = (JLabel) e.getSource();
		  label.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	  }
    	
    } 
}
