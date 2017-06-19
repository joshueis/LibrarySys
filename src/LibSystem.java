import java.sql.*;
import java.util.*;

public class LibSystem {
	
	static Connection conn = null;

	public static void main(String[] args) {
		String custTab = "    "; //to add some indentation 
		int selection  = 4; // initialize to quit
		Scanner in                = new Scanner(System.in);
		LibSystem libSys          = new LibSystem();  //initialize class 
		BorrowProcedure borrow    = new BorrowProcedure(); //borrower
		LibrarianProcedure manage = new LibrarianProcedure(); //librarian
		AdminProcedure admin      = new AdminProcedure(); //admin
		libSys.connectDB();
		
		while (true) {//stay in the system
			System.out.println("Welcome to the GCIT Library Management System. Which category of a user are you");
			System.out.println(custTab + "1) Librarian");
			System.out.println(custTab + "2) Administrator");
			System.out.println(custTab + "3) Borrower");
			System.out.println(custTab + "4) Quit");
			selection = in.nextInt();
			switch (selection) {
				case 1: {
					manage.execute();
					continue;
				}
				case 2: {
					admin.execute();
					continue;
				}
				case 3: {
					borrow.execute();
					continue;
				}
				default: {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						System.out.println("Thank you!");
						//System.exit(0);
					}
					break;
				}
			}
		}
	}
	
	void connectDB(){
		String url = "jdbc:mysql://localhost/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
		//jdbc:mysql://127.0.0.1:3306/library?useSSL=false";
		final String username = "root";
		final String password = "1719002634";
		
		try {
			conn = DriverManager.getConnection(url,username,password);
			conn.setAutoCommit(false);
		} catch(SQLException e) {
			System.out.println("Unable to connect to database!");
			e.printStackTrace();
			return;
		}
	}
	
	protected ResultSet executeSQL(String SQL) throws SQLException{
		ResultSet rs = null; //query would return a resultset
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(SQL);	
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
		}
		return rs;
	}
	
	protected ResultSet executeSQL(List<Object> para, String SQL) throws SQLException {
		//to avoid injection 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(SQL);
			for (int i = 0; i < para.size(); i++){
				if (para.get(i).getClass() == Integer.class){
					pstmt.setInt(i+1, (int)para.get(i));
				}
				else if (para.get(i).getClass() == String.class){
					pstmt.setString(i+1, para.get(i).toString());
				}
			}
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
		} 
		return rs;
	}
	
	protected void executeUpdateSQL(List<Object> para, String SQL) throws SQLException{
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(SQL);
			//set up the arguments passed to sql query -> branchName=? ... ? replaced by arguments passed
			for (int i = 0; i < para.size(); i++){
				if (para.get(i).getClass() == Integer.class){
					pstmt.setInt(i+1, (int)para.get(i));
				}
				else if (para.get(i).getClass() == String.class){
					pstmt.setString(i+1, para.get(i).toString());
				}
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}
	}
	
	protected void unholdExecuteSQL() throws SQLException{
		conn.commit();
	}
}
