package gestionerecensioni;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import jdbc.DriverManagerConnectionPool;

public class RecensioneManager {

  private static final String TABLENAME = "Recensione";

  public boolean creaRecensione(Recensione recensione) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    boolean control = true;
    try {
      if (recuperaPerUtenti(recensione.getMittente(), recensione.getDestinatario()) != null) {
        control = false;
      } else {
        String sql = "INSERT INTO " + TABLENAME + " VALUES(?,?,null,?,?)";
        connection = DriverManagerConnectionPool.getConnection();
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, recensione.getDescrizione());
        preparedStatement.setString(2, recensione.getMittente());
        preparedStatement.setString(3, recensione.getDestinatario());
        preparedStatement.setInt(4, recensione.getValutazione());
        preparedStatement.executeUpdate();
        connection.commit();
      }
    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
        return control;
      }
    }
  }

  public void rimuoviRecensione(Recensione recensione) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String delete = "DELETE FROM " + TABLENAME + " WHERE Id = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(delete);
      preparedStatement.setInt(1, recensione.getId());
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

  public ArrayList<Recensione> listaRecensioni(ResultSet rs) throws SQLException {
    rs.beforeFirst();
    ArrayList<Recensione> lista = new ArrayList<Recensione>();
    Recensione temp;
    while (rs.next()) {
      temp = new Recensione();
      temp.setDescrizione(rs.getString("Descrizione"));
      temp.setDestinatario(rs.getString("Destinatario"));
      temp.setId(rs.getInt("ID"));
      temp.setMittente(rs.getString("Mittente"));
      temp.setValutazione(rs.getInt("Valutazione"));
      lista.add(temp);
    }
    return lista;
  }

  public ArrayList<Recensione> recuperaRecensioni(String utenteDestinatario) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ArrayList<Recensione> temp = null;

    String sql = "SELECT * FROM " + TABLENAME + " WHERE Destinatario LIKE '"
        + utenteDestinatario + "'";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        temp = listaRecensioni(rs);
      } else {
        temp = new ArrayList<Recensione>();
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public Recensione recuperaPerId(int id) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Recensione temp = null;

    String sql = "SELECT * FROM " + TABLENAME + " WHERE ID = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        temp = new Recensione(rs.getInt("ID"), rs.getInt("Valutazione"),
               rs.getString("Descrizione"), rs.getString("Mittente"),
               rs.getString("Destinatario"));
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public Recensione recuperaPerUtenti(String mittente, String destinatario) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Recensione temp = null;

    String sql = "SELECT * FROM " + TABLENAME + " WHERE Destinatario = '" + destinatario
            + "' AND Mittente = '" + mittente + "'";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        temp = new Recensione(rs.getInt("ID"), rs.getInt("Valutazione"),
               rs.getString("Descrizione"), rs.getString("Mittente"),
               rs.getString("Destinatario"));
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public void modificaRecensione(Recensione recensione) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    String sql = "UPDATE " + TABLENAME + " SET Descrizione = ?, Valutazione = ?"
        + " WHERE Destinatario = ? AND Mittente = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, recensione.getDescrizione());
      preparedStatement.setInt(2, recensione.getValutazione());
      preparedStatement.setString(3, recensione.getDestinatario());
      preparedStatement.setString(4, recensione.getMittente());
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

  public int media(String username) throws SQLException {
    ArrayList<Recensione> temp = recuperaRecensioni(username);
    if (temp != null && temp.size() != 0) {
      Iterator<Recensione> lista = temp.iterator();
      int a = 0;
      while (lista.hasNext()) {
        a += lista.next().getValutazione();
      }
      return a / temp.size();
    }
    return 0;
  }

  public ArrayList<Recensione> recuperaPerUtente(String username) throws SQLException {
    ArrayList<Recensione> lista = new ArrayList<Recensione>();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    String sql = "SELECT * FROM " + TABLENAME + " WHERE Destinatario LIKE ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        lista = listaRecensioni(rs);
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return lista;
  }
}
