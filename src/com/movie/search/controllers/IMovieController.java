package com.movie.search.controllers;

import java.util.List;

public interface IMovieController {
	void addMovie(final String title, final String description) throws Exception;

	@SuppressWarnings("rawtypes")
	List getMoviesMatching(final String matchStr) throws Exception;

}
