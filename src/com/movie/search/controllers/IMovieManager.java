package com.movie.search.controllers;

import java.util.List;

import com.movie.search.models.Movie;

public interface IMovieManager {
	boolean addMovie(final String title, final String description);

	List<Movie> searchMovie(final String matchStr);

}
