package Control_Class;

import DAO_Classes.LezioneDAO;
import DAO_Classes.PagamentoDAO;
import DAO_Classes.RegistrazioneDAO;
import DAO_Classes.StudenteDAO;
import Entity_Classes.Lezione;
import Entity_Classes.Pagamento;
import Entity_Classes.Studente;
import Entity_Classes.Registrazione;
import exception.PrenotazioneException;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class GestioneScuolaMusica {
    private StudenteDAO studenteDAO;
    private PagamentoDAO pagamentoDAO;
    private RegistrazioneDAO registrazioneDAO;
    private final LezioneDAO dao;
    private final Scanner scanner = new Scanner(System.in);

    public GestioneScuolaMusica() {
        this.studenteDAO = new StudenteDAO();
        this.pagamentoDAO = new PagamentoDAO();
        this.registrazioneDAO = new RegistrazioneDAO();
        this.dao = new LezioneDAO();
    }

    // Richiesta interattiva di nome o cognome con validazione
    private String richiediNomeValido(String campo) {
        while (true) {
            System.out.print("Inserisci " + campo + ": ");
            String input = scanner.nextLine().trim();

            input = input.replaceAll("\\s+", " ").toLowerCase();

            if (!input.matches("[a-zA-ZàèéìòùÀÈÉÌÒÙ'\\s-]+")) {
                System.out.println("Inserisci solo lettere. Riprova.");
                continue;
            }

            String[] parole = input.split(" ");
            StringBuilder sb = new StringBuilder();
            for (String parola : parole) {
                sb.append(Character.toUpperCase(parola.charAt(0)))
                  .append(parola.substring(1))
                  .append(" ");
            }

            return sb.toString().trim();
        }
    }

    // Procedura per rinnovare la registrazione di uno studente
    public void rinnovoRegistrazione() {
        System.out.println("\n=== RINNOVA REGISTRAZIONE STUDENTE ===");

        try {
            List<Studente> tuttiStudenti = getAllStudenti();
            System.out.println("\nElenco completo degli studenti:");
            for (Studente s : tuttiStudenti) {
                System.out.printf("ID: %d | Nome: %s | Cognome: %s | Username: %s%n",
                        s.getId(), s.getNome(), s.getCognome(), s.getUsername());
            }

            String nome = richiediNomeValido("nome dello studente");
            String cognome = richiediNomeValido("cognome dello studente");

            List<Studente> trovati = ricercaStudente(nome, cognome);
            if (trovati.isEmpty()) {
                System.out.println("Nessuno studente trovato.");
                return;
            }

            System.out.println("\nStudenti trovati:");
            for (Studente s : trovati) {
                System.out.printf("ID: %d | Nome: %s | Cognome: %s | Username: %s%n",
                        s.getId(), s.getNome(), s.getCognome(), s.getUsername());
            }

            System.out.print("Inserisci l'ID dello studente da selezionare: ");
            int id = Integer.parseInt(scanner.nextLine());

            Optional<Studente> selezionato = trovati.stream().filter(s -> s.getId() == id).findFirst();
            if (selezionato.isEmpty()) {
                System.out.println("ID non valido.");
                return;
            }

            Pagamento pagamento = visualizzaBonificoRicevuto(id);

            SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat meseFormatter = new SimpleDateFormat("MMMM yyyy", Locale.ITALIAN);

            System.out.println("\nPagamento trovato:");
            System.out.printf("ID Pagamento: %d%n", pagamento.getId());
            System.out.printf("Importo: €%.2f%n", pagamento.getImporto());
            System.out.printf("Data pagamento: %s%n", dataFormatter.format(pagamento.getDataPagamento()));
            System.out.printf("Mese di riferimento: %s%n", meseFormatter.format(pagamento.getMeseRiferimento()));

            System.out.print("Confermare rinnovo? (s/n): ");
            String conferma = scanner.nextLine().trim().toLowerCase();
            if (conferma.equals("s") || conferma.equals("si")) {
                String esito = rinnovaRegistrazione(nome, cognome, id);
                System.out.println(esito);
            } else {
                System.out.println("Operazione annullata.");
            }

        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ID non valido.");
        } catch (Exception e) {
            System.out.println("Errore imprevisto: " + e.getMessage());
        }
    }

    public List<Studente> ricercaStudente(String nome, String cognome) throws SQLException {
        List<Studente> studenti = studenteDAO.readStudenti(nome, cognome);
        if (studenti.isEmpty()) {
            throw new SQLException("Studente non trovato");
        }
        return studenti;
    }

    public Pagamento visualizzaBonificoRicevuto(int studenteId) throws SQLException {
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.JUNE, 1);
        Date meseRiferimento = cal.getTime();

        Pagamento pagamento = pagamentoDAO.readPagamento(studenteId, meseRiferimento);
        if (pagamento == null) {
            throw new SQLException("Bonifico non trovato, registrazione non rinnovabile");
        }

        Calendar pagamentoCal = Calendar.getInstance();
        pagamentoCal.setTime(pagamento.getDataPagamento());

        if (pagamentoCal.get(Calendar.DAY_OF_MONTH) > 28) {
            throw new SQLException("Pagamento ricevuto oltre il termine, registrazione non rinnovabile");
        }

        return pagamento;
    }

    public String rinnovaRegistrazione(String nome, String cognome, int studenteId) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(2025, Calendar.JUNE, 1);
            Date meseRiferimento = cal.getTime();

            Pagamento pagamento = pagamentoDAO.readPagamento(studenteId, meseRiferimento);

            if (pagamento == null) {
                return "Pagamento non trovato, impossibile procedere.";
            }

            Calendar pagamentoCal = Calendar.getInstance();
            pagamentoCal.setTime(pagamento.getDataPagamento());

            if (pagamentoCal.get(Calendar.DAY_OF_MONTH) > 28) {
                return "Pagamento ricevuto oltre il 28 del mese, registrazione non rinnovabile.";
            }

            boolean esito = setRegistrazione(studenteId, pagamento.getId());
            return esito ? "Registrazione rinnovata con successo." : "Errore nel rinnovo.";
        } catch (Exception e) {
            return "Errore durante il rinnovo: " + e.getMessage();
        }
    }

    public boolean setRegistrazione(int studenteId, int pagamentoId) throws SQLException {
        boolean success = registrazioneDAO.updateRegistrazione(studenteId, pagamentoId);
        if (!success) {
            throw new SQLException("Errore durante l’aggiornamento della registrazione");
        }
        return true;
    }

    public List<Lezione> richiediLezioniDisponibili() {
        try {
            return dao.getLezioniDisponibili();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Lezione> filtraLezioni(String livello, double prezzo, String strumento) {
        return dao.getLezioniFiltrate(livello, prezzo, strumento);
    }

    public void prenotaLezione(int idLezione, String livello, double prezzo, String strumento) throws PrenotazioneException {
        if (dao.controllaDisponibilita(idLezione)) {
            dao.prenotaLezione(idLezione, livello, prezzo, strumento);
        } else {
            throw new PrenotazioneException("Lezione non disponibile.");
        }
    }

    public List<Studente> getAllStudenti() throws SQLException {
        return studenteDAO.readAllStudenti();
    }

    // Utility per verifica validità giorno/ora prenotazione
    public static class GestioneDisponibilita {
        public static boolean verificaDataOra(String data, String ora, List<String> festivi) {
            try {
                LocalDate giorno = LocalDate.parse(data);
                DayOfWeek giornoSettimana = giorno.getDayOfWeek();
                if (giornoSettimana == DayOfWeek.SATURDAY || giornoSettimana == DayOfWeek.SUNDAY) {
                    return false;
                }
                if (festivi.contains(data)) {
                    return false;
                }
                LocalTime orario = LocalTime.parse(ora);
                return !orario.isBefore(LocalTime.of(17, 0)) && !orario.isAfter(LocalTime.of(21, 0));
            } catch (Exception e) {
                return false;
            }
        }
    }

    // Autenticazione segreteria (statica semplice)
    public static class AuthenticationService {
        public boolean loginSegreteria(String username, String password) {
            return "admin".equals(username) && "admin123".equals(password);
        }
    }
    


	public Registrazione getRegistrazioneStudente(int studenteId) throws SQLException {
        return registrazioneDAO.readRegistrazione(studenteId);
    }

    public void visualizzaStudentiConRegistrazioni() {
        List<Studente> studenti;
    		try {
        		studenti = studenteDAO.readAllStudenti();
    		} catch (SQLException e) {
        		System.err.println("Errore nel recupero degli studenti: " + e.getMessage());
        	return;
    		}

        for (Studente studente : studenti) {
            try {
                Registrazione reg = getRegistrazioneStudente(studente.getId());

                String stato = (reg != null && reg.isValidita()) ? "valida" : "non valida";

                System.out.println(
                    "ID: " + studente.getId() +
                    " | Username: " + studente.getUsername() +
                    " | Nome: " + studente.getNome() +
                    " | Cognome: " + studente.getCognome() +
                    " | Email: " + studente.getEmail() +
                    " | Registrazione: " + stato
                );
            } catch (SQLException e) {
                System.err.println("Errore nel recupero della registrazione per lo studente con ID " +
                                   studente.getId() + ": " + e.getMessage());
            }
        }
    }

    public void setStudenteDAO(StudenteDAO studenteDAO2) {
        this.studenteDAO = studenteDAO2;
    }

    public void setPagamentoDAO(PagamentoDAO pagamentoDAO2) {
        this.pagamentoDAO = pagamentoDAO2;
    }

    public void setRegistrazioneDAO(RegistrazioneDAO registrazioneDAO2) {
        this.registrazioneDAO = registrazioneDAO2;
    }

}


