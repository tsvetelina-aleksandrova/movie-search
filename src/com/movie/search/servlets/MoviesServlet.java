package com.movie.search.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.movie.search.converters.MovieToJsonStringConverter;
import com.movie.search.dao.MovieDAO;
import com.movie.search.models.Movie;
import com.movie.search.providers.EntityManagerProvider;

@WebServlet("/movies")
public class MoviesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MovieToJsonStringConverter movieListConverter;
	private MovieDAO movieDao;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		final EntityManager entityManager = EntityManagerProvider.getEntityManager();
		movieDao = new MovieDAO(entityManager);
		movieListConverter = new MovieToJsonStringConverter();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final String textToMatch = request.getParameter("textToMatch");
		List<Movie> movies = new LinkedList<>();
		try {
			movies = movieDao.findByMatchingTitleOrDescr(textToMatch);
			if (movies.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				response.getWriter().write("No movies found");
			} else {
				String jsonOutput = movieListConverter.convert(movies);

				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonOutput);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("HERE");
		final String title = request.getParameter("movie-title");
		final String description = request.getParameter("movie-descr");
		final Movie newMovie = new Movie(title, description);
		try {
			if (movieDao.addMovie(newMovie)) {
				System.out.println("add");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("Movie added successfully");
			} else {
				System.out.println("confl");
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				response.getWriter().write("Duplicate movie title");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("Movie was not added successfully");
		}
	}
}
