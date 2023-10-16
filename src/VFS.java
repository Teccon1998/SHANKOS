public class VFS implements Device{

    private int deviceLimit = 10;
    private Device[] deviceArr = new Device[deviceLimit];
    private int[] intArr = new int[deviceLimit];
    private RandomDevice randomDevice = new RandomDevice();
    private FakeFileSystem fakeFileSystem = new FakeFileSystem();

    public VFS()
    {
        for(int i = 0; i < deviceLimit; i++)
        {
            deviceArr[i] = null;
            intArr[i] = -1;
        }
    }


    @Override
    public int Open(String s) {
        
        if(s.isEmpty() || s == null)
        {
            return -1;
        }

        String args[] = s.toLowerCase().split(" ");
        switch(args[0])
        {
            case "random":
                for(int i = 0; i < deviceLimit; i++)
                {
                    if(intArr[i] == -1)
                    {   
                        intArr[i] = randomDevice.Open(args[1]);
                        deviceArr[i] = randomDevice;
                        return i;
                    }
                }
                break;
            case "file":
                for(int i = 0; i < deviceLimit; i++)
                {
                    if(intArr[i] == -1)
                    {
                        intArr[i] = fakeFileSystem.Open(args[1]);
                        deviceArr[i] = fakeFileSystem;
                        return i;
                    }
                }
                break;
            default:
                System.out.println("Illegal Argument Opening Device in VFS");
                return -1;
        }
        return -1;
    }

    @Override
    public void Close(int id) {
        if(id == -1)
        {
            System.out.println("Illegal Argument Closing Device in VFS, -1 is not a valid id.");
            return;
        }
        deviceArr[id].Close(id);
        deviceArr[id] = null;
        intArr[id] = -1;
    }

    @Override
    public byte[] Read(int id, int size) {
        if(id == -1)
        {
            System.out.println("Illegal Argument Trying to Read in VFS, -1 is not a valid id.");
            return null;
        }
        return deviceArr[id].Read(id, size);
    }

    @Override
    public void Seek(int id, int to) {
        if(id == -1)
        {
            System.out.println("Illegal Argument Trying to Seek in VFS, -1 is not a valid id.");
            return;
        }
        deviceArr[id].Seek(id, to);
    }

    @Override
    public int Write(int id, byte[] data) {
        if(id == -1)
        {
            System.out.println("Illegal Argument Trying to Write in VFS, -1 is not a valid id.");
            return -1;
        }
        return deviceArr[id].Write(id, data);
    }
    
}
