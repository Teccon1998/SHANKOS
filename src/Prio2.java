public class Prio2 extends UserlandProcess{
    @Override
    public void run() {
        while(true)
        {
            System.out.println("Prio2");   
            OS.Sleep(100);
        }
    }
}
