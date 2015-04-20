package api;

public interface ProgressStep {
    void setProgressPercentage(int value);

    void setText(String text);

    int getProgressPercentage();
}
