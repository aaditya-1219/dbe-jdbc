import java.sql.*;
import java.util.Scanner;

class Test {

    void display(Connection con, Statement st) throws SQLException {
        String query = "select * from Student"; // query to be run
        ResultSet rs = st.executeQuery(query); // execute query
        System.out.println();
        while (rs.next()) {
            String name = rs.getString("Name");
            int age = rs.getInt("age");
            int score = rs.getInt("Score");
            System.out.println(name + "\t" + age + "\t" + score);
        }
        System.out.println();
        // Close the ResultSet
        rs.close();
    }

	void insert(Connection con, Statement st) throws SQLException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter your name: ");
        String name = sc.nextLine();

        // Prompt the user for their age
        System.out.print("Enter your age: ");
        int age = sc.nextInt();

        // Prompt the user for their score
        System.out.print("Enter your score: ");
        int score = sc.nextInt();

        String insertQuery = "INSERT INTO Student (Name, Age, Score) VALUES (?, ?, ?)"; 
        PreparedStatement preparedStatement = con.prepareStatement(insertQuery);
        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, age);
        preparedStatement.setInt(3, score);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Data inserted successfully.");
        } else {
            System.out.println("Data insertion failed.");
        }

        preparedStatement.close();
    }

    void delete(Connection con, Statement st) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name of record to be deleted: ");
        String name = sc.nextLine();
    
        String deleteQuery = "DELETE FROM Student WHERE Name = ?"; 
        PreparedStatement preparedStatement = con.prepareStatement(deleteQuery);
        preparedStatement.setString(1, name);
    
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Data deleted successfully.");
        } else {
            System.out.println("Data deletion failed. No matching record found.");
        }
    
        preparedStatement.close();
    }

    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/trig"; // Database URL
        String username = "root"; // MySQL username
        String password = "root";

        Class.forName("com.mysql.cj.jdbc.Driver"); // Driver name
        Connection con = DriverManager.getConnection(url, username, password);

        System.out.println("Connection Established successfully");
        Statement st = con.createStatement();

        Test t = new Test();

        Scanner sc = new Scanner(System.in);
        Boolean loop = true;
        while(loop){
            System.out.println("Enter choice:");
            int choice;
            System.out.println("1. Display: ");
            System.out.println("2. Insert: ");
            System.out.println("3. Delete: ");
            System.out.println("0. Exit: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    t.display(con, st);
                    break;
                case 2:
                    t.insert(con, st);
                    break;
                case 3:
                    t.delete(con, st);
                    break;
                case 0:
                    loop = false;
                    break;
                default:
                    break;
            }
        }

        // Close resources
        st.close();
        con.close();
        System.out.println("Connection Closed....");
    }
}
