package com.movie.search.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.movie.search.converters.ResultSetToMovieConverter;
import com.movie.search.models.Movie;
import com.movie.search.persist.MySqlConnection;

public class MovieManager implements IMovieManager {
	private MySqlConnection connection;
	private ResultSetToMovieConverter movieConverter;

	public MovieManager() {
		connection = new MySqlConnection();
		movieConverter = new ResultSetToMovieConverter();
		connection.connect();
	}

	@Override
	public boolean addMovie(String title, String description) {
		try {
			connection.insert(title, description);
			return true;
		} catch (SQLException e) {
			System.out.println("A database error occurred. Movie was not added.");
			return false;
		}
	}

	@Override
	public List<Movie> searchMovie(String textToMatch) {
		List<Movie> result = new LinkedList<>();
		try {
			ResultSet dbResultSet = connection.getMatching(textToMatch);
			result = movieConverter.convert(dbResultSet);
			// now they should be returned in correct order
		} catch (SQLException e) {
			System.out.println("A database error occurred. No movies could be found.");
		}
		return result;
	}

}
