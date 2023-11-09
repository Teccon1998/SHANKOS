public class Ping extends UserlandProcess{
    @Override
    public void run()
    {
        while(true)
        {
            OS.SendMessage(new KernelMessage(OS.GetPid(),OS.GetPidByName("Pong"),"Ping".getBytes(),0));
            try
            {
                System.out.println(OS.WaitForMessage().toString());
                Thread.sleep(240);
                
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }
}
