package Entity_Classes;
import java.util.Date;
// Pagamento.java



public class Pagamento {
    private int id;
    private int studenteId;
    private double importo;
    private Date meseRiferimento;
    private Date dataPagamento;
    
    // Costruttori
    public Pagamento() {}
    
    public Pagamento(int id, int studenteId, double importo, 
                    Date meseRiferimento, Date dataPagamento) {
        this.id = id;
        this.studenteId = studenteId;
        this.importo = importo;
        this.meseRiferimento = meseRiferimento;
        this.dataPagamento = dataPagamento;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getStudenteId() { return studenteId; }
    public void setStudenteId(int studenteId) { this.studenteId = studenteId; }
    
    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }
    
    public Date getMeseRiferimento() { return meseRiferimento; }
    public void setMeseRiferimento(Date meseRiferimento) { this.meseRiferimento = meseRiferimento; }
    
    public Date getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(Date dataPagamento) { this.dataPagamento = dataPagamento; }

    @Override
    public String toString() {
        return "Pagamento{id=" + id + ", studenteId=" + studenteId + 
               ", importo=" + importo + ", meseRiferimento='" + meseRiferimento + "'}";
    }
	
}