import java.sql.*;
import java.util.Scanner;

class Test {

    void display(Connection con, Statement st) throws SQLException {
        String query = "select * from Student"; // query to be run
        ResultSet rs = st.executeQuery(query); // execute query

        while (rs.next()) {
            String name = rs.getString("Name");
            System.out.println(name);
        }
        
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

    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/trig"; // Database URL
        String username = "root"; // MySQL username
        String password = "sql123";

        Class.forName("com.mysql.cj.jdbc.Driver"); // Driver name
        Connection con = DriverManager.getConnection(url, username, password);

        System.out.println("Connection Established successfully");
        Statement st = con.createStatement();

        Test t = new Test();
        t.display(con, st);
		t.insert(con, st);
		t.display(con, st);

        // Close resources
        st.close();
        con.close();
        System.out.println("Connection Closed....");
    }
}
