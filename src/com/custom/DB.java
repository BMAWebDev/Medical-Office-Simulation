package com.custom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.TreeMap;

public class DB {
	private final String DB_URL;

	public DB() {
		this.DB_URL = this.getDbUrl();
	}

	private String getDbUrl() {
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

	public String getSqlFromMap(String tableName, TreeMap<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ")
			.append(tableName)
			.append(" (")
			.append(String.join(", ", map.keySet()))
			.append(") VALUES (");

		for (Object value : map.values()) {

			sb.append("'").append(value).append("'");
			if (map.lastEntry().getValue() == value) continue;
			sb.append(", ");
		}

		sb.append(");");

		return sb.toString();
	}
}

