package pv260.solid.ocp.solution;

/**
 * Thrown from any persistence handling class if
 * problem related to persistence occurs
 */
public class PersistenceException extends Exception {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }

    public PersistenceException(String message,
                                Throwable cause) {
        super(message,
              cause);
    }
}
