package com.movie.search.persist;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ResultSetToMovieConverterTest {
	@Mock
	private ResultSet resultSet;

	private ResultSetToMovieConverter converter;
	private final String[] testTitles = { "title1", "title2" };
	private final String[] testDescrs = { "descr1", "descr2" };

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		converter = new ResultSetToMovieConverter();

		Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultSet.getString(ResultSetToMovieConverter.TITLE_COLUMN_NAME)).thenReturn(testTitles[0])
				.thenReturn(testTitles[1]);
		Mockito.when(resultSet.getString(ResultSetToMovieConverter.DESCR_COLUMN_NAME)).thenReturn(testDescrs[0])
				.thenReturn(testDescrs[1]);
	}

	@Test
	public void testConvert() throws Exception {
		List<Movie> convertedResult = converter.convert(resultSet);
		Mockito.verify(resultSet).close();
		assertEquals(2, convertedResult.size());

		for (int i = 0; i < convertedResult.size(); i++) {
			assertEquals(testTitles[i], convertedResult.get(i).getTitle());
			assertEquals(testDescrs[i], convertedResult.get(i).getDescription());
		}
	}
}
