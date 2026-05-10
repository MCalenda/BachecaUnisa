-- Schema database per BachecaUnisa
-- DB: Bacheca | Host: localhost:3306 | User: root | Password: 0000

CREATE DATABASE IF NOT EXISTS Bacheca
  CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE Bacheca;

-- Ordine colonne CRITICO: corrisponde agli INSERT positional nel codice Java

CREATE TABLE IF NOT EXISTS Utente (
  Nome        VARCHAR(50)   NOT NULL,
  Cognome     VARCHAR(50)   NOT NULL,
  Sesso       CHAR(1)       NOT NULL,
  Username    VARCHAR(50)   NOT NULL,
  Password    VARCHAR(100)  NOT NULL,
  Gestore     TINYINT(1)    NOT NULL DEFAULT 0,
  NumAnnunci  INT           NOT NULL DEFAULT 0,
  Descrizione VARCHAR(500)           DEFAULT 'Descrizione di default',
  Media       INT           NOT NULL DEFAULT 0,
  PRIMARY KEY (Username)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Annuncio (
  Dipartimento    VARCHAR(100)  NOT NULL,
  Titolo          VARCHAR(200)  NOT NULL,
  Descrizione     TEXT          NOT NULL,
  Tipologia       TINYINT(1)    NOT NULL DEFAULT 0,
  NumSegnalazioni INT           NOT NULL DEFAULT 0,
  ID              INT           NOT NULL AUTO_INCREMENT,
  Utente_Username VARCHAR(50)   NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (Utente_Username) REFERENCES Utente(Username) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Recensione (
  Descrizione  TEXT         NOT NULL,
  Mittente     VARCHAR(50)  NOT NULL,
  ID           INT          NOT NULL AUTO_INCREMENT,
  Destinatario VARCHAR(50)  NOT NULL,
  Valutazione  INT          NOT NULL DEFAULT 0,
  PRIMARY KEY (ID),
  FOREIGN KEY (Mittente)     REFERENCES Utente(Username) ON DELETE CASCADE,
  FOREIGN KEY (Destinatario) REFERENCES Utente(Username) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Segnalazione (
  ID          INT          NOT NULL AUTO_INCREMENT,
  Descrizione TEXT         NOT NULL,
  Utente      VARCHAR(50)  NOT NULL,
  Motivazione INT          NOT NULL,
  SegnalatoA  INT                   DEFAULT NULL,
  SegnalatoR  INT                   DEFAULT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (Utente)     REFERENCES Utente(Username)   ON DELETE CASCADE,
  FOREIGN KEY (SegnalatoA) REFERENCES Annuncio(ID)       ON DELETE CASCADE,
  FOREIGN KEY (SegnalatoR) REFERENCES Recensione(ID)     ON DELETE CASCADE
) ENGINE=InnoDB;

-- Utente gestore di default (username: admin, password: adminadmin)
INSERT IGNORE INTO Utente VALUES ('Admin', 'Admin', 'M', 'admin', 'adminadmin', 1, 0, 'Amministratore del sistema', 0);
