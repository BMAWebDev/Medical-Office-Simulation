package com.custom;

import java.time.LocalDate;
import java.util.TreeMap;

public class Client extends Person {
	private final LocalDate date_of_birth;
	public int client_id;
	private String email, cnp, address;

	public Client(String _first_name, String _last_name, String _phone_number, String _cnp, LocalDate _date_of_birth, String _address) {
		super(_first_name, _last_name, _phone_number);

		this.date_of_birth = _date_of_birth;
		this.cnp = _cnp;
		this.address = _address;
	}

	public Client(String _first_name, String _last_name, String _phone_number, String _cnp, LocalDate _date_of_birth, String _address, String _email) {
		super(_first_name, _last_name, _phone_number);

		this.date_of_birth = _date_of_birth;
		this.cnp = _cnp;
		this.address = _address;
		this.email = _email;
	}

	public void setEmail(String _email) {
		this.email = _email;
	}

	@Override
	public TreeMap<String, Object> getPersonalInfo() {
		TreeMap<String, Object> personalInfo = super.getPersonalInfo();

		personalInfo.put("date_of_birth", this.date_of_birth);
		personalInfo.put("address", this.address);
		personalInfo.put("cnp", this.cnp);

		if (this.email != null)
			personalInfo.put("email", this.email);

		return personalInfo;
	}
}

