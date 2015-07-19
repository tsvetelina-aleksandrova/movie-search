package com.movie.search.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity(name = "movies")
@NamedQueries({
		@NamedQuery(name = "findByTitle", query = "select new com.movie.search.models.Movie(m.title, m.description) from movies m where m.title = :title"),
		@NamedQuery(name = "findByMatchingTitleOrDescr", query = "select new com.movie.search.models.Movie(m.title, m.description) from movies m where m.title like :textToMatch or m.description like :textToMatch") })
public class Movie implements Serializable {
	private static final long serialVersionUID = 1L;

	public Movie() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String title;
	private String description;

	public Movie(final String title, final String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(getClass().getSimpleName());
		result.append(" ");
		if (title != null && !title.trim().isEmpty())
			result.append("title: ").append(title);
		if (description != null && !description.trim().isEmpty())
			result.append(", description: ").append(description);
		return result.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Movie)) {
			return false;
		}
		Movie other = (Movie) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + ((id == null) ? 0 : id.hashCode());
	}
}