package com.movie.search.persist;

import static org.junit.Assert.assertSame;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MoviePrepStatementTest {
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement prepStatement;
	private MoviePrepStatement statement;
	private String testStatementStr;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testStatementStr = "test";
		statement = new MoviePrepStatement(connection, testStatementStr);
		Mockito.when(connection.prepareStatement(testStatementStr)).thenReturn(prepStatement);
	}

	@Test
	public void testGetPrepStatement() throws Exception {
		PreparedStatement returnedStatement = statement.getPrepStatement();
		assertSame(prepStatement, returnedStatement);
	}

	@Test
	public void testUniquePrepStatementReturned() throws Exception {
		statement.getPrepStatement();
		Mockito.verify(connection).prepareStatement(testStatementStr);

		statement.getPrepStatement();
		Mockito.verifyNoMoreInteractions(connection);
	}

	@Test
	public void testClose() throws Exception {
		statement.close();
		Mockito.verifyZeroInteractions(connection);

		statement.getPrepStatement();
		statement.close();
		Mockito.verify(prepStatement).close();
	}
}
