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
	private int size;
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void writeFile() {
		int i = 0;
		while (i < numBuffers) {
			try {
				raf.write(buffers[i].getData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}

	/*
	 * This method checks all of the buffers in the pool to see if the wanted buffer
	 * is in there already, if it is then we copy the contents into the destination
	 * "space." If not, then we either add the buffer to the pool, or evict the
	 * least recently used buffer. Either way, we shift down the buffers so that the
	 * buffer we were searching for is now at the front of the pool.
	 */
	public void getbytes(byte[] space, int sz, int pos) throws IOException {
		int block = pos / BLOCK_SIZE;
		int start = pos % BLOCK_SIZE;

		/*
		 * this is code for when we have a cache hit
		 */
		for (int i = 0; i < size; i++) {
			if (buffers[i] != null &&buffers[i].getBlockIndex() == block) {
				System.arraycopy(buffers, 0, buffers, 1, i);
				buffers[0] = buffers[i];
				stat.increaseHits();
				System.arraycopy(buffers[i].getData(), start, space, 0, sz);
				return;
			}
		}
		/*
		 * when we have a cache miss and have to load
		 */
		byte[] data = new byte[BLOCK_SIZE];
		raf.seek(block * BLOCK_SIZE);
		raf.readFully(data);
		Buffer newBuff = new Buffer(block, data);

		/*
		 * buffer pool full
		 */
		if (numBuffers == size) {
			Buffer evicted = buffers[numBuffers - 1];
			if (evicted.isDirty()) {
				raf.seek(evicted.getBlockIndex() * BLOCK_SIZE);
				raf.write(evicted.getData());
				evicted.setDirty(false);
				stat.increaseWrites();
			}
			System.arraycopy(buffers, 0, buffers, 1, numBuffers - 1);
			buffers[0] = newBuff;
		}
		/*
		 * not full
		 */
		else {
			System.arraycopy(buffers, 0, buffers, 1, size);
			buffers[0] = newBuff;
			size++;
		}
		System.arraycopy(data, start, space, 0, sz);

	}

	/*
	 * Insert uses a lot of the same logic as 
	 */
	public void insert(byte[] space, int sz, int pos) throws IOException {
		int block = pos / BLOCK_SIZE;
		int start = pos % BLOCK_SIZE;

		/*
		 * this is code for when we have a cache hit
		 */
		for (int i = 0; i < size; i++) {
			if (buffers[i] != null &&buffers[i].getBlockIndex() == block) {
				System.arraycopy(buffers, 0, buffers, 1, i);
				buffers[0] = buffers[i];
				stat.increaseHits();
				System.arraycopy(buffers[i].getData(), start, space, 0, sz);
				return;
			}
		}
		/*
		 * when we have a cache miss and have to load
		 */
		byte[] data = new byte[BLOCK_SIZE];
		raf.seek(block * BLOCK_SIZE);
		raf.readFully(data);
		Buffer newBuff = new Buffer(block, data);

		/*
		 * buffer pool full
		 */
		if (numBuffers == size) {
			Buffer evicted = buffers[numBuffers - 1];
			if (evicted.isDirty()) {
				raf.seek(evicted.getBlockIndex() * BLOCK_SIZE);
				raf.write(evicted.getData());
				evicted.setDirty(false);
				stat.increaseWrites();
			}
			System.arraycopy(buffers, 0, buffers, 1, numBuffers - 1);
			buffers[0] = newBuff;
		}
		/*
		 * not full
		 */
		else {
			System.arraycopy(buffers, 0, buffers, 1, size);
			buffers[0] = newBuff;
			size++;
		}
		System.arraycopy(space, 0, newBuff.getData(), start, sz);
		newBuff.setDirty(true);

	}

	/*
	 * We find the dirty blocks, go to it and write out its entire block to disk. We
	 * make sure to set dirt to false at the end.
	 */
	public void flush() throws IOException {
		for (int i = 0; i < numBuffers; i++) {
			Buffer buffer = buffers[i];
			if (buffer.isDirty() && buffer != null) {
				int start = buffer.getBlockIndex() * BLOCK_SIZE;
				raf.seek(start);
				raf.write(buffer.getData());
				buffer.setDirty(false);
			}
		}

	}
}
