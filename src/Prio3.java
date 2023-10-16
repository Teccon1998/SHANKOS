public class Prio3 extends UserlandProcess{
    @Override
    public void run() {
        while(true)
        {
            System.out.println("Prio3");   
            OS.Sleep(100);
        }
    }
}
