package Entity_Classes;

public class Disponibilita {
    private int id;
    private int idInsegnante;
    private String data;
    private String ora;
    private String strumento;
    private String livello;
    private double prezzo;
    private String stato;
    private String insegnante;

    // Costruttore completo
    public Disponibilita(int id, int idInsegnante, String data, String ora, String strumento,
                         String livello, double prezzo, String stato, String insegnante) {
        this.id = id;
        this.idInsegnante = idInsegnante;
        this.data = data;
        this.ora = ora;
        this.strumento = strumento;
        this.livello = livello;
        this.prezzo = prezzo;
        this.stato = stato;
        this.insegnante = insegnante;
    }

    // Getter
    public int getId() { return id; }
    public int getIdDocente() { return idInsegnante; }
    public String getData() { return data; }
    public String getOra() { return ora; }
    public String getStrumento() { return strumento; }
    public String getLivello() { return livello; }
    public double getPrezzo() { return prezzo; }
    public String getStato() { return stato; }
    public String getInsegnante() { return insegnante; }

    // Setter
    public void setData(String data) { this.data = data; }
    public void setOra(String ora) { this.ora = ora; }
    public void setStrumento(String strumento) { this.strumento = strumento; }
    public void setLivello(String livello) { this.livello = livello; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
    public void setStato(String stato) { this.stato = stato; }
    public void setInsegnante(String insegnante) { this.insegnante = insegnante; }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Data: " + data +
                " | Ora: " + ora +
                " | Strumento: " + strumento +
                " | Livello: " + livello +
                " | Prezzo: €" + prezzo +
                " | Stato: " + stato +
                " | Insegnante: " + insegnante;
    }
}
