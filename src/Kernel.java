import java.util.HashMap;
import java.util.LinkedList;

public class Kernel implements Device{
    
    private Scheduler scheduler;
    private VFS vfs = new VFS();
    private boolean[] freeList = new boolean[1024];
    private int fileLocation; 
    private FakeFileSystem ffs = new FakeFileSystem();

    private HashMap<Integer, KernelandProcess> WaitingProcesses = new HashMap<Integer, KernelandProcess>();

    public Kernel()
    {
        this.scheduler = new Scheduler(this);
        this.fileLocation = ffs.Open("swap.txt");
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

    public int GetPid()
    {
        return this.scheduler.GetPid();
    }

    public int GetPidByName(String name)
    {
        return this.scheduler.GetPidByName(name);
    }

    public void SendMessage(KernelMessage message)
    {
        KernelMessage temp = new KernelMessage(message);

        if(WaitingProcesses.containsKey(temp.getTargetPID()))
        {
            KernelandProcess targetProcess = WaitingProcesses.get(temp.getTargetPID());
            targetProcess.addMessages(temp);
            this.scheduler.getPriorityList(targetProcess.getPriority()).add(targetProcess);
        }
        else
        {
            if(this.scheduler.getHashMap().containsKey(temp.getTargetPID()))
            {
                this.scheduler.getHashMap().get(temp.getTargetPID()).addMessages(temp);
            }
            else
            {
                System.out.println("Target Process does not exist.");
            }
        }
    }

    public KernelMessage WaitForMessage()
    {
        LinkedList<KernelMessage> messages = this.scheduler.getCurrentlyRunning().getMessages();
        if(!messages.isEmpty())
        {
            return messages.removeFirst();
        }
        else
        {
            WaitingProcesses.put(this.scheduler.getCurrentlyRunning().getPid(), this.scheduler.getCurrentlyRunning());
            KernelandProcess temp = this.scheduler.getCurrentlyRunning();

            this.scheduler.setCurrentProcess(null);
            this.scheduler.SwitchProcess();
            temp.stop();

            return this.scheduler.getCurrentlyRunning().getMessages().removeFirst();
        }
    }

    public KernelandProcess getCurrentlyRunning()
    {
        return this.scheduler.getCurrentlyRunning();
    }

    public void KillCurrentProcess()
    {
        this.scheduler.KillCurrentProcess();
    }

    public int AllocateMemory(int size)
    {
        KernelandProcess KLP = this.scheduler.getCurrentlyRunning();
        int pagesNeeded = size/1024;
        VirtualToPhysicalMapping[] virtualMemory = KLP.getVirtualMemory();
        boolean exit = false;
        int initalPage = -1;

        //Conditions in loop are for if there is not enough space in virtual memory. Aka int[] virtualMemory does not contain contiguous 'pagesNeeded' number of -1's
        for(int i = 0; i<virtualMemory.length && exit == false; i++)
        {
            //if we find a place in the process' virtual memory that has a -1, we check to see if the next 'pagesNeeded' number of spaces are also -1's
            if(virtualMemory[i].getPhysicalPageNumber() == -1)
            {
                int count = 0;
                //set j = i so we can index from i to i + pagesNeeded   
                //First condition is dont index outside of the virtualMemory, if you do make sure return -1 in the condition outside both of these loops.
                //Second condition is we must find 'pagesNeeded' number of -1's in a row to allocate the memory.
                for(int j = i; j < virtualMemory.length && j < i + pagesNeeded; j++)
                {
                    //if we find a -1, add a value to count.
                    if(virtualMemory[j].getPhysicalPageNumber() == -1)
                    {
                        count++;
                    }

                    //If we find a value that is -1 and count == pagesNeeded, we have found a place to allocate memory.
                    //Set that inital page value to i because thats the location we found for contiguous memory.
                    //Break out of inner loop and set exit to true to break out of outer loop.
                    if(virtualMemory[j].getPhysicalPageNumber()== -1 && count == pagesNeeded)
                    {
                        initalPage = i;
                        exit = true;
                        break;
                    }
                    //If we find a value that is not -1, we need to start looking for contiguous memory again.
                    //We do this by skipping the indexes we already looked at in the outer loop because we know they wont contain enough memory.
                    //and then we break out of the inner loop.
                    if(virtualMemory[j].getPhysicalPageNumber() != -1)
                    {
                        i = j;
                        break;
                    }

                }
            }
        }

        //If we never found a place to allocate memory, return -1 which is a failure
        if(initalPage == -1)
        {
            return -1;
        }

        //If we found a place in virtual memory to allocate memory, we need to find 'pagesNeeded' number of free pages in PHYSICAL memory.
        //This does not need to be contiguous because we are using paging.
        //We find the pages by looping through the freeList and adding the index of the freeList to an array of ints.
        //This can be all over the place so physicalPage[0] can be equal to freeList[24]: physicalPage[0] = freeList[24];
        int[] physicalPages = new int[pagesNeeded];
        int j = 0;
        for(int i = 0; i <freeList.length && j < pagesNeeded; i++)
        {
            if(!freeList[i])
            {
                physicalPages[j] = i;
                j++;
            }


        }

        //If we did not find 'pagesNeeded' number of free pages in physical memory, return -1 which is a failure.
        if(j != pagesNeeded)
        {
            return -1;
        }

        //We have an array of ints that are the indexes of the freeList that are free called physical pages.
        //We set the inital page of the contiguous memory in virtual memory to the first index of physicalPages.
        /*
         * Example: We need 4096 bytes.
         * 
         * Allocate(4096);
         * Then:
         * 
         * freelist:
         * 0--------------------1024
         * TTTtTTFTFTFFTF
         * becomes
         * TTTTTTTTTTTFTF
         * 
         * 
         * VirtualPages:
         * 0--------------------100
         * 23,12,-1,-1,-1,-1,-1,-1,-1,-1,-1,
         * becomes
         * 23,12,6,8,10,11,-1,-1,-1,-1,-1
         * 
         * phys pages:
         * 0-----4
         * 6,8,10,11
         * 
         * This happens because we take the locations in the freelist that we chose and map them to our virtual pages
         * so we know where in the freelist our actual pages are. We then return a pointer because we multiply that inital page, in our case 3, by 1024
         * and that gives a pointer to the first byte of the contiguous memory we allocated for RAM.
         * 
         * So in this example we would return 6*1024 = 6144.
         * 
         * and if we did Write(6144, 10);
         * 
         * It would look at the TLB and if the TLB did not have the mapping for 3072, it would call GetMapping(3);
         * and the TLB would go to the current process, index 3 in the virtual memory inside the process and find the physical page number 
         * which in our case is 6 as the inital and move along from there.
         * 
         * So therefore our ram looks like this:
         * 
         * RAM:
         * 0--6144---------------------------------------------1024*1024
         * ????10?????????????????????????????????????????????????????
         * So we can write to RAM[6144] and it will write 10 to that location in virtual memory and it will seem contiguous to the process but 
         * in reality the RAM would not be completely contiguous, the pages would be all over the place.
         * So if we filled that page the NEXT time you'd see in the actual RAM that our byte that we could end up writing to something like 10240 because the next page is 10.
         */
        for(int i = initalPage, k = 0; i < initalPage + pagesNeeded; i++, k++)
        {
            VirtualToPhysicalMapping temp = new VirtualToPhysicalMapping();
            temp.setPhysicalPageNumber(physicalPages[k]);
            virtualMemory[i] = temp;
            freeList[physicalPages[k]] = true;
        }
        return initalPage * 1024;
    }

    public boolean FreeMemory(int pointer, int size) {

        int initalPage = pointer / 1024;
        VirtualToPhysicalMapping[] virtualMemory = scheduler.getCurrentlyRunning().getVirtualMemory();

        if(pointer + size> virtualMemory.length * 1024)
        {
            return false;
        }

        for(int i = initalPage; i < virtualMemory.length; i++)
        {
            int physicalPage = virtualMemory[i].getPhysicalPageNumber();
            if(physicalPage != -1)
            {
                freeList[physicalPage] = false;
                virtualMemory[i] = null;
            }
        }
        return true;
    }
    public int getFileLocation() {
        return this.fileLocation;
    }

    public void setFileLocation(int fileLocation) {
        this.fileLocation = fileLocation;
    }
}
