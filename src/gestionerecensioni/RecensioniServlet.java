package gestionerecensioni;

import gestioneutenti.SessioneUtente;
import gestioneutenti.UtenteManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RecensioniServlet")
public class RecensioniServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  RecensioneManager recensioneManager = new RecensioneManager();
  UtenteManager utenteManager = new UtenteManager();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    SessioneUtente sessione = (SessioneUtente) request.getSession().getAttribute("Utente");
    String usernameLog = sessione.getUsername();

    try {
      String azione = request.getParameter("azione");

      if (azione.equalsIgnoreCase("stampaRecensioni")) {
        String username = request.getParameter("username");
        ArrayList<Recensione> caso = stampaRecensioni(username);
        request.getSession().setAttribute("Lista", caso);
        String luogo = request.getParameter("luogo");
        response.sendRedirect(request.getContextPath() + "/" + luogo + ".jsp");

      } else if (azione.equalsIgnoreCase("rimuoviRecensione")) {
        int id = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("usernameUtente");
        if (sessione.getRuolo().equalsIgnoreCase("Gestore")) {
          rimuoviRecensione(id);
          response.sendRedirect(request.getContextPath() + "/HomepageGestore.jsp");
        } else {
          if (usernameLog.equalsIgnoreCase(username)) {
            rimuoviRecensione(id);
            response.sendRedirect(request.getContextPath() + "/ProfiloPersonale.jsp");
          }
        }

      } else if (azione.equalsIgnoreCase("modificaRecensione")) {
        int valutazione = Integer.parseInt(request.getParameter("rating"));
        String descrizione = request.getParameter("descrizione");
        String username = request.getParameter("mittente");
        int id = Integer.parseInt(request.getParameter("id"));
        if (usernameLog.equalsIgnoreCase(username)) {
          modificaRecensione(id, valutazione, descrizione);
          response.sendRedirect(request.getContextPath() + "/ProfiloUtente.jsp?username=" + request.getParameter("destinatario"));
        }

      } else if (azione.equalsIgnoreCase("segnalaRecensione")) {
        int id = Integer.parseInt(request.getParameter("id"));
        Recensione rec = recensioneManager.recuperaPerId(id);
        request.getSession().setAttribute("recensione", rec);
        response.sendRedirect(request.getContextPath() + "/");

      } else if (azione.equalsIgnoreCase("creaRecensione")) {
        if (sessione.getRuolo().equalsIgnoreCase("Utente")) {
          int valutazione = Integer.parseInt(request.getParameter("rating"));
          String descrizione = request.getParameter("descrizione");
          String mittente = request.getParameter("mittente");
          String destinatario = request.getParameter("destinatario");
          boolean control = creaRecensione(valutazione, descrizione, mittente, destinatario);
          if (control) {
            response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
          } else {
            response.sendRedirect(request.getContextPath() + "/RilascioFeedback.jsp?username=" + destinatario + "&es=true");
          }
        }

      } else if (azione.equalsIgnoreCase("visualizzarecensione")) {
        int id = Integer.parseInt(request.getParameter("id"));
        Recensione recensioneTrovata = recensioneManager.recuperaPerId(id);
        request.getSession().setAttribute("recensioneTrovata", recensioneTrovata);

        if (request.getParameter("luogo").equalsIgnoreCase("se")) {
          response.sendRedirect(request.getContextPath() + "/segnalaRecensione.jsp?id=" + id);
        } else if (request.getParameter("luogo").equalsIgnoreCase("mo")) {
          response.sendRedirect(request.getContextPath() + "/modificaRecensione.jsp?id=" + id);
        }

      } else if (azione.equalsIgnoreCase("AggiungiSegnalazione")) {
        aggiungiSegnalazione(request);
        response.sendRedirect(request.getContextPath() + "/Homepage.jsp");

      } else if (azione.equalsIgnoreCase("recensioniUtente")) {
        ArrayList<Recensione> lista = recensioneManager.recuperaPerUtente(
            request.getParameter("username"));
        request.getSession().setAttribute("recensioni", lista);

        if (request.getParameter("luogo").equalsIgnoreCase("perso")) {
          response.sendRedirect(request.getContextPath() + "/ProfiloPersonale.jsp");
        } else {
          response.sendRedirect(request.getContextPath() + "/ProfiloUtente.jsp?username="
               + request.getParameter("username"));
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private ArrayList<Recensione> stampaRecensioni(String username) throws SQLException {
    return recensioneManager.recuperaRecensioni(username);
  }

  private void aggiungiSegnalazione(HttpServletRequest request) throws SQLException {
    int id = Integer.parseInt(request.getParameter("id"));
    Recensione a = recensioneManager.recuperaPerId(id);
    a.setNumSegnalazioni(a.getNumSegnalazioni() + 1);
    recensioneManager.modificaRecensione(a);
  }

  private void rimuoviRecensione(int id) throws SQLException {
    Recensione recensione = recensioneManager.recuperaPerId(id);
    recensioneManager.rimuoviRecensione(recensione);
  }

  private void modificaRecensione(int id, int valutazione, String descrizione) throws SQLException {
    Recensione recensione = recensioneManager.recuperaPerId(id);
    recensione.setDescrizione(descrizione);
    recensione.setValutazione(valutazione);
    recensioneManager.modificaRecensione(recensione);
  }

  private boolean creaRecensione(int valutazione, String descrizione,
      String mittente, String destinatario) throws SQLException {
    Recensione temp = new Recensione(valutazione, descrizione, mittente, destinatario);
    return recensioneManager.creaRecensione(temp);
  }
}
