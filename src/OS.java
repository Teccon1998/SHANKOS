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

    
}
