package librarysystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.*;

import business.Address;
import business.Author;
import business.Book;
import business.ControllerInterface;
import business.SystemController;
import dataaccess.Auth;

public class BookPanel extends JPanel{
	
	public static BookPanel INSTANCE;
	
	List<Author> auths = new ArrayList<>();
	SystemController systemController = new SystemController();
	
	private static final long serialVersionUID = 1L;
	private JPanel topPanel;
	private JPanel centerPanel;
	
    private JTextField isbnField;
    private JTextField titleField;
    private JTextField checkoutField;
    private JTextField copiesField;
    private JButton addButton;
    private JButton addAuthor;
    private JTable bookTable;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField telephoneField;
    private JTextField bioField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField zipField;
    
    JLabel showAuthoreLabel = new JLabel();
    
    
    private ControllerInterface ci = new SystemController();
    
    private final String[] headerNames = {"ISBN", "Title", "CheckOut Length", "Copy", "Author"};
    private DefaultTableModel dataSource = new DefaultTableModel(0, 0) {

        private static final long serialVersionUID = 1L;

		@Override
        public boolean isCellEditable(int row, int column) {
           return false;
        }
    };
	
	
	private BookPanel() {
		super(new BorderLayout());
		initTopPanel();
		initCenterPanel();
		initDataToTable();
		initEvent();	
	}
	
	public static BookPanel getInstance() {
		
		if (INSTANCE == null) {
			INSTANCE = new BookPanel();
		}
		return INSTANCE;
	}
	
	
	private void initDataToTable() {
		List<Book> record = systemController.getAllBooks();
		
		//Refresh data
		for (int i = dataSource.getRowCount() - 1; i > -1; i--) {
			 dataSource.removeRow(i);
		}
		 
		for (Book book1 : record) {
			dataSource.addRow(new Object[] { book1.getIsbn(), book1.getTitle(), book1.getMaxCheckoutLength(), book1.getNumCopies(), book1.getAuthors().toString()});	
		}
	}
	
	private void initTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		JPanel bookPanel = new JPanel();
		bookPanel.setLayout(new GridLayout(2, 2));
		bookPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
		
		// Create the input fields
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnField = new JTextField(20);
        
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(20);
        
        JLabel checkoutLabel = new JLabel("MaxCheckoutLength:");
        checkoutField = new JTextField(20);
        
        JLabel copiesLabel = new JLabel("Number of Copies:");
        copiesField = new JTextField(20);
        
        JLabel authorsLabel = new JLabel("Authors:");
        
        // Create the input fields
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);
        
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);
        
        JLabel telephoneLabel = new JLabel("Telephone:");
        telephoneField = new JTextField(20);
        
        JLabel bioLabel = new JLabel("Bio:");
        bioField = new JTextField(20);
        
        JLabel streetLabel = new JLabel("Street:");
        streetField = new JTextField(20);
        
        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField(20);
        
        JLabel stateLabel = new JLabel("State:");
        stateField = new JTextField(20);
        
        JLabel zipLabel = new JLabel("Zip:");
        zipField = new JTextField(20);
        
     // Add the components to the panel
        bookPanel.add(isbnLabel);
        bookPanel.add(isbnField);
        
        bookPanel.add(titleLabel);
        bookPanel.add(titleField);
        
        bookPanel.add(checkoutLabel);
        bookPanel.add(checkoutField);
        
        bookPanel.add(copiesLabel);
        bookPanel.add(copiesField);
        
		JPanel a = new JPanel();
		a.setLayout(new GridLayout(1, 1));
		a.setBorder(new EmptyBorder(10, 30, 10, 30));
        
        a.add(authorsLabel);
        
		JPanel authoPanel = new JPanel();
		authoPanel.setLayout(new GridLayout(4, 2));
		authoPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        
		   // Add the components to the panel
		authoPanel.add(firstNameLabel);
		authoPanel.add(firstNameField);
     
		authoPanel.add(lastNameLabel);
		authoPanel.add(lastNameField);
     
		authoPanel.add(telephoneLabel);
		authoPanel.add(telephoneField);
     
		authoPanel.add(bioLabel);
		authoPanel.add(bioField);
     
		authoPanel.add(streetLabel);
		authoPanel.add(streetField);
     
		authoPanel.add(cityLabel);
		authoPanel.add(cityField);
     
		authoPanel.add(stateLabel);
		authoPanel.add(stateField);
     
		authoPanel.add(zipLabel);
		authoPanel.add(zipField);
		
		JPanel showAuthors = new JPanel();
		showAuthors.setLayout(new GridLayout(1, 1));
		showAuthors.setBorder(new EmptyBorder(10, 30, 10, 30));
        
		showAuthors.add(showAuthoreLabel);
		
        
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout());
        
        addAuthor = new JButton("Add Author");
        btnPanel.add(addAuthor);
        
		JPanel buttomPanel = new JPanel();
		buttomPanel.setLayout(new FlowLayout());
		
		addButton = new JButton("Add Book");
		buttomPanel.add(addButton);
				
		topPanel.add(bookPanel);
		topPanel.add(a);
		topPanel.add(authoPanel);
		topPanel.add(showAuthors);
		topPanel.add(btnPanel);
		topPanel.add(buttomPanel);
		
		this.add(topPanel, BorderLayout.NORTH);
	}
	
	private void initCenterPanel() {
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
	
		bookTable = new JTable(){
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
		    {
		        Component c = super.prepareRenderer(renderer, row, column);

		        //  Alternate row color

		        if (!isRowSelected(row))
		            c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);

		        return c;
		    }
		};
		dataSource.setColumnIdentifiers(headerNames);
		bookTable.setModel(dataSource);
	 
        JScrollPane scrollPane = new JScrollPane(bookTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
    	this.add(centerPanel, BorderLayout.CENTER);
	        
	}
	
	private void initEvent() {
		addAuthor.addActionListener(e -> {
			String fName = firstNameField.getText().trim().toString();
			String lName = lastNameField.getText().trim().toString();
			String tel = telephoneField.getText().trim().toString();
			String bio = bioField.getText().trim().toString();
			
			if (fName.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input first name!", "Error", JOptionPane.ERROR_MESSAGE);
				firstNameField.requestFocus();
				return;
			}
			if (lName.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input last name!", "Error", JOptionPane.ERROR_MESSAGE);
				lastNameField.requestFocus();
				return;
			}
			if (tel.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input telephone!", "Error", JOptionPane.ERROR_MESSAGE);
				telephoneField.requestFocus();
				return;
			}

			
			String street = streetField.getText().trim().toString();
			String city = cityField.getText().trim().toString();
			String state = stateField.getText().trim().toString();
			String zip = zipField.getText().trim().toString();
			
			if (street.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input street!", "Error", JOptionPane.ERROR_MESSAGE);
				streetField.requestFocus();
				return;
			}
			
			if (city.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input city!", "Error", JOptionPane.ERROR_MESSAGE);
				cityField.requestFocus();
				return;
			}
			
			if (state.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input state!", "Error", JOptionPane.ERROR_MESSAGE);
				stateField.requestFocus();
				return;
			}
			
			if (zip.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input zip!", "Error", JOptionPane.ERROR_MESSAGE);
				zipField.requestFocus();
				return;
			}
			
			Address address = new Address(street, city, state, zip);
			
			auths.add(new Author(fName, lName, tel, address, bio));
			clearAuthorTextField();
			showAuthoreLabel.setText("");
			showAuthoreLabel.setText(auths.toString());
			System.out.println(auths);
		});
		addButton.addActionListener(e -> {
			
			// Retrieve the input values
            String isbn = isbnField.getText().trim();
            String title = titleField.getText().trim();
            String checkoutLength = checkoutField.getText().trim();
//            int numOfCopies = Integer.parseInt(copiesField.getText().trim());
            
			if (isbn.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input isbn!", "Error", JOptionPane.ERROR_MESSAGE);
				isbnField.requestFocus();
				return;
			}
			
			if (title.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input title!", "Error", JOptionPane.ERROR_MESSAGE);
				titleField.requestFocus();
				return;
			}
			
			if (checkoutLength.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input checkout length!", "Error", JOptionPane.ERROR_MESSAGE);
				checkoutField.requestFocus();
				return;
			}
			if (!isNumber(checkoutLength)) {
				JOptionPane.showMessageDialog(this, "Checkout length is only number!", "Error", JOptionPane.ERROR_MESSAGE);
				checkoutField.requestFocus();
				return;
			}
			
			if (auths.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input author information!", "Error", JOptionPane.ERROR_MESSAGE);
				firstNameField.requestFocus();
				return;
			}
		
			int checkoutLengthInt = Integer.parseInt(checkoutLength);
			
			Book book = new Book(isbn, title, checkoutLengthInt, auths);
			
			systemController.addBook(book);
			initDataToTable();
			clear();
			
		});
	}
	
	public void clear() {
		isbnField.setText("");
		titleField.setText("");
		checkoutField.setText("");
		copiesField.setText("");
	}
	
	public void clearAuthorTextField() {
		firstNameField.setText("");
		lastNameField.setText("");
		telephoneField.setText("");
		bioField.setText("");
		streetField.setText("");
		cityField.setText("");
		stateField.setText("");
		zipField.setText("");
	}
	
	public boolean isNumber(String text) {
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(text).matches();
    }

}
