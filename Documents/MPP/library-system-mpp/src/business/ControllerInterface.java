package business;

import java.util.HashMap;
import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
	public void login(String id, String password) throws LoginException;
	public List<String> allMemberIds();

	void saveNewMember(LibraryMember member);

	List<LibraryMember> getAllMembers();

    public List<String> allBookIds();
	
	// CheckOut
	public LibraryMember findMember(String id);
	public Book findBook(String isbn);
	public void saveCheckOutRecord(LibraryMember member, Book book);
	public HashMap<LibraryMember, CheckoutRecordEntry> getMembersOverDue (Book book);
	
	public void addBook(Book book);
	public List<Book> getAllBooks();
	
}
