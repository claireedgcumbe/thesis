package pdms;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.RandomAccessFile;
import java.util.Vector;

import mediation.Mapping;


public class test1 {

	static String[] schemaNames = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
		"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", 
		"AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", 
		"AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ",
		"BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", 
		"BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ",
		"CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", 
		"CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ",
		"DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", 
		"DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ"};
	public test1(){
		
	}
	
	public static void main(String args[]) throws InterruptedException, IOException, ClassNotFoundException {
		
		Vector mappingsIn = new Vector();
		FileInputStream fis_mappings = new FileInputStream("pdms/mappings.log");
		ObjectInputStream ois_mappings = new ObjectInputStream(fis_mappings);

		int size = ois_mappings.readInt();
				
		
		for (int i = 0; i < size; i++){
			Mapping m = (Mapping)ois_mappings.readObject();
			System.out.println(m.printString());
			mappingsIn.addElement(m);
		}
		
		
	}
		
		

}

