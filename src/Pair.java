public class Pair {
    
    private long sleepTime;
    private KernelandProcess process;


    public Pair(long sleepTime, KernelandProcess process) {
        this.sleepTime = sleepTime;
        this.process = process;
    }

    public long getSleepTime() {
        return this.sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public KernelandProcess getProcess() {
        return this.process;
    }

    public void setProcess(KernelandProcess process) {
        this.process = process;
    }
}
