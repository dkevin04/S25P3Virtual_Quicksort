import java.io.*;

/*
 * This might need to be changed to properly use bufferpool, according to the
 * specs in piazza and since you did the constructor for buffer write something
 * to initialize all the buffer objects
 */
public class BufferPool {
    private static int BLOCK_SIZE = 4096;
    private Buffer[] buffers;
    private int numBuffers;
    private RandomAccessFile raf;

    public BufferPool(String file, int num) throws FileNotFoundException {
        this.raf = new RandomAccessFile(file, "rw");
        this.numBuffers = num;
        this.buffers = new Buffer[num];

        
    }


    void readFile() {
        int i = 0;
        try {
            while (raf.read(buffers[i].getData()) > -1 && i < numBuffers) {
                i++;
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    void writeFile() {
        int i = 0;
        while (i < numBuffers) {
            try {
                raf.write(buffers[i].getData());
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            i++;
        }
    }


	public void getbytes(byte[] pivot, int i, int j) {
		// TODO Auto-generated method stub
		
	}


	public void insert(byte[] left, int i, int j) {
		// TODO Auto-generated method stub
		
	}
}
