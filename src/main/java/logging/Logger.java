package logging;

public interface Logger {
    Logger push(String description); // appends and returns a logger with this description
    void message(String message); // logs this message
}