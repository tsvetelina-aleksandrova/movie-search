package com.movie.search.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.movie.search.controllers.MovieController;
import com.movie.search.converters.MovieToJsonStringConverter;
import com.movie.search.models.Movie;
import com.movie.search.persist.PoolingMySqlDriverConfig;

@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PoolingMySqlDriverConfig poolingDriverConfig;
	private MovieController movieController;
	private MovieToJsonStringConverter movieListConverter;

	public MoviesServlet() {
		super();
		movieController = new MovieController();
		movieListConverter = new MovieToJsonStringConverter();
		poolingDriverConfig = new PoolingMySqlDriverConfig();

		try {
			poolingDriverConfig.setupDriver();
		} catch (Exception e) {
			System.out.println("Error with database pool configuration");
			e.printStackTrace();
		}
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

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.flushBuffer();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String title = request.getParameter("movie-title");
		final String description = request.getParameter("movie-descr");
		try {
			if (movieController.addMovie(title, description)) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("Movie added successfully");
			} else {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				response.getWriter().write("Duplicate movie title");
			}

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Movie was not added successfully");
		}
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			poolingDriverConfig.setupDriver();
		} catch (Exception e) {
			System.out.println("Error with database pool configuration");
			e.printStackTrace();
		}
	}

	public void destroy() {
		try {
			poolingDriverConfig.shutdownDriver();
		} catch (Exception e) {
			System.out.println("Error while shutting down database pool");
			e.printStackTrace();
		}
	}

	protected void finalize() {
		destroy();
	}
}
