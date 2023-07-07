package business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
	
	public static Auth currentAuth = null;
	
	public void login(String id, String password) throws LoginException {
		DataAccess da = new DataAccessFacade();
		HashMap<String, User> map = da.readUserMap();
		
		if(!map.containsKey(id)) {
			throw new LoginException("ID " + id + " not found");
		}
		
		String passwordFound = map.get(id).getPassword();
		
		if(!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		
		currentAuth = map.get(id).getAuthorization();
		
	}
	@Override
	public List<String> allMemberIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readMemberMap().keySet());
		
		return retval;
	}

	@Override
	public void saveNewMember(LibraryMember member) {
		DataAccess da = new DataAccessFacade();
		da.saveNewMember(member);
	}

	@Override
	public List<LibraryMember> getAllMembers() {
		DataAccess da = new DataAccessFacade();
		Collection<LibraryMember> members = da.readMemberMap().values();
		return new ArrayList<>(members);
	}
	
	@Override
	public List<String> allBookIds() {
		DataAccess da = new DataAccessFacade();
		List<String> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().keySet());
		
		return retval;
	}
	
	// CheckOut Override
	@Override
	public LibraryMember findMember(String id) {
		DataAccess da = new DataAccessFacade();
		HashMap<String, LibraryMember> tbMembers = da.readMemberMap();
		LibraryMember member = null;
		
		for (Object key : tbMembers.keySet().toArray()) {
			
			if (key.toString().compareToIgnoreCase(id) == 0) {
				member =  (LibraryMember) tbMembers.get(id);
				break;
			}
		}
		
		return member;
	}
	
	@Override
	public Book findBook(String isbn) {
		DataAccess da = new DataAccessFacade();
		HashMap<String, Book> tbBooks = da.readBooksMap();
		Book book = null;
		
		for (Object key : tbBooks.keySet().toArray()) {
			if (key.toString().compareToIgnoreCase(isbn) == 0) {
				book =  (Book) tbBooks.get(key);
				break;
			}
		}
		
		return book;
	}
	
	public void saveCheckOutRecord(LibraryMember member, Book book) {
		DataAccess da = new DataAccessFacade();
		da.saveNewMember(member);
		da.saveBook(book);
	}
	
	// OverDue Override
	public HashMap<LibraryMember, CheckoutRecordEntry> getMembersOverDue (Book book) {
		HashMap<LibraryMember, CheckoutRecordEntry> overDueMembers = new HashMap<LibraryMember, CheckoutRecordEntry>();
		
		DataAccess da = new DataAccessFacade();
		HashMap<String, LibraryMember> tbMembers = da.readMemberMap();
		List<LibraryMember> members = new ArrayList<LibraryMember>(tbMembers.values());
		
		for (LibraryMember m : members) {
			for (CheckoutRecordEntry entry : m.getCheckoutRecord().getCheckoutEntries()) {
				if (entry.getOriginalBook().equals(book) 
						&& entry.getDueDate().isBefore(LocalDate.now())) {
					overDueMembers.put(m, entry);
					continue;
				}
			}
		}
		
		return overDueMembers;
		
	}
	@Override
	public void addBook(Book book) {
		DataAccess da = new DataAccessFacade();
		da.saveBook(book);
		
	}
	@Override
	public List<Book> getAllBooks() {
		DataAccess da = new DataAccessFacade();
		List<Book> retval = new ArrayList<>();
		retval.addAll(da.readBooksMap().values());
		return retval;
	}
	
}
