package pdms;

import java.io.FileWriter;
import java.io.IOException;

public class Worker {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		FileWriter fw = null;
		while(true) {
			System.out.println("write sth.");
			fw = new FileWriter("log/worker.txt");
			fw.write("0 ");
			fw.flush();
			fw.close();
			Thread.sleep(10000);
		} 
		
		
		


	}

}
