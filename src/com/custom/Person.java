package com.custom;

import java.util.TreeMap;

public class Person {
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

	public TreeMap<String, Object> getPersonalInfo() {
		TreeMap<String, Object> personalInfo = new TreeMap<>();

		personalInfo.put("first_name", first_name);
		personalInfo.put("last_name", last_name);
		personalInfo.put("phone_number", phone_number);

		return personalInfo;
	}
}

