package librarysystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;

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

import business.Book;
import business.BookCopy;
import business.CheckoutRecord;
import business.CheckoutRecordEntry;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;

public class OverDuePanel extends JPanel {
	
	public static OverDuePanel INSTANCE;
	
	private static final long serialVersionUID = 1L;
	private JPanel topPanel;
	private JPanel centerPanel;
	
    private JTextField isbnField;
    private JButton searchButton;
    private JTable bookTable;
    
    private ControllerInterface ci = new SystemController();
    
    private final String[] headerNames = {"ISBN", "Title", "Copy Number", "Check Out Date", "Due Date", "Member"};
    private DefaultTableModel dataSource = new DefaultTableModel(0, 0) {

        private static final long serialVersionUID = 1L;

		@Override
        public boolean isCellEditable(int row, int column) {
           return false;
        }
    };
	
	private OverDuePanel() {
		super(new BorderLayout());
		initTopPanel();
		initCenterPanel();
		initEvent();
	}
	
	public static OverDuePanel getInstance() {
		
		if (INSTANCE == null) {
			INSTANCE = new OverDuePanel();
		}
		return INSTANCE;
	}
	
	public void reset() {
		isbnField.setText("");
		dataSource.setNumRows(0);
	}
	
	private void initTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(1, 1));
		searchPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
		
	
		JLabel isbnLabel = new JLabel("ISBN:");
		isbnField = new JTextField();
		isbnField.setText("23-11451");
		
		searchPanel.add(isbnLabel);
		searchPanel.add(isbnField);
		
		JPanel buttomPanel = new JPanel();
		buttomPanel.setLayout(new FlowLayout());
		
		searchButton = new JButton("Search");
		buttomPanel.add(searchButton);
		
		topPanel.add(searchPanel);
		topPanel.add(buttomPanel);
		
		this.add(topPanel, BorderLayout.NORTH);
	}
	
	private void initCenterPanel() {
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
	
		bookTable = new JTable();
		dataSource.setColumnIdentifiers(headerNames);
		bookTable.setModel(dataSource);
	 
        JScrollPane scrollPane = new JScrollPane(bookTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
    	this.add(centerPanel, BorderLayout.CENTER);
	        
	}
	
	private void initEvent() {
		searchButton.addActionListener(e -> {
			
			String isbn = isbnField.getText().trim();
		
			if (isbn.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input ISBN!", "Error", JOptionPane.ERROR_MESSAGE);
				isbnField.requestFocus();
				return;
			}

			Book book = ci.findBook(isbn);
			
			if (book == null) {
				JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
//			if (book.isAvailable()) {
//				JOptionPane.showMessageDialog(this, "Book is available!", "Information", JOptionPane.INFORMATION_MESSAGE);
//				return;
//			}
			
			HashMap<LibraryMember, CheckoutRecordEntry> overDueMembers = ci.getMembersOverDue(book);
			
			if (overDueMembers.size() == 0) {
				JOptionPane.showMessageDialog(this, "There is no over due for this book", "Information", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			showDataOnTable(overDueMembers);
			
		});
	}
	
	private void showDataOnTable(HashMap<LibraryMember, CheckoutRecordEntry> overDueMembers) {
		for (int i = dataSource.getRowCount() - 1; i > -1; i--) {
			 dataSource.removeRow(i);
		}
		
		for (LibraryMember m : overDueMembers.keySet()) {
			CheckoutRecordEntry entry = overDueMembers.get(m);
			dataSource.addRow(new Object[] { entry.getOriginalBook().getIsbn(), entry.getBookTitle(),entry.getCopyBook().getCopyNum(), entry.getCheckOutDateStr(), entry.getDueDateStr(), m.getFirstName() + " " + m.getLastName()});	
		}
	}
	
}
