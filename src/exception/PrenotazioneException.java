
package exception;

public class PrenotazioneException extends Exception {
    private static final long serialVersionUID = 1L;


    // Costruttore con messaggio
    public PrenotazioneException(String message) {
        super(message);
    }

    // Costruttore con messaggio e causa
    public PrenotazioneException(String message, Throwable cause) {
        super(message, cause);
    }

    // Costruttore di default
    public PrenotazioneException() {
        super();
    }
}