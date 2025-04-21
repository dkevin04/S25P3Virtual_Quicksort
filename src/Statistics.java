import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Statistics {
	private int cacheHits;
    private int diskReads;
    private int diskWrites;
    private String file;
    private long exTime;
    
    public Statistics(String input) {
    	this.file = file;
    	cacheHits = 0;
        diskReads = 0;
        diskWrites = 0;
        exTime = 0;
    }
    
    public void increaseHits() {
    	this.cacheHits++;
    }
    
    public void increaseReads() {
    	this.diskReads++;
    }
    
    public void increaseWrites() {
    	this.diskWrites++;
    }
    
    public void setExTime(long time) {
    	this.exTime = time;
    }
    
    public void setCacheHits(int hits) {
        this.cacheHits = hits;
    }

    public void setDiskReads(int reads) {
        this.diskReads = reads;
    }

    public void setDiskWrites(int writes) {
        this.diskWrites = writes;
    }
    
    public void writeToFile(String statFileName) throws IOException {
        BufferedWriter writer = null;
        try {
            FileWriter fw = new FileWriter(statFileName, true);
            writer = new BufferedWriter(fw);

            writer.write("Standard sort on " + file + "\n");
            writer.write("Cache Hits: " + cacheHits + "\n");
            writer.write("Disk Reads: " + diskReads + "\n");
            writer.write("Disk Writes: " + diskWrites + "\n");
            writer.write("Execution Time (ms): " + exTime + "\n\n");
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
