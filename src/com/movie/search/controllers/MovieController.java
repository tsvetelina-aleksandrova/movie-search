package com.movie.search.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.movie.search.converters.ResultSetToMovieConverter;
import com.movie.search.models.Movie;
import com.movie.search.persist.MySqlConnection;

public class MovieController implements IMovieController {
	private MySqlConnection connection;
	private ResultSetToMovieConverter movieConverter;

	public MovieController() {
		connection = new MySqlConnection();
		movieConverter = new ResultSetToMovieConverter();
	}

	MovieController(final MySqlConnection connection, final ResultSetToMovieConverter movieConverter) {
		this.connection = connection;
		this.movieConverter = movieConverter;
	}

	@Override
	public boolean addMovie(String title, String description) {
		try {
			connection.connect();
			connection.insert(title, description);
			connection.close();
			return true;
		} catch (SQLException e) {
			System.out.println("A database error occurred. Movie was not added.");
			return false;
		}
	}

	@Override
	public List<Movie> getMoviesMatching(String textToMatch) {
		List<Movie> result = new LinkedList<>();
		try {
			connection.connect();
			ResultSet dbResultSet = connection.getMatching(textToMatch);
			result = movieConverter.convert(dbResultSet);
			connection.close();
		} catch (SQLException e) {
			System.out.println("A database error occurred. No movies could be found.");
		}
		// movies with titles containing the textToMatch
		// should be returned first
		return getMoviesWithMatchingTitlesFirst(result, textToMatch);
	}

	private List<Movie> getMoviesWithMatchingTitlesFirst(final List<Movie> movies, final String textToMatch) {
		List<Movie> titleMatchingMovies = new LinkedList<>();
		List<Movie> descrMathingMovies = new LinkedList<>();

		for (Movie movie : movies) {
			if (movie.getTitle().contains(textToMatch)) {
				titleMatchingMovies.add(movie);
			} else {
				descrMathingMovies.add(movie);
			}
		}
		titleMatchingMovies.addAll(descrMathingMovies);
		return titleMatchingMovies;
	}

}
