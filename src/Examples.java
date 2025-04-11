import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Examples {
    
    final static int REC_BYTES_LENGTH = 4;
    
    public static void main(String[] args) {
        byte[] someTwoRecs = {3, 4, 8, 8, 2, 5, 9, 9}; 
        // rec0 key is bytes 3 4 combined into a short; value is 8 and 8
        // rec1 key is bytes 2 5 combined into a short; value is 9 and 9
        sortTwoRecords(someTwoRecs);
        for (byte b: someTwoRecs) {
            System.out.print(b + " ");
        }
        System.out.println();
        
        // Demo of copying a file....
        int randNum = (int)(System.currentTimeMillis() % 30);
        Path src = Paths.get("src", "Examples.java");// inside src folder is Examples.java
        Path dest = Paths.get("src", "ExamplesCopy" + randNum + ".java");
        try {
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied myself! Refresh project in eclipse to see...");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void sortTwoRecords(byte[] recs) {
        final int recLen = REC_BYTES_LENGTH; // just for a shorter name
        assert recs.length == 2*recLen;

        ByteBuffer bf = ByteBuffer.wrap(recs); 
        // ^^ creates a ByteBuffer using an existing byte array.
        // Note that telling bf to do actions will do them to the array, 
        // and actions that happen to that array will be visible by bf. 
        // bf is an object with a reference to that array.  
        
        short key0 = bf.getShort(); // record zero's key is at start (byte zero)
        short key1 = bf.getShort(1*recLen); // key1 begins at byte pos 4
        
        if (key1 < key0) {
            byte[] swapArea = new byte[recLen]; // a one record swap area
            System.arraycopy(recs, 0*recLen, swapArea, 0, recLen);
            System.arraycopy(recs, 1*recLen, recs, 0*recLen, recLen);
            System.arraycopy(swapArea, 0, recs, 1*recLen, recLen);
        }
        assert bf.getShort(0) < bf.getShort(1*recLen); // they are sorted!
    }
}