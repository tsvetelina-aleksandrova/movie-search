package com.movie.search.persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MySqlConnection implements IConnection {
	private Connection connection;

	private static final String DB_NAME = "movie_search";
	private static final String DB_USER_NAME = "root";
	private static final String CONNECTION_TEMPL = "jdbc:mysql://localhost/%s?" + "user=%s";

	private HashMap<String, MoviePrepStatement> movieStatements;

	public MySqlConnection() {
		movieStatements = new LinkedHashMap<>();
	}

	MySqlConnection(final Connection connection) {
		this();
		this.connection = connection;
	}

	@Override
	public void connect() throws SQLException, ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String connStr = String.format(CONNECTION_TEMPL, DB_NAME, DB_USER_NAME);
			connection = (Connection) DriverManager.getConnection(connStr);

			String insertStr = "insert into movies (title, descr) values (?, ?)";
			String selectLikeStr = "select * " + "from movies m " + "where m.title like ?" + " or m.descr like ?";

			movieStatements.put(MoviePrepStatement.INSERT_STATEMENT_NAME,
					new MoviePrepStatement(connection, insertStr));
			movieStatements.put(MoviePrepStatement.SELECT_MATCHING_STATEMENT_NAME,
					new MoviePrepStatement(connection, selectLikeStr));

		} catch (ClassNotFoundException e) {
			System.out.println("MySQL driver is not available!");
			throw e;
		} catch (SQLException e) {
			System.out.println("Connection to database failed!");
			throw e;
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
		final String textMatchExpr = "%" + textToMatch + "%";

		prepStatement.setString(1, textMatchExpr);
		prepStatement.setString(2, textMatchExpr);

		return prepStatement.executeQuery();
	}

	HashMap<String, MoviePrepStatement> getMoviePrepStatements() {
		return movieStatements;
	}
}
