package com.movie.search.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.movie.search.models.Movie;

public class MovieDAO extends AbstractDAO {
	public MovieDAO(final EntityManager entityManager) {
		super(entityManager);
	}

	public boolean addMovie(final Movie movie) {
		EntityTransaction tx = beginTransaction();
		Movie foundBook = findByTitle(movie.getTitle());

		if (foundBook == null) {
			getEntityManager().persist(movie);
			commitTransaction(tx);
			return true;
		}
		return false;
	}

	public List<Movie> findByMatchingTitleOrDescr(final String textToMatch) {
		final String textToMatchWithLike = "%" + textToMatch + "%";
		List<Movie> movieResults = new LinkedList<>();
		TypedQuery<Movie> query = getEntityManager().createNamedQuery("findByMatchingTitleOrDescr", Movie.class)
				.setParameter("textToMatch", textToMatchWithLike);
		try {
			movieResults = query.getResultList();
		} catch (NoResultException e) {
			System.out.println("No movies found in db");
		}
		return movieResults;
	}

	public Movie findByTitle(final String title) {
		return getEntityManager().find(Movie.class, title);
	}
}
