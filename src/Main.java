public class Main {
    public static void main(String[] args)
    {
        OS.Startup(new UserlandProcess() {
            @Override
            public void run() {
                OS.CreateProcess(new UserlandProcess() {
                    @Override
                    public void run(){
                        while(true)
                        {
                            try {
                                OS.AllocateMemory(1024);
                                for(int i = 0; i < 2048 ;i++)
                                {
                                    System.out.print("Writing " + i);
                                    System.out.println(" ByteVersion: " + (byte) i);
                                    Write(i, (byte) i);
                                    System.out.println("Sleeping thread for debugging and inspecting RAM.");
                                    Thread.sleep(1000000);
                                }

                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                });
            }
        });

        
    }
}
