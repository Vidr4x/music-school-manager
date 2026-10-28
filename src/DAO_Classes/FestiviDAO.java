package DAO_Classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FestiviDAO {

    public static List<String> getFestivi(String data) {
        LocalDate localDate = LocalDate.parse(data);
        int anno = localDate.getYear();
        return getFestiviPerAnno(anno);
    }

    public static List<String> getFestiviPerAnno(int anno) {
        List<String> festivi = new ArrayList<>();

        // Festività fisse
        festivi.add(anno + "-01-01"); // Capodanno
        festivi.add(anno + "-01-06"); // Epifania
        festivi.add(anno + "-04-25"); // Liberazione
        festivi.add(anno + "-05-01"); // Lavoro
        festivi.add(anno + "-06-02"); // Repubblica
        festivi.add(anno + "-08-15"); // Ferragosto
        festivi.add(anno + "-11-01"); // Tutti i Santi
        festivi.add(anno + "-12-08"); // Immacolata
        festivi.add(anno + "-12-25"); // Natale
        festivi.add(anno + "-12-26"); // Santo Stefano

        // Festività mobili: Pasqua e Pasquetta
        LocalDate pasqua = calcolaPasqua(anno);
        festivi.add(pasqua.toString()); // Pasqua
        festivi.add(pasqua.plusDays(1).toString()); // Lunedì dell'Angelo

        return festivi;
    }

    // Algoritmo di Butcher per calcolare la data di Pasqua (valido per calendario gregoriano)
    private static LocalDate calcolaPasqua(int anno) {
        int a = anno % 19;
        int b = anno / 100;
        int c = anno % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int mese = (h + l - 7 * m + 114) / 31;
        int giorno = ((h + l - 7 * m + 114) % 31) + 1;

        return LocalDate.of(anno, mese, giorno);
    }
}
