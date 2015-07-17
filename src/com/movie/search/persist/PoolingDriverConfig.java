package com.movie.search.persist;

import java.sql.DriverManager;

import org.apache.tomcat.dbcp.dbcp2.ConnectionFactory;
import org.apache.tomcat.dbcp.dbcp2.DriverManagerConnectionFactory;
import org.apache.tomcat.dbcp.dbcp2.PoolableConnection;
import org.apache.tomcat.dbcp.dbcp2.PoolableConnectionFactory;
import org.apache.tomcat.dbcp.dbcp2.PoolingDriver;
import org.apache.tomcat.dbcp.pool2.ObjectPool;
import org.apache.tomcat.dbcp.pool2.PooledObjectFactory;
import org.apache.tomcat.dbcp.pool2.impl.GenericObjectPool;

public class PoolingDriverConfig {
	private PoolingDriver driver;

	public static String JDBC_POOLING_DRIVER_NAME = "jdbc:apache:commons:dbcp:";
	public static String POOL_NAME = "movies";

	public void setupDriver(final String connectURI) throws Exception {

		Class.forName("com.mysql.jdbc.Driver");

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);

		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);

		ObjectPool<PoolableConnection> connectionPool = (ObjectPool<PoolableConnection>) new GenericObjectPool<PoolableConnection>(
				(PooledObjectFactory<PoolableConnection>) poolableConnectionFactory);
		poolableConnectionFactory.setPool(connectionPool);

		Class.forName("org.apache.tomcat.dbcp.dbcp2.PoolingDriver");
		driver = (PoolingDriver) DriverManager.getDriver(JDBC_POOLING_DRIVER_NAME);

		driver.registerPool(POOL_NAME, connectionPool);
	}

	public void shutdownDriver() throws Exception {
		if (driver != null) {
			driver.closePool(POOL_NAME);
		}
	}
}
