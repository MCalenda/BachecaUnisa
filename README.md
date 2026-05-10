# BachecaUnisa

Applicazione web per la gestione di annunci universitari (gruppi di studio e tutorati) sviluppata con Java EE / Servlet + JSP su Apache Tomcat 9, con database MariaDB.

## Requisiti

- [Docker](https://docs.docker.com/get-docker/) e Docker Compose

## Avvio rapido

```bash
docker compose up --build
```

L'applicazione sarà disponibile su **http://localhost:8080/BACHECAUNISA**.

Credenziali amministratore di default:

| Campo    | Valore     |
|----------|------------|
| Username | `admin`    |
| Password | `adminadmin` |

### Interfaccia database (Adminer)

Disponibile su **http://localhost:8081** con i seguenti parametri:

| Campo    | Valore     |
|----------|------------|
| Server   | `db`       |
| Username | `root`     |
| Password | `0000`     |
| Database | `Bacheca`  |

## Reset del database

Per ripartire da zero (utile dopo modifiche a `bacheca.sql`):

```bash
docker compose down -v
docker compose up --build
```

## Struttura del progetto

```
BachecaUnisa/
├── src/                        # Sorgenti Java
│   ├── gestioneannunci/        # Servlet, Manager e bean per gli annunci
│   ├── gestioneutenti/         # Servlet, Manager e bean per gli utenti
│   ├── gestionerecensioni/     # Servlet, Manager e bean per le recensioni
│   ├── gestionesegnalazioni/   # Servlet, Manager e bean per le segnalazioni
│   └── jdbc/                   # Connection pool JDBC
├── WebContent/                 # Pagine JSP, CSS e risorse statiche
│   └── WEB-INF/
│       ├── web.xml
│       └── lib/                # mysql-connector-java
├── bacheca.sql                 # Schema e dati iniziali del database
├── Dockerfile
└── docker-compose.yml
```

## Funzionalità principali

- **Registrazione e login** utenti
- **Creazione annunci** di tipo "Gruppo di studio" o "Attività di tutorato" (max 5 per utente)
- **Ricerca annunci** per tipologia, dipartimento o testo libero
- **Profilo utente** con recensioni e valutazioni
- **Segnalazione** di annunci e recensioni
- **Pannello gestore** per la moderazione dei contenuti

## Variabili d'ambiente

| Variabile | Default     | Descrizione                  |
|-----------|-------------|------------------------------|
| `DB_HOST` | `localhost` | Hostname del database        |
| `DB_USER` | `root`      | Utente database              |
| `DB_PASS` | `0000`      | Password database            |
| `DB_NAME` | `Bacheca`   | Nome del database            |
