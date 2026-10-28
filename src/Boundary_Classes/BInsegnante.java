package Boundary_Classes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import Control_Class.GestioneScuolaMusica.GestioneDisponibilita;
import DAO_Classes.DisponibilitaDAO;
import DAO_Classes.FestiviDAO;
import DAO_Classes.InsegnanteDAO;
import Entity_Classes.Disponibilita;
import Entity_Classes.Insegnante;

public class BInsegnante {

    private static final int MAX_TENTATIVI = 6;

    public void avviaInterfaccia() {
        Scanner input = new Scanner(System.in);
        DateTimeFormatter visualFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Insegnante docente = null;

        while (docente == null) {
            System.out.print("Username: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();

            try {
                docente = InsegnanteDAO.login(username, password);
                System.out.println("Benvenuto, " + docente.getNome() + " " + docente.getCognome());
            } catch (Exception e) {
                System.out.println("Errore login: " + e.getMessage());
            }
        }

        boolean continua = true;
        while (continua) {
            try {
                System.out.println("\n             Menu:");
                System.out.println("--------------------------------");
                System.out.println("1. Modifica disponibilità");
                System.out.println("2. Inserisci disponibilità");
                System.out.println("3. Visualizza lezioni inserite");
                System.out.println("4. Logout");
                System.out.println("--------------------------------");
                System.out.print("Scelta: ");
                String scelta = input.nextLine().trim();

                switch (scelta) {
                    case "1" -> {
                        List<Disponibilita> lista = DisponibilitaDAO.getDisponibilitaByInsegnante(docente.getId());
                        if (lista.isEmpty()) {
                            System.out.println("Nessuna disponibilità trovata.");
                            break;
                        }

                        for (Disponibilita d : lista) {
                            LocalDate data = LocalDate.parse(d.getData());
                            System.out.println("ID: " + d.getId() + " | Data: " + data.format(visualFormatter) +
                                    " | Ora: " + d.getOra().substring(0, 5) +
                                    " | Strumento: " + docente.getStrumento());
                        }

                        System.out.print("Inserisci ID disponibilità da modificare: ");
                        int idModifica;
                        try {
                            idModifica = Integer.parseInt(input.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ID non valido.");
                            break;
                        }

                        Disponibilita selezionata = lista.stream()
                                .filter(d -> d.getId() == idModifica)
                                .findFirst()
                                .orElse(null);

                        if (selezionata == null) {
                            System.out.println("Disponibilità non trovata.");
                            break;
                        }

                        System.out.println("Cosa vuoi modificare?");
                        System.out.println("1. Data");
                        System.out.println("2. Ora");
                        System.out.print("Scelta: ");
                        String tipo = input.nextLine().trim();

                        if (tipo.equals("1")) {
                            int tentativi = 0;
                            boolean successo = false;
                            while (tentativi < MAX_TENTATIVI) {
                                System.out.print("Inserisci nuova data (GG-MM-AAAA o GG/MM/AAAA): ");
                                String nuovaData = input.nextLine().replace("/", "-").trim();
                                try {
                                    String[] parti = nuovaData.split("-");
                                    if (parti.length != 3) throw new IllegalArgumentException();
                                    nuovaData = parti[2] + "-" + parti[1] + "-" + parti[0];
                                    LocalDate data = LocalDate.parse(nuovaData);
                                    if (data.isBefore(LocalDate.now()) || data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY ||
                                            !GestioneDisponibilita.verificaDataOra(nuovaData, selezionata.getOra(), FestiviDAO.getFestivi(nuovaData))) {
                                        throw new IllegalArgumentException("Data non valida o non disponibile.");
                                    }
                                    final String nuovaDataFinale = nuovaData;
                                    boolean conflitto = DisponibilitaDAO.getDisponibilitaByInsegnante(docente.getId()).stream()
                                            .anyMatch(d -> d.getId() != selezionata.getId() && d.getData().equals(nuovaDataFinale) &&
                                                    !LocalTime.parse(selezionata.getOra()).isBefore(LocalTime.parse(d.getOra())) &&
                                                    LocalTime.parse(selezionata.getOra()).isBefore(LocalTime.parse(d.getOra()).plusHours(1)));
                                    if (conflitto) throw new IllegalArgumentException("Conflitto con un'altra lezione.");
                                    selezionata.setData(nuovaData);
                                    successo = true;
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Errore: " + e.getMessage());
                                    tentativi++;
                                }
                            }
                            if (!successo) {
                                System.out.println("Numero massimo di tentativi superato. Operazione annullata.");
                                break;
                            }

                        } else if (tipo.equals("2")) {
                            int tentativi = 0;
                            boolean successo = false;
                            while (tentativi < MAX_TENTATIVI) {
                                System.out.print("Inserisci nuova ora (HH:MM): ");
                                String nuovaOra = input.nextLine().trim();
                                try {
                                    LocalTime orario = LocalTime.parse(nuovaOra);
                                    if (orario.isBefore(LocalTime.of(17, 0)) || orario.isAfter(LocalTime.of(20, 0)) ||
                                            !GestioneDisponibilita.verificaDataOra(selezionata.getData(), nuovaOra, FestiviDAO.getFestivi(selezionata.getData()))) {
                                        throw new IllegalArgumentException("Ora non valida o non disponibile.");
                                    }
                                    final String nuovaOraFinale = nuovaOra;
                                    boolean conflitto = DisponibilitaDAO.getDisponibilitaByInsegnante(docente.getId()).stream()
                                            .anyMatch(d -> d.getId() != selezionata.getId() && d.getData().equals(selezionata.getData()) &&
                                                    !LocalTime.parse(nuovaOraFinale).isBefore(LocalTime.parse(d.getOra())) &&
                                                    LocalTime.parse(nuovaOraFinale).isBefore(LocalTime.parse(d.getOra()).plusHours(1)));
                                    if (conflitto) throw new IllegalArgumentException("Conflitto con un'altra lezione.");
                                    selezionata.setOra(nuovaOra);
                                    successo = true;
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Errore: " + e.getMessage());
                                    tentativi++;
                                }
                            }
                            if (!successo) {
                                System.out.println("Numero massimo di tentativi superato. Operazione annullata.");
                                break;
                            }
                        } else {
                            System.out.println("Scelta non valida.");
                            break;
                        }

                        System.out.println("\nConfermi le modifiche?");
                        System.out.println("Nuova disponibilità → ID: " + selezionata.getId() +
                                " | Strumento: " + docente.getStrumento() +
                                " | Data: " + LocalDate.parse(selezionata.getData()).format(visualFormatter) +
                                " | Ora: " + selezionata.getOra().substring(0, 5));
                        System.out.print("Confermare? (s/n): ");
                        if (input.nextLine().trim().equalsIgnoreCase("s")) {
                            DisponibilitaDAO.aggiornaDisponibilita(selezionata);
                            System.out.println("Modifica confermata.");
                        } else {
                            System.out.println("Modifica annullata.");
                        }
                    }
                    case "2" -> {
                        String nuovaData = null;
                        String nuovaOra = null;
                        String strumentoLezione = docente.getStrumento();
                        String livello = null;
                        double prezzo = 0.0;

                        while (true) {
                            System.out.print("Inserisci il livello (principiante/intermedio): ");
                            livello = input.nextLine().trim().toLowerCase();
                            if (livello.equals("principiante")) {
                                prezzo = 25.0;
                                break;
                            } else if (livello.equals("intermedio")) {
                                prezzo = 20.0;
                                break;
                            } else {
                                System.out.println("Livello non valido. Riprova.");
                            }
                        }

                        boolean successData = false;
                        for (int i = 0; i < 10; i++) {
                            System.out.print("Inserisci nuova data (GG-MM-AAAA o GG/MM/AAAA): ");
                            String inputData = input.nextLine().replace("/", "-").trim();
                            try {
                                String[] parti = inputData.split("-");
                                if (parti.length != 3) throw new IllegalArgumentException();
                                String dataFormattata = parti[2] + "-" + parti[1] + "-" + parti[0];
                                LocalDate data = LocalDate.parse(dataFormattata);
                                if (data.isBefore(LocalDate.now()) ||
                                        data.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                        data.getDayOfWeek() == DayOfWeek.SUNDAY ||
                                        FestiviDAO.getFestivi(dataFormattata).contains(dataFormattata)) {
                                    System.out.println("Data non valida. Riprova.");
                                    continue;
                                }
                                nuovaData = dataFormattata;
                                successData = true;
                                break;
                            } catch (Exception e) {
                                System.out.println("Formato data non valido. Riprova.");
                            }
                        }
                        if (!successData) break;

                        boolean successOra = false;
                        for (int i = 0; i < 10; i++) {
                            System.out.print("Inserisci ora (HH:MM): ");
                            String inputOra = input.nextLine().trim();
                            try {
                                LocalTime orario = LocalTime.parse(inputOra);
                                if (orario.isBefore(LocalTime.of(17, 0)) || orario.isAfter(LocalTime.of(20, 0)) ||
                                        !GestioneDisponibilita.verificaDataOra(nuovaData, inputOra, FestiviDAO.getFestivi(nuovaData))) {
                                    System.out.println("Orario non valido. Riprova.");
                                    continue;
                                }
                                final String dataFinaleConflitto = nuovaData;
                                final String oraFinaleConflitto = inputOra;
                                List<Disponibilita> esistenti = DisponibilitaDAO.getDisponibilitaByInsegnante(docente.getId());
                                boolean conflitto = esistenti.stream().anyMatch(d -> {
                                    if (!d.getData().equals(dataFinaleConflitto)) return false;
                                    LocalTime oraEsistente = LocalTime.parse(d.getOra());
                                    LocalTime oraNuova = LocalTime.parse(oraFinaleConflitto);
                                    return !oraNuova.isBefore(oraEsistente) && oraNuova.isBefore(oraEsistente.plusHours(1));
                                });
                                if (conflitto) {
                                    System.out.println("Hai già una disponibilità in quell'orario. Riprova.");
                                    continue;
                                }
                                nuovaOra = inputOra;
                                successOra = true;
                                break;
                            } catch (Exception e) {
                                System.out.println("Formato ora non valido. Riprova.");
                            }
                        }
                        if (!successOra) break;

                        int nuovoId = DisponibilitaDAO.getProssimoIdDisponibilita();
                        Disponibilita nuova = new Disponibilita(
                                nuovoId,
                                docente.getId(),
                                nuovaData,
                                nuovaOra,
                                strumentoLezione,
                                livello,
                                prezzo,
                                "disponibile",
                                docente.getNome()
                        );

                        System.out.println("\nConfermi l'inserimento?");
                        System.out.println("Strumento: " + strumentoLezione);
                        System.out.println("Nuova disponibilità → ID: " + nuova.getId() +
                                " | Data: " + LocalDate.parse(nuovaData).format(visualFormatter) +
                                " | Ora: " + nuovaOra +
                                " | Livello: " + livello +
                                " | Prezzo: €" + prezzo);
                        System.out.print("Confermare? (s/n): ");
                        if (input.nextLine().trim().equalsIgnoreCase("s")) {
                            try {
                                DisponibilitaDAO.aggiungiDisponibilita(nuova);
                                System.out.println("Inserimento confermato.");
                            } catch (Exception e) {
                                System.out.println("Errore durante l'inserimento: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Inserimento annullato.");
                        }
                    }

                    case "3" -> {
                        List<Disponibilita> lista = DisponibilitaDAO.getDisponibilitaByInsegnante(docente.getId());
                        if (lista.isEmpty()) {
                            System.out.println("Nessuna lezione registrata.");
                        } else {
                            System.out.println("\nLezioni registrate:");
                            for (Disponibilita d : lista) {
                                LocalDate data = LocalDate.parse(d.getData());
                                System.out.println("ID: " + d.getId() +
                                        " | Strumento: " + d.getStrumento() +
                                        " | Data: " + data.format(visualFormatter) +
                                        " | Ora: " + d.getOra().substring(0, 5) +
                                        " | Livello: " + d.getLivello() +
                                        " | Prezzo: €" + d.getPrezzo() +
                                        " | Stato: " + d.getStato());
                            }
                        }
                    }

                    case "4" -> {
                        System.out.println("Logout effettuato. Ritorno al menu principale.");
                        continua = false;
                        return;
                    }

                    default -> System.out.println("Scelta non valida. Riprova.");
                }

            } catch (Exception e) {
                System.out.println("Errore imprevisto: " + e.getMessage());
            }
        }

        input.close();
    }
                

           
        
   }
    
   
