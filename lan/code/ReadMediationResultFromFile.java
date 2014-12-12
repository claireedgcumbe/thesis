package pdms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import mediation.GLAVMapping;
import mediation.MappingTable;
import mediation.Schema;

public class ReadMediationResultFromFile {
	
	public ReadMediationResultFromFile(){
		
	}
	
	public static void main(String args[]) throws IOException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream("log/Query/finalResult.log");
	    ObjectInputStream ois = new ObjectInputStream(fis);
		
		int size = ois.readInt();
		for (int i = 0; i < size; i++){ //first print glavMappings
			GLAVMapping glavmap = (GLAVMapping)ois.readObject();
			System.out.println(glavmap.printString());
	        
		}
		
		Schema s = (Schema) ois.readObject();
		System.out.println(s.printString());
/*		
		size = ois.readInt();
		for (int i = 0; i < size; i++){
			MappingTable mtset = (MappingTable)ois.readObject();
			mtset.printMappingTable();
		}
		
		size = ois.readInt();
		for(int i = 0; i < size; i++){
			String mappingInfo = (String)ois.readObject();
			System.out.println(mappingInfo);
		}
*/		
		ois.close();
	}
}
