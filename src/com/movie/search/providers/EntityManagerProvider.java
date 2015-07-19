package com.movie.search.providers;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class EntityManagerProvider {
	private static final String PERSISTENCE_UNIT = "movie-search";

	public static EntityManager getEntityManager() {
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT).createEntityManager();
	}
}