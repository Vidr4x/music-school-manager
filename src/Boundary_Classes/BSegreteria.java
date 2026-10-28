package Boundary_Classes;

import Control_Class.GestioneScuolaMusica;
import java.util.Scanner;

public class BSegreteria {
    private final GestioneScuolaMusica controller;
    private final Scanner scanner;

    public BSegreteria() {
        this.controller = new GestioneScuolaMusica();
        this.scanner = new Scanner(System.in);
    }

    public void menuSegreteria() {
        boolean continua = true;

        while (continua) {
            System.out.println("\n=== MENU SEGRETERIA ===");
            System.out.println("1. Rinnova Registrazione Studente");
            System.out.println("2. Visualizza Elenco Studenti");
            System.out.println("3. Logout");
            System.out.print("Seleziona opzione: ");

            try {
                int scelta = Integer.parseInt(scanner.nextLine());

                switch (scelta) {
                    case 1 -> controller.rinnovoRegistrazione();

                    case 2 -> controller.visualizzaStudentiConRegistrazioni();

                    case 3 -> {
                        System.out.println("Logout effettuato con successo.");
                        continua = false;
                    }

                    default -> System.out.println("Opzione non valida. Riprova.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Inserire un numero valido.");
            } catch (Exception e) {
                System.out.println("Errore imprevisto: " + e.getMessage());
            }
        }
    }
}
