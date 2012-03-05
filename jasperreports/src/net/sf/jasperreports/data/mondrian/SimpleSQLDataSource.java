package net.sf.jasperreports.data.mondrian;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class SimpleSQLDataSource implements DataSource {
	private Connection connection;
	private PrintWriter pw = new PrintWriter(System.out);
	private int loginTimeout = 0;

	public SimpleSQLDataSource(Connection connection) {
		this.connection = connection;
	}

	public PrintWriter getLogWriter() throws SQLException {
		return pw;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		pw = out;
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		loginTimeout = seconds;
	}

	public int getLoginTimeout() throws SQLException {
		return loginTimeout;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public Connection getConnection() throws SQLException {
		return connection;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return connection;
	}

}
