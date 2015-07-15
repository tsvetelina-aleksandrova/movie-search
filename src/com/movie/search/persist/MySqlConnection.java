package com.movie.search.persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MySqlConnection implements IConnection {
	private Connection connection = null;

	private static final String DB_NAME = "movie_search";
	private static final String DB_USER_NAME = "root";
	private static final String DB_PASS = "pass";
	private static final String CONNECTION_TEMPL = "jdbc:mysql://localhost/$s?" + "user=%s&password=%s";

	private HashMap<String, MoviePrepStatement> movieStatements;

	public MySqlConnection() {
		movieStatements = new LinkedHashMap<>();

		String insertStr = "insert into movie_search.movies values (?, ?)";
		String selectLikeStr = "select m.title" + "from movie_search.movies m" + "where m.title like '%?%'"
				+ "or m.descr like '%?%'";

		movieStatements.put(MoviePrepStatement.INSERT_STATEMENT_NAME, new MoviePrepStatement(connection, insertStr));
		movieStatements.put(MoviePrepStatement.SELECT_MATCHING_STATEMENT_NAME,
				new MoviePrepStatement(connection, selectLikeStr));
	}

	MySqlConnection(final Connection connection) {
		this();
		this.connection = connection;
	}

	@Override
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

	@Override
	public void close() {
		try {
			for (MoviePrepStatement statement : movieStatements.values()) {
				statement.close();
			}

			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println("Error occured while closing connection to database!");
		}
	}

	public void insert(final String title, final String description) throws SQLException {
		MoviePrepStatement insertStatement = movieStatements.get(MoviePrepStatement.INSERT_STATEMENT_NAME);
		PreparedStatement prepStatement = insertStatement.getPrepStatement();

		prepStatement.setString(1, title);
		prepStatement.setString(2, description);

		prepStatement.executeUpdate();
	}

	public ResultSet getMatching(final String textToMatch) throws SQLException {
		MoviePrepStatement selectStatement = movieStatements.get(MoviePrepStatement.SELECT_MATCHING_STATEMENT_NAME);
		PreparedStatement prepStatement = selectStatement.getPrepStatement();

		prepStatement.setString(1, textToMatch);
		prepStatement.setString(2, textToMatch);

		return prepStatement.executeQuery();
	}

	HashMap<String, MoviePrepStatement> getMoviePrepStatements() {
		return movieStatements;
	}
}
