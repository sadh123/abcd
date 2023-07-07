package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckoutRecord implements Serializable {
	
	private static final long serialVersionUID = -891229800414579999L;
	private List<CheckoutRecordEntry> checkoutEntries;
	
	CheckoutRecord(List<CheckoutRecordEntry> checkoutEntries) {
		this.checkoutEntries = checkoutEntries;
	}
	
	public List<CheckoutRecordEntry> getCheckoutEntries() {
		return this.checkoutEntries;
	}
	
	public CheckoutRecordEntry addCheckOutEntry(LocalDate checkOutDate, LocalDate dueDate, BookCopy book) {
		CheckoutRecordEntry entry = new CheckoutRecordEntry(checkOutDate, dueDate, book);
		this.checkoutEntries.add(entry);
		book.changeAvailability();
		
		return entry;
	}
}
