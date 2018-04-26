// Authors: Mitchell Huston, Vincent Nguyen

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class prog3 {
	

	static Integer totalMemSize; // Total memory size
	static Integer minAllocSize; // Minimium allocation size possible
	static ArrayList<MemoryRequest> memRequests = new ArrayList<MemoryRequest>(); // List of all memory requests
	
	public static void main(String[] args)
	{
		
		String fileName = args[0];
		initSizesAndMemRequestsFrom(fileName);
		
		MemoryManager memMan = new MemoryManager(totalMemSize, minAllocSize);
		
		for(MemoryRequest memReq : memRequests) {
			memMan.request(memReq);
		}
	}

	private static void initSizesAndMemRequestsFrom(String fileName) {
		String line = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			// Initialize total mem size and minimum allocation size
			if ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				
				totalMemSize = Integer.parseInt(st.nextToken());
				minAllocSize = Integer.parseInt(st.nextToken());
			} 
			else {
				System.out.println("Invalid input file '" + fileName + "': Empty");
			}
			while((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				
				Integer id = Integer.parseInt(st.nextToken());
				
				String allocIdentifier = st.nextToken();
				Boolean alloc = false;
				Integer memSize = null;
				if (allocIdentifier.equals("+")) {
					alloc = true;
					memSize = Integer.parseInt(st.nextToken());
				}
					
				memRequests.add(new MemoryRequest(id, memSize, alloc));
			}
			
			reader.close();
		} catch (Exception ex) {
			System.out.println("Error reading file '" + fileName + "':\n" + ex);
		}
	}
}
