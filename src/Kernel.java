public class Kernel implements Device{
    
    private Scheduler scheduler;
    private VFS vfs = new VFS();

    public Kernel()
    {
        this.scheduler = new Scheduler();
    }

    public int CreateProcess(UserlandProcess up) 
    {    
        return this.scheduler.CreateProcess(up);
    }

    public int CreateProcess(UserlandProcess up, Priority priority) 
    {
        return this.scheduler.CreateProcess(up, priority);
    }

    public void Sleep(int milliseconds)
    {
        this.scheduler.Sleep(milliseconds);
    }

    @Override
    public int Open(String s) {
        KernelandProcess KLP = this.scheduler.getCurrentlyRunning(); 
        int[] intarr = KLP.getIntArr();
        for(int i = 0; i < intarr.length; i++)
        {
            if(intarr[i] == -1)
            {
                intarr[i] = vfs.Open(s);
                KLP.setintArr(intarr);
                return i;
            }
        }   
        System.out.println("NO SPACE IN VFS.");
        return -1;
    }

    @Override
    public void Close(int id) {
        KernelandProcess KLP = this.scheduler.getCurrentlyRunning();
        int[] intarr = KLP.getIntArr();
        vfs.Close(intarr[id]);
        intarr[id] = -1;
        KLP.setintArr(intarr);
    }

    @Override
    public byte[] Read(int id, int size) {
        KernelandProcess KLP = this.scheduler.getCurrentlyRunning();
        return vfs.Read(KLP.getIntArr()[id], size);

    }

    @Override
    public void Seek(int id, int to) {
        KernelandProcess KLP = this.scheduler.getCurrentlyRunning();
        vfs.Seek(KLP.getIntArr()[id], to);
    }

    @Override
    public int Write(int id, byte[] data) {
        KernelandProcess KLP = this.scheduler.getCurrentlyRunning();
        return vfs.Write(KLP.getIntArr()[id], data);
    }
}
