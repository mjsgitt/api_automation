package api.testcases;


public class SeleniumProgressTracker extends baseClass{
    private int totalSteps;
    private int completedSteps;

    public SeleniumProgressTracker(int totalSteps) {
        this.totalSteps = totalSteps;
        this.completedSteps = 0;
    }

    public void incrementProgress() {
        completedSteps++;
        displayProgress();
    }

    private void displayProgress() {
//        float percentage = (getTrows - completedSteps);
        logger.info(new StringBuilder().append("number of steps has been compeleted: ").append(completedSteps).append("\n").toString());
    }
}