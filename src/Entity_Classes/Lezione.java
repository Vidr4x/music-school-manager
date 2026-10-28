package Entity_Classes;

public class Lezione {
    private int id;
    private String livello;
    private double prezzo;
    private String insegnante;
    private String stato;
    private String strumento;
    private String data;
    private String ora;

    // Costruttore aggiornato
    public Lezione(int id, String livello, double prezzo, String insegnante, String stato, String strumento, String data, String ora) {
        this.id = id;
        this.livello = livello;
        this.prezzo = prezzo;
        this.insegnante = insegnante;
        this.stato = stato;
        this.strumento = strumento;
        this.data = data;
        this.ora = ora;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getLivello() {
        return livello;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public String getInsegnante() {
        return insegnante;
    }

    public String getStato() {
        return stato;
    }

    public String getStrumento() {
        return strumento;
    }

    public String getData() {
        return data;
    }

    public String getOra() {
        return ora;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public void setInsegnante(String insegnante) {
        this.insegnante = insegnante;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public void setStrumento(String strumento) {
        this.strumento = strumento;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }
}
