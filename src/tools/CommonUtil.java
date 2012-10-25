package tools;
import java.io.*;
import java.util.ArrayList;
import no.uib.cipr.matrix.*;
import java.net.*;

public class CommonUtil {
	
//  trace: Trace for square matrix m

        
	public static double trace(Matrix m){
		int c = m.numColumns(), r = m.numRows();
		if(c != r){
			System.out.println("Try to get trace of non square matrix\n");
			return 0;
		}
		double tr = 0;
		for(int i = 0; i < c; i++){			
				tr += m.get(i, i);			
		}
		return tr;
		
	}
	
	public static void printObject(Matrix ma, String file){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < ma.numRows(); i++){
				for(int j = 0; j < ma.numColumns(); j++){
					bw.write(""+ma.get(i, j));
					bw.write("  ");
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        public static void printRemoteObject(Matrix ma, String file){
		try{
                        URL url = new URL(tools.PrefixUtil.ftpReadServer+file);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));
			for(int i = 0; i < ma.numRows(); i++){
				for(int j = 0; j < ma.numColumns(); j++){
					bw.write(""+ma.get(i, j));
					bw.write("  ");
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        
	
	public static void printObject(Vector ve, String file){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < ve.size(); i++){
				bw.write(""+ve.get(i));
				bw.write("  ");				
			}
			bw.newLine();
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        public static void printRemoteObject(Vector ve, String file){
		try{
                        URL url = new URL(tools.PrefixUtil.ftpReadServer+file);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));
	                
			//BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < ve.size(); i++){
				bw.write(""+ve.get(i));
				bw.write("  ");				
			}
			bw.newLine();
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void printObject(double[][] data, String file){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < data.length; i++){
				for(int j = 0; j < data[0].length; j++){
					bw.write(""+data[i][j]);
					bw.write("  ");
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
 
        public static void printRemoteObject(double[][] data, String file){
		try{
		        URL url = new URL(tools.PrefixUtil.ftpReadServer+file);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));
	    	
//                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < data.length; i++){
				for(int j = 0; j < data[0].length; j++){
					bw.write(""+data[i][j]);
					bw.write("  ");
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
                
        public static void printObject(int[][] data, String file){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < data.length; i++){
				for(int j = 0; j < data[0].length; j++){
					bw.write(""+data[i][j]);
					bw.write("  ");
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

        public static void printRemoteObject(int[][] data, String file){
		try{
			//BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			URL url = new URL(tools.PrefixUtil.ftpReadServer+file);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));
	
                        for(int i = 0; i < data.length; i++){
				for(int j = 0; j < data[0].length; j++){
					bw.write(""+data[i][j]);
					bw.write("  ");
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        public static void printObject(float[] data, String file){
		try{
			//BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
	
                        for(int i = 0; i < data.length; i++){
//				for(int j = 0; j < data[0].length; j++){
					bw.write(""+data[i]);
					bw.write("  ");
//				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        public static void printObject(double[][] data, String file, char sep){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(int i = 0; i < data.length; i++){
				for(int j = 0; j < data[0].length; j++){
					bw.write(""+data[i][j]);
					bw.write(sep);
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        public static void printRemoteObject(double[][] data, String file, char sep){
		try{
			//BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			URL url = new URL(tools.PrefixUtil.ftpReadServer+file);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));

                        for(int i = 0; i < data.length; i++){
				for(int j = 0; j < data[0].length; j++){
					bw.write(""+data[i][j]);
					bw.write(sep);
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        public static void printObject(ArrayList<float[]> data, String file){
		try{
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        for(int i = 0; i < data.size(); i++){
                    		for(int j = 0; j < data.get(i).length; j++){
					bw.write(""+data.get(i)[j]);
                                        if(j < data.get(i).length-1){
                                            bw.write("\t");
                                        }
//					bw.write(sep);
				}
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        public static void printStringList(ArrayList<String> data, String file){
		try{
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        for(int i = 0; i < data.size(); i++){
                    		bw.write(data.get(i));                                
				bw.newLine();
			}
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
        
        
        public static ArrayList<float[]> readDataArray(String filename){
            ArrayList<float[]> dataArray = new ArrayList<float[]>();
            try{
                    BufferedReader br = new BufferedReader(new FileReader(filename));
                    String line;
                    while((line = br.readLine()) != null){
                        line = line.trim();
                        String sep = "\t";
                        if(line.indexOf(sep) == -1){
                                sep = "  ";
                        }

                        String[] vs = line.split(sep);
                        float[] data = new float[vs.length];
                        for(int i = 0; i < vs.length; i++){
                                data[i] = Float.parseFloat(vs[i]);
                        }
                        dataArray.add(data);
                    }
                    

            }
            catch(IOException e){
                    e.printStackTrace();
            }
            return dataArray;
            
	}
                
        
	public static void printObject(double data, String file){
		try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(""+data);
				bw.newLine();
				bw.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
		
        public static void printRemoteObject(double data, String file){
		try{
			URL url = new URL(tools.PrefixUtil.ftpReadServer+file);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));
		        //BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(""+data);
			bw.newLine();
			bw.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
                
	public static double[] readVector(String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = br.readLine().trim();
			String sep = "\t";
			if(line.indexOf(sep) == -1){
				sep = "  ";
			}
			
			String[] vs = line.split(sep);
			double[] data = new double[vs.length];
			for(int i = 0; i < vs.length; i++){
				data[i] = Double.parseDouble(vs[i]);
				if(data[i] == 0){
					data[i] = Double.NaN;
				}
			}
			
			return data;
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
        
       public static ArrayList<String> readStringList(String filename){
            ArrayList<String> data = new ArrayList<String>();	
           try{
                        
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
                        while((line = br.readLine()) != null){
                            data.add(line);
                        }
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return data;
	}
                
	
//	M = M + alpha*I
	public static void addIdentity(double alpha, Matrix M){
		int rows = M.numRows();
		int cols = M.numColumns();
		if(rows != cols){
			System.out.println("Matrix is not square");
			return;
		}
		for(int i = 0; i < rows; i++){
			
				M.set(i, i, M.get(i, i)+alpha);
					
		}		
	}
	
//Read a matrix into two dimension double array. NaNflag is true if we need NaN for zero item.
	public static double[][] readData(String filename, boolean NaNflag){
		ArrayList<String> al = new ArrayList<String>();
		
		try{
			int x = 1, y = 0;
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = br.readLine().trim();
			String sep = "\t";
			if(line.indexOf(sep) == -1){
				sep = "  ";
			}
			
			String[] vs = line.split(sep);
			
			al.add(line);
			while((line = (br.readLine())) != null){
				line = line.trim();
				al.add(line);
				x++;
			}
			double[][] data = new double[x][];
			
			
			for(int i = 0; i < x; i++){
				line = al.get(i);
				vs = line.split(sep);
				y = vs.length;
				data[i] = new double[y];
				for(int j = 0; j < y; j++){
					data[i][j] = Double.parseDouble(vs[j]);
					if(data[i][j] == 0 && NaNflag){
						data[i][j] = Double.NaN;
					}
				}
				
			}
			return data;
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
		
	}
        
        //Read a matrix into two dimension double array. NaNflag is true if we need NaN for zero item.
	public static int[][] readIntData(String filename){
		ArrayList<String> al = new ArrayList<String>();
		
		try{
			int x = 1, y = 0;
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = br.readLine().trim();
			String sep = "\t";
			if(line.indexOf(sep) == -1){
				sep = "  ";
			}
                        if(line.indexOf(sep) == -1){
				sep = " ";
			}
			
			String[] vs = line.split(sep);
			
			al.add(line);
			while((line = (br.readLine())) != null){
				line = line.trim();
				al.add(line);
				x++;
			}
			int[][] data = new int[x][];
			
			
			for(int i = 0; i < x; i++){
				line = al.get(i);
				vs = line.split(sep);
				y = vs.length;
				data[i] = new int[y];
				for(int j = 0; j < y; j++){
					data[i][j] = Integer.parseInt(vs[j]);
					
				}
				
			}
			return data;
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
		
	}
	
//	return: (A*B*C)_ij
	public static double getMM3(Matrix A, Matrix B, Matrix C, int x, int y){
		int rows = B.numRows();
		int cols = B.numColumns();
		if(A.numColumns() != rows || cols != C.numRows()){
			System.out.println("Matrix multiply error");
			return 0;
		}
		double ret = 0;
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				ret += A.get(x, i) * B.get(i, j) * C.get(j, y);				
			}
		}
		
		return ret;
		
		
	}
	
//	C = A.*B
	public static void dotMult(DenseMatrix A, DenseMatrix B, DenseMatrix C){
		double[] adata = A.getData();
		double[] bdata = B.getData();
		double[] cdata = C.getData();
		for(int i = 0; i < adata.length; i++){
			cdata[i] = adata[i] * bdata[i];
		}
	}
	
//	C = A./B
	public static void dotDiv(DenseMatrix A, DenseMatrix B, DenseMatrix C){
		double[] adata = A.getData();
		double[] bdata = B.getData();
		double[] cdata = C.getData();
		for(int i = 0; i < adata.length; i++){
			cdata[i] = adata[i] / bdata[i];
		}
	}
	
//	Return |A-B|_F
	public static double euclidianDist(DenseMatrix A, DenseMatrix B){
		double dist = 0;
		double[] adata = A.getData();
		double[] bdata = B.getData();
		
		for(int i = 0; i < adata.length; i++){
			dist += (adata[i] - bdata[i])*(adata[i] - bdata[i]);
		}
		
		
		return Math.sqrt(dist);
	}
	
}
