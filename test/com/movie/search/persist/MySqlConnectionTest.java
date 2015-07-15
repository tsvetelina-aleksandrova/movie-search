package com.movie.search.persist;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MySqlConnectionTest {
	@Mock
	private Connection connection;
	@Mock
	private PreparedStatement prepStatement;

	private MySqlConnection mySqlconnection;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		mySqlconnection = new MySqlConnection(connection);
	}

	@Test
	public void testAvailablePrepStatements() {
		HashMap<String, MoviePrepStatement> statements = mySqlconnection.getMoviePrepStatements();
		assertEquals(2, statements.size());
	}

	@Test
	public void testClose() throws Exception {
		mySqlconnection.close();

		for (MoviePrepStatement statement : mySqlconnection.getMoviePrepStatements().values()) {
			Mockito.verify(statement).close();
		}
		Mockito.verify(connection).close();
	}

	@Test
	public void testInsert() {

	}

	@Test
	public void testGetMatching() {

	}
}
