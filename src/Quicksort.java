import java.io.*;
import java.nio.ByteBuffer;

/**
 * The class containing the main method and a external sorting quicksort method.
 * Here assumed to be working with bufferpools, see BufferPool.java
 *
 * @author Kevin Dong
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

public class Quicksort {
    /**
     * @param args
     *            Command line parameters. See the project spec!!!
     */
    public static void main(String[] args) {

        // Checks for proper arguments to be passed to avoid null pointer and
        // i/o errors
        if (args.length != 3) {
            System.out.println(
                "Wrong number of command line args, please pass 2 files and the numbers of buffers");
        }
        if (Integer.valueOf(args[1]).intValue() > 20 && Integer.valueOf(args[1])
            .intValue() < 1) {
            System.out.println(
                "Wrong number of buffers, please input an integer between 1 and 20");
        }

        // variables used to access i/o files, construct the BufferPool, and a
        // stat object to track important data
        String inputFile = args[0];
        int numBuffers = Integer.valueOf(args[1]);
        String outputFile = args[2];
        BufferPool data = null;
        Statistics stat = null;

        // this try catch block opens the input data file and begins the process
        // of sorting data and processing data to be written to the stat file
        try (RandomAccessFile stmt = new RandomAccessFile(inputFile, "rw")) {

            // initializes two variables, stat to track performance data and
            // data to interface with the large input file to avoid excessive
            // memory calls
            stat = new Statistics(inputFile);
            data = new BufferPool(inputFile, numBuffers, stat);

            int numRecords = (int)stmt.length() / 4;

            // starts the clock to measure the time it takes to sort
            long start = System.currentTimeMillis();
            // sorting here
            quickSort(data, 0, numRecords - 1);
            // end of the sorting process, time to execute measured in
            // milliseconds
            long end = System.currentTimeMillis();

            // rewrites the inputfile's data to now be fully sorted
            data.flush();

            // Calculates sorting time, passes it to the stat object and now
            // write the performance data to the stat file
            stat.setExTime(end - start);
            stat.writeToFile(outputFile);
        }

        // if file is not present or something else in i/o has gone very wrong
        catch (IOException e) {
            // Exception handling
            e.printStackTrace();
        }
    }


    /**
     * QuickSort implementation using an external sorting paradigm so sort large
     * files of data. Note that the current implementation assumes the data is
     * readable to a byte format
     * 
     * @param bp
     *            bufferpool object which stores the file's data
     * @param begin
     *            first index of the unsorted portion of array
     * @param end
     *            last index of the unsorted portion of array
     * @throws IOException
     *             throws upon failure to acquire or write i/o sufficiently
     */
    public static void quickSort(BufferPool bp, int begin, int end)
        throws IOException {
        if (begin < end) {
            // creates a pivot to bisect array and now gives the index of that
            // pivot so array can be bisected here in quickSort
            int partitionIndex = partition(bp, begin, end);

            // begins sorting the left half of the array
            quickSort(bp, begin, partitionIndex - 1);
            // begins sorting the right half of the array
            quickSort(bp, partitionIndex + 1, end);
        }
    }


    /**
     * Takes a value as the pivot and places the rest of the array left or right
     * relative to the pivot. Returns the index of the pivot
     * 
     * @param bp
     *            bufferpool object which stores the file's data
     * @param begin
     *            first index of the unsorted portion of array
     * @param end
     *            last index of the unsorted portion of array
     * @return index of the pivot
     * @throws IOException
     *             throws upon failure to acquire or write i/o sufficiently
     */
    private static int partition(BufferPool bp, int begin, int end)
        throws IOException {
        // variables to store data that is being swapped
        byte[] pivot = new byte[4];
        byte[] left = new byte[4];
        byte[] right = new byte[4];

        // loads the 4 byte record as a pivot used to sort the array
        bp.getbytes(pivot, 4, end * 4);
        short pivotKey = ByteBuffer.wrap(pivot).getShort();

        int i = begin - 1;

        for (int j = begin; j < end; j++) {
            // loads the 4 byte record and extracts the short value from the
            // first 2 that is used in sorting
            bp.getbytes(left, 4, j * 4);
            short key = ByteBuffer.wrap(left).getShort();

            // this records belongs on the left side of the pivot
            if (key <= pivotKey) {
                i++;

                // Swaps the 4 bytes that begin at i and j
                bp.getbytes(right, 4, i * 4);
                bp.insert(left, 4, i * 4);
                bp.insert(right, 4, j * 4);
            }
        }
        // swaps the pivot the middle of the array
        bp.getbytes(left, 4, (i + 1) * 4);
        bp.insert(pivot, 4, (i + 1) * 4);
        bp.insert(left, 4, end * 4);

        // index of the pivot
        return i + 1;
    }

}
