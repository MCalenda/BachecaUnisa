package gestionerecensioni;

import gestionesegnalazioni.Segnalabile;
import gestionesegnalazioni.Segnalazione;

public class Recensione implements Segnalabile {

  public Recensione(int id, int valutazione, String descrizione,
      String mittente, String destinatario) {
    this.id = id;
    this.valutazione = valutazione;
    this.descrizione = descrizione;
    this.mittente = mittente;
    this.destinatario = destinatario;
  }

  public Recensione(int valutazione, String descrizione,
      String mittente, String destinatario) {
    this.valutazione = valutazione;
    this.descrizione = descrizione;
    this.mittente = mittente;
    this.destinatario = destinatario;
  }

  public Recensione() {
  }

  public int getValutazione() {
    return valutazione;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public int getId() {
    return id;
  }

  public String getMittente() {
    return mittente;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public int getNumSegnalazioni() {
    return numSegnalazioni;
  }

  public void setNumSegnalazioni(int numSegnalazioni) {
    this.numSegnalazioni = numSegnalazioni;
  }

  public void setMittente(String mittente) {
    this.mittente = mittente;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setValutazione(int valutazione) {
    this.valutazione = valutazione;
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

  private int id;
  private int valutazione;
  private String descrizione;
  private String mittente;
  private String destinatario;
  private int numSegnalazioni = 0;
}
