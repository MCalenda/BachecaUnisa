package gestionesegnalazioni;

public class Segnalazione {

  public Segnalazione(int id, String descrizione, int motivazione,
      Integer recensione, Integer annuncio, String utente) {
    this.id = id;
    this.descrizione = descrizione;
    this.motivazione = motivazione;
    this.annuncio = annuncio;
    this.recensione = recensione;
    this.utente = utente;
  }

  public Segnalazione(Integer recensione, Integer annuncio) {
    this.recensione = recensione;
    this.annuncio = annuncio;
  }

  public Segnalazione() {
  }

  public String getDescrizione() {
    return descrizione;
  }

  public int getMotivazione() {
    return motivazione;
  }

  public int getId() {
    return id;
  }

  /** @return id dell'annuncio segnalato se {@code isTipoSegnalazione()}, altrimenti della recensione. */
  public Integer getIdSegnalato() {
    if (isTipoSegnalazione()) {
      return annuncio;
    } else {
      return recensione;
    }
  }

  /** @return {@code true} se l'oggetto segnalato è un annuncio, {@code false} se è una recensione. */
  public boolean isTipoSegnalazione() {
    return annuncio != null;
  }

  public int getAnnuncio() {
    return annuncio;
  }

  public int getRecensione() {
    return recensione;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public void setMotivazione(int motivazione) {
    this.motivazione = motivazione;
  }

  public void setAnnuncio(Integer annuncio) {
    this.annuncio = annuncio;
    recensione = null;
  }

  public void setRecensione(Integer recensione) {
    this.recensione = recensione;
    annuncio = null;
  }

  public String getUtente() {
    return utente;
  }

  public void setUtente(String utente) {
    this.utente = utente;
  }

  private int id;
  private String descrizione;
  public static final int MOTIVO1 = 1;
  public static final int MOTIVO2 = 2;
  public static final int MOTIVO3 = 3;
  private int motivazione;
  private Integer annuncio;
  private Integer recensione;
  private String utente;
}
