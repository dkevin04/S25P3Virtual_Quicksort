/**
 * Buffer holds an array of byte data that is data from a file. Additionally
 * tracks the index if the block so i/o and sorting work and if the buffer has
 * been written to the file.
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
public class Buffer {
    private int blockIndex;
    private byte[] data;
    private boolean dirty;

    /**
     * Constructs a buffer object
     * 
     * @param blockIndex
     *            index of the block of data being read in
     * @param data
     *            array with the file's data
     */
    public Buffer(int blockIndex, byte[] data) {
        this.blockIndex = blockIndex;
        this.data = data;
        this.dirty = false;

    }


    /**
     * Returns the byte array
     * 
     * @return byte array
     */
    public byte[] getData() {
        return this.data;
    }


    /**
     * Changes which block the buffer says it's "pointing" to
     * 
     * @param index
     *            index of new block
     */
    public void setBlockIndex(int index) {
        this.blockIndex = index;
    }


    /**
     * Returns the block index
     * 
     * @return block index
     */
    public int getBlockIndex() {
        return this.blockIndex;
    }


    /**
     * Gives if the buffer is dirty(not written to file) or not
     * 
     * @return true if the block is not written to the file
     */
    public boolean isDirty() {
        return this.dirty;
    }


    /**
     * Changes if the block is written to the file to false, upon reading a new
     * block is true
     * 
     * @param cond
     *            true if the block is not written to the file
     */
    public void setDirty(boolean cond) {
        this.dirty = cond;
    }

}
