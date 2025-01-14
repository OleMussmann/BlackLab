package nl.inl.blacklab.exceptions;

/**
 * A RuntimeException generated by BlackLab.
 *
 * This will be the base class of all BlackLab-thrown RuntimeExceptions. More
 * specific subclasses can be caught to handle specific situations.
 */
public abstract class BlackLabException extends Exception {

    public BlackLabException() {
        super();
    }

    public BlackLabException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlackLabException(String message) {
        super(message);
    }

    public BlackLabException(Throwable cause) {
        super(cause);
    }

}
