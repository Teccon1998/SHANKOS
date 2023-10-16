import java.time.Clock;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {
    private class Interrupt extends TimerTask {
        @Override
        public void run() {
            SwitchProcess();
        }
    }

    private List<KernelandProcess> realTimeProcesses;
    private List<KernelandProcess> interactiveProcesses;
    private List<KernelandProcess> backgroundProcesses;

    private List<Pair> sleepingProcesses;
    private Timer timer;
    private KernelandProcess currentProcess;
    private Clock clock;


    public Scheduler()
    {
        this.realTimeProcesses = Collections.synchronizedList(new LinkedList<KernelandProcess>());
        this.interactiveProcesses = Collections.synchronizedList(new LinkedList<KernelandProcess>());
        this.backgroundProcesses = Collections.synchronizedList(new LinkedList<KernelandProcess>());  
        this.sleepingProcesses = Collections.synchronizedList(new LinkedList<Pair>());
        this.timer = new Timer();
        timer.schedule(new Interrupt(), 250, 250);
        this.clock = Clock.systemDefaultZone();
    }

    public int CreateProcess(UserlandProcess up)
    {
        KernelandProcess process = new KernelandProcess(up);
        this.interactiveProcesses.add(process);

        if(this.currentProcess == null)
        {
            SwitchProcess();
        }

        process.setNextPID();
        return process.getPid();
    }

    public int CreateProcess(UserlandProcess up, Priority priority)
    {
        KernelandProcess process = new KernelandProcess(up, priority);
        

        switch(priority)
        {
            case RealTime:
                this.realTimeProcesses.add(process);
                break;
            case Interactive:
                this.interactiveProcesses.add(process);
                break;
            case Background:
                this.backgroundProcesses.add(process);
                break;
        }

        if(this.currentProcess == null)
        {
            SwitchProcess();
        }

        process.setNextPID();
        return process.getPid();
    }

    private KernelandProcess getAProcess(Priority priority)
    {
        switch(priority)
        {
            case RealTime:
                if(!this.realTimeProcesses.isEmpty())
                    return this.realTimeProcesses.remove(0);
                if(!this.interactiveProcesses.isEmpty())
                    return this.interactiveProcesses.remove(0);
                if(!this.backgroundProcesses.isEmpty())
                    return this.backgroundProcesses.remove(0);
                break;
            case Interactive:
                if(!this.interactiveProcesses.isEmpty())
                    return this.interactiveProcesses.remove(0);
                if(!this.backgroundProcesses.isEmpty())
                    return this.backgroundProcesses.remove(0);
                if(!this.realTimeProcesses.isEmpty())
                    return this.realTimeProcesses.remove(0);
                break;
            case Background:
                if(!this.backgroundProcesses.isEmpty())
                    return this.backgroundProcesses.remove(0);
                if(!this.realTimeProcesses.isEmpty())
                    return this.realTimeProcesses.remove(0);
                if(!this.interactiveProcesses.isEmpty())
                    return this.interactiveProcesses.remove(0);
                break;
            default:
                return null;
        }
        return null;
    }

    private void getNextProcessFromQueue()
    {

        Random random = new Random(System.currentTimeMillis());
        //If realTime queue not empty get a number between 0-9 
        if(!this.realTimeProcesses.isEmpty())
        {
            int num = random.nextInt(10);
            switch(num)
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    this.currentProcess = getAProcess(Priority.RealTime);
                    break;
                case 6:
                case 7:
                case 8:
                    this.currentProcess = getAProcess(Priority.Interactive);
                    break;
                case 9:
                    this.currentProcess = getAProcess(Priority.Background);
                    break;
            }
        }
        else
        {
            int num = random.nextInt(4);
            switch(num)
            {
                case 0:
                case 1:
                case 2:
                    this.currentProcess = getAProcess(Priority.Interactive);
                    break;
                case 3:
                    this.currentProcess = getAProcess(Priority.Background);
                    break;
            }
        }
    }

    private void wakeFromSleep()
    {
        while(!this.sleepingProcesses.isEmpty() && this.sleepingProcesses.get(0).getSleepTime() < clock.millis())
        {
            KernelandProcess temp = this.sleepingProcesses.remove(0).getProcess();
            switch(temp.getPriority())
            {
                case RealTime:
                    this.realTimeProcesses.add(temp);
                    break;
                case Interactive:
                    this.interactiveProcesses.add(temp);
                    break;
                case Background:
                    this.backgroundProcesses.add(temp);
                    break;
            }
        }
    }

    private void requeueProcess()
    {
        if(this.currentProcess != null)
        {
            this.currentProcess.stop();

            if(!this.currentProcess.isDone())
            {
                this.currentProcess.addTimesRun();
                if(this.currentProcess.getTimesRun() >= 5)
                {
                    this.currentProcess.demote();
                    this.currentProcess.setTimesRun(0);
                }
                switch(this.currentProcess.getPriority())
                {
                    case RealTime:                        
                        this.realTimeProcesses.add(this.currentProcess);
                        break;
                    case Interactive:
                        this.interactiveProcesses.add(this.currentProcess);
                        break;
                    case Background:
                        this.backgroundProcesses.add(this.currentProcess);
                        break;
                }
            }
            else
            {
                this.currentProcess = null;
            }
        }
    }
    public void SwitchProcess()
    {
        wakeFromSleep();
        requeueProcess();
        getNextProcessFromQueue();
        if(this.currentProcess != null)
        {        
            this.currentProcess.run();
        }
    }

    public void Sleep(int milliseconds)
    {
        long time = clock.millis() + milliseconds;

        KernelandProcess temp = currentProcess;
        currentProcess = null;

        Pair pair = new Pair(time, temp);

        if(this.sleepingProcesses.isEmpty())
        {
            this.sleepingProcesses.add(pair);
        }
        else
        {
            int i = 0;
            while(true)
            {
                if(i == this.sleepingProcesses.size() || this.sleepingProcesses.get(i).getSleepTime() > pair.getSleepTime())
                {
                    this.sleepingProcesses.add(i, pair);

                    break;
                }
                i++;
            }
        }
        
        SwitchProcess();
        temp.stop();
    }

    public KernelandProcess getCurrentlyRunning()
    {
        return this.currentProcess;
    }
}
