import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

class DB {
	private final String DB_URL;

	public DB() {
		this.DB_URL = this.getDBURL();
	}

	private String getDBURL() {
		try (BufferedReader reader = new BufferedReader(new FileReader("./.env"))) {
			String line = reader.readLine();

			if (line == null) return null;

			return line.substring(line.indexOf("postgres"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Connection getConnection() throws SQLException {
		if (this.DB_URL == null) return null;

		return DriverManager.getConnection("jdbc:" + this.DB_URL);
	}

	public ResultSet query(String sqlQuery) throws SQLException {
		Connection con = this.getConnection();
		if (con == null) throw new SQLException("Connection is not valid.");

		Statement stmt = con.createStatement();

		System.out.println(sqlQuery);

		return stmt.executeQuery(sqlQuery);
	}

	public int queryUpdate(String sqlQuery) throws SQLException {
		Connection con = this.getConnection();
		if (con == null) throw new SQLException("Connection is not valid.");

		Statement stmt = con.createStatement();

		return stmt.executeUpdate(sqlQuery);
	}
}

public class Main {
	public static void main(String[] args) throws SQLException {
		DB db = new DB();
	}
}