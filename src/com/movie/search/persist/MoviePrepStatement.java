package com.movie.search.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MoviePrepStatement {
	public static final String INSERT_STATEMENT_NAME = "insert";
	public static final String SELECT_MATCHING_STATEMENT_NAME = "selectMatching";

	private final String statementString;
	private final Connection connection;
	private PreparedStatement prepStatement;

	public MoviePrepStatement(final Connection connection, final String statementString) {
		this.statementString = statementString;
		this.connection = connection;
	}

	public String getStatementString() {
		return statementString;
	}

	public PreparedStatement getPrepStatement() throws SQLException {
		if (prepStatement == null) {
			prepStatement = connection.prepareStatement(statementString);
		}
		return prepStatement;
	}

	public void close() throws SQLException {
		if (prepStatement != null) {
			prepStatement.close();
		}
	}
}
