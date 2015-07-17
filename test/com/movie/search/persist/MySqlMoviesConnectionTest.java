package com.movie.search.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MySqlMoviesConnectionTest {
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement prepStatement;
	@Mock
	private MoviePrepStatement insertTestStatement;
	@Mock
	private MoviePrepStatement selectTestStatement;
	@Mock
	private PreparedStatement insertTestPrepSt;
	@Mock
	private PreparedStatement selectTestPrepSt;

	private MySqlMoviesConnection mySqlconnection;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		mySqlconnection = new MySqlMoviesConnection(connection);
	}

	@Test
	public void testAvailablePrepStatements() {
		HashMap<String, MoviePrepStatement> statements = mySqlconnection.getMoviePrepStatements();
		assertEquals(2, statements.size());
		assertTrue(statements.containsKey(MoviePrepStatement.INSERT_STATEMENT_NAME));
		assertTrue(statements.containsKey(MoviePrepStatement.SELECT_MATCHING_STATEMENT_NAME));
	}

	@Test
	public void testClose() throws Exception {
		mockStatements();
		mySqlconnection.close();

		for (MoviePrepStatement statement : mySqlconnection.getMoviePrepStatements().values()) {
			Mockito.verify(statement).close();
		}
		Mockito.verify(connection).close();
	}

	@Test
	public void testInsert() throws Exception {
		mockStatements();
		final String testTitle = "title";
		final String testDescription = "description";
		mySqlconnection.insert(testTitle, testDescription);

		Mockito.verify(insertTestPrepSt).setString(1, testTitle);
		Mockito.verify(insertTestPrepSt).setString(2, testDescription);
		Mockito.verify(insertTestPrepSt).executeUpdate();
	}

	@Test
	public void testGetMatching() throws Exception {
		mockStatements();
		final String testMatchText = "text";
		mySqlconnection.getMatching(testMatchText);

		Mockito.verify(selectTestPrepSt).setString(1, testMatchText);
		Mockito.verify(selectTestPrepSt).setString(2, testMatchText);
		Mockito.verify(selectTestPrepSt).executeQuery();
	}

	private void mockStatements() throws Exception {
		HashMap<String, MoviePrepStatement> statements = mySqlconnection.getMoviePrepStatements();
		statements.clear();

		statements.put(MoviePrepStatement.INSERT_STATEMENT_NAME, insertTestStatement);
		statements.put(MoviePrepStatement.SELECT_MATCHING_STATEMENT_NAME, selectTestStatement);

		Mockito.when(insertTestStatement.getPrepStatement()).thenReturn(insertTestPrepSt);
		Mockito.when(selectTestStatement.getPrepStatement()).thenReturn(selectTestPrepSt);
	}
}
