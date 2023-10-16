public class Prio1 extends UserlandProcess{
    @Override
    public void run() {
        while(true)
        {
            System.out.println("Prio1");   
            try {
                Thread.sleep(400);
                OS.Sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
