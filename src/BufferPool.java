import java.io.*;

public class BufferPool {
	private static int BLOCK_SIZE = 4096;
	private Buffer[] buffers;
	private int numBuffers;
	private RandomAccessFile raf;
	
	private int cacheHits;
	private int diskReads = 0;
    private int diskWrites = 0;
	
	public BufferPool(File file, int num) throws FileNotFoundException {
		this.raf = new RandomAccessFile(file, "rw");
		this.numBuffers = num;
		this.buffers = new Buffer[num];
		
		cacheHits = 0;
		diskReads = 0;
		diskWrites = 0;
	}
	
	
	
	
}
