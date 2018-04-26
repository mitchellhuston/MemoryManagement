
public class MemoryBlock {

	private Integer ownerID; // ID of the memory request owner
	
	public MemoryBlock() {
		this.ownerID = null;
	}
	
	public void allocate(Integer newID) {
		this.ownerID = newID;
	}
	
	public void deallocate() {
		this.ownerID = null;
	}
	
	public boolean isAllocated() {
		return (this.ownerID != null);
	}
	
	public Integer getOwnerID() {
		return this.ownerID;
	}
}
