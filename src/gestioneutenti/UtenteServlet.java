package gestioneutenti;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UtenteServlet")
public class UtenteServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final String DEFAULT_DESCRIPTION = "Questo utente non ha ancora"
      + " scritto una descrizione :(";
  UtenteManager utenteManager = new UtenteManager();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    SessioneUtente sessione = (SessioneUtente) request.getSession().getAttribute("Utente");

    try {
      String azione = request.getParameter("azione");
      if (azione.equalsIgnoreCase("stampaUtenti")) {
        ArrayList<Utente> risultato = stampaUtenti();
        request.getSession().setAttribute("urisultato", risultato);
        response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
      }

      if (azione.equalsIgnoreCase("prelevaUtente")) {
        Utente u = prelevautente(request.getParameter("username"));
        request.getSession().setAttribute("utenteTrovato", u);
        if (request.getParameter("luogo").equalsIgnoreCase("crea")) {
          response.sendRedirect(request.getContextPath() + "/CreaNuovoAnnuncio.jsp");
        } else if (request.getParameter("luogo").equalsIgnoreCase("pro")) {
          response.sendRedirect(request.getContextPath() + "/ProfiloPersonale.jsp");
        } else if (request.getParameter("luogo").equalsIgnoreCase("mod")) {
          response.sendRedirect(request.getContextPath() + "/modificaProfilo.jsp");
        } else if (request.getParameter("luogo").equalsIgnoreCase("feed")) {
          if (request.getParameter("es").equals("true")) {
            response.sendRedirect(request.getContextPath() + "/RilascioFeedback.jsp?es=true&username="
                + u.getUsername());
          } else {
            response.sendRedirect(request.getContextPath() + "/RilascioFeedback.jsp?es=false&username="
                + u.getUsername());
          }
        } else {
          response.sendRedirect(request.getContextPath() + "/ProfiloUtente.jsp?username="
              + u.getUsername());
        }
      }

      if (azione.equalsIgnoreCase("rimuoviUtente")) {
        if (sessione.getRuolo().equals("Gestore")) {
          String username = request.getParameter("username");
          rimuoviUtente(username);
          response.sendRedirect(request.getContextPath() + "/VisualeGestore.jsp");
        }
      }

      if (azione.equalsIgnoreCase("modificaPassword")) {
        String newPassword = request.getParameter("newPassword");
        String usernameLog = sessione.getUsername();
        modificaPassword(usernameLog, newPassword);
        response.sendRedirect(request.getContextPath() + "/ProfiloPersonale.jsp");
      }

      if (azione.equalsIgnoreCase("modificaUtente")) {
        String username = sessione.getUsername();
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String descrizione = request.getParameter("descrizione");
        modificaUtente(username, nome, cognome, descrizione);
        response.sendRedirect(request.getContextPath() + "/ProfiloPersonale.jsp");
      }

      if (azione.equalsIgnoreCase("creaUtente")) {
        if (utenteManager.recuperaPerUsername(request.getParameter("username")) != null) {
          response.sendRedirect(request.getContextPath() + "/registrazione.jsp?ar=t");
        } else {
          String username = request.getParameter("username");
          String nome = request.getParameter("nome");
          String cognome = request.getParameter("cognome");
          String sesso = request.getParameter("sesso");
          String password = request.getParameter("password");
          creaUtente(username, nome, cognome, sesso, password, false);
          Utente u = new Utente(username, nome, cognome, sesso, password, DEFAULT_DESCRIPTION,
              0, false);
          SessioneUtente su = new SessioneUtente(u);
          request.getSession().setAttribute("Utente", su);
          response.sendRedirect(request.getContextPath() + "/Login.jsp");
        }
      }

      if (azione.equalsIgnoreCase("Login")) {
        doLogin(request, response);
      }

      if (azione.equalsIgnoreCase("Logout")) {
        doLogout(request, response);
      }

      if (azione.equalsIgnoreCase("AggiungiAnnuncio")) {
        aggiungiAnnuncio(request);
        response.sendRedirect(request.getContextPath() + "/AnnunciPersonali.jsp");
      }

    } catch (Exception exc) {
      exc.printStackTrace();
    }
  }

  private void aggiungiAnnuncio(HttpServletRequest request) throws SQLException {
    SessioneUtente su = (SessioneUtente) request.getSession().getAttribute("Utente");
    Utente u = prelevautente(su.getUsername());
    u.setNumAnnunci(u.getNumAnnunci() + 1);
    utenteManager.modificaUtente(u);
  }

  private Utente prelevautente(String parameter) throws SQLException {
    return utenteManager.recuperaPerUsername(parameter);
  }

  private ArrayList<Utente> stampaUtenti() throws SQLException {
    return utenteManager.recuperaUtenti();
  }

  private void rimuoviUtente(String username) throws SQLException {
    Utente temp = utenteManager.recuperaPerUsername(username);
    utenteManager.rimuoviUtente(temp);
  }

  private void modificaUtente(String username, String nome, String cognome,
      String descrizione) throws SQLException {
    Utente temp = utenteManager.recuperaPerUsername(username);
    temp.setNome(nome);
    temp.setCognome(cognome);
    temp.setDescrizione(descrizione);
    utenteManager.modificaUtente(temp);
  }

  private void modificaPassword(String username, String newPassword) throws SQLException {
    Utente temp = utenteManager.recuperaPerUsername(username);
    temp.setPassword(newPassword);
    utenteManager.modificaUtente(temp);
  }

  private void creaUtente(String username, String nome, String cognome, String sesso,
      String password, boolean gestore) throws SQLException {
    Utente u = new Utente(username, nome, cognome, sesso, password, 0, gestore);
    utenteManager.creaUtente(u);
  }

  private void doLogin(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    try {
      Utente u = utenteManager.recuperaSeRegistrato(username, password);
      SessioneUtente su = u.isGestore()
          ? new SessioneUtente(u, "Gestore")
          : new SessioneUtente(u, "Utente");
      request.getSession().setAttribute("Utente", su);
      response.sendRedirect(request.getContextPath() + "/Homepage.jsp");
    } catch (Exception e) {
      request.setAttribute("Done", "falso");
      RequestDispatcher x = getServletContext().getRequestDispatcher("/Login.jsp?error=true");
      x.forward(request, response);
    }
  }

  private void doLogout(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    if (request.getSession().getAttribute("Utente") == null
        || request.getSession().getAttribute("Gestore") == null) {
      response.sendRedirect(request.getContextPath() + "/Login.jsp");
      return;
    }
    request.getSession().removeAttribute("Utente");
    request.getSession().removeAttribute("Gestore");
    request.getSession().invalidate();
    response.sendRedirect(request.getContextPath() + "/Login.jsp");
  }
}
