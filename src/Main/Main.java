package Main;

import Boundary_Classes.BInsegnante;
import Boundary_Classes.BSegreteria;
import Boundary_Classes.BStudente;
import Control_Class.GestioneScuolaMusica.AuthenticationService;

import java.util.Scanner;

public class Main {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthenticationService authService = new AuthenticationService();

    public static void main(String[] args) {
        System.out.println("=== SISTEMA GESTIONE SCUOLA DI MUSICA ===");
        System.out.println("Benvenuto nel sistema di gestione!");

        boolean continua = true;

        while (continua) {
            continua = mostraMenuPrincipale();
        }

        System.out.println("Grazie per aver utilizzato il sistema. Arrivederci!");
        scanner.close();
    }

    private static boolean mostraMenuPrincipale() {
        System.out.println("\n==========================================");
        System.out.println("              MENU PRINCIPALE");
        System.out.println("==========================================");
        System.out.println("Seleziona il tipo di interfacciamento:");
        System.out.println("1. Segreteria");
        System.out.println("2. Studente");
        System.out.println("3. Insegnante");
        System.out.println("4. Esci dal sistema");
        System.out.println("==========================================");
        System.out.print("Inserisci la tua scelta (1-4): ");

        try {
            int scelta = Integer.parseInt(scanner.nextLine().trim());

            switch (scelta) {
                case 1:
                    return accessoSegreteria();
                case 2:
                    BStudente bStudente = new BStudente();
                    bStudente.avviaPrenotazione();
                    return true;
                case 3:
                    BInsegnante bInsegnante = new BInsegnante();
                    bInsegnante.avviaInterfaccia();
                    return true;
                case 4:
                    return false;
                default:
                    System.out.println("Scelta non valida! Seleziona un numero da 1 a 4.");
                    return true;
            }

        } catch (Exception e) {
            System.out.println("Errore nell'input! Inserisci un numero valido.");
            return true;
        }
    }

    private static boolean accessoSegreteria() {
        System.out.println("\n==========================================");
        System.out.println("              LOGIN SEGRETERIA");
        System.out.println("==========================================");

        for (int tentativi = 0; tentativi < 3; tentativi++) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (authService.loginSegreteria(username, password)) {
                System.out.println("Login effettuato con successo!");
                System.out.println("Benvenuto nel sistema Segreteria");

                BSegreteria segreteria = new BSegreteria();
                segreteria.menuSegreteria();

                return true;
            } else {
                System.out.println("Credenziali non valide!");
                if (tentativi < 2) {
                    System.out.println("Riprova (" + (2 - tentativi) + " tentativi rimasti):");
                }
            }
        }

        System.out.println("Troppi tentativi falliti. Ritorno al menu principale.");
        return true;
    }
}
