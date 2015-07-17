package com.movie.search.persist;

public class PoolingMySqlDriverConfig extends PoolingDriverConfig {

	private static final String DB_NAME = "movie_search";
	private static final String DB_USER_NAME = "root";
	private static final String CONNECTION_TEMPL = "jdbc:mysql://localhost/%s?" + "user=%s";

	public void setupDriver() throws Exception {
		super.setupDriver(String.format(CONNECTION_TEMPL, DB_NAME, DB_USER_NAME));
	}
}
