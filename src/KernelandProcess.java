
public class KernelandProcess {

    private static int nextpid = 1;
    private int pid;
    private boolean threadStarted;
    private Thread thread;
    private Priority priority;
    private int timesRun;
    private int[] intArr = new int[10];
    

    public KernelandProcess(UserlandProcess up)
    {
        thread = new Thread(up);
        this.pid = nextpid++;
        for(int i = 0; i<intArr.length; i++)
        {
            intArr[i] = -1;
        }
    }
    
    public KernelandProcess(UserlandProcess up, Priority priority)
    {
        thread = new Thread(up);
        this.pid = nextpid++;
        this.priority = priority;
        for(int i = 0; i<intArr.length; i++)
        {
            intArr[i] = -1;
        }
    }

    public void stop()
    {
        if(this.threadStarted)
        {
            this.thread.suspend();
        }
    }

    public void addTimesRun()
    {
        this.timesRun++;
    }

    public int getTimesRun()
    {
        return this.timesRun;
    }
    public void setTimesRun(int timeRun)
    {
        this.timesRun = timeRun;
    }

    public void demote()
    {
        switch(this.priority)
        {
            case RealTime:
                this.priority = Priority.Interactive;
                break;
            case Interactive:
                this.priority = Priority.Background;
                break;
            case Background:
                break;
        }
    }

    public boolean isDone()
    {
        if(this.threadStarted && !thread.isAlive())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void run()
    {
        if(threadStarted)
        {
            this.thread.resume();
        }
        else
        {
            this.threadStarted = true;
            this.thread.start();
        }
    }

    public void setNextPID()
    {
        nextpid++;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public boolean isThreadStarted() {
        return this.threadStarted;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setintArr(int[] arr)
    {
        this.intArr = arr;
    }

    public int[] getIntArr()
    {
        return this.intArr;
    }


}
