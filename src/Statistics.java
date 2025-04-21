import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Simple class to track interactions with disk and cache memory, thereby
 * measuring the efficiency of an algorithm in terms of # of times the code
 * writes/reads to the disk and hits the cached memory, which prevent expensive
 * read/write operations. Also capable of writing that data to a file along with
 * the execution time of any task with the System.currentTimeMillis() method.
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
public class Statistics {
    private int cacheHits;
    private int diskReads;
    private int diskWrites;
    private String file;
    private long exTime;

    /**
     * Constructs a statistics objects, thus assume no work has been done yet so
     * metrics are initialized to 0
     * 
     * @param input
     *            name of the input file
     */
    public Statistics(String input) {
        this.file = input;
        cacheHits = 0;
        diskReads = 0;
        diskWrites = 0;
        exTime = 0;
    }


    /**
     * If the cache finds a match, increase the # of hits
     */
    public void increaseHits() {
        this.cacheHits++;
    }


    /**
     * If the code reads from disk memory, increase # of reads
     */
    public void increaseReads() {
        this.diskReads++;
    }


    /**
     * If code writes to disk memory, increase # of writes
     */
    public void increaseWrites() {
        this.diskWrites++;
    }


    /**
     * Sets the exTime variable which tracks the length of a certain task or
     * length to execute a block of code
     * 
     * @param time
     *            time elapsed for a certain block of code or task
     */
    public void setExTime(long time) {
        this.exTime = time;
    }


    /**
     * Sets the # of cache hits
     * 
     * @param hits
     *            new # of hits
     */
    public void setCacheHits(int hits) {
        this.cacheHits = hits;
    }


    /**
     * Sets the number of disk reads
     * 
     * @param reads
     *            new # of reads
     */
    public void setDiskReads(int reads) {
        this.diskReads = reads;
    }


    /**
     * Sets the # of writes
     * 
     * @param writes
     *            new # of writes
     */
    public void setDiskWrites(int writes) {
        this.diskWrites = writes;
    }


    /**
     * Writes the efficiency data to the passed file
     * 
     * @param statFileName
     *            name of the stat file
     * @throws IOException
     *             throws upon failure to acquire or write i/o sufficiently
     */
    public void writeToFile(String statFileName) throws IOException {
        BufferedWriter writer = null;
        try {
            FileWriter fw = new FileWriter(statFileName, true);
            writer = new BufferedWriter(fw);

            writer.write("Standard sort on " + file + "\n");
            writer.write("Cache Hits: " + cacheHits + "\n");
            writer.write("Disk Reads: " + diskReads + "\n");
            writer.write("Disk Writes: " + diskWrites + "\n");
            writer.write("Execution Time (ms): " + exTime + "\n");
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
