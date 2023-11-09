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
                                Thread.sleep(40);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                });

                OS.CreateProcess(new UserlandProcess() {
                    @Override
                    public void run(){
                        while(true)
                        {
                            try {
                                Thread.sleep(40);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                    }
                });
                System.out.println("Hello World");
                Read(0);
                System.out.println("Hello World2");
            }
        });

        
    }
}
