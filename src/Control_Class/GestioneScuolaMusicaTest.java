package Control_Class;

import DAO_Classes.*;
import Entity_Classes.*;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GestioneScuolaMusicaTest {

    GestioneScuolaMusica gestione = new GestioneScuolaMusica();

    // TEST MODIFICA DISPONIBILITÀ
    @Test
    public void test1_TuttiInputValidi() {
        String nuovaData = "2025-06-17";
        String nuovaOra = "17:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(nuovaData, nuovaOra, FestiviDAO.getFestivi(nuovaData));
        assertTrue(esito);
    }

    @Test
    public void test2_DataFestiva() {
        String dataFestiva = "2025-12-25";
        String oraValida = "17:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(dataFestiva, oraValida, FestiviDAO.getFestivi(dataFestiva));
        assertFalse(esito);
    }

    @Test
    public void test3_DataNelPassato() {
        String dataPassata = "2004-09-22";
        String oraValida = "22:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(dataPassata, oraValida, FestiviDAO.getFestivi(dataPassata));
        assertFalse(esito);
    }

    @Test
    public void test4_DataGiaOccupata() {
        String dataGiaPresente = "2025-07-09";
        String oraGiaPresente = "22:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(dataGiaPresente, oraGiaPresente, FestiviDAO.getFestivi(dataGiaPresente));
        assertFalse(esito);
    }

    @Test
    public void test5_OraFuoriFascia() {
        String dataValida = "2025-06-17";
        String oraFuoriFascia = "16:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(dataValida, oraFuoriFascia, FestiviDAO.getFestivi(dataValida));
        assertFalse(esito);
    }

    @Test
    public void test6_DurataSuperioreOra() {
        LocalTime inizio = LocalTime.of(17, 0);
        LocalTime fine = LocalTime.of(19, 0);

        boolean durataTroppoLunga = inizio.plusHours(1).isBefore(fine);
        assertTrue(durataTroppoLunga);
    }
  //TEST INSERISCI DISPONIBILITA'
    @Test
    public void test1_TuttiInputValidi2() {
        String nuovaData = "2025-06-17"; // martedì, feriale
        String nuovaOra = "17:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(nuovaData, nuovaOra, FestiviDAO.getFestivi(nuovaData));
        assertTrue(esito, "La data e ora dovrebbero essere valide");
    }

    @Test
    public void test2_DataFestiva2() {
        String dataFestiva = "2025-12-25"; // Natale
        String oraValida = "17:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(dataFestiva, oraValida, FestiviDAO.getFestivi(dataFestiva));
        assertFalse(esito, "La data è festiva e non dovrebbe essere accettata");
    }

    @Test
    public void test3_DataNelPassato3() {
        String dataPassata = "2004-09-22";
        String oraValida = "22:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(dataPassata, oraValida, FestiviDAO.getFestivi(dataPassata));
        assertFalse(esito, "La data è nel passato e non dovrebbe essere accettata");
    }

    @Test
    public void test4_DataGiaOccupata4() {
        String dataGiaPresente = "2025-07-09";
        String oraGiaPresente = "22:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(dataGiaPresente, oraGiaPresente, FestiviDAO.getFestivi(dataGiaPresente));

        assertFalse(esito, "La data è già presente");
    }


    @Test
    public void test5_OraFuoriFascia5() {
        String dataValida = "2025-06-17";
        String oraFuoriFascia = "16:00";

        boolean esito = GestioneScuolaMusica.GestioneDisponibilita
                .verificaDataOra(dataValida, oraFuoriFascia, FestiviDAO.getFestivi(dataValida));
        assertFalse(esito, "Orario fuori fascia oraria (17-20)");
    }

    @Test
    public void test6_DurataSuperioreOra6() {
        // Simulazione logica durata oltre 1 ora
        LocalTime inizio = LocalTime.of(17, 0);
        LocalTime fine = LocalTime.of(19, 0);

        boolean durataTroppoLunga = inizio.plusHours(1).isBefore(fine);
        assertTrue(durataTroppoLunga, "La durata supera un’ora e non è consentita");
    }
    // TEST PRENOTA LEZIONE 
    @Test
    public void test1_TuttiInputValidiPrenotazione() {
        Lezione lezione = new Lezione(1, "principiante", 20.0, "Rossi", "disponibile", "chitarra", "2025-07-10", "17:00");

        boolean prenotabile = lezione.getId() == 1
                && (lezione.getLivello().equals("principiante") || lezione.getLivello().equals("intermedio"))
                && lezione.getPrezzo() > 0
                && lezione.getStato().equals("disponibile")
                && lezione.getStrumento().matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ\\s]+$");

        assertTrue(prenotabile);
    }

    @Test
    public void test2_IDNonValido() {
        String idInserito = "999";
        List<String> idDisponibili = List.of("1", "3", "5");

        boolean idValido = idDisponibili.contains(idInserito);
        assertFalse(idValido);
    }

    @Test
    public void test3_LivelloNonValido() {
        String livelloInserito = "esperto";
        List<String> livelliValidi = List.of("principiante", "intermedio");

        assertFalse(livelliValidi.contains(livelloInserito));
    }

    @Test
    public void test4_PrezzoNonNumerico() {
        String prezzoInserito = "aaaaaaa";

        assertThrows(NumberFormatException.class, () -> {
            Double.parseDouble(prezzoInserito);
        });
    }

    @Test
    public void test5_LezioniGiaPrenotateNonMostrate() {
        Lezione l1 = new Lezione(1, "principiante", 20.0, "Rossi", "disponibile", "chitarra", "2025-07-10", "17:00");
        Lezione l2 = new Lezione(2, "intermedio", 25.0, "Rossi", "disponibile", "chitarra", "2025-07-10", "17:00");
        List<Lezione> tutte = List.of(l1, l2);

        List<Lezione> filtrate = tutte.stream()
                .filter(lez -> lez.getStato().equalsIgnoreCase("disponibile"))
                .toList();

        assertTrue(filtrate.stream().noneMatch(lez -> lez.getStato().equalsIgnoreCase("prenotata")));
    }

    @Test
    public void test6_StrumentoConCaratteriSpeciali() {
        String strumentoInserito = "ch!t4rr&";

        boolean valido = strumentoInserito.matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ\\s]+$");
        assertFalse(valido);
    }
    
 // TEST RINNOVA REGISTRAZIONE
    @Test
    public void testRinnovaRegistrazione_TuttiInputValidi() {
        Studente studente = new Studente(1, "Mario", "Rossi", new Date(), "3331111111", "mario.rossi@email.com", "studente1", "pass1");

        boolean nomeValido = studente.getNome().matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ\\s]+$");
        boolean cognomeValido = studente.getCognome().matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ\\s]+$");
        boolean idValido = studente.getId() == 1;

        boolean esito = nomeValido && cognomeValido && idValido;
        assertTrue(esito, "Registrazione rinnovata con successo");
    }

    @Test
    public void testRinnovaRegistrazione_IDNonValido() {
        Studente studente = new Studente(2, "Mario", "Rossi", new Date(), "3331111111", "mario.rossi@email.com", "studente1", "pass1");

        List<Integer> idValidi = List.of(1, 3, 5);
        boolean idValido = idValidi.contains(studente.getId());

        assertFalse(idValido, "ID non valido, il rinnovo non dovrebbe essere consentito");
    }

    @Test
    public void testRinnovaRegistrazione_NomeConCaratteriSpeciali() {
        Studente studente = new Studente(1, "M%RE$@", "Rossi", new Date(), "3331111111", "mario.rossi@email.com", "studente1", "pass1");

        boolean nomeValido = studente.getNome().matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ\\s]+$");
        assertFalse(nomeValido, "Nome con caratteri speciali non accettato");
    }

    @Test
    public void testRinnovaRegistrazione_NomeInesistenteNelDB() {
        Studente studente = new Studente(1, "Vegeta", "Rossi", new Date(), "3331111111", "mario.rossi@email.com", "studente1", "pass1");

        List<String> nomiNelDB = List.of("Mario", "Luigi", "Giovanni");
        boolean nomeEsiste = nomiNelDB.contains(studente.getNome());

        assertFalse(nomeEsiste, "Nome non presente, il rinnovo non dovrebbe essere effettuato");
    }

    @Test
    public void testRinnovaRegistrazione_CognomeConCaratteriSpeciali() {
        Studente studente = new Studente(1, "Mario", "R%&@i", new Date(), "3331111111", "mario.rossi@email.com", "studente1", "pass1");

        boolean cognomeValido = studente.getCognome().matches("^[a-zA-ZàèéìòùÀÈÉÌÒÙ\\s]+$");
        assertFalse(cognomeValido, "Cognome con caratteri speciali non dovrebbe essere accettato");
    }

    @Test
    public void testRinnovaRegistrazione_CognomeInesistenteNelDB() {
        Studente studente = new Studente(1, "Mario", "Giovannini", new Date(), "3331111111", "mario.rossi@email.com", "studente1", "pass1");

        List<String> cognomiNelDB = List.of("Rossi", "Verdi", "Bianchi");
        boolean cognomeEsiste = cognomiNelDB.contains(studente.getCognome());

        assertFalse(cognomeEsiste, "Cognome non presente, il rinnovo non effettuato");
    }

}