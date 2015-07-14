package com.movie.search.persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlConnection {
	private Connection connection = null;

	private static final String DB_NAME = "movie_search";
	private static final String DB_USER_NAME = "root";
	private static final String DB_PASS = "py234pass";
	private static final String CONNECTION_TEMPL = "jdbc:mysql://localhost/$s?" + "user=%s&password=%s";

	private MoviePrepStatement insertStatement;
	private MoviePrepStatement selectStatement;

	public MySqlConnection() {

		String insertSt = "insert into movie_search.movies values (?, ?)";
		String selectLikeSt = "select m.title" + "from movie_search.movies m" + "where m.title like '%?%'"
				+ "or m.descr like '%?%'";

		insertStatement = new MoviePrepStatement(connection, insertSt);
		selectStatement = new MoviePrepStatement(connection, selectLikeSt);

	}

	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connection = (Connection) DriverManager
					.getConnection(String.format(CONNECTION_TEMPL, DB_NAME, DB_USER_NAME, DB_PASS));
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL driver is not available!");
		} catch (SQLException e) {
			System.out.println("Connection to database failed!");
		}
	}

	public void close() {
		try {
			insertStatement.close();
			selectStatement.close();

			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("Error occured while closing connection to database!");
		}
	}

	public void insert(final String title, final String description) {
		try {
			PreparedStatement prepStatement = insertStatement.getPrepStatement();
			prepStatement.setString(1, title);
			prepStatement.setString(2, description);
			prepStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("A database error occured. Movie was not inserted!");
		}
	}

	public ResultSet getMatching(final String textToMatch) {
		try {
			PreparedStatement prepStatement = selectStatement.getPrepStatement();
			prepStatement.setString(1, textToMatch);
			prepStatement.setString(2, textToMatch);
			return prepStatement.executeQuery();
		} catch (SQLException e) {
			System.out.println("A database error occured. Movie could not be found!");
			return null;
		}
	}
}
