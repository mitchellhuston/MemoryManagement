
public class MemoryRequest {

	private Integer id;
	private Integer bytes;
	private Boolean allocate; // If true, allocate. Else, deallocate
	
	
	public MemoryRequest(Integer i, Integer b, Boolean a) {
		this.id = i;
		this.bytes = b;
		this.allocate = a;
	}
	
	public Integer getID() {
		return this.id;
	}
	
	public Integer getRequestSize() {
		return this.bytes;
	}
	
	public Boolean isAllocate() {
		return allocate;
	}
}
