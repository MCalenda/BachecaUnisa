package gestionesegnalazioni;

import gestioneannunci.AnnuncioManager;
import gestionerecensioni.RecensioneManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jdbc.DriverManagerConnectionPool;

public class SegnalazioneManager {

  private static final String TABLENAME = "Segnalazione";
  private static final int MAX_SEGNALAZIONI = 50;

  public boolean creaSegnalazione(Segnalazione segnalazione) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    String sql;
    String test;

    try {
      connection = DriverManagerConnectionPool.getConnection();

      if (segnalazione.isTipoSegnalazione()) {
        sql = "INSERT INTO " + TABLENAME + " VALUES(null, ?, ?, ?, ?, null)";
        test = "SELECT COUNT(ID) AS segnalazioni FROM " + TABLENAME
            + " WHERE SegnalatoA LIKE ?";
      } else {
        sql = "INSERT INTO " + TABLENAME + " VALUES(null, ?, ?, ?, null, ?)";
        test = "SELECT COUNT(ID) AS segnalazioni FROM " + TABLENAME
            + " WHERE SegnalatoR LIKE ?";
      }
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, segnalazione.getDescrizione());
      preparedStatement.setString(2, segnalazione.getUtente());
      preparedStatement.setInt(3, segnalazione.getMotivazione());
      preparedStatement.setInt(4, segnalazione.getIdSegnalato());
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
    int numero = 0;
    try {
      preparedStatement = connection.prepareStatement(test);
      preparedStatement.setInt(1, segnalazione.getIdSegnalato());
      ResultSet rs = preparedStatement.executeQuery();
      rs.first();
      numero = rs.getInt("segnalazioni");
    } finally {
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } finally {
        DriverManagerConnectionPool.releaseConnection(connection);
      }
    }
    if (numero > MAX_SEGNALAZIONI) {
      if (segnalazione.isTipoSegnalazione()) {
        AnnuncioManager io = new AnnuncioManager();
        io.rimuoviAnnuncio(io.recuperaPerId(segnalazione.getIdSegnalato()));
      } else {
        RecensioneManager io = new RecensioneManager();
        io.rimuoviRecensione(io.recuperaPerId(segnalazione.getIdSegnalato()));
      }
      return false;
    } else {
      return true;
    }
  }

  public void rimuoviSegnalazione(Segnalazione temp) throws SQLException {
    String sql = "DELETE FROM " + TABLENAME + " WHERE ID LIKE " + temp.getId();
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
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

  public ArrayList<Segnalazione> recuperaSegnalazioni() throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ArrayList<Segnalazione> temp = null;

    String sql = "SELECT * FROM " + TABLENAME;

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.next()) {
        temp = listaSegnalazioni(rs);
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public ArrayList<Segnalazione> listaSegnalazioni(ResultSet rs) throws SQLException {
    rs.first();
    ArrayList<Segnalazione> lista = new ArrayList<Segnalazione>();
    Segnalazione temp;
    while (!rs.isAfterLast()) {
      temp = new Segnalazione(rs.getInt("ID"), rs.getString("Descrizione"),
          rs.getInt("Motivazione"), rs.getInt("SegnalatoA"),
          rs.getInt("SegnalatoR"), rs.getString("Utente"));
      lista.add(temp);
      rs.next();
    }
    return lista;
  }

  public Segnalazione recuperaPerId(int id) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Segnalazione temp = new Segnalazione();

    String sql = "SELECT * FROM " + TABLENAME + " WHERE id = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      ResultSet rs = preparedStatement.executeQuery();
      if (!rs.next()) {
        temp = null;
      } else {
        temp.setMotivazione(rs.getInt("motivazione"));
        temp.setDescrizione(rs.getString("descrizione"));
        temp.setAnnuncio(rs.getInt("annuncio"));
        temp.setRecensione(rs.getInt("recensione"));
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }
}
