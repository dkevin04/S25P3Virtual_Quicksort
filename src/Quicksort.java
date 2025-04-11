import java.io.*;

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
        /*
         * Take file and construct char[] from it
         */
        String inputFile = args[0];
        String outputFile = args[2];

        try (InputStream inputStream = new FileInputStream(inputFile);
            OutputStream outputStream = new FileOutputStream(outputFile);) {
            int byteRead = -1;

            while ((byteRead = inputStream.read()) != -1) {
                ;
                /*
                 * Must write to output
                 * 
                 * name of the data file
                 * 
                 * The number of cache hits, or times your program found the
                 * data it needed in a buffer and did not have to go to the disk
                 * 
                 * The number of disk reads, or times your program had to read a
                 * block of data from disk into a buffer
                 * 
                 * The number of disk writes, or times your program had to write
                 * a block of data to disk from a buffer
                 * 
                 * The time that your program took to execute the Quicksort. Put
                 * two calls to the standard Java timing method
                 * System.currentTimeMillis() in your program, before and after
                 * you call the sort function. This method returns a long value.
                 * The difference between the two values will be the total
                 * runtime in milliseconds. You should ONLY time the sorting,
                 * and not the time to write the program output as described
                 * above
                 */
            }

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void quickSort(char[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex, end);
        }
    }


    public int partition(char[] arr, int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                char swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        char swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }
}
