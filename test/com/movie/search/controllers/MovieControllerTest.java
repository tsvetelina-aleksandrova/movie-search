package com.movie.search.controllers;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.movie.search.converters.ResultSetToMovieConverter;
import com.movie.search.models.Movie;
import com.movie.search.persist.MySqlMoviesConnection;

public class MovieControllerTest {
	@Mock
	private MySqlMoviesConnection connection;
	@Mock
	private ResultSetToMovieConverter movieConverter;
	@Mock
	private ResultSet resultSet;

	private MovieController movieController;
	private final String testTextToMatch = "test";
	private List<Movie> moviesList;
	private Movie movieTitleMatching;
	private Movie movieDescrMatching;
	private Movie movieTitleAndDescrMatching;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		movieController = new MovieController(connection, movieConverter);
		movieTitleMatching = new Movie(testTextToMatch + "a", "123");
		movieTitleAndDescrMatching = new Movie("a" + testTextToMatch, "b" + testTextToMatch);
		movieDescrMatching = new Movie("123", testTextToMatch);

		moviesList = new LinkedList<>();
		moviesList.add(movieDescrMatching);
		moviesList.add(movieTitleMatching);
		moviesList.add(movieTitleAndDescrMatching);

		Mockito.when(connection.getMatching(testTextToMatch)).thenReturn(resultSet);
		Mockito.when(movieConverter.convert(resultSet)).thenReturn(moviesList);
	}

	@Test
	public void testAddMovie() throws Exception {
		final String testTitle = "testTitle";
		final String testDescr = "testDescr";
		movieController.addMovie(testTitle, testDescr);

		InOrder connOrder = Mockito.inOrder(connection, connection, connection);
		connOrder.verify(connection).connect();
		connOrder.verify(connection).insert(testTitle, testDescr);
		connOrder.verify(connection).close();
	}

	@Test
	public void testGetMoviesMatching() throws Exception {
		List<Movie> movies = movieController.getMoviesMatching(testTextToMatch);

		InOrder connOrder = Mockito.inOrder(connection, connection, connection);
		connOrder.verify(connection).connect();
		connOrder.verify(connection).getMatching(testTextToMatch);
		connOrder.verify(connection).close();

		assertEquals(3, movies.size());
		assertEquals(movieDescrMatching, movies.get(movies.size() - 1));

	}
}
