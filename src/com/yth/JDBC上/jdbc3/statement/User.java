package com.yth.JDBC上.jdbc3.statement;

public class User {

	private String user;
	private String password;

	public User() {
	}

	public User(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}

	@Override
	public String toString() {
		return "com.yth.statement.User [user=" + user + ", password=" + password + "]";
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
