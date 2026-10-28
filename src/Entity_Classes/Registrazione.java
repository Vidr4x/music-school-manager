
// Registrazione.java
package Entity_Classes;

public class Registrazione {
    private int studenteId;
    private boolean validita;
    
    // Costruttori
    public Registrazione() {}
    
    public Registrazione(int studenteId, boolean validita) {
        this.studenteId = studenteId;
        this.validita = validita;
    }
    
    // Getters e Setters
    public int getStudenteId() { return studenteId; }
    public void setStudenteId(int studenteId) { this.studenteId = studenteId; }
    
    public boolean isValidita() { return validita; }
    public void setValidita(boolean validita) { this.validita = validita; }

   
}