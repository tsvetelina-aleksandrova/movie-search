package com.movie.search.converters;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.movie.search.models.Movie;

public class MovieToJsonConverter implements IConverter<List<Movie>, JSONObject> {

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject convert(List<Movie> movies) {
		JSONObject jsonResult = new JSONObject();
		JSONArray jsonArr = new JSONArray();

		for (Movie movie : movies) {
			JSONObject movieObj = new JSONObject();
			movieObj.put("title", movie.getTitle());
			movieObj.put("description", movie.getDescription());
			jsonArr.add(movieObj);
		}
		jsonResult.put("movies", jsonArr);
		return jsonResult;
	}
}
