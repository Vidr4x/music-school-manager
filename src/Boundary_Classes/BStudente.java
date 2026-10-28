package Boundary_Classes;

import Control_Class.GestioneScuolaMusica;
import DAO_Classes.StudenteDAO;
import Entity_Classes.Lezione;
import Entity_Classes.Studente;
import exception.LoginException;
import exception.PrenotazioneException;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BStudente {

    private final GestioneScuolaMusica sistema;
    private final Scanner scanner;

    public BStudente() {
        sistema = new GestioneScuolaMusica();
        scanner = new Scanner(System.in);
    }

    public void avviaPrenotazione() {

        Studente studente = null;
        System.out.println("==========================================");
        System.out.println("           LOGIN STUDENTE");
        System.out.println("==========================================");
        while (studente == null) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            try {
                studente = StudenteDAO.login(username, password);
                System.out.println("Login effettuato con successo! Benvenuto, " + studente.getUsername());
            } catch (LoginException e) {
                System.out.println("Errore login: " + e.getMessage());
                System.out.println("Riprova.\n");
            }
        }

        System.out.println("\nBenvenuto/a nella sezione prenotazione lezioni.");
        boolean continua = true;
        while (continua) {
            List<Lezione> lezioni = sistema.richiediLezioniDisponibili();
            if (lezioni.isEmpty()) {
                System.out.println("Nessuna lezione disponibile al momento.");
                break;
            }

            mostraLezioni(lezioni);

            System.out.println("\nInserisci il livello, il prezzo massimo e lo strumento desiderato:");

            // Validazione del livello
            String livello = "";
            while (true) {
                System.out.print("Livello (principiante/intermedio): ");
                livello = scanner.nextLine().trim().toLowerCase();
                if (livello.equals("principiante") || livello.equals("intermedio")) {
                    break;
                } else {
                    System.out.println("Livello non valido. Inserisci: principiante o intermedio.");
                }
            }

            // Validazione del prezzo
            double prezzo = -1;
            while (prezzo < 0) {
                System.out.print("Prezzo massimo: ");
                try {
                    prezzo = Double.parseDouble(scanner.nextLine().trim());
                    if (prezzo < 0) {
                        System.out.println("Il prezzo non può essere negativo. Riprova.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Inserisci un numero valido per il prezzo.");
                }
            }

            // Validazione dello strumento
            String strumento = "";
            while (true) {
                List<String> strumentiDisponibili = lezioni.stream()
                        .map(Lezione::getStrumento)
                        .distinct()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());

                System.out.print("Strumento (disponibili: " + String.join(", ", strumentiDisponibili) + "): ");
                strumento = scanner.nextLine().trim().toLowerCase();

                if (strumentiDisponibili.contains(strumento)) {
                    break;
                } else {
                    System.out.println("Strumento non valido. Scegli tra quelli disponibili.");
                }
            }

            // Filtra lezioni
            List<Lezione> filtrate = sistema.filtraLezioni(livello, prezzo, strumento);
            if (filtrate.isEmpty()) {
                System.out.println("Nessuna lezione trovata con i criteri selezionati.");
            } else {
                System.out.println("\nLezioni disponibili con i criteri inseriti:");
                mostraLezioni(filtrate);

                // Inserimento ID
                int id = -1;
                boolean idValido = false;
                while (!idValido) {
                    System.out.print("\nInserisci l'ID della lezione da prenotare: ");
                    try {
                        id = Integer.parseInt(scanner.nextLine().trim());
                        final int idFinal = id;
                        idValido = filtrate.stream().anyMatch(l -> l.getId() == idFinal);

                        if (!idValido) {
                            System.out.println("ID non valido. Riprova.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Inserisci un numero valido per l'ID.");
                    }
                }

                try {
                    sistema.prenotaLezione(id, livello, prezzo, strumento);
                    System.out.println("Prenotazione effettuata con successo.");
                } catch (PrenotazioneException e) {
                    System.out.println("Errore nella prenotazione: " + e.getMessage());
                }
            }

            // Altra prenotazione?
            String risposta;
            do {
                System.out.print("\nVuoi prenotare un'altra lezione? (s/n): ");
                risposta = scanner.nextLine().trim().toLowerCase();
                if (!risposta.equals("s") && !risposta.equals("n")) {
                    System.out.println("Risposta non valida. Inserisci 's' per sì o 'n' per no.");
                }
            } while (!risposta.equals("s") && !risposta.equals("n"));

            if (!risposta.equals("s")) {
                continua = false;
            }
        }

        System.out.println("Grazie per aver usato il sistema di prenotazione. Arrivederci!");
    }

    private void mostraLezioni(List<Lezione> lezioni) {
        System.out.println("\n== LEZIONI DISPONIBILI ==");
        for (Lezione l : lezioni) {
            System.out.println("ID: " + l.getId() +
                    " | Data: " + l.getData() +
                    " | Ora: " + l.getOra() +
                    " | Livello: " + l.getLivello() +
                    " | Prezzo: €" + l.getPrezzo() +
                    " | Insegnante: " + l.getInsegnante() +
                    " | Strumento: " + l.getStrumento());
        }
    }

}
