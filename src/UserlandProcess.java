public abstract class UserlandProcess implements Runnable{

    private static final int PAGESIZE = 1024;
    private static byte[] RAM = new byte[PAGESIZE*PAGESIZE];
    public static int[][] TLB = {{-1,-1},{-1,-1}};

    public byte Read(int address)
    {
        int VirtualPage = address / PAGESIZE;
        int Offset = address % PAGESIZE;


        int physicalAddress, physicalPage;
        while(true)
        {
            if(TLB[0][0] == VirtualPage)
            {
                physicalPage = TLB[0][1];
                break;
            }
            else if(TLB[1][0] == VirtualPage)
            {
                physicalPage = TLB[1][1];
                break;
            }
            else
            {
                OS.GetMapping(VirtualPage);
            }
        }

        physicalAddress = physicalPage * PAGESIZE + Offset;
        return RAM[physicalAddress];
    }

    public void Write(int address,byte data)
    {
        int VirtualPage = address / PAGESIZE;
        int Offset = address % PAGESIZE;


        int physicalPage;
        while(true)
        {
            if(TLB[0][0] == VirtualPage)
            {
                physicalPage = TLB[0][1];
                break;
            }
            else if(TLB[1][0] == VirtualPage)
            {
                physicalPage = TLB[1][1];
                break;
            }
            else
            {
                OS.GetMapping(VirtualPage);
            }
        }

        RAM[physicalPage * PAGESIZE + Offset] = data;
    }
}
