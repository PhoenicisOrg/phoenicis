package api;

public interface ProgressStep {
    void setProgressPercentage(double value);

    void setText(String text);

    double getProgressPercentage();
}
