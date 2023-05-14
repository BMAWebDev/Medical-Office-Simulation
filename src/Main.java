import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

enum DoctorRank {Nurse, FamilyDoctor, Surgeon}

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

class Person {
	private String first_name, last_name, phone_number;

	public Person(String _first_name, String _last_name, String _phone_number) {
		this.first_name = _first_name;
		this.last_name = _last_name;
		this.phone_number = _phone_number;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String _last_name) {
		this.last_name = _last_name;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String _first_name) {
		this.first_name = _first_name;
	}

	public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String _phone_number) {
		this.phone_number = _phone_number;
	}

	public String getName() {
		return String.format("%s %s", first_name, last_name);
	}

	public Map<String, String> getPersonalInfo() {
		Map<String, String> personalInfo = new HashMap<>();

		personalInfo.put("first_name", first_name);
		personalInfo.put("last_name", last_name);
		personalInfo.put("phone_number", phone_number);

		return personalInfo;
	}
}

class Medic extends Person {
	private final LocalDate employed_date;
	public int medic_id;
	private String office_location, email;
	private DoctorRank rank;

	public Medic(int _medic_id, String _first_name, String _last_name, String _phone_number, LocalDate _employed_date, DoctorRank _rank) {
		super(_first_name, _last_name, _phone_number);

		this.medic_id = _medic_id;
		this.employed_date = _employed_date;
		this.rank = _rank;
	}

	public Medic(int _medic_id, String _first_name, String _last_name, String _phone_number, LocalDate _employed_date, DoctorRank _rank, String _office_location) {
		super(_first_name, _last_name, _phone_number);

		this.medic_id = _medic_id;
		this.employed_date = _employed_date;
		this.rank = _rank;
		this.office_location = _office_location;
	}

	public Medic(int _medic_id, String _first_name, String _last_name, String _phone_number, LocalDate _employed_date, DoctorRank _rank, String _office_location, String _email) {
		super(_first_name, _last_name, _phone_number);

		this.medic_id = _medic_id;
		this.employed_date = _employed_date;
		this.rank = _rank;
		this.office_location = _office_location;
		this.email = _email;
	}

	public LocalDate getEmployedDate() {
		return employed_date;
	}

	public DoctorRank getRank() {
		return rank;
	}

	public void setRank(DoctorRank _rank) {
		this.rank = _rank;
	}

	public String getOfficeLocation() {
		return office_location;
	}

	public void setOfficeLocation(String _office_location) {
		this.office_location = _office_location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String _email) {
		this.email = _email;
	}

	@Override
	public Map<String, String> getPersonalInfo() {
		Map<String, String> personalInfo = super.getPersonalInfo();

		personalInfo.put("medic_id", Integer.toString(this.medic_id));
		personalInfo.put("employed_date", this.employed_date.toString());
		personalInfo.put("rank", this.rank.name());
		personalInfo.put("office_location", this.office_location);
		personalInfo.put("email", this.email);

		return personalInfo;
	}
}

public class Main {
	public static void main(String[] args) throws SQLException {
		DB db = new DB();
	}
}