package com.custom;

import java.time.LocalDateTime;
import java.util.TreeMap;

public class Appointment {
	private final int client_id, medic_id;
	private final LocalDateTime date;
	private String address = "online";

	public Appointment(int client_id, int medic_id, LocalDateTime date) {
		this.client_id = client_id;
		this.medic_id = medic_id;
		this.date = date;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public TreeMap<String, Object> getAppointmentDetails() {
		TreeMap<String, Object> appointmentDetails = new TreeMap<>();

		appointmentDetails.put("client_id", this.client_id);
		appointmentDetails.put("medic_id", this.medic_id);
		appointmentDetails.put("date", this.date);
		appointmentDetails.put("address", this.address);

		return appointmentDetails;
	}
}
