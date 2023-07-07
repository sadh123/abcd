package business;

import java.io.Serializable;
import java.time.LocalDate;

import java.util.ArrayList;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

final public class LibraryMember extends Person implements Serializable {
	
	private static final long serialVersionUID = -2226197306790714013L;
	
	private String memberId;
	private CheckoutRecord checkoutRecord;
	
//	public LibraryMember(String memberId, String fname, String lname, String tel,Address add) {
//		super(fname,lname, tel, add);
//		this.memberId = memberId;	
//	}
	
	public LibraryMember(String memberId, String fname, String lname, String tel,Address add, CheckoutRecord checkoutRecord) {
		super(fname,lname, tel, add);
		this.memberId = memberId;
		this.checkoutRecord = checkoutRecord;
		
		if (this.checkoutRecord == null) {
			this.checkoutRecord = new CheckoutRecord(new ArrayList<CheckoutRecordEntry>());
		}
	}
	
	public String getMemberId() {
		return memberId;
	}
	
	public CheckoutRecord getCheckoutRecord() {
		return checkoutRecord;
	}
	
	public CheckoutRecord checkOutBook(Book book) {
		
		for (CheckoutRecordEntry entry : checkoutRecord.getCheckoutEntries()) {
			if (entry.getOriginalBook().equals(book)) {
				return null;
			}
		}
		
		LocalDate checkOutDate = LocalDate.now();
		LocalDate dueDate = LocalDate.now().plusDays(book.getMaxCheckoutLength());
		BookCopy bookCopy = book.getNextAvailableCopy();
		
		checkoutRecord.addCheckOutEntry(checkOutDate, dueDate, bookCopy);
		
		return checkoutRecord;
	}
	
	// Test Data
	public CheckoutRecord checkOutBookOverDue(BookCopy bookCopy) {
		LocalDate checkOutDate = LocalDate.now().minusDays(bookCopy.getBook().getMaxCheckoutLength() + 1);
		LocalDate dueDate = LocalDate.now().minusDays(1);
		
		checkoutRecord.addCheckOutEntry(checkOutDate, dueDate, bookCopy);
		bookCopy.changeAvailability();
		
		return checkoutRecord;
	}

	@Override
	public String toString() {
		return "Member Info: " + "ID: " + memberId + ", name: " + getFirstName() + " " + getLastName() + 
				", " + getTelephone() + " " + getAddress();
	}

}
