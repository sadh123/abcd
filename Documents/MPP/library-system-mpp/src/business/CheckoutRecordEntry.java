package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CheckoutRecordEntry implements Serializable {
	
	private static final long serialVersionUID = -891229800414574999L;
	private LocalDate checkOutDate;
	private LocalDate dueDate;
	private BookCopy bookCopy;
	
	CheckoutRecordEntry(LocalDate checkOutDate, LocalDate dueDate, BookCopy bookCopy) {
		this.checkOutDate = checkOutDate;
		this.dueDate = dueDate;
		this.bookCopy = bookCopy;
	}
	
	@Override
	public String toString() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return "(" + bookCopy.getBook().getTitle() + ", " + checkOutDate.format(format) + ", " + dueDate.format(format) + ")";
	}
	
	public String getCheckOutDateStr() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return this.checkOutDate.format(formatter);
	}
	
	public String getDueDateStr() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return this.dueDate.format(formatter);
	}
	
	public LocalDate getDueDate() {
		return this.dueDate;
	}
	
	public String getBookTitle() {
		return this.bookCopy.getBook().getTitle();
	}
	
	public Book getOriginalBook() {
		return this.bookCopy.getBook();
	}
	
	public BookCopy getCopyBook() {
		return this.bookCopy;
	}
	
	
}
