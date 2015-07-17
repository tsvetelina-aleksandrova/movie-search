package com.movie.search.converters;

import java.util.List;

import com.movie.search.models.Movie;

public class MovieToJsonStringConverter implements IConverter<List<Movie>, String> {

	@Override
	public String convert(List<Movie> movies) {
		// for now, we are only interested in titles
		StringBuilder jsonString = new StringBuilder("[");

		for (int i = 0; i < movies.size() - 1; i++) {
			jsonString.append(movies.get(i).getTitle()).append(", ");
		}
		jsonString.append(movies.get(movies.size() - 1).getTitle()).append("]");

		return jsonString.toString();
	}
}
