package com.movie.search.persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class MySqlMoviesConnection implements IConnection {
	private Connection connection;

	private static final String INSERT_QUERY = "insert into movies (title, descr) values (?, ?)";
	private static final String SELECT_LIKE_QUERY = "select * " + "from movies m " + "where m.title like ?"
			+ " or m.descr like ?";

	private HashMap<String, MoviePrepStatement> movieStatements;

	public MySqlMoviesConnection() {
		movieStatements = new LinkedHashMap<>();
	}

	MySqlMoviesConnection(final Connection connection) {
		this();
		this.connection = connection;
	}

	@Override
	public void connect() throws SQLException, ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection(String.format("%s:%s",
					PoolingDriverConfig.JDBC_POOLING_DRIVER_NAME, PoolingDriverConfig.POOL_NAME));

			movieStatements.put(MoviePrepStatement.INSERT_STATEMENT_NAME,
					new MoviePrepStatement(connection, INSERT_QUERY));
			movieStatements.put(MoviePrepStatement.SELECT_MATCHING_STATEMENT_NAME,
					new MoviePrepStatement(connection, SELECT_LIKE_QUERY));

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
		final String textMatchExpr = "%" + textToMatch + "%";
		MoviePrepStatement selectStatement = movieStatements.get(MoviePrepStatement.SELECT_MATCHING_STATEMENT_NAME);
		PreparedStatement prepStatement = selectStatement.getPrepStatement();

		prepStatement.setString(1, textMatchExpr);
		prepStatement.setString(2, textMatchExpr);

		return prepStatement.executeQuery();
	}

	HashMap<String, MoviePrepStatement> getMoviePrepStatements() {
		return movieStatements;
	}
}
