package com.movie.search.persist;

import java.sql.SQLException;

public interface IConnection {
	void connect() throws SQLException, ClassNotFoundException;

	void close();
}
