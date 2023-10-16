import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device{

    private RandomAccessFile[] fileArray = new RandomAccessFile[10];

    public FakeFileSystem()
    {
        for(int i = 0 ; i<fileArray.length; i++)
        {
            fileArray[i] = null;
        }
    }

    @Override
    public int Open(String s) {
        if(s.isEmpty() || s == null)
        {
            System.out.println("Open must be provided a file name.");
        }
        try {
            for(int i = 0; i<fileArray.length; i++)
            {
                if(fileArray[i]==null)
                {
                    fileArray[i] = new RandomAccessFile(s, "rw");
                    return i;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void Close(int id) {
        fileArray[id] = null;
    }

    @Override
    public byte[] Read(int id, int size) {
        
        byte[] byteArr = new byte[size];
        try {
            fileArray[id].read(byteArr, 0, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArr;
        
    }

    @Override
    public void Seek(int id, int to) {
        try {
            fileArray[id].seek(to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int Write(int id, byte[] data) {
        try {
            fileArray[id].write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.length;
    }
    
}
