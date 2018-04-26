import java.util.ArrayList;

public class MemoryManager {

	public ArrayList<MemoryBlock> memBlocks;
	private ArrayList<MemoryRequest> defRequests; // Saved list of currently deferred requests at this moment of time
	private Integer blockSize;
	
	public MemoryManager(Integer totalSize, Integer minBlockSize) {
		Integer numBlocks = totalSize / minBlockSize;
		
		memBlocks = new ArrayList<MemoryBlock>();
		for(int i = 0; i < numBlocks; i++) {
			this.memBlocks.add(new MemoryBlock());
		}
		this.defRequests = new ArrayList<MemoryRequest>();
		this.blockSize = minBlockSize;
	}
	
	public void request(MemoryRequest memReq) {
		if (memReq.isAllocate()) {
			System.out.println("Request ID " + memReq.getID() + ": allocate " + memReq.getRequestSize() + "bytes.");
			
			Integer availableLoc = null;
			if ((availableLoc = availableMemory(memReq.getRequestSize())) > -1) { // If available location is found
				allocate(availableLoc, memReq.getID(), memReq.getRequestSize());
				
				String hexLoc = String.format("%08X", (availableLoc*blockSize) & 0xFFFFF);
				System.out.println("   Success; addr 0x" + hexLoc);
			}
			else if (availableLoc == -1) { // If no available location found
				deferRequest(memReq);
			}
			else if (availableLoc == -2) { // If request for available memory is larger than total memory size
				System.out.println("Request ignored: request size larger than total memory available.");
			}
			else { // Unknown error -- shouldn't get here?
				System.out.println("Request ignored: Unknown error");
			}
		}
		else { // is Deallocate
			System.out.println("Request ID " + memReq.getID() + ": deallocate.");
			
			deallocate(memReq.getID());
			System.out.println("   Success.");
			checkDeferredRequests();
		}
	}

	private void deallocate(Integer id) {
		for (MemoryBlock memBlock : memBlocks) {
			if (memBlock.getOwnerID() != null && memBlock.getOwnerID().equals(id)) {
				memBlock.deallocate();
			}
		}
	}

	private void checkDeferredRequests() {
		for(int i = 0; i < defRequests.size(); i++) {
			
			MemoryRequest memReq = defRequests.get(i);
			Integer availableLoc = null;
			
			if ((availableLoc = availableMemory(memReq.getRequestSize())) > -1) { // If available location is found
				allocate(availableLoc, memReq.getID(), memReq.getRequestSize());
				defRequests.remove(i);
				
				String hexLoc = String.format("%08X", (availableLoc*blockSize) & 0xFFFFF);
				System.out.println("   Deferred request " + memReq.getID() + " allocated; addr 0x" + hexLoc);
				
				i--;
			}
		}
	}

	private void deferRequest(MemoryRequest memReq) {
		defRequests.add(memReq);
		System.out.println("Request deferred");
	}

	private void allocate(Integer availableLoc, Integer requestID, Integer requestSize) {
		Integer blocksRequired = getBlocksRequired(requestSize);
		for(int i = 0; i < blocksRequired; i++) {
			memBlocks.get(i+availableLoc).allocate(requestID);
		}
	}

	private Integer availableMemory(Integer requestSize) {
		Integer blocksRequired = getBlocksRequired(requestSize);
		if (blocksRequired > memBlocks.size()) {
			return -2; // Too large of a request
		}
		
		for(int i = 0; i <= memBlocks.size() - blocksRequired; i += blocksRequired) { // For each block range (of size blocksRequired)
			Boolean isAvailable = true;
			for(int j = 0; j < blocksRequired; j++) { // For each block within the block range
				if (memBlocks.get(i+j).isAllocated())
					isAvailable = false;
			}
			if (isAvailable)
				return i;
		}
		
		return -1; // No available location found
	}
	
	private Integer getBlocksRequired(Integer requestBytes) {
		Integer requiredSize = blockSize;
		while (requiredSize < requestBytes) {
			requiredSize *= 2;
		}
		return requiredSize / blockSize;
	}
}
