package festival.persistance.Exceptions;

public class RepositoryException extends RuntimeException {
    public RepositoryException()
    {

    }

    /**
     *
     * @param message -> message to throw
     */
    public RepositoryException(String message){super(message);}
}
