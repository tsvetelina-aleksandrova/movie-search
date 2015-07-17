package com.movie.search.converters;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.movie.search.models.Movie;

public class MovieToJsonStringConverterTest {
	private MovieToJsonStringConverter converter;
	private final String movieTitle1 = "title1";
	private final String movieTitle2 = "title2";

	@Test
	public void testConvert() {
		final String expectedResult = "[" + movieTitle1 + ", " + movieTitle2 + "]";
		converter = new MovieToJsonStringConverter();
		List<Movie> movies = new LinkedList<>();
		movies.add(new Movie(movieTitle1, "123"));
		movies.add(new Movie(movieTitle2, "456"));
		assertEquals(expectedResult, converter.convert(movies));
	}
}
