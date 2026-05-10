package gestioneannunci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jdbc.DriverManagerConnectionPool;

public class AnnuncioManager {

  private static final String TABLENAME = "Annuncio";

  public ArrayList<Annuncio> listaAnnunci(ResultSet rs) throws SQLException {
    rs.beforeFirst();
    ArrayList<Annuncio> lista = new ArrayList<Annuncio>();
    Annuncio temp;
    while (rs.next()) {
      temp = new Annuncio();
      temp.setDipartimento(rs.getString(1));
      temp.setTitolo(rs.getString(2));
      temp.setDescrizione(rs.getString(3));
      temp.setTipologia(rs.getBoolean(4));
      temp.setNumSegnalazioni(rs.getInt(5));
      temp.setId(rs.getInt(6));
      temp.setUsernameUtente(rs.getString(7));
      lista.add(temp);
    }
    return lista;
  }

  public void creaAnnuncio(Annuncio annuncio) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String sql = "INSERT INTO " + TABLENAME + " VALUES(?,?,?,?,0, null, ?)";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      String dip = annuncio.getDipartimento();
      if (dip == null) {
        dip = "Informatica";
      }
      preparedStatement.setString(1, dip);
      preparedStatement.setString(2, annuncio.getTitolo());
      preparedStatement.setString(3, annuncio.getDescrizione());
      int tip;
      if (annuncio.isTipologia()) {
        tip = 1;
      } else {
        tip = 0;
      }
      preparedStatement.setInt(4, tip);
      preparedStatement.setString(5, annuncio.getUsernameUtente());
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

  public void rimuoviAnnuncio(Annuncio annuncio) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    String delete = "DELETE FROM " + TABLENAME + " WHERE Id = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(delete);
      preparedStatement.setInt(1, annuncio.getId());
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

  public void modificaAnnuncio(Annuncio annuncio) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    String sql = "UPDATE " + TABLENAME + " SET Dipartimento = ?, Titolo = ?, Descrizione = ?"
        + ", Tipologia = ?, NumSegnalazioni = ?, ID = ?, Utente_Username = ?"
        + " WHERE ID = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, annuncio.getDipartimento());
      preparedStatement.setString(2, annuncio.getTitolo());
      preparedStatement.setString(3, annuncio.getDescrizione());
      int a;
      if (annuncio.isTipologia()) {
        a = 1;
      } else {
        a = 0;
      }
      preparedStatement.setInt(4, a);
      preparedStatement.setInt(5, annuncio.getNumSegnalazioni());
      preparedStatement.setInt(6, annuncio.getId());
      preparedStatement.setString(7, annuncio.getUsernameUtente());
      preparedStatement.setInt(8, annuncio.getId());
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

  public ArrayList<Annuncio> recuperaAnnunci() throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ArrayList<Annuncio> temp = null;

    String sql = "SELECT * FROM " + TABLENAME;

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      ResultSet rs = preparedStatement.executeQuery();
      if (rs.first()) {
        temp = listaAnnunci(rs);
      }
      if (temp == null) {
        temp = new ArrayList<Annuncio>();
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public Annuncio recuperaPerId(int id) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Annuncio temp = new Annuncio();

    String sql = "SELECT * FROM " + TABLENAME + " WHERE id = ?";
    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, id);
      ResultSet rs = preparedStatement.executeQuery();
      if (!rs.next()) {
        temp = null;
      } else {
        temp.setId(rs.getInt("ID"));
        temp.setTitolo(rs.getString("titolo"));
        temp.setDescrizione(rs.getString("descrizione"));
        temp.setDipartimento(rs.getString("dipartimento"));
        temp.setTipologia(rs.getBoolean("tipologia"));
        temp.setUsernameUtente(rs.getString("Utente_Username"));
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public ArrayList<Annuncio> recuperaPerTipologia(String descrizione, boolean tipo,
      String dipartimento) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ArrayList<Annuncio> temp;
    int t = tipo ? 1 : 0;

    String sql = "SELECT * FROM " + TABLENAME + " WHERE (Descrizione LIKE ? OR Titolo LIKE ?)"
        + " AND Tipologia = ? AND Dipartimento = ?";
    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, "%" + descrizione + "%");
      preparedStatement.setString(2, "%" + descrizione + "%");
      preparedStatement.setString(3, Integer.toString(t));
      preparedStatement.setString(4, dipartimento);
      ResultSet rs = preparedStatement.executeQuery();
      if (!rs.next()) {
        temp = new ArrayList<Annuncio>();
      } else {
        temp = listaAnnunci(rs);
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public ArrayList<Annuncio> recuperaPerDipartimento(String dipartimento) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ArrayList<Annuncio> temp;

    String sql = "SELECT * FROM " + TABLENAME + " WHERE Dipartimento = ?";
    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, dipartimento);
      ResultSet rs = preparedStatement.executeQuery();
      temp = listaAnnunci(rs);
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }

  public ArrayList<Annuncio> recuperaPerUtente(String username) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ArrayList<Annuncio> temp;

    String sql = "SELECT * FROM " + TABLENAME + " WHERE Utente_Username = ?";

    try {
      connection = DriverManagerConnectionPool.getConnection();
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, username);
      ResultSet rs = preparedStatement.executeQuery();
      if (!rs.next()) {
        temp = new ArrayList<Annuncio>();
      } else {
        temp = listaAnnunci(rs);
      }
    } finally {
      DriverManagerConnectionPool.releaseConnection(connection);
    }
    return temp;
  }
}
