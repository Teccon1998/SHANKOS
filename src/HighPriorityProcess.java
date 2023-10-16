public class HighPriorityProcess extends UserlandProcess
{
    private static long i;
    @Override
    public void run()
    {
        i = 0;
        while(true)
        {
            System.out.println("High Priority World"+i);
            OS.Sleep(10000);
            i++;
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                //throw new RuntimeException(e);
            }
        }
    }
}
