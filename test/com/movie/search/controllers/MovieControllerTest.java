package com.movie.search.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.movie.search.converters.ResultSetToMovieConverter;
import com.movie.search.models.Movie;
import com.movie.search.persist.MySqlConnection;

public class MovieControllerTest {
	@Mock
	private MySqlConnection connection;
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
	public void testAddMovieSuccessful() {
		assertTrue(movieController.addMovie("testTitle", "testDescr"));
	}

	@Test
	public void testAddMovieWithError() throws Exception {
		Mockito.doThrow(SQLException.class).when(connection).insert(Mockito.anyString(), Mockito.anyString());
		assertFalse(movieController.addMovie("testTitle", "testDescr"));
	}

	@Test
	public void testGetMoviesMatching() throws Exception {
		List<Movie> movies = movieController.getMoviesMatching(testTextToMatch);
		assertEquals(3, movies.size());
		assertEquals(movieDescrMatching, movies.get(movies.size() - 1));
	}
}
