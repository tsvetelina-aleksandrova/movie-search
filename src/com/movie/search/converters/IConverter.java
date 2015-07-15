package com.movie.search.converters;

public interface IConverter<T, V> {
	V convert(T valueToConvert) throws Exception;
}
