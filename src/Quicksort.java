import java.io.*;
import java.nio.ByteBuffer;

/*
 * A quicksort implementation which takes text files as input assuming each data
 * entry is a single char followed by three white space chars
 */

/**
 * The class containing the main method.
 *
 * @author Kevin Dong
 * @version 31/03/2025
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

public class Quicksort {
    /**
     * @param args
     *            Command line parameters. See the project spec!!!
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(
                "Wrong number of command line args, please pass 2 files and the numbers of buffers");
        }
        if (Integer.valueOf(args[1]).intValue() > 20 && Integer.valueOf(args[1])
            .intValue() < 1) {
            System.out.println(
                "Wrong number of buffers, please input an integer between 1 and 20");
        }
        /*
         * Take file and construct char[] from it
         */
        String inputFile = args[0];
        int numBuffers = Integer.valueOf(args[1]);
        String outputFile = args[2];
        BufferPool data = null;
        Statistics stat = null;
        try (RandomAccessFile stmt = new RandomAccessFile(inputFile, "rw")) {
            data = new BufferPool(inputFile, numBuffers);
            stat = new Statistics(inputFile);
            int numRecords = (int) stmt.length() /4;
            long start = System.currentTimeMillis();
            quickSort(data, 0, numRecords - 1);
            data.flush();
            long end = System.currentTimeMillis();

            stat.setExTime(end - start);
            stat.writeToFile(outputFile);
        }
        catch (IOException e) {
            // Exception handling
            e.printStackTrace();
        }
        
        /*
         * for (int i = 0; i < fileContents.length; i++) {
           	System.out.print(fileContents[i] + " ");
        	}
         * try (RandomAccessFile stmt = new RandomAccessFile(outputFile, "rw")) {
            stmt.writeChars("Standard sort on " + inputFile + "\n");
            stmt.writeChars("Cache Hits: " + String.valueOf(data.getCacheHits())
                + "\n");
            stmt.writeChars("Disk Reads: " + String.valueOf(data.getDiskReads())
                + "\n");
            stmt.writeChars("Disk Writes: " + String.valueOf(data
                .getDiskWrites()) + "\n");
            long execTime = endTime - beginTime;
            stmt.writeChars(String.valueOf(execTime));
            stmt.close();
        }
        catch (IOException e) {
            // Exception handling
            e.printStackTrace();
        }
         */
        
    }


    /*
     * Blake, so the project says this quick sort should be an external sort
     * which was the textbook 9.6 module you shouldn't need to change the code I
     * have since that is just the quicksort algorithm
     */
    public static void quickSort(BufferPool bp, int begin, int end) throws IOException {
        if (begin < end) {
            int partitionIndex = partition(bp, begin, end);

            quickSort(bp, begin, partitionIndex - 1);
            quickSort(bp, partitionIndex, end);
        }
    }


    public static int partition(BufferPool bp, int begin, int end) throws IOException {
        byte[] pivot = new byte[4];
        byte[] left = new byte[4];
        byte[] right = new byte[4];

        bp.getbytes(pivot, 4, end * 4);
        short pivotKey = ByteBuffer.wrap(pivot).getShort();

        int i = begin - 1;

        for (int j = begin; j < end; j++) {
            bp.getbytes(left, 4, j * 4);
            short key = ByteBuffer.wrap(left).getShort();

            if (key <= pivotKey) {
                i++;

                bp.getbytes(right, 4, i * 4);

                // Swap left <-> right
                bp.insert(left, 4, i * 4);
                bp.insert(right, 4, j * 4);
            }
        }

        // Final pivot swap
        bp.getbytes(left, 4, (i + 1) * 4);
        bp.insert(pivot, 4, (i + 1) * 4);
        bp.insert(left, 4, end * 4);

        return i + 1;
    }

}
