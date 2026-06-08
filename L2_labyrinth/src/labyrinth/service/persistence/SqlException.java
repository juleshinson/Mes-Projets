package labyrinth.service.persistence;

public class SqlException extends RuntimeException {
    public SqlException(String message, Throwable cause) {
        super(message, cause);
    }
}