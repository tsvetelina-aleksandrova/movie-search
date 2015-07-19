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

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		movieListConverter = new MovieToJsonConverter();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final String textToMatch = request.getParameter("textToMatch");
		final MovieDAO movieDao = new MovieDAO(EntityManagerProvider.getEntityManager());
		List<Movie> movies = new LinkedList<>();

		try {
			movies = movieDao.findByMatchingTitleOrDescr(textToMatch);
			if (movies.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("No movies found");
			} else {
				JSONObject jsonOutput = movieListConverter.convert(movies);

				response.setContentType("application/json");
				System.out.println(jsonOutput.toJSONString());
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
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				response.getWriter().write("Duplicate movie titles are not allowed");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("DB error");
		}
	}
}
