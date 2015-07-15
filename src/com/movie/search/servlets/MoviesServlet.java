package com.movie.search.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.movie.search.controllers.MovieController;
import com.movie.search.converters.MovieToJsonConverter;
import com.movie.search.models.Movie;

@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MovieController movieController;
	private MovieToJsonConverter movieListConverter;

	public MoviesServlet() {
		super();
		movieController = new MovieController();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String textToMatch = request.getParameter("textToMatch");

		List<Movie> movies = new LinkedList<>();
		try {
			movies = movieController.getMoviesMatching(textToMatch);
			if (movies.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.flushBuffer();
				return;
			}
			String jsonOutput = movieListConverter.convert(movies);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonOutput);

		} catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.flushBuffer();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// watch out for these param names
		final String title = request.getParameter("movie-title");
		final String description = request.getParameter("movie-descr");

		if (movieController.addMovie(title, description)) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("Movie added successfully");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Movie was not added successfully");
		}
	}
}
