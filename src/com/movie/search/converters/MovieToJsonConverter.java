package com.movie.search.converters;

import java.util.List;

import org.json.simple.JSONObject;

import com.movie.search.models.Movie;

public class MovieToJsonConverter implements IConverter<List<Movie>, JSONObject> {

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject convert(List<Movie> movies) {
		JSONObject jsonResult = new JSONObject();

		for (Movie movie : movies) {
			jsonResult.put("title", movie.getTitle());
			jsonResult.put("description", movie.getDescription());
		}

		return jsonResult;
	}
}
