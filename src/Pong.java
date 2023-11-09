public class Pong extends UserlandProcess{
    @Override
    public void run()
    {
        while(true)
        {
            OS.SendMessage(new KernelMessage(OS.GetPid(),OS.GetPidByName("Ping"),"Pong".getBytes(),0));
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
