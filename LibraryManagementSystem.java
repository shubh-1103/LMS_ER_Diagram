package com.library;

import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/library";
	private static final String USER = "root";
	private static final String PASS = "Shubham@11";
	private static Connection conn = null;
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			boolean exit = false;
			while (!exit) {
				System.out.println(
						"-------------------------------------------------------------------------------------------------------");
				System.out.println("Library Management System");
				System.out.println(
						"-------------------------------------------------------------------------------------------------------");
				System.out.println("1. Manage Books");
				System.out.println("2. Manage Members");
				System.out.println("3. Manage Publishers");
				System.out.println("4. Manage Categories");
				System.out.println("5. Manage Borrowings");
				System.out.println("6. Exit");
				System.out.println(
						"-------------------------------------------------------------------------------------------------------");
				System.out.print("Select an option: ");
				int choice = scanner.nextInt();
				scanner.nextLine(); // consume newline

				switch (choice) {
				case 1:
					manageBooks();
					break;
				case 2:
					manageMembers();
					break;
				case 3:
					managePublishers();
					break;
				case 4:
					manageCategories();
					break;
				case 5:
					manageBorrowings();
					break;
				case 6:
					exit = true;
					System.out.println(
							"-------------------------------------------------------------------------------------------------------");
					System.out.println("Thank you for using our library.");
					System.out.println(
							"-------------------------------------------------------------------------------------------------------");
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// Manage Books
	private static void manageBooks() throws SQLException {
		boolean back = false;
		while (!back) {
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("Manage Books");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("1. Add Book");
			System.out.println("2. View Books");
			System.out.println("3. Update Book");
			System.out.println("4. Delete Book");
			System.out.println("5. Back");
			System.out.print("Select an option: ");
			int choice = scanner.nextInt();
			scanner.nextLine(); // consume newline

			switch (choice) {
			case 1:
				addBook();
				break;
			case 2:
				viewBooks();
				break;
			case 3:
				updateBook();
				break;
			case 4:
				deleteBook();
				break;
			case 5:
				back = true;
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private static void addBook() throws SQLException {
		System.out.print("Enter book title: ");
		String title = scanner.nextLine();
		System.out.print("Enter category ID: ");
		int categoryId = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter publisher ID: ");
		int publisherId = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter price: ");
		double price = scanner.nextDouble();
		scanner.nextLine(); // consume newline
		System.out.print("Enter quantity: ");
		int quantity = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter availability: ");
		boolean availability = scanner.nextBoolean();
		scanner.nextLine(); // consume newline

		String query = "INSERT INTO Books (title, category_id, publisher_id, price, quantity, availability) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, title);
		pstmt.setInt(2, categoryId);
		pstmt.setInt(3, publisherId);
		pstmt.setDouble(4, price);
		pstmt.setInt(5, quantity);
		pstmt.setBoolean(6, availability);
		pstmt.executeUpdate();
		System.out.println("Book added successfully.");
	}

	private static void viewBooks() throws SQLException {
		String query = "SELECT b.id, b.title, c.name AS category, p.name AS publisher, b.price, b.quantity, b.availability FROM Books b "
				+ "JOIN Categories c ON b.category_id = c.id " + "JOIN Publishers p ON b.publisher_id = p.id";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.isBeforeFirst()) {
			System.out.println("The book list is empty.");
		} else {
			System.out.printf("%-5s %-20s %-20s %-20s %-10s %-10s %-15s%n", "ID", "Title", "Category", "Publisher",
					"Price", "Quantity", "Availability");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.printf("%-5d %-20s %-20s %-20s %-10.2f %-10d %-15b%n", rs.getInt("id"),
						rs.getString("title"), rs.getString("category"), rs.getString("publisher"),
						rs.getDouble("price"), rs.getInt("quantity"), rs.getBoolean("availability"));
			}
		}
	}

	private static void updateBook() throws SQLException {
		System.out.print("Enter book ID to update: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new book title: ");
		String title = scanner.nextLine();
		System.out.print("Enter new category ID: ");
		int categoryId = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new publisher ID: ");
		int publisherId = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new price: ");
		double price = scanner.nextDouble();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new quantity: ");
		int quantity = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter new availability: ");
		boolean availability = scanner.nextBoolean();
		scanner.nextLine(); // consume newline

		String query = "UPDATE Books SET title = ?, category_id = ?, publisher_id = ?, price = ?, quantity = ?, availability = ? WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, title);
		pstmt.setInt(2, categoryId);
		pstmt.setInt(3, publisherId);
		pstmt.setDouble(4, price);
		pstmt.setInt(5, quantity);
		pstmt.setBoolean(6, availability);
		pstmt.setInt(7, id);
		pstmt.executeUpdate();
		System.out.println("Book updated successfully.");
	}

	private static void deleteBook() throws SQLException {
		System.out.print("Enter book ID to delete: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline

		String query = "DELETE FROM Books WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		System.out.println("Book deleted successfully.");
	}

	// Manage Members
	private static void manageMembers() throws SQLException {
		boolean back = false;
		while (!back) {
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("Manage Members");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("1. Add Member");
			System.out.println("2. View Members");
			System.out.println("3. Update Member");
			System.out.println("4. Delete Member");
			System.out.println("5. Back");
			System.out.print("Select an option: ");
			int choice = scanner.nextInt();
			scanner.nextLine(); // consume newline

			switch (choice) {
			case 1:
				addMember();
				break;
			case 2:
				viewMembers();
				break;
			case 3:
				updateMember();
				break;
			case 4:
				deleteMember();
				break;
			case 5:
				back = true;
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private static void addMember() throws SQLException {
		System.out.print("Enter member name: ");
		String name = scanner.nextLine();
		System.out.print("Enter member address: ");
		String address = scanner.nextLine();
		System.out.print("Enter membership type: ");
		String membershipType = scanner.nextLine();
		System.out.print("Enter membership info: ");
		String membershipInfo = scanner.nextLine();
		System.out.print("Enter expiry date (YYYY-MM-DD): ");
		String expiryDate = scanner.nextLine();

		String query = "INSERT INTO Members (name, address, membership_type, membership_info, expiry_date) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.setString(2, address);
		pstmt.setString(3, membershipType);
		pstmt.setString(4, membershipInfo);
		pstmt.setString(5, expiryDate);
		pstmt.executeUpdate();
		System.out.println("Member added successfully.");
	}

	private static void viewMembers() throws SQLException {
		String query = "SELECT * FROM Members";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.isBeforeFirst()) {
			System.out.println("The member list is empty.");
		} else {
			System.out.printf("%-5s %-20s %-30s %-20s %-30s %-15s%n", "ID", "Name", "Address", "Membership Type",
					"Membership Info", "Expiry Date");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.printf("%-5d %-20s %-30s %-20s %-30s %-15s%n", rs.getInt("id"), rs.getString("name"),
						rs.getString("address"), rs.getString("membership_type"), rs.getString("membership_info"),
						rs.getString("expiry_date"));
			}
		}
	}

	private static void updateMember() throws SQLException {
		System.out.print("Enter member ID to update: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new member name: ");
		String name = scanner.nextLine();
		System.out.print("Enter new member address: ");
		String address = scanner.nextLine();
		System.out.print("Enter new membership type: ");
		String membershipType = scanner.nextLine();
		System.out.print("Enter new membership info: ");
		String membershipInfo = scanner.nextLine();
		System.out.print("Enter new expiry date (YYYY-MM-DD): ");
		String expiryDate = scanner.nextLine();

		String query = "UPDATE Members SET name = ?, address = ?, membership_type = ?, membership_info = ?, expiry_date = ? WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.setString(2, address);
		pstmt.setString(3, membershipType);
		pstmt.setString(4, membershipInfo);
		pstmt.setString(5, expiryDate);
		pstmt.setInt(6, id);
		pstmt.executeUpdate();
		System.out.println("Member updated successfully.");
	}

	private static void deleteMember() throws SQLException {
		System.out.print("Enter member ID to delete: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline

		String query = "DELETE FROM Members WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		System.out.println("Member deleted successfully.");
	}

	// Manage Publishers
	private static void managePublishers() throws SQLException {
		boolean back = false;
		while (!back) {
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("Manage Publishers");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("1. Add Publisher");
			System.out.println("2. View Publishers");
			System.out.println("3. Update Publisher");
			System.out.println("4. Delete Publisher");
			System.out.println("5. Back");
			System.out.print("Select an option: ");
			int choice = scanner.nextInt();
			scanner.nextLine(); // consume newline

			switch (choice) {
			case 1:
				addPublisher();
				break;
			case 2:
				viewPublishers();
				break;
			case 3:
				updatePublisher();
				break;
			case 4:
				deletePublisher();
				break;
			case 5:
				back = true;
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private static void addPublisher() throws SQLException {
		System.out.print("Enter publisher name: ");
		String name = scanner.nextLine();
		System.out.print("Enter publisher address: ");
		String address = scanner.nextLine();

		String query = "INSERT INTO Publishers (name, address) VALUES (?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.setString(2, address);
		pstmt.executeUpdate();
		System.out.println("Publisher added successfully.");
	}

	private static void viewPublishers() throws SQLException {
		String query = "SELECT * FROM Publishers";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.isBeforeFirst()) {
			System.out.println("The publisher list is empty.");
		} else {
			System.out.printf("%-5s %-20s %-30s%n", "ID", "Name", "Address");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.printf("%-5d %-20s %-30s%n", rs.getInt("id"), rs.getString("name"), rs.getString("address"));
			}
		}
	}

	private static void updatePublisher() throws SQLException {
		System.out.print("Enter publisher ID to update: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new publisher name: ");
		String name = scanner.nextLine();
		System.out.print("Enter new publisher address: ");
		String address = scanner.nextLine();

		String query = "UPDATE Publishers SET name = ?, address = ? WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.setString(2, address);
		pstmt.setInt(3, id);
		pstmt.executeUpdate();
		System.out.println("Publisher updated successfully.");
	}

	private static void deletePublisher() throws SQLException {
		System.out.print("Enter publisher ID to delete: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline

		String query = "DELETE FROM Publishers WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		System.out.println("Publisher deleted successfully.");
	}

	// Manage Categories
	private static void manageCategories() throws SQLException {
		boolean back = false;
		while (!back) {
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("Manage Categories");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("1. Add Category");
			System.out.println("2. View Categories");
			System.out.println("3. Update Category");
			System.out.println("4. Delete Category");
			System.out.println("5. Back");
			System.out.print("Select an option: ");
			int choice = scanner.nextInt();
			scanner.nextLine(); // consume newline

			switch (choice) {
			case 1:
				addCategory();
				break;
			case 2:
				viewCategories();
				break;
			case 3:
				updateCategory();
				break;
			case 4:
				deleteCategory();
				break;
			case 5:
				back = true;
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private static void addCategory() throws SQLException {
		System.out.print("Enter category name: ");
		String name = scanner.nextLine();

		String query = "INSERT INTO Categories (name) VALUES (?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.executeUpdate();
		System.out.println("Category added successfully.");
	}

	private static void viewCategories() throws SQLException {
		String query = "SELECT * FROM Categories";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.isBeforeFirst()) {
			System.out.println("The category list is empty.");
		} else {
			System.out.printf("%-5s %-20s%n", "ID", "Name");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.printf("%-5d %-20s%n", rs.getInt("id"), rs.getString("name"));
			}
		}
	}

	private static void updateCategory() throws SQLException {
		System.out.print("Enter category ID to update: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new category name: ");
		String name = scanner.nextLine();

		String query = "UPDATE Categories SET name = ? WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, name);
		pstmt.setInt(2, id);
		pstmt.executeUpdate();
		System.out.println("Category updated successfully.");
	}

	private static void deleteCategory() throws SQLException {
		System.out.print("Enter category ID to delete: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline

		String query = "DELETE FROM Categories WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		System.out.println("Category deleted successfully.");
	}

	// Manage Borrowings
	private static void manageBorrowings() throws SQLException {
		boolean back = false;
		while (!back) {
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("Manage Borrowings");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			System.out.println("1. Add Borrowing");
			System.out.println("2. View Borrowings");
			System.out.println("3. Update Borrowing");
			System.out.println("4. Delete Borrowing");
			System.out.println("5. Back");
			System.out.print("Select an option: ");
			int choice = scanner.nextInt();
			scanner.nextLine(); // consume newline

			switch (choice) {
			case 1:
				addBorrowing();
				break;
			case 2:
				viewBorrowings();
				break;
			case 3:
				updateBorrowing();
				break;
			case 4:
				deleteBorrowing();
				break;
			case 5:
				back = true;
				break;
			default:
				System.out.println("Invalid choice. Please try again.");
			}
		}
	}

	private static void addBorrowing() throws SQLException {
		System.out.print("Enter book ID: ");
		int bookId = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter member ID: ");
		int memberId = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter borrow date (YYYY-MM-DD): ");
		String borrowDate = scanner.nextLine();
		System.out.print("Enter return date (YYYY-MM-DD): ");
		String returnDate = scanner.nextLine();

		String query = "INSERT INTO Borrowings (book_id, member_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, bookId);
		pstmt.setInt(2, memberId);
		pstmt.setDate(3, Date.valueOf(borrowDate));
		pstmt.setDate(4, Date.valueOf(returnDate));
		pstmt.executeUpdate();
		System.out.println("Borrowing added successfully.");
	}

	private static void viewBorrowings() throws SQLException {
		String query = "SELECT br.id, b.title AS book_title, m.name AS member_name, br.borrow_date, br.return_date FROM Borrowings br "
				+ "JOIN Books b ON br.book_id = b.id " + "JOIN Members m ON br.member_id = m.id";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		if (!rs.isBeforeFirst()) {
			System.out.println("The borrowings list is empty.");
		} else {
			System.out.printf("%-5s %-20s %-20s %-15s %-15s%n", "ID", "Book Title", "Member Name", "Borrow Date",
					"Return Date");
			System.out.println(
					"-------------------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.printf("%-5d %-20s %-20s %-15s %-15s%n", rs.getInt("id"), rs.getString("book_title"),
						rs.getString("member_name"), rs.getDate("borrow_date"), rs.getDate("return_date"));
			}
		}
	}

	private static void updateBorrowing() throws SQLException {
		System.out.print("Enter borrowing ID to update: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new book ID: ");
		int bookId = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new member ID: ");
		int memberId = scanner.nextInt();
		scanner.nextLine(); // consume newline
		System.out.print("Enter new borrow date (YYYY-MM-DD): ");
		String borrowDate = scanner.nextLine();
		System.out.print("Enter new return date (YYYY-MM-DD): ");
		String returnDate = scanner.nextLine();

		String query = "UPDATE Borrowings SET book_id = ?, member_id = ?, borrow_date = ?, return_date = ? WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, bookId);
		pstmt.setInt(2, memberId);
		pstmt.setDate(3, Date.valueOf(borrowDate));
		pstmt.setDate(4, Date.valueOf(returnDate));
		pstmt.setInt(5, id);
		pstmt.executeUpdate();
		System.out.println("Borrowing updated successfully.");
	}

	private static void deleteBorrowing() throws SQLException {
		System.out.print("Enter borrowing ID to delete: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // consume newline

		String query = "DELETE FROM Borrowings WHERE id = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		System.out.println("Borrowing deleted successfully.");
	}
}
