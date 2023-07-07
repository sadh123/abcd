package librarysystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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

public class CheckOutPanel extends JPanel {
	
	public static CheckOutPanel INSTANCE;
	
	private static final long serialVersionUID = 1L;
	private JPanel topPanel;
	private JPanel centerPanel;
	
    private JTextField memberIdField;
    private JTextField isbnField;
    private JButton checkOutButton;
    private JTable checkOutTable;
    
    private ControllerInterface ci = new SystemController();
    
    private final String[] headerNames = {"ISBN", "Book", "Check Out Date", "Due Date"};
    private DefaultTableModel dataSource = new DefaultTableModel(0, 0) {

        private static final long serialVersionUID = 1L;

		@Override
        public boolean isCellEditable(int row, int column) {
           return false;
        }
    };
	
	
	private CheckOutPanel() {
		super(new BorderLayout());
		initTopPanel();
		initCenterPanel();
		initEvent();
	}
	
	public static CheckOutPanel getInstance() {
		
		if (INSTANCE == null) {
			INSTANCE = new CheckOutPanel();
		}
		return INSTANCE;
	}
	
	public void reset() {
		memberIdField.setText("");
		isbnField.setText("");
		dataSource.setNumRows(0);
	}
	
	private void initTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new GridLayout(3, 2));
		searchPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
		
		JLabel memberIdLabel = new JLabel("Member ID:");
		memberIdField = new JTextField();
		memberIdField.setText("1001");
		
		JLabel isbnLabel = new JLabel("ISBN:");
		isbnField = new JTextField();
		isbnField.setText("23-11451");
		
		searchPanel.add(memberIdLabel);
		searchPanel.add(memberIdField);
		searchPanel.add(isbnLabel);
		searchPanel.add(isbnField);
		
		JPanel buttomPanel = new JPanel();
		buttomPanel.setLayout(new FlowLayout());
		
		checkOutButton = new JButton("Check Out");
		buttomPanel.add(checkOutButton);
		
		topPanel.add(searchPanel);
		topPanel.add(buttomPanel);
		
		this.add(topPanel, BorderLayout.NORTH);
	}
	
	private void initCenterPanel() {
		
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
	
		checkOutTable = new JTable();
		dataSource.setColumnIdentifiers(headerNames);
		checkOutTable.setModel(dataSource);
	 
        JScrollPane scrollPane = new JScrollPane(checkOutTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
    	this.add(centerPanel, BorderLayout.CENTER);
	        
	}
	
	private void initEvent() {
		checkOutButton.addActionListener(e -> {
			
			String memberId = memberIdField.getText().trim();
			String isbn = isbnField.getText().trim();
			
			if (memberId.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input member id!", "Error", JOptionPane.ERROR_MESSAGE);
				memberIdField.requestFocus();
				return;
			}
			
			if (isbn.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please input isbn!", "Error", JOptionPane.ERROR_MESSAGE);
				isbnField.requestFocus();
				return;
			}
			
			LibraryMember member = ci.findMember(memberId);
			Book book = ci.findBook(isbn);
			
			if (member == null) {
				JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (book == null) {
				JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (!book.isAvailable()) {
				JOptionPane.showMessageDialog(this, "Book is not available!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			CheckoutRecord record = member.checkOutBook(book);
			
			if (record == null) {
				JOptionPane.showMessageDialog(this, "This member hasn't returned this book from last time!", "Error", JOptionPane.ERROR_MESSAGE);
				record = member.getCheckoutRecord();
			} else {
				// Save Record
				ci.saveCheckOutRecord(member, book);
			}
			
			showDataOnTable(record);
		});
	}
	
	private void showDataOnTable(CheckoutRecord record) {
		//Refresh data
		for (int i = dataSource.getRowCount() - 1; i > -1; i--) {
			 dataSource.removeRow(i);
		}
		 
		for (CheckoutRecordEntry entry : record.getCheckoutEntries()) {
			dataSource.addRow(new Object[] { entry.getOriginalBook().getIsbn(), entry.getBookTitle(), entry.getCheckOutDateStr(), entry.getDueDateStr()});	
		}
	}
	
	
}
