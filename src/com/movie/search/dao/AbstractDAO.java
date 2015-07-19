package com.movie.search.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class AbstractDAO {
	private EntityManager entityManager;

	public AbstractDAO(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected EntityTransaction beginTransaction() {
		EntityTransaction tx = getEntityManager().getTransaction();
		tx.begin();
		return tx;
	}

	protected void commitTransaction(EntityTransaction tx) {
		try {
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
