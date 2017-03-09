package pv260.solid.ocp.solution;

/**
 * Can store data persistently.
 * Format, location etc. are all up to implementation
 */
public interface Persistence {

    /**
     * Persistently store the comment
     * @throws PersistenceException if any exception occurs while storing the comment
     */
    void persist(Comment comment) throws PersistenceException;

}
