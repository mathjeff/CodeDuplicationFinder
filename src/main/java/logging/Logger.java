package logging;

// In this new small project, I'm trying out a variant on one of my previous logging implementations
// The behavior will probably be the same, where each function call gets more spaces in front of its log messages based on its depth in the call stack
// This time it'll be implemented by creating a new logger object at each call, to avoid having to reset it when done

public interface Logger {
    Logger push(String description); // appends and returns a logger with this description
    void message(String message); // logs this message
}