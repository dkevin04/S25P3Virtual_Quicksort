
public class Buffer {
	private int blockIndex;
	private byte[] data;
	private boolean dirty;
	
	public Buffer(int blockIndex, byte[] data) {
		this.blockIndex = blockIndex;
		this.data = data;
		this.dirty = false;
		
	}
	
	public byte[] getData() {
		return this.data;
	}
	
	public void setBlockIndex(int index) {
		this.blockIndex = index;
	}
	
	public int getBlockIndex() {
		return this.blockIndex;
	}
	
	public boolean isDirty() {
		return this.dirty;
	}
	
	public void setDirty(boolean cond) {
		this.dirty = cond;
	}
	
}
