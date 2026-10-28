package Entity_Classes;

import java.time.LocalDate;

public class Insegnante {
    private int id;
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
    private String strumento;
    private LocalDate dataNascita;

    public Insegnante(int id, String username, String password, String email,
                      String nome, String cognome, String strumento, LocalDate dataNascita) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.strumento = strumento;
        this.dataNascita = dataNascita;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getStrumento() { return strumento; }
    public LocalDate getDataNascita() { return dataNascita; }
}

