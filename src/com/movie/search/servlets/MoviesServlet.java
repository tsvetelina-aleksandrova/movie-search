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

import org.json.simple.JSONObject;

import com.movie.search.converters.MovieToJsonConverter;
import com.movie.search.dao.MovieDAO;
import com.movie.search.models.Movie;
import com.movie.search.providers.EntityManagerProvider;

@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MovieToJsonConverter movieListConverter;
	private static String RESPONSE_STR = "{\"status\": %d, \"msg\": \"%s\"}";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		movieListConverter = new MovieToJsonConverter();
	}

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final String textToMatch = request.getParameter("textToMatch");
		final MovieDAO movieDao = new MovieDAO(EntityManagerProvider.getEntityManager());
		List<Movie> movies = new LinkedList<>();

		try {
			movies = movieDao.findByMatchingTitleOrDescr(textToMatch);
			if (movies.isEmpty()) {
				final int responseStatus = HttpServletResponse.SC_NO_CONTENT;
				final String statusMsg = "No movies found";
				response.setStatus(responseStatus);

				response.getWriter().write(String.format(RESPONSE_STR, responseStatus, statusMsg));
			} else {
				JSONObject jsonOutput = movieListConverter.convert(movies);
				jsonOutput.put("status", HttpServletResponse.SC_OK);

				response.setContentType("application/json");
				response.getWriter().write(jsonOutput.toJSONString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("DB error");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final String title = request.getParameter("movie-title");
		final String description = request.getParameter("movie-descr");

		final Movie newMovie = new Movie(title, description);
		final MovieDAO movieDao = new MovieDAO(EntityManagerProvider.getEntityManager());

		try {
			if (movieDao.addMovie(newMovie)) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("Movie added");
			} else {
				final int duplicateStatus = HttpServletResponse.SC_CONFLICT;
				final String statusMsg = "Duplicate movie titles are not allowed";
				response.setStatus(duplicateStatus);
				response.getWriter().write(String.format(RESPONSE_STR, duplicateStatus, statusMsg));
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("DB error");
		}
	}
}
