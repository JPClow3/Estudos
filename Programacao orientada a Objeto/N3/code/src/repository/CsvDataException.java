package repository;

import java.nio.file.Path;

public class CsvDataException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CsvDataException(Path file, String message) {
        super(file.getFileName() + ": " + message);
    }

    public CsvDataException(Path file, int row, String message) {
        super(file.getFileName() + " (linha " + row + "): " + message);
    }

    public CsvDataException(Path file, String message, Throwable cause) {
        super(file.getFileName() + ": " + message, cause);
    }
}
