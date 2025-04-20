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
    private Statistics stat; 
    public BufferPool(String file, int num, Statistics stat) throws FileNotFoundException {
        this.raf = new RandomAccessFile(file, "rw");
        this.numBuffers = num;
        this.buffers = new Buffer[num];
        this.stat = stat;

        
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


	public void getbytes(byte[] space, int size, int pos) {
		int block = pos/BLOCK_SIZE;
		int start = pos%BLOCK_SIZE;
		
		/*
		 * this is code for when we have a cache hit
		 */
		for (int i =0; i < numBuffers; i++) {
			if (buffers[i].getBlockIndex() == block) {
				System.arraycopy(buffers, 0, buffers, 1, i);
				buffers[0] = buffers[i];
				stat.increaseHits();
				System.arraycopy(buffers[i].getData(), start, space, 0, size);
	            return;
			}
		}
		/*
		 * when we have a cache miss and have to load
		 */
		
		
	}


	public void insert(byte[] space, int size, int pos) {
		// TODO Auto-generated method stub
		
	}


	public void flush() throws IOException {
		for (int i = 0; i < numBuffers; i++) {
			Buffer buffer = buffers[i];
			if (buffer.isDirty()) {
				int start = buffer.getBlockIndex()%BLOCK_SIZE;
				raf.seek(start);
				raf.write(buffer.getData());
				buffer.setDirty(false);
			}
		}
		
	}
}
