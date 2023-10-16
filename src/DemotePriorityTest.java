public class DemotePriorityTest extends UserlandProcess {
    public void run(){
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(240);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    OS.CreateProcess(new Prio1(),Priority.RealTime);
    OS.CreateProcess(new Prio1(),Priority.RealTime);
    OS.CreateProcess(new Prio1(),Priority.RealTime);
    OS.CreateProcess(new Prio1(),Priority.RealTime);
    OS.CreateProcess(new Prio1(),Priority.RealTime);
    OS.CreateProcess(new Prio1(),Priority.RealTime);
    OS.CreateProcess(new Prio1(),Priority.RealTime);

    OS.CreateProcess(new Prio2(),Priority.Interactive);
    OS.CreateProcess(new Prio2(),Priority.Interactive);
    OS.CreateProcess(new Prio2(),Priority.Interactive);
    OS.CreateProcess(new Prio2(),Priority.Interactive);
    OS.CreateProcess(new Prio2(),Priority.Interactive);
    OS.CreateProcess(new Prio2(),Priority.Interactive);
    OS.CreateProcess(new Prio2(),Priority.Interactive);
    OS.CreateProcess(new Prio2(),Priority.Interactive);

    OS.CreateProcess(new Prio3(),Priority.Background);
    OS.CreateProcess(new Prio3(),Priority.Background);
    OS.CreateProcess(new Prio3(),Priority.Background);
    OS.CreateProcess(new Prio3(),Priority.Background);
    OS.CreateProcess(new Prio3(),Priority.Background);
    OS.CreateProcess(new Prio3(),Priority.Background);
    OS.CreateProcess(new Prio3(),Priority.Background);
    OS.CreateProcess(new Prio3(),Priority.Background);
    }


    
}
