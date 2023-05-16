import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

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

class Client extends Person {
	private final LocalDate date_of_birth;
	public int client_id;
	private String email, cnp, address;

	public Client(int _client_id, String _first_name, String _last_name, String _phone_number, String _cnp, LocalDate _date_of_birth, String _address) {
		super(_first_name, _last_name, _phone_number);

		this.client_id = _client_id;
		this.date_of_birth = _date_of_birth;
		this.cnp = _cnp;
		this.address = _address;
	}

	public Client(int _client_id, String _first_name, String _last_name, String _phone_number, String _cnp, LocalDate _date_of_birth, String _address, String _email) {
		super(_first_name, _last_name, _phone_number);

		this.client_id = _client_id;
		this.date_of_birth = _date_of_birth;
		this.cnp = _cnp;
		this.address = _address;
		this.email = _email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String _email) {
		this.email = _email;
	}

	public String getCNP() {
		return cnp;
	}

	public void setCNP(String _cnp) {
		this.cnp = _cnp;
	}

	@Override
	public Map<String, String> getPersonalInfo() {
		Map<String, String> personalInfo = super.getPersonalInfo();

		personalInfo.put("client_id", Integer.toString(this.client_id));
		personalInfo.put("date_of_birth", this.date_of_birth.toString());
		personalInfo.put("address", this.address);
		personalInfo.put("cnp", this.cnp);
		personalInfo.put("email", this.email);

		return personalInfo;
	}
}

class Answer {
	private final String label;
	private final int answerNumber;
	private boolean shouldExit = false;

	public Answer(String _label, int _stepNumber) {
		this.label = _label;
		this.answerNumber = _stepNumber;
	}

	public Answer(String _label, int _stepNumber, boolean _shouldExit) {
		this.label = _label;
		this.shouldExit = _shouldExit;
		this.answerNumber = _stepNumber;
	}

	public HashMap<String, Object> getAnswer() {
		HashMap<String, Object> answer = new HashMap<>();

		answer.put("label", label);
		answer.put("answerNumber", answerNumber);
		answer.put("shouldExit", shouldExit);

		return answer;
	}
}

class Menu {
	private static Menu menu; // Singleton
	private final int steps = 4;
	public Boolean isOpened = false;
	private int step;

	private Menu() {
		this.step = 1;
	}

	public static Menu getInstance() {
		if (menu == null) menu = new Menu();

		return menu;
	}

	public void openMenu() {
		this.isOpened = true;
		System.out.println("Welcome! Please answer the question only with the numbers shown. Any other response will result in exiting the menu.");
	}

	private String getAnswers(ArrayList<HashMap<String, Object>> _answers) {
		StringBuilder sb = new StringBuilder();

		for (HashMap<String, Object> answer : _answers) {
			String label = answer.get("label").toString();
			sb.append(label).append("\n");
		}

		return sb.toString();
	}

	public Boolean isLastStep() {
		return this.step == this.steps;
	}

	private void exitMenu() {
		this.isOpened = false;
		System.out.println("The menu was stopped.");
	}

	private boolean shouldContinueStep(ArrayList<HashMap<String, Object>> _answers, int _parsedAnswerInput) {
		int[] answersNumbersList = IntStream.range(1, _answers.size() + 1).toArray();
		boolean isStepInput = Arrays.stream(answersNumbersList).anyMatch(n -> n == _parsedAnswerInput);

		boolean shouldContinue = !this.isLastStep() && isStepInput;

		for (HashMap<String, Object> answer : _answers) {
			int answerNumber = (int) answer.get("answerNumber");
			boolean shouldExit = (boolean) answer.get("shouldExit");

			if (_parsedAnswerInput == answerNumber && shouldExit) {
				shouldContinue = false;
				break;
			}
		}

		return shouldContinue;
	}

	private void confirmCancel() {
		String question = "Are you sure you want to exit?";
		ArrayList<HashMap<String, Object>> answers = new ArrayList<>();

		Answer answer1 = new Answer("Yes, exit the menu (1)", 1, true);
		Answer answer2 = new Answer("No, return to the last question (2)", 2);
		Collections.addAll(answers, answer1.getAnswer(), answer2.getAnswer());

		int parsedUserInput = this.handleStepUserInput(question, answers);

		if (parsedUserInput == 1) this.exitMenu();
	}

	private int handleStepUserInput(String question, ArrayList<HashMap<String, Object>> answers) {
		this.showStepAnswers(question, answers);

		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		int parsedAnswerInput;
		try {
			parsedAnswerInput = Integer.parseInt(input.trim());
		} catch (NumberFormatException ignored) {
			parsedAnswerInput = 0;
		}

		return parsedAnswerInput;
	}

	private void showStepAnswers(String _question, ArrayList<HashMap<String, Object>> _answers) {
		String stepHandler = String.format("%s\n%s", _question, this.getAnswers(_answers));

		System.out.println(stepHandler);
	}

	public void handleStep() {
		if (this.step != 1 && this.step != 2) return;

		String question;
		ArrayList<HashMap<String, Object>> answers = new ArrayList<>();

		if (this.step == 1) {
			question = "Do you want to make an appointment?";

			Answer answer1 = new Answer("Yes (1)", 1);
			Answer answer2 = new Answer("No (2)", 2, true);
			Collections.addAll(answers, answer1.getAnswer(), answer2.getAnswer());
		} else {
			question = "Please confirm that you have read the Terms and Conditions and you agree with them:";
			Answer answer1 = new Answer("I agree, please continue (1)", 1);
			Answer answer2 = new Answer("I do not agree and i want to exit the appointment (2)", 2, true);
			Collections.addAll(answers, answer1.getAnswer(), answer2.getAnswer());
		}

		int parsedUserInput = this.handleStepUserInput(question, answers);

		if (shouldContinueStep(answers, parsedUserInput)) this.step++;
		else this.confirmCancel();
	}
}

public class Main {
	public static void main(String[] args) {
		Menu menu = Menu.getInstance();
		menu.openMenu();

		while (menu.isOpened) {
			menu.handleStep();
		}
	}
}