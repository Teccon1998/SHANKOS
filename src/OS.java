import java.util.Random;

public class OS {
    
    private static Kernel kernel;

    public static void Startup(UserlandProcess init) {
        kernel = new Kernel();
        CreateProcess(init,Priority.Interactive);
    }

    public static int CreateProcess(UserlandProcess up) {
        return kernel.CreateProcess(up,Priority.Interactive);
    }

    public static int CreateProcess(UserlandProcess up, Priority priority) {
        return kernel.CreateProcess(up, priority);
    }

    public static void Sleep(int milliseconds)
    {
        kernel.Sleep(milliseconds);
    }

    public static int Open(String string) {
        return kernel.Open(string);
    }

    public static void Close(int id) {
        kernel.Close(id);
    }

    
    public static byte[] Read(int id, int size) {
        return kernel.Read(id, size);
    }

    
    public static void Seek(int id, int to) {
        kernel.Seek(id, to);
    }

    
    public static int Write(int id, byte[] data) {
        return kernel.Write(id, data);
    }

    public static int GetPid()
    {
        return kernel.GetPid();
    }

    public static int GetPidByName(String name)
    {
        return kernel.GetPidByName(name);
    }
    
    public static void SendMessage(KernelMessage message)
    {
        kernel.SendMessage(message);
    }

    public static KernelMessage WaitForMessage()
    {
        return kernel.WaitForMessage();
    }

    public static KernelandProcess getCurrentlyRunning()
    {
        return kernel.getCurrentlyRunning();
    }
    
    public static void GetMapping(int virtualPageNumber)
    {
        KernelandProcess currentlyRunning = getCurrentlyRunning();
        int physicalPageNumber;
        if((physicalPageNumber = currentlyRunning.getVirtualMemory()[virtualPageNumber]) != -1)
        {
            Random random = new Random(System.currentTimeMillis());
            int rand = random.nextInt(2); 
            UserlandProcess.TLB[rand][0]= virtualPageNumber;
            UserlandProcess.TLB[rand][1]= physicalPageNumber;
        }
        else
        {
            kernel.KillCurrentProcess();
        }
    }

    public static int AllocateMemory(int size)
    {
        if(size % 1024 != 0)
        {
            return -1;
        }
        return kernel.AllocateMemory(size);
    }

    public static boolean FreeMemory(int pointer, int size)
    {
        if(size % 1024 != 0 && pointer % 1024 != 0)
        {
            return false;
        }

        return kernel.FreeMemory(pointer, size);
    }
}
