package gestioneutenti;

import gestionerecensioni.RecensioneManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.DriverManagerConnectionPool;

public class UtenteManager {

  private static final String TABLENAME = "Utente";

  public ArrayList<Utente> listaUtenti(ResultSet rs) throws SQLException {
    RecensioneManager rm = new RecensioneManager();
    rs.first();
    ArrayList<Utente> lista = new ArrayList<Utente>();
    Utente temp;
    while (!rs.isAfterLast()) {
      temp = new Utente();
      temp.setUsername(rs.getString("Username"));
      temp.setNome(rs.getString("Nome"));
      temp.setCognome(rs.getString("Cognome"));
      temp.setSesso(rs.getString("Sesso"));
      temp.setPassword(rs.getString("Password"));
      temp.setDescrizione(rs.getString("Descrizione"));
      temp.setNumAnnunci(rs.getInt("NumAnnunci"));
      temp.setGestore(rs.getBoolean("Gestore"));
      temp.setMedia(rm.media(temp.getUsername()));
      lista.add(temp);
      rs.next();
    }
    return lista;
  }

  public Utente recuperaSeRegistrato(String username, String password) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Utente temp = new Utente();

    String sql = "SELECT * FROM " + TABLENAME + " WHERE Username= ? AND Password= ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);

      ResultSet rs = preparedStatement.executeQuery();

      if (rs.next()) {
        temp.setUsername(rs.getString("Username"));
        temp.setNome(rs.getString("Nome"));
        temp.setCognome(rs.getString("Cognome"));
        temp.setSesso(rs.getString("Sesso"));
        temp.setPassword(rs.getString("Password"));
        temp.setDescrizione(rs.getString("Descrizione"));
        temp.setNumAnnunci(rs.getInt("NumAnnunci"));
        temp.setGestore(rs.getBoolean("Gestore"));
      } else {
        temp = null;
      }

    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
      }
    }
    return temp;
  }

  @SuppressWarnings("resource")
  public Utente recuperaPerUsername(String username) throws SQLException {
    RecensioneManager rm = new RecensioneManager();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Utente temp = null;
    ResultSet rs = null;
    String sql = "SELECT * FROM " + TABLENAME + " WHERE Username = ?";
    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      rs = preparedStatement.executeQuery();
      if (rs != null && rs.first()) {
        temp = new Utente();
        temp.setUsername(rs.getString("Username"));
        temp.setNome(rs.getString("Nome"));
        temp.setCognome(rs.getString("Cognome"));
        temp.setSesso(rs.getString("Sesso"));
        temp.setPassword(rs.getString("Password"));
        temp.setDescrizione(rs.getString("Descrizione"));
        temp.setNumAnnunci(rs.getInt("NumAnnunci"));
        temp.setMedia(rm.media(temp.getUsername()));
      }
    } finally {
      rs.close();
      preparedStatement.close();
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public ArrayList<Utente> recuperaUtenti() throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ArrayList<Utente> temp;

    String sql = "SELECT * FROM " + TABLENAME;
    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      ResultSet rs = preparedStatement.executeQuery();
      temp = listaUtenti(rs);
      if (temp == null) {
        temp = new ArrayList<Utente>();
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public void creaUtente(Utente u) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String sql = "INSERT INTO " + TABLENAME + " VALUES(?,?,?,?,?,?,?,?,?)";
    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, u.getNome());
      preparedStatement.setString(2, u.getCognome());
      preparedStatement.setString(3, u.getSesso());
      preparedStatement.setString(4, u.getUsername());
      preparedStatement.setString(5, u.getPassword());
      preparedStatement.setBoolean(6, u.isGestore());
      preparedStatement.setInt(7, 0);
      preparedStatement.setString(8, "Descrizione di default");
      preparedStatement.setInt(9, 0);
      preparedStatement.executeUpdate();
      connection.commit();
    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
      }
    }
  }

  public void modificaUtente(Utente u) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String updateQuery = "UPDATE " + TABLENAME
        + " SET Nome = ?, Cognome = ?, Password = ?, Descrizione = ? , NumAnnunci = ?"
        + " WHERE Username = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(updateQuery);
      preparedStatement.setString(1, u.getNome());
      preparedStatement.setString(2, u.getCognome());
      preparedStatement.setString(3, u.getPassword());
      preparedStatement.setString(4, u.getDescrizione());
      preparedStatement.setInt(5, u.getNumAnnunci());
      preparedStatement.setString(6, u.getUsername());
      preparedStatement.executeUpdate();
      connection.commit();
    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
      }
    }
  }

  public void rimuoviUtente(Utente utente) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String delete = "DELETE FROM " + TABLENAME + " WHERE Username = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(delete);
      preparedStatement.setString(1, utente.getUsername());
      preparedStatement.executeUpdate();
      connection.commit();
    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
      }
    }
  }
}
