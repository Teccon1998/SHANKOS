public class PagingTestProcess extends UserlandProcess {
    public void run(){
        OS.Startup(new HelloWorld());
        System.out.println("Testing Reading and Writing");
        System.out.println("Allocationg memory...");
        this.Write(10, (byte) 95);
        this.Write(11, (byte) 105);
        System.out.println("Value from first read should be equal to 95: " + this.Read(10));
        System.out.println("Value from first read should be equal to 105: " + this.Read(11));
        System.out.println();

        System.out.println("Testing error checking for Memory Allocation and Freeing.");
        // This call shouldn't work and output a message about an invalid parameter in the terminal
        OS.AllocateMemory(50);
        OS.FreeMemory(2300, 2);
        System.out.println();

        System.out.println("Testing memory allocation.");
        // its functional with this line
        int addr = OS.AllocateMemory(1024);
        int addr2 = OS.AllocateMemory(1024 * 2);
        int addr3 = OS.AllocateMemory(1024 * 3);
        int addr4 = OS.AllocateMemory(1024 * 4);
        int addr5 = OS.AllocateMemory(1024 * 5);

        System.out.println("Using a lot of memory at the same time, showing the pointers to that memory");
        System.out.println("Address given from memory allocation one : " + addr);
        System.out.println("Address given from memory allocation two : " + addr2);
        System.out.println("Address given from memory allocation three : " + addr3);
        System.out.println("Address given from memory allocation four : " + addr4);
        System.out.println("Address given from memory allocation five : " + addr5);

        OS.FreeMemory(addr * 1024, 1024);
        OS.FreeMemory(addr2 * 1024, 1024);
        OS.FreeMemory(addr3 * 1024, 1024);
        OS.FreeMemory(addr4 * 1024, 1024);
        OS.FreeMemory(addr5 * 1024, 1024);


    }   
}