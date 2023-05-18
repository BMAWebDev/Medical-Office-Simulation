package com.custom;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

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

public class Menu {
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
		System.out.println("Welcome! Please answer the questions only with the numbers shown. Any other response will result in exiting the menu.");
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

		if (parsedUserInput == 1 || parsedUserInput == 0) this.exitMenu();
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

	private void handleAppointment() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("First things first we will need to call you somehow. What's your first name?");
		String first_name = scanner.nextLine();

		System.out.println("...and your last name?");
		String last_name = scanner.nextLine();

		System.out.println("A phone number is mandatory in order to continue with your appointment:");
		String phone_number = scanner.nextLine();

		System.out.println("As well as your personal identification number (EN: PIN - RO: CNP):");
		String cnp = scanner.nextLine();

		System.out.println("We need to know when were you born (DD-MM-YYYY):");
		String dob = scanner.nextLine();
		String[] dobParts = dob.split("-");

		LocalDate date_of_birth;

		try {
			date_of_birth = LocalDate.of(Integer.parseInt(dobParts[2]), Integer.parseInt(dobParts[1]), Integer.parseInt(dobParts[0]));
		} catch (NumberFormatException | DateTimeException | ArrayIndexOutOfBoundsException ex) {
			System.out.println("You need to enter a valid date.");
			this.confirmCancel();
			return;
		}

		System.out.println("We need to know where you live:");
		String address = scanner.nextLine();

		// optional field
		System.out.println("Last but not least, if you want us to send you your results via email, please enter it. Otherwise, skip this field");
		String email = scanner.nextLine();

		System.out.println("Before we finish the reservation, please revise the details you have entered:");

		System.out.println("Name: " + String.format("%s %s", first_name, last_name));
		System.out.println("Phone number: " + phone_number);
		System.out.println("CNP: " + cnp);
		System.out.println("Date of birth: " + date_of_birth);
		System.out.println("Address: " + address);
		if (email.length() > 0) System.out.println("Email: " + email);

		String validationQuestion = "Are these the correct details? If not, select the second option to restart the form.";
		ArrayList<HashMap<String, Object>> validationAnswers = new ArrayList<>();
		Answer answer1 = new Answer("Yes, please continue with the appointment (1)", 1);
		Answer answer2 = new Answer("No, restart the form (2)", 2);
		Collections.addAll(validationAnswers, answer1.getAnswer(), answer2.getAnswer());

		int parsedUserInput = this.handleStepUserInput(validationQuestion, validationAnswers);
		if (parsedUserInput == 1) { // user accepts to continue
			// db stuff here to create appointment
			Client client = new Client(first_name, last_name, phone_number, cnp, date_of_birth, address);
			if (email.length() > 0) client.setEmail(email);
			System.out.println(client.getPersonalInfo());

			DB db = new DB();
			System.out.println(db.getSqlFromMap("clients", client.getPersonalInfo()));
		} else if (parsedUserInput == 2) { // user denies the continuation in order to restart the form
			return; // this restarts the step, so it also restarts the details form
		} else { // user presses enters any other input
			this.exitMenu();
			return;
		}

		this.exitMenu();
	}

	public void handleStep() {
		if (this.step != 1 && this.step != 2 && this.step != 3) return;

		String question;
		ArrayList<HashMap<String, Object>> answers = new ArrayList<>();

		if (this.step == 1) {
			question = "Do you want to make an appointment?";

			Answer answer1 = new Answer("Yes (1)", 1);
			Answer answer2 = new Answer("No (2)", 2, true);
			Collections.addAll(answers, answer1.getAnswer(), answer2.getAnswer());
		} else if (this.step == 2) {
			question = "Please confirm that you have read the Terms and Conditions and you agree with them:";
			Answer answer1 = new Answer("I agree, please continue (1)", 1);
			Answer answer2 = new Answer("I do not agree and i want to exit the appointment (2)", 2, true);
			Collections.addAll(answers, answer1.getAnswer(), answer2.getAnswer());
		} else {
			this.handleAppointment();
			return;
		}

		int parsedUserInput = this.handleStepUserInput(question, answers);

		if (shouldContinueStep(answers, parsedUserInput)) this.step++;
		else this.confirmCancel();
	}
}

