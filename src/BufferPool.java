import java.io.*;

/**
 * BufferPool reads and writes to a file stored in disk memory, it manages large
 * data by splitting it into separate buffers of certain length, here determined
 * by the number of 4 byte long data records that are being stored. Also keeps
 * track of certain metrics that relate to the efficiency of an algorithms
 * memory calls
 *
 * @author Blake Everman
 * @version 20/04/2025
 */
// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.
public class BufferPool {
    private static int BLOCK_SIZE = 4096;
    private Buffer[] buffers;
    private int numBuffers;
    private int size;
    private RandomAccessFile raf;
    private Statistics stat;

    /**
     * Constructs a BufferPool object
     * 
     * @param file
     *            the file that stores the data being read in
     * @param num
     *            number of buffer arrays to be created
     * @param stat
     *            stat object so track efficiency metrics
     * @throws FileNotFoundException
     *             throw if the file does not exist in the library
     */
    public BufferPool(String file, int num, Statistics stat)
        throws FileNotFoundException {
        this.raf = new RandomAccessFile(file, "rw");
        this.numBuffers = num;
        this.buffers = new Buffer[num];
        this.stat = stat;

    }


    /**
     * This method checks all of the buffers in the pool to see if the wanted
     * buffer is in there already, if it is then we copy the contents into the
     * destination "space." If not, then we either add the buffer to the pool,
     * or evict the least recently used buffer. Either way, we shift down the
     * buffers so that the buffer we were searching for is now at the front of
     * the pool.
     * 
     * @param space
     *            the byte array which will hold the 4 byte record
     * @param sz
     *            size of the record being read, here assumed to be 4 as the
     *            data in the file is 4 byte records
     * @param pos
     *            position of the beginning of the 4 byte to be read
     * @throws IOException
     *             throws upon failure to acquire or write i/o sufficiently
     */
    public void getbytes(byte[] space, int sz, int pos) throws IOException {
        int block = pos / BLOCK_SIZE;
        int start = pos % BLOCK_SIZE;

        /*
         * this is code for when we have a cache hit
         */
        for (int i = 0; i < size; i++) {
            if (buffers[i] != null && buffers[i].getBlockIndex() == block) {
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


    /**
     * Inserts the passed byte array space at the specificity position
     * 
     * @param space
     *            the byte array which will hold the 4 byte record
     * @param sz
     *            size of the record being inserted, here assumed to be 4 as the
     *            data in the file is 4 byte records
     * @param pos
     *            position of the beginning of the 4 byte to be edited
     * @throws IOException
     *             throws upon failure to acquire or write i/o sufficiently
     */
    public void insert(byte[] space, int sz, int pos) throws IOException {
        int block = pos / BLOCK_SIZE;
        int start = pos % BLOCK_SIZE;

        /*
         * this is code for when we have a cache hit
         */
        for (int i = 0; i < size; i++) {
            if (buffers[i] != null && buffers[i].getBlockIndex() == block) {
                System.arraycopy(buffers, 0, buffers, 1, i);
                buffers[0] = buffers[i];
                stat.increaseHits();
                System.arraycopy(space, 0, buffers[i].getData(), start, sz);
                buffers[0].setDirty(true);
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


    /**
     * We find the dirty blocks, go to it and write out its entire block to
     * disk. We make sure to set dirt to false at the end.
     * 
     * @throws IOException
     *             throws upon failure to acquire or write i/o sufficiently
     */
    public void flush() throws IOException {
        for (int i = 0; i < numBuffers; i++) {
            Buffer buffer = buffers[i];
            if (buffer.isDirty() && buffer != null) {
                int start = buffer.getBlockIndex() * BLOCK_SIZE;
                raf.seek(start);
                raf.write(buffer.getData());
                stat.increaseWrites();
                buffer.setDirty(false);
            }
        }

    }
}
