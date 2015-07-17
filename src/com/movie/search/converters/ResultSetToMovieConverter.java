package com.movie.search.converters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.movie.search.models.Movie;

public class ResultSetToMovieConverter implements IConverter<ResultSet, List<Movie>> {
	static final String TITLE_COLUMN_NAME = "title";
	static final String DESCR_COLUMN_NAME = "descr";

	@Override
	public List<Movie> convert(ResultSet resultSet) throws SQLException {
		List<Movie> movies = new LinkedList<>();
		if (resultSet != null) {
			while (resultSet.next()) {
				String title = resultSet.getString(TITLE_COLUMN_NAME);
				String description = resultSet.getString(DESCR_COLUMN_NAME);

				movies.add(new Movie(title, description));
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}

		return movies;
	}
}
