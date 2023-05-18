package com.custom;

import java.time.LocalDate;
import java.util.TreeMap;

enum DoctorRank {Nurse, FamilyDoctor, Surgeon}

public class Medic extends Person {
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
	public TreeMap<String, Object> getPersonalInfo() {
		TreeMap<String, Object> personalInfo = super.getPersonalInfo();

		personalInfo.put("employed_date", this.employed_date);
		personalInfo.put("rank", this.rank.name());
		if (this.office_location != null)
			personalInfo.put("office_location", this.office_location);

		if (this.email != null)
			personalInfo.put("email", this.email);

		return personalInfo;
	}
}

