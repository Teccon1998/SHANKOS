import java.util.Random;

public class RandomDevice implements Device {

    private Random[] randomArr = new Random[10];

    public RandomDevice()
    {
        for(int i = 0; i<randomArr.length; i++)
        {
            randomArr[i] = null;
        }
    }


    @Override
    public int Open(String s) {
        Random random;
        if(!s.isEmpty() || s==null)
        {
            long val = Long.parseLong(s);
            random = new Random(val);
        }      
        else 
        {
            random = new Random();
        }
        for(int i = 0; i < randomArr.length; i++)
        {
            if(randomArr[i]== null)
            {
                randomArr[i] = random;
                return i;
            }
        }
        return -1;  
    }

    @Override
    public void Close(int id) {
        randomArr[id] = null;
    }

    @Override
    public byte[] Read(int id, int size) {
        Random random = randomArr[id];
        byte[] byteArr = new byte[size];
        for(int i = 0; i < byteArr.length; i++)
        {
            byteArr[i] = (byte) random.nextInt();
        }
        return byteArr;
    }

    @Override
    public void Seek(int id, int to) {
        Read(id, to);
    }

    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }
    
}
