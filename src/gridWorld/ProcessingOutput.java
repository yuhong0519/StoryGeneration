package gridWorld;
import java.io.*;

public class ProcessingOutput {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String inputfile = "output.txt";
		String trainfile = "train.txt";
		String test1file = "test1.txt";
		String test2file = "test2.txt";
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(inputfile));
			BufferedWriter train = new BufferedWriter(new FileWriter(trainfile));
			BufferedWriter test1 = new BufferedWriter(new FileWriter(test1file));
			BufferedWriter test2 = new BufferedWriter(new FileWriter(test2file));
			String line = null;
			while((line=br.readLine()) != null){
				int t = line.indexOf("Training RMSE:");
				if(t >= 0){
					double v = Double.parseDouble(line.substring(15, 20));
					train.write(""+v+"\t");
					continue;
				}
				t = line.indexOf("New RMSE:");
				if(t >= 0){
					double v = Double.parseDouble(line.substring(10, 15));
					test1.write(""+v+"\t");
					continue;
				}
				
				t = line.indexOf("Test error");
				if(t >= 0){
					t = line.indexOf(".");
					double v = Double.parseDouble(line.substring(t-1, t+4));
					test2.write(""+v);
					test1.newLine();
					test2.newLine();
					train.newLine();
					continue;
				}
				
			}
			
			
			br.close();
			train.close();
			test1.close();
			test2.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
