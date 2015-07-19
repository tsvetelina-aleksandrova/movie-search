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
		final Movie movieWithSameTitle = findByTitle(movie.getTitle());

		if (movieWithSameTitle == null) {
			getEntityManager().persist(movie);
			commitTransaction(tx);
			return true;
		}
		return false;
	}

	public List<Movie> findByMatchingTitleOrDescr(final String textToMatch) {
		final String textToMatchWithLike = "%" + textToMatch + "%";
		List<Movie> movieResults = new LinkedList<Movie>();

		TypedQuery<Movie> query = getEntityManager().createNamedQuery("findByMatchingTitleOrDescr", Movie.class)
				.setParameter("textToMatch", textToMatchWithLike);
		try {
			movieResults = query.getResultList();
		} catch (NoResultException e) {
			//
		}
		return movieResults;
	}

	public Movie findByTitle(final String title) {
		TypedQuery<Movie> query = getEntityManager().createNamedQuery("findByTitle", Movie.class).setParameter("title",
				title);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
