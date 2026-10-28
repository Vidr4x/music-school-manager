package Entity_Classes;
import java.util.Date;
// Studente.java



public class Studente {
    private int id;
    private String nome;
    private String cognome;
    private Date dataNascita;
    private String telefono;
    private String email;
    private String username;
    private String password;
    
    // Costruttori
    public Studente() {}
    
    public Studente(int id, String nome, String cognome, Date dataNascita, 
                   String telefono, String email, String username, String password) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.email = email;
        this.username = username;
        this.password = password;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    
    public Date getDataNascita() { return dataNascita; }
    public void setDataNascita(Date dataNascita) { this.dataNascita = dataNascita; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    
    @Override
    public String toString() {
        return "Studente{id=" + id + ", nome='" + nome + "', cognome='" + cognome + 
               "', username='" + username + "'}";
    }
}