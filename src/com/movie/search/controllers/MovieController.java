package com.movie.search.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.movie.search.converters.ResultSetToMovieConverter;
import com.movie.search.models.Movie;
import com.movie.search.persist.MySqlMoviesConnection;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

public class MovieController implements IMovieController {
	private MySqlMoviesConnection connection;
	private ResultSetToMovieConverter movieConverter;

	public MovieController() {
		connection = new MySqlMoviesConnection();
		movieConverter = new ResultSetToMovieConverter();
	}

	MovieController(final MySqlMoviesConnection connection, final ResultSetToMovieConverter movieConverter) {
		this.connection = connection;
		this.movieConverter = movieConverter;
	}

	@Override
	public boolean addMovie(String title, String description) throws SQLException, ClassNotFoundException {
		connection.connect();
		try {
			connection.insert(title, description);
			return true;
		} catch (MySQLIntegrityConstraintViolationException e) {
			return false;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<Movie> getMoviesMatching(String textToMatch) throws Exception {
		List<Movie> result = new LinkedList<>();
		try {
			connection.connect();

			ResultSet dbResultSet = connection.getMatching(textToMatch);
			result = movieConverter.convert(dbResultSet);
			return getMoviesWithMatchingTitlesFirst(result, textToMatch);
		} catch (Exception e) {
			throw e;
		} finally {
			connection.close();
		}
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
