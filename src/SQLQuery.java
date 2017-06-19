
public class SQLQuery {

	final public String SQLReadBranches  = "SELECT * FROM tbl_library_branch lb;",
			SQLReadBooks     = "SELECT * FROM tbl_book_copies bc JOIN (tbl_book b, tbl_library_branch lb) "
					+ "ON (b.bookId=bc.bookId AND lb.branchId=bc.branchId) WHERE lb.branchName=? AND bc.noOfCopies > 0;",
			SQLAddCheckOut   = "INSERT INTO tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate);"
							+ "VALUE (?,?,?,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 7 DAY));",
			SQLIDCheck       = "SELECT * FROM tbl_borrower br WHERE br.cardNo=?;",
			SQLBorrowedBook  = "SELECT * FROM tbl_book_loans bl JOIN tbl_book b ON (bl.bookId=b.bookId) WHERE bl.dateIn IS NULL AND bl.cardNo=?;",
			SQLReturnBook    = "UPDATE tbl_book_loans bl SET bl.dateIn=CURDATE() WHERE bl.bookId=?",
			SQLUpdBranchName = "UPDATE tbl_library_branch lb SET lb.branchName=? WHERE lb.branchId=?",
			SQLUpdBranchAddr = "UPDATE tbl_library_branch lb SET lb.branchAddress=? WHERE lb.branchId=?",
			SQLRdAllBooks    = "SELECT * FROM tbl_book_copies bc JOIN (tbl_book b, tbl_library_branch lb) "
					+ "ON (b.bookId=bc.bookId AND lb.branchId=bc.branchId) WHERE lb.branchId=?",
			SQLRdBookCopies  = "SELECT * FROM tbl_book_copies bc WHERE bc.bookId=?",
			SQLUpdBookCopies = "UPDATE tbl_book_copies bc SET bc.noOfCopies=? WHERE bc.bookId=? AND bc.branchId=?",
			SQLShowAllBks    = "SELECT * FROM tbl_book b",
			SQLShowAllAus    = "SELECT * FROM tbl_author a",
			SQLShowAllPbs    = "SELECT * FROM tbl_publisher p",
			SQLShowAllBch    = "SELECT * FROM tbl_library_branch lb",
			SQLShowAllBrw	= "SELECT * FROM tbl_borrower br",
			SQLShowAllLoan   = "SELECT * FROM tbl_book_loans bl, tbl_book b, tbl_borrower br, tbl_library_branch lb WHERE "
					+ "b.bookId=bl.bookId AND br.cardNo=bl.cardNo AND lb.branchId=bl.branchId",
			SQLUpdateBkName  = "UPDATE tbl_book b SET b.title=? WHERE b.bookId=?",
			SQLUpdateBkName2 = "UPDATE tbl_book b SET b.title=?, b.pubId=? WHERE b.bookId=?",
			SQLInsertBkAu    = "INSERT INTO tbl_book_authors VALUE (?,?)",
			SQLUpdateBkAu    = "UPDATE tbl_book_authors ba SET ba.authorId=? WHERE ba.bookId=?",
			SQLUpdateAuName  = "UPDATE tbl_author a SET a.authorName=? WHERE a.authorId=?",
			SQLInsertBkName  = "INSERT INTO tbl_book(title,pubId) VALUE(?,?)",
			SQLInsertAuName  = "INSERT INTO tbl_author(authorName) VALUE(?)",
			SQLDeleteBkName  = "DELETE FROM tbl_book WHERE bookId=?",
			SQLDeleteAuName  = "DELETE FROM tbl_author WHERE authorId=?",
			SQLInsertPbName  = "INSERT INTO tbl_publisher(publisherName, publisherAddress, publisherPhone) VALUE(?,?,?)",
			SQLUpdatePbName	= "UPDATE tbl_publisher p SET p.publisherName=?, p.publisherAddress=?, p.publisherPhone=? WHERE p.publisherId=?",
			SQLDeletePbName  = "DELETE FROM tbl_publisher WHERE publisherId=?",
			SQLInsertBchName = "INSERT INTO tbl_library_branch(branchName, branchAddress) VALUE(?,?)",
			SQLUpdateBchName = "UPDATE tbl_library_branch lb SET lb.branchName=?, lb.branchAddress=? WHERE lb.branchId=?",
			SQLDeleteBchName = "DELETE FROM tbl_library_branch WHERE branchId=?",
			SQLInsertBrwName = "INSERT INTO tbl_borrower(name, address, phone) VALUE(?,?,?)",
			SQLUpdateBrwName = "UPDATE tbl_borrower br SET br.name=?, br.address=?, br.phone=? WHERE br.cardNo=?",
			SQLDeleteBrwName = "DELETE FROM tbl_borrower WHERE cardNo=?",
			SQLUpdateDueDate = "UPDATE tbl_book_loans bl SET bl.dueDate=? WHERE bl.bookId=? AND bl.branchId=? AND bl.cardNo=?",
			SQLCheckBkName	= "SELECT * FROM tbl_book b WHERE b.title=?",
			SQLCheckAuName   = "SELECT * FROM tbl_author a WHERE a.authorName=?",
			SQLCheckPbName   = "SELECT * FROM tbl_publisher p WHERE p.publisherName=? AND p.publisherAddress=?",
			SQLCheckBchName  = "SELECT * FROM tbl_library_branch lb WHERE lb.branchName=? AND lb.branchAddress=?",
			SQLBrBookCheck  = "SELECT * FROM tbl_book_loans bl WHERE bl.bookId=? AND bl.branchId=? AND bl.cardNo=?",
			SQLUpdBrBook    = "UPDATE tbl_book_loans bl SET bl.dateOut=NOW(), bl.dueDate=DATE_ADD(CURDATE(),INTERVAL 7 DAY), bl.dateIn=NULL "
					+ "WHERE bl.bookId=? AND bl.cardNo=?",
			SQLDrsBookCpy   = "UPDATE tbl_book_copies bc SET bc.noOfCopies=bc.noOfCopies-1 WHERE bc.bookId=? AND bc.branchId=?",
			SQLIrsBookCpy   = "UPDATE tbl_book_copies bc SET bc.noOfCopies=bc.noOfCopies+1 WHERE bc.bookId=? AND bc.branchId=?";
	private static SQLQuery instance;
	//implementing singleton pattern
	public synchronized static SQLQuery getSQLQuery() {
		if(instance == null){
			instance = new SQLQuery();
			
		}
		return instance;
		// TODO Auto-generated constructor stub
	}
}
