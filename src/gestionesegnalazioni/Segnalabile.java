package gestionesegnalazioni;

import java.util.List;

public interface Segnalabile {

  void addSegnalazione(Segnalazione a);

  void deleteSegnalazione(int id);

  Segnalazione leggiSegnalazione(int indice);

  List<Segnalazione> lista = null;
}
