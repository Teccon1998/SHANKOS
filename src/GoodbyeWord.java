public class GoodbyeWord extends UserlandProcess{
    
    @Override
    public void run() {
        while(true)
        {
            System.out.println("Goodbye World");
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            
        }
    }
}
