package gestioneannunci;

import gestionesegnalazioni.Segnalabile;
import gestionesegnalazioni.Segnalazione;

public class Annuncio implements Segnalabile {

  public Annuncio(int id, String titolo, String descrizione,
      boolean tipologia, String dipartimento, String usernameUtente) {
    this.id = id;
    this.titolo = titolo;
    this.descrizione = descrizione;
    this.tipologia = tipologia;
    this.dipartimento = dipartimento;
    this.usernameUtente = usernameUtente;
  }

  public Annuncio(String titolo, String descrizione,
      boolean tipologia, String dipartimento, String usernameUtente) {
    this.titolo = titolo;
    this.descrizione = descrizione;
    this.tipologia = tipologia;
    this.dipartimento = dipartimento;
    this.usernameUtente = usernameUtente;
  }

  public Annuncio() {
  }

  public String getTitolo() {
    return titolo;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public boolean isTipologia() {
    return tipologia;
  }

  public String getDipartimento() {
    return dipartimento;
  }

  public int getId() {
    return id;
  }

  public int getNumSegnalazioni() {
    return numSegnalazioni;
  }

  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public void setTipologia(boolean tipologia) {
    this.tipologia = tipologia;
  }

  public void setDipartimento(String dipartimento) {
    this.dipartimento = dipartimento;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setNumSegnalazioni(int numSegnalazioni) {
    this.numSegnalazioni = numSegnalazioni;
  }

  public void addSegnalazione(Segnalazione a) {
    lista.add(a);
  }

  public void deleteSegnalazione(int id) {
    lista.remove(id);
  }

  public Segnalazione leggiSegnalazione(int indice) {
    return lista.get(indice);
  }

  public String getUsernameUtente() {
    return usernameUtente;
  }

  public void setUsernameUtente(String usernameUtente) {
    this.usernameUtente = usernameUtente;
  }

  private String titolo;
  private String descrizione;
  private boolean tipologia;
  private String dipartimento;
  private int id;
  private int numSegnalazioni = 0;
  private String usernameUtente;
}
