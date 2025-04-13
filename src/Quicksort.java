import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        byte[] fileContents = new byte[1024];
        try (RandomAccessFile stmt = new RandomAccessFile(inputFile, "rw")) {
            stmt.read(fileContents);
            quickSort(fileContents, 0, 1023);
            stmt.close();
        }
        catch (IOException e) {
            // Exception handling
        }
        for (int i = 0; i < fileContents.length; i++) {
            System.out.print(fileContents[i] + " ");
        }
    }


    public static void quickSort(byte[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex, end);
        }
    }


    public static int partition(byte[] arr, int begin, int end) {
        byte pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                byte swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        byte swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }
}
