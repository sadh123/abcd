package librarysystem;

import business.*;
import dataaccess.Auth;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

public class LibraryMemberPanel extends JPanel implements LibWindow {
	
	public static LibraryMemberPanel INSTANCE;

    private static final long serialVersionUID = 1L;

	private JTable memberTable;
    private DefaultTableModel tableModel;
    private DefaultTableModel bookModel;
    private boolean isInitialized = false;
    private JFormattedTextField memberIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField streetField;
    private JTextField cityField;
    private JTextField stateField;
    private JFormattedTextField zipField;
    private JFormattedTextField telephoneField;
    JTextField memberIDSearchField;
    private JButton searchButton;
    JButton addButton;
    JButton editButton;
    private JButton submitButton;
    private ActionType action;
    private ControllerInterface ci = new SystemController();

    private List<LibraryMember> libraryMembers;

    private LibraryMemberPanel() {
        setLayout(new BorderLayout());

        JPanel upperLeftPanel = new JPanel();
        upperLeftPanel.setLayout(new GridLayout(1, 2, 5, 5));

        JPanel addAndEditButtonPanel = new JPanel();
        addAndEditButtonPanel.setLayout(new GridLayout(1, 2, 5, 5));
        addAndEditButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        addAndEditButtonPanel.add(addButton);
        addAndEditButtonPanel.add(editButton);

        JLabel searchLabel = new JLabel("Search by ID:");
        memberIDSearchField = new JTextField();
        searchButton = new JButton("Search");
        upperLeftPanel.add(memberIDSearchField);
        upperLeftPanel.add(searchButton);

        JPanel formFieldPanel = new JPanel();
        formFieldPanel.setLayout(new GridLayout(11, 2, 5, 5));
        formFieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create and add components to the main panel
        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdField = createNumField();
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        JLabel streetLabel = new JLabel("Street:");
        streetField = new JTextField();
        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField();
        JLabel stateLabel = new JLabel("State:");
        stateField = new JTextField();
        JLabel zipLabel = new JLabel("ZIP:");
        zipField = createNumField();
        JLabel telephoneLabel = new JLabel("Telephone:");
        telephoneField = createNumField();
        submitButton = new JButton("Submit");

        formFieldPanel.add(searchLabel);
        formFieldPanel.add(upperLeftPanel);
        formFieldPanel.add(addAndEditButtonPanel);
        formFieldPanel.add(new JPanel());

        formFieldPanel.add(memberIdLabel);
        formFieldPanel.add(memberIdField);
        formFieldPanel.add(firstNameLabel);
        formFieldPanel.add(firstNameField);
        formFieldPanel.add(lastNameLabel);
        formFieldPanel.add(lastNameField);
        formFieldPanel.add(streetLabel);
        formFieldPanel.add(streetField);
        formFieldPanel.add(cityLabel);
        formFieldPanel.add(cityField);
        formFieldPanel.add(stateLabel);
        formFieldPanel.add(stateField);
        formFieldPanel.add(zipLabel);
        formFieldPanel.add(zipField);
        formFieldPanel.add(telephoneLabel);
        formFieldPanel.add(telephoneField);
        formFieldPanel.add(new JLabel()); // Empty label for alignment
        formFieldPanel.add(submitButton);

        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());

        // Create table model and table
        tableModel = createDefaultTableModel(
            new Object[] {
                "Member ID",
                "First Name",
                "Last Name",
                "Telephone"
            }
        );
        memberTable = new JTable(tableModel){
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);

                //  Alternate row color

                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);

                return c;
            }
        };
        memberTable.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {  // Detect single click
                    Point point = e.getPoint();
                    int row = memberTable.rowAtPoint(point);
                    String id = (String) memberTable.getValueAt(row, 0);
                    setAllFieldValue(Objects.requireNonNull(getMemberByID(id)));
                }
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(memberTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2, 1));
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JLabel bookListLabel = new JLabel("Checked Books");
        bookListLabel.setHorizontalAlignment(JLabel.CENTER);
        bookListLabel.setPreferredSize(new Dimension(1, 2));

        JPanel bookListPanel = new JPanel(new BorderLayout());
        bookModel = createDefaultTableModel(
                new Object[] {
                    "ISBN", "Title", "Checkout Date", "Due Date"
                }
        );
        JTable bookTable = new JTable(bookModel);
        JScrollPane bookTableScrollPane = new JScrollPane(bookTable);
        bookListPanel.add(bookTableScrollPane, BorderLayout.CENTER);
        bookListPanel.setPreferredSize(new Dimension(1, 200));

        southPanel.add(bookListLabel, BorderLayout.NORTH);
        southPanel.add(bookListPanel, BorderLayout.CENTER);
        southPanel.setPreferredSize(new Dimension(1, 250));

        add(formFieldPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
        if (isAllowToPrintCheckoutBooks()) {
            add(southPanel, BorderLayout.SOUTH);
        }

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFormFields();
                enableAllFields(true);
                editButton.setEnabled(false);
                action = ActionType.ADD;
                addButton.requestFocusInWindow();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LibraryMember member = getMemberByID(memberIDSearchField.getText());

                if (member == null) {
                    JOptionPane.showMessageDialog(null, "Member ID Not Found", "Not Found", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                clearFormFields();
                enableAllFields(false);
                tableModel.setRowCount(0);
                addMemberToTable(member);
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enableAllFields(true);
                memberIdField.setEnabled(false);
                editButton.setEnabled(false);
                action = ActionType.EDIT;
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                submitForm();
            }
        });

        // Set preferred size for the main panel
        int panelWidth = 450;
        int panelHeight = 400;
        formFieldPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
    }
    
	public static LibraryMemberPanel getInstance() {
		
		if (INSTANCE == null) {
			INSTANCE = new LibraryMemberPanel();
		}
		return INSTANCE;
	}

    private void submitForm() {

        String memberId = memberIdField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String street = streetField.getText();
        String city = cityField.getText();
        String state = stateField.getText();
        String zip = zipField.getText();
        String telephone = telephoneField.getText();

        // Perform validation logic for each field
        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Member ID.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            memberIdField.requestFocusInWindow();
            return;
        }

        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a First Name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            firstNameField.requestFocusInWindow();
            return;
        }

        if (lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Last Name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            lastNameField.requestFocusInWindow();
            return;
        }

        if (street.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Street.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            streetField.requestFocusInWindow();
            return;
        }

        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a City.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            cityField.requestFocusInWindow();
            return;
        }

        if (state.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a State.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            stateField.requestFocusInWindow();
            return;
        }

        if (zip.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a ZIP.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            zipField.requestFocusInWindow();
            return;
        }

        if (telephone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Telephone.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            telephoneField.requestFocusInWindow();
            return;
        }

        if (action == ActionType.ADD) {
            this.addMember();
        } else if (action == ActionType.EDIT) {
            this.updateMember();
        }
    }

    private DefaultTableModel createDefaultTableModel(Object[] headers) {
        return new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LibraryMemberPanel();
            }
        });
    }

    /* From LibWindow interfaces */
    @Override
    public void init() {
        this.clearFormFields();
        tableModel.setRowCount(0);
        this.loadAllMembers();
        this.enableAllFields(false);
    }

    private void enableAllFields(boolean enableValue) {
        memberIdField.setEnabled(enableValue);
        firstNameField.setEnabled(enableValue);
        lastNameField.setEnabled(enableValue);
        streetField.setEnabled(enableValue);
        cityField.setEnabled(enableValue);
        stateField.setEnabled(enableValue);
        zipField.setEnabled(enableValue);
        telephoneField.setEnabled(enableValue);
        editButton.setEnabled(enableValue);
        submitButton.setEnabled(enableValue);

        boolean isAllowToAddAndEdit = isAllowToAddAndEdit();
        addButton.setEnabled(isAllowToAddAndEdit);
        editButton.setEnabled(isAllowToAddAndEdit);

    }

    private void loadAllMembers() {
        this.libraryMembers = this.ci.getAllMembers();
        tableModel.setRowCount(0);

        for (LibraryMember mem : libraryMembers) {
            this.addMemberToTable(mem);
        }
    }

    private void addMemberToTable(LibraryMember member) {

        tableModel.addRow(
                new Object[] {
                        member.getMemberId(),
                        member.getFirstName(),
                        member.getLastName(),
                        member.getTelephone()
                }
        );
    }

    private void updateMember() {

        LibraryMember member = getMemberByID(this.memberIdField.getText());
        member.setFirstName(this.firstNameField.getText());
        member.setLastName(this.lastNameField.getText());
        member.setTelephone(this.telephoneField.getText());

        Address address = new Address(
                this.streetField.getText(),
                this.cityField.getText(),
                this.stateField.getText(),
                this.zipField.getText()
        );

        member.setAddress(address);

        this.ci.saveNewMember(member);
        enableAllFields(false);
        editButton.setEnabled(true);
        loadAllMembers();
        JOptionPane.showMessageDialog(this, "Member info updated", "Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addMember() {

        Address address = new Address(
                this.streetField.getText(),
                this.cityField.getText(),
                this.stateField.getText(),
                this.zipField.getText()
        );

        LibraryMember newMember = new LibraryMember(
                this.memberIdField.getText(),
                this.firstNameField.getText(),
                this.lastNameField.getText(), this.telephoneField.getText(), address, null
        );

        this.libraryMembers.add(newMember);
        this.addMemberToTable(newMember);

        this.ci.saveNewMember(newMember);
        this.clearFormFields();
        JOptionPane.showMessageDialog(this, "Member added", "Successful", JOptionPane.INFORMATION_MESSAGE);
        this.memberIdField.requestFocusInWindow();

    }

    private void clearFormFields() {
        memberIdField.setValue(null);
        firstNameField.setText("");
        lastNameField.setText("");
        streetField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setValue(null);
        telephoneField.setValue(null);
        bookModel.setRowCount(0);
    }

    private void setAllFieldValue(LibraryMember member) {
        Address address = member.getAddress();
        memberIdField.setText(member.getMemberId());
        firstNameField.setText(member.getFirstName());
        lastNameField.setText(member.getLastName());
        streetField.setText(address.getStreet());
        cityField.setText(address.getCity());
        stateField.setText(address.getState());
        zipField.setText(address.getZip());
        telephoneField.setText(member.getTelephone());

        if (isAllowToPrintCheckoutBooks()) {
            this.loadCheckedOutBooks(member);
        }

        this.enableAllFields(false);
        editButton.setEnabled(isAllowToAddAndEdit());
        action = ActionType.EDIT;
    }

    private void loadCheckedOutBooks(LibraryMember member) {

        bookModel.setRowCount(0);

        if (member.getCheckoutRecord() != null) {
            for (CheckoutRecordEntry entry : member.getCheckoutRecord().getCheckoutEntries()) {

                BookCopy bookCopy = entry.getCopyBook();
                Book book = bookCopy.getBook();
                bookModel.addRow(new Object[]{
                        book.getIsbn(),
                        book.getTitle(),
                        entry.getCheckOutDateStr(),
                        entry.getDueDateStr()
                });

            }
        }
    }

    private LibraryMember getMemberByID(String memberId) {
        for (LibraryMember mem : this.libraryMembers) {
            if (memberId.equals(mem.getMemberId())) {
                return mem;
            }
        }
        return null;
    }

    private boolean isAllowToAddAndEdit() {
        return (Auth.ADMIN == SystemController.currentAuth) || (Auth.BOTH == SystemController.currentAuth);
    }

    private boolean isAllowToPrintCheckoutBooks() {
        return Auth.LIBRARIAN == SystemController.currentAuth || Auth.BOTH == SystemController.currentAuth;
    }

    private JFormattedTextField createNumField() {
        NumberFormat numberFormat = new DecimalFormat("#");
        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
//        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setCommitsOnValidEdit(true);
        return new JFormattedTextField(numberFormatter);
    }

    @Override
    public boolean isInitialized() {
        return this.isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        this.isInitialized = val;
    }

}

enum ActionType {

    ADD("ADD"),
    EDIT("EDIT");

    private final String name;

    ActionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}