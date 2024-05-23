package edu.duke.projectTeam8;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDatabase implements Database {
  protected final String connector;
  protected final String dbms;
  protected final String url;
  protected final String port;
  protected final String databaseName;
  protected final String user;
  protected final String password;
  protected final Connection connection;

  public AbstractDatabase(String connector, String dbms, String url, String port, String databaseName, String user,
      String password) throws SQLException {
    this.connector = connector;
    this.dbms = dbms;
    this.url = url;
    this.port = port;
    this.databaseName = databaseName;
    this.user = user;
    this.password = password;
    StringBuilder databaseURLBuilder = new StringBuilder(connector);
    databaseURLBuilder.append(":");
    databaseURLBuilder.append(dbms);
    databaseURLBuilder.append("://");
    databaseURLBuilder.append(url);
    databaseURLBuilder.append(":");
    databaseURLBuilder.append(port);
    databaseURLBuilder.append("/");
    databaseURLBuilder.append(databaseName);
    databaseURLBuilder.append("?allowLoadLocalInfile=true");

    String databaseURL = databaseURLBuilder.toString();
    this.connection = DriverManager.getConnection(databaseURL, user, password);
  }

  public abstract User getUser(String id) throws Exception;

}
