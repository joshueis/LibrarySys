import java.sql.*;
import java.util.*;

class Borrower{
	
	int cardID;
	String bookName, bookID;
	String branchName, branchID;
	String dateOut, dateDue, dateIn;
	
}

class BorrowProcedure {
	
	SQLQuery query = SQLQuery.getSQLQuery();
	Scanner in = new Scanner(System.in);
	LibSystem libSys  = new LibSystem();
	String custTab    = "    ";
	Borrower borrower = new Borrower();
	
	boolean execute(){
		Integer cardID;
		String custTab = "    ";
		int selection = 0;
		ResultSet rs = null;
		List<Object> para = new ArrayList<>();
		System.out.println("Enter your card number:");
		
		//validation check
		while (true) {
			cardID = new Integer(in.nextInt());
			para.add(cardID);
			try {
				rs = libSys.executeSQL(para, query.SQLIDCheck);
				libSys.unholdExecuteSQL();
				if (!rs.next()) {
					System.out.println("Invalid card number! Please try again!");
					para.clear();
					continue;
				}
				else {
					borrower.cardID = cardID;
					System.out.println("Welcome " + rs.getString("name") + "");
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		
		while (true) {
			System.out.println(custTab + "1) Check out a book");
			System.out.println(custTab + "2) Return a Book");
			System.out.println(custTab + "3) Quit to Previous");
			selection = in.nextInt();
			switch (selection) {
			case 1: {
				while (true) {
					if (!checkOutBranch()) {//if branch doesn't exit then send back to main menu
						return false;
					}
					if (!checkOutBooks()) {//
						return false;
					}
					checkOut();//update the book records in library
					break;
				}
				continue;
			}
			case 2: {
				while (true) {
					if (!returnBooks()) {
						break;
					}
					returns();
					break;
				}
				continue;
			}
			case 3: {
				return false;
			}
			}
		}
		
	}
	
	void checkOut(){
		List<Object> para = new ArrayList<>();
		// rs = null;
		para.add(Integer.parseInt(borrower.bookID));
		para.add(Integer.parseInt(borrower.branchID));
		para.add(new Integer(borrower.cardID));	
		try {
			libSys.executeUpdateSQL(para, query.SQLAddCheckOut);
			changeBookCopies(borrower.bookID, borrower.branchID, query.SQLDrsBookCpy);
			libSys.unholdExecuteSQL();
			System.out.println("Book: " + borrower.bookName + " has been checked out.");
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	boolean checkOutBooks(){
		int displayIndex = 1;
		ResultSet rs = null;
		List<String> bookNames = new ArrayList<>(),
				     bookIDs   = new ArrayList<>();
		List<Object> para      = new ArrayList<>();
		para.add(borrower.branchName);
		System.out.println("Pick the Book you want to check out:");			
		try {
			rs = libSys.executeSQL(para,query.SQLReadBooks);
			libSys.unholdExecuteSQL();
			while(rs.next()){
				String bookID   = rs.getString("bookId");
				bookIDs.add(bookID);
				String bookName = rs.getString("title");
				bookNames.add(bookName);
				System.out.println(custTab + displayIndex++ + ") " + bookName); 
			}
			System.out.println(custTab + displayIndex++ + ") Quit to previous");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int selection = in.nextInt();
		if (selection == bookIDs.size()+1) {
			return false;
		}
		else if (selection < 1 || selection > bookIDs.size()+1){
			return false;
		}
		borrower.bookID   = bookIDs.get(selection-1);
		borrower.bookName = bookNames.get(selection-1);
		return true;
	}
	
	boolean checkOutBranch(){
		System.out.println("Pick the branch you want to check out from:");
		ResultSet rs = null;
		List<String> branchNames = new ArrayList<>(), 
				     branchAddrs = new ArrayList<>(), 
				     branchIDs   = new ArrayList<>();
		int displayIndex = 1;
		try {
			rs = libSys.executeSQL(query.SQLReadBranches);
			libSys.unholdExecuteSQL();
			while(rs.next()){
				String branchID   = rs.getString("branchId");
				branchIDs.add(branchID);
				String branchName = rs.getString("branchName");
				branchNames.add(branchName);
				String branchAddr = rs.getString("branchAddress");
				branchAddrs.add(branchAddr);
				System.out.println(custTab + displayIndex++ + ") " + branchName + ", " + branchAddr);
			}
			System.out.println(custTab + displayIndex++ + ") Quit to previous");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int selection = in.nextInt();
		if (selection == branchIDs.size()+1) {
			return false;
		}
		else if (selection < 1 || selection > branchIDs.size()+1){
			return false;
		}
		
		borrower.branchID   = branchIDs.get(selection-1);
		borrower.branchName = branchNames.get(selection-1);
		System.out.println("Branch: " + borrower.branchName + " is selected.");
		return true;
	}
	
	void returns(){
		List<Object> para = new ArrayList<>();
		para.add(borrower.bookID);
		try {
			libSys.executeUpdateSQL(para, query.SQLReturnBook);
			changeBookCopies(borrower.bookID, borrower.branchID, query.SQLIrsBookCpy);
			libSys.unholdExecuteSQL();
			System.out.println("Book: " + borrower.bookName + " has been returned.");	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	boolean returnBooks(){
		int displayIndex = 1;
		ResultSet rs = null;
		List<String> bookNames = new ArrayList<>(),
				     bookIDs   = new ArrayList<>(),
					 branchIDs = new ArrayList<>();
		List<Object> para      = new ArrayList<>();
		para.add((Integer)borrower.cardID);
		System.out.println("Pick the Book you want to return:");			
		try {
			rs = libSys.executeSQL(para,query.SQLBorrowedBook);
			libSys.unholdExecuteSQL();
			while(rs.next()){
				String bookID   = rs.getString("bookId");
				bookIDs.add(bookID);
				String bookName = rs.getString("title");
				bookNames.add(bookName);
				String branchID = rs.getString("branchId");
				branchIDs.add(branchID);
				System.out.println(custTab + displayIndex++ + ") " + bookName); 
			}
			System.out.println(custTab + displayIndex++ + ") Quit to previous");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int selection = in.nextInt();
		if (selection == bookIDs.size()+1) {
			return false;
		}
		else if (selection < 1 || selection > bookIDs.size()+1){
			return false;
		}
		borrower.bookID   = bookIDs.get(selection-1);
		borrower.bookName = bookNames.get(selection-1);
		borrower.branchID = branchIDs.get(selection-1);
		return true;		
	}
	
	boolean returnBranch(){
		System.out.println("Pick the branch you want to return from:");
		ResultSet rs = null;
		List<String> branchNames = new ArrayList<>(), 
				     branchAddrs = new ArrayList<>(), 
				     branchIDs   = new ArrayList<>();
		int displayIndex = 1;
		try {
			rs = libSys.executeSQL(query.SQLReadBranches);
			libSys.unholdExecuteSQL();
			while(rs.next()){
				String branchID   = rs.getString("branchId");
				branchIDs.add(branchID);
				String branchName = rs.getString("branchName");
				branchNames.add(branchName);
				String branchAddr = rs.getString("branchAddress");
				branchAddrs.add(branchAddr);
				System.out.println(custTab + displayIndex++ + ") " + branchName + ", " + branchAddr);
			}
			System.out.println(custTab + displayIndex++ + ") Quit to previous");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int selection = in.nextInt();
		if (selection == branchIDs.size()+1) {
			return false;
		}
		else if (selection < 1 || selection > branchIDs.size()+1){
			return false;
		}
		borrower.branchID   = branchIDs.get(selection-1);
		borrower.branchName = branchNames.get(selection-1);
		return true;
	}
	
	void changeBookCopies(String bookID, String branchID, String SQL){
		List<Object> para  = new ArrayList<>();
		para.add(bookID);
		para.add(branchID);
		try {
			libSys.executeUpdateSQL(para, SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
}
