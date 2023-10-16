public class DeviceTest {
    public void run()
    {
        OS.CreateProcess(new UserlandProcess() {
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(240);    
                    } catch(Exception e){}
                }
            }
        });


        byte [][] ReadArr = new byte[10][10];
        int[] randIDArray = new int[10];
        for(int i = 0; i < 10; i++)
        {
            randIDArray[i] = OS.Open("random " + String.valueOf(i));
        }
        for(int i = 0; i < 10; i++)
        {
            ReadArr[i] = OS.Read(randIDArray[i], 10);
        }

        for(int i = 0; i<10; i++)
        {
            System.out.println("Random " + String.valueOf(i+1));
            for(int j = 0; j<10; j++)
            {

                System.out.println(ReadArr[i][j]);
            }
        }

        //This should fail and return no space in VFS.
        int randID11Test = OS.Open("random 11");

        for(int i = 0; i<10; i++)
        {
            OS.Close(randIDArray[i]);
        }
        //Now that everything is closed this should work without issue.
        byte[] ReadArr2;
        int randID12Test = OS.Open("random 12");
        ReadArr2 = OS.Read(randID12Test, 10);    
        System.out.println("Random 12");
        for(int i = 0; i<10; i++)
        {
            System.out.println(ReadArr2[i]);
        }

        //closing the last open random device.
        OS.Close(randID12Test);


        byte[] writeArr = "TEST SENTENCE".getBytes();
        int filePointer = OS.Open("file TestFile.txt");
        OS.Write(filePointer, writeArr);
        OS.Seek(filePointer, 0);
        System.out.println(new String(OS.Read(filePointer, writeArr.length)));
        OS.Close(filePointer);
        OS.Seek(filePointer, 0);
        byte[] returnedBytes = OS.Read(filePointer, writeArr.length);
        //This should throw an exception because the file is closed, and throws a null pointer exception.
        System.out.println(new String(returnedBytes));
    }
}