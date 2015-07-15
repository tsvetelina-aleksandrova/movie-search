package com.movie.search.controllers;

import java.util.List;

public interface IMovieController {
	boolean addMovie(final String title, final String description);

	@SuppressWarnings("rawtypes")
	List getMoviesMatching(final String matchStr);

}
