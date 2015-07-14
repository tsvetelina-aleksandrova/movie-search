package com.movie.search.persist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ResultSetToMovieConverter {
	private static final String TITLE_COLUMN_NAME = "title";
	private static final String DESCR_COLUMN_NAME = "descr";

	public List<Movie> convert(ResultSet resultSet) throws SQLException {
		List<Movie> movies = new LinkedList<>();

		while (resultSet.next()) {
			String title = resultSet.getString(TITLE_COLUMN_NAME);
			String description = resultSet.getString(DESCR_COLUMN_NAME);

			movies.add(new Movie(title, description));
		}
		if (resultSet != null) {
			resultSet.close();
		}

		return movies;
	}
}
