public class VirtualToPhysicalMapping {
    public int physicalPageNumber;
    public int onDiskPageNumber;

    public VirtualToPhysicalMapping()
    {
        this.physicalPageNumber = -1;
        this.onDiskPageNumber = -1;
    }

    public void setPhysicalPageNumber(int physicalPageNumber)
    {
        this.physicalPageNumber = physicalPageNumber;
    }

    public void setOnDiskPageNumber(int onDiskPageNumber)
    {
        this.onDiskPageNumber = onDiskPageNumber;
    }

    public int getPhysicalPageNumber()
    {
        return this.physicalPageNumber;
    }

    public int getOnDiskPageNumber()
    {
        return this.onDiskPageNumber;
    }
}
