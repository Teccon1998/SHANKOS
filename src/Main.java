public class Main {
    public static void main(String[] args)
    {
        OS.Startup(new HelloWorld());
        
        DeviceTest dt = new DeviceTest();
        dt.run();

        
    }
}
