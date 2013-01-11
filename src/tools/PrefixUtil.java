package tools;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import prefix.AllOptions;
import prefix.IntegerPlotPoint;
import prefix.OptionItem;
import prefix.PPOptions;
import prefix.PlotPointLibrary;
import prefix.Prefix;

public class PrefixUtil {
        public static String ftpReadServer = "ftp://bunnih:xllyjj@scarecrow.cc.gt.atl.ga.us//ftp//";
	public static String ftpWriteServer = "ftp://bunnih:xllyjj@scarecrow.cc.gt.atl.ga.us//ftp//ratings//";
	public static String optionFile = "par/options.txt";
        public static String prefixListFile = "par/PrefixList.txt";
        public static String plotpointFile = "par/PlotPoints.txt";
        public static String storySpaceFile = "par/StorySpace.txt";
        public static String quizFile = "par/quiz.txt";
        public static String allRatingFile = "AllRatings.txt";
        public static String optionRatingFile = "OptionRatings.txt";
        
        public static String ratingWOptionTrainingFolder = "ratings_wOptionTraining";
        public static String optionTrainingFolder = "Options_training";
        public static String trainDataSplitFile = "svmData/optionDataSplit.txt";
        
        /**
         * Write prefix list beginning with prefix ID each line
         * @param pl 
         */
        public static void writePrefixList(ArrayList<Prefix> pl){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(prefixListFile));
			for(int i = 0; i < pl.size(); i++){
				bw.write(""+i+":");
				Prefix pi = pl.get(i);
				for(int j = 0; j < pi.itemList.size(); j++){
					bw.write("\t"+pi.itemList.get(j));
				}
				bw.newLine();
			}
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
        
        /**
         * Write prefix list beginning with prefix preference each line
         * @param pl prefix list
         * @param fileName  the file name
         */
        public static void writePreferencePrefixList(ArrayList<Prefix> pl, String fileName){
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			for(int i = 0; i < pl.size(); i++){
				Prefix pi = pl.get(i);
                                bw.write("" + pi.rating +":");
				for(int j = 0; j < pi.itemList.size(); j++){
					bw.write("\t"+pi.itemList.get(j));
				}
				bw.newLine();
			}
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
                
        /**
         * Write options id with preference to file
         * @param pl
         * @param fileName 
         */
        public static void writeOptionPreference(ArrayList<Prefix> pl, String fileName) {
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                        for(int i = 0; i < pl.size(); i++){
				Prefix pi = pl.get(i);
                                PPOptions ppo = pi.options;
                                if(ppo == null){
                                    continue;
                                }
				for(int j = 0; j < ppo.getAllOptions().size(); j++){
                                    bw.write("" + ppo.getAllOptions().get(j).getOID() + ":" + ppo.getAllOptions().get(j).getPreference() + "\t");
				}
				bw.newLine();
			}
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}
        
        public static void writeString2Server(String s, String fileName){
		try{
			//BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                        URL url = new URL(ftpWriteServer+fileName);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));
                        bw.write(s);
                        bw.close();
                }
                catch(Exception e){
			e.printStackTrace();
		}
        }
                
        public static void writeString(String s, String fileName){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                        bw.write(s);
                        bw.close();
                }
                catch(Exception e){
			e.printStackTrace();
		}
        }
                
        public static void writePreferencePrefixList2Server(ArrayList<Prefix> pl, String fileName) {
		try{
			//BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                        URL url = new URL(ftpWriteServer+fileName);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));
                        for(int i = 0; i < pl.size(); i++){
				Prefix pi = pl.get(i);
                                bw.write(""+pi.rating +":");
				for(int j = 0; j < pi.itemList.size(); j++){
					bw.write("\t"+pi.itemList.get(j));
				}
				bw.newLine();
			}
			bw.close();
//                        JOptionPane jo = new JOptionPane();
//                        jo.showMessageDialog(null,"Save ratings!");
		}
		catch(Exception e){
			e.printStackTrace();
                        JOptionPane jo = new JOptionPane();
                        jo.showMessageDialog(null,e.getMessage());
                        throw new RuntimeException("Cannot write to " + fileName);
		}		
	}
             
        public static void writeOptionPreference2Server(ArrayList<Prefix> pl, String fileName) {
		
		try{
			//BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                        URL url = new URL(ftpWriteServer+fileName);
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(url.openConnection().getOutputStream()));
                        for(int i = 0; i < pl.size(); i++){
				Prefix pi = pl.get(i);
                                PPOptions ppo = pi.options;
                                if(ppo == null){
                                    continue;
                                }
				for(int j = 0; j < ppo.getAllOptions().size(); j++){
                                    bw.write("" + ppo.getAllOptions().get(j).getOID() + ":" + ppo.getAllOptions().get(j).getPreference() + "\t");
				}
				bw.newLine();
			}
			bw.close();
//                        JOptionPane jo = new JOptionPane();
//                        jo.showMessageDialog(null,"Save ratings!");
		}
		catch(Exception e){
			e.printStackTrace();
                        JOptionPane jo = new JOptionPane();
                        jo.showMessageDialog(null,e.getMessage());
                        throw new RuntimeException("Cannot write to " + fileName);
		}		
	}
                
	
        public static ArrayList<Prefix> readStorySpace(String fileName){
		ArrayList<Prefix> prefixList = new ArrayList<Prefix>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				String[] t = line.split("\t");
				int[] list = new int[t.length];
				for(int i = 0; i < list.length; i++){
					list[i] = Integer.parseInt(t[i]);
				}
				for(int i = 0; i < list.length; i++){
					Prefix pi = new Prefix(Arrays.copyOf(list, i+1));
					if(Collections.binarySearch(prefixList, pi) < 0){
						prefixList.add(pi);
						Collections.sort(prefixList);
					}
					
				}
			}
		}
		catch(Exception e){
                    e.printStackTrace();
//                        System.out.println("Cannot read file " + fileName + " locally. Try to read it remotely");
                        try{
                            URL url = new URL(ftpReadServer+fileName);
                            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                            String line;
                            while((line = br.readLine()) != null){
				String[] t = line.split("\t");
				int[] list = new int[t.length];
				for(int i = 0; i < list.length; i++){
					list[i] = Integer.parseInt(t[i]);
				}
				for(int i = 0; i < list.length; i++){
					Prefix pi = new Prefix(Arrays.copyOf(list, i+1));
					if(Collections.binarySearch(prefixList, pi) < 0){
						prefixList.add(pi);
						Collections.sort(prefixList);
					}
					
				}
                            }
                            System.out.println("Read " + fileName + " successfully");
                        }
                        catch(Exception e2){
                            e2.printStackTrace();
                        }
		}
		
		return prefixList;
		
	}
//        read all the options
        public static AllOptions readOptions(String fileName){
            	AllOptions ao = new AllOptions();
		String line = null;
                try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			
                        int PPID = -1;
                        PPOptions ppo = null;
			while((line = br.readLine()) != null){
                            if(line.startsWith("//")){
                                continue;
                            }
                            if(line.startsWith("<")){
                                String tp = line.substring(1, line.indexOf(">"));
                                PPID = Integer.parseInt(tp);
                                if(ppo!=null){
                                    ao.addPPOptions(ppo);
                                }
                                ppo = new PPOptions(PPID);
                                continue;
                            }
                            String[] t = line.split(":::");
                            int iPP = Integer.parseInt(t[0]);
                            int OID = Integer.parseInt(t[1]);
                            OptionItem oi = new OptionItem(iPP, OID, t[2]);
//                            oi.setOID(OID);
                            ppo.add(oi);
			}
                        ao.addPPOptions(ppo);
		}
		catch(Exception e){
                    e.printStackTrace();
//                    System.out.println("Cannot read file " + fileName + " locally. Try to read it remotely");
                        try{
                            URL url = new URL(ftpReadServer+fileName);
                            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                            int PPID = -1;
                            PPOptions ppo = null;
                            while((line = br.readLine()) != null){
                            if(line.startsWith("//")){
                                continue;
                            }
                                if(line.startsWith("<")){
                                    String tp = line.substring(1, line.indexOf(">"));
                                    PPID = Integer.parseInt(tp);
                                    if(ppo!=null){
                                        ao.addPPOptions(ppo);
                                    }
                                    ppo = new PPOptions(PPID);
                                    continue;
                                }
                                String[] t = line.split(":::");
                                int iPP = Integer.parseInt(t[0]);
                                int OID = Integer.parseInt(t[1]);
                                ppo.add(new OptionItem(iPP, OID, t[2]));
                            }
                            ao.addPPOptions(ppo);
                            System.out.println("Read " + fileName + " successfully");
                        }
                        catch(Exception e2){
                            e2.printStackTrace();
                        }
                }
//                catch(Exception e){
//                    System.out.println(line);
//                }
                return ao;
            
        }
                
	public static void writeStorySpace(ArrayList<Prefix> pl, String filename){
		if(filename == null)
			filename = new String("GridStorySpace.txt");
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			for(int i = 0; i < pl.size(); i++){
//				bw.write(""+i+":");
				Prefix pi = pl.get(i);
				bw.write(""+pi.itemList.get(0));
				for(int j = 1; j < pi.itemList.size(); j++){
					bw.write("\t"+pi.itemList.get(j));
				}
				bw.newLine();
			}
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
//        Write option item list. Format: PrefixID \t PPID \t ItemID \t string
        public static void writeOptionItemList(ArrayList<OptionItem> oiList, String filename){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
                        bw.write("//OptionID: Prefix ID, PP ID, Indicated PP, Option ");
                        bw.newLine();
			for(int i = 0; i < oiList.size(); i++){
                                bw.write("" + oiList.get(i).getOID() + ":\t" + oiList.get(i).getPrefixID() + "\t" + oiList.get(i).getPPID() + "\t" + oiList.get(i).getIndicatedPP() + "\t" + oiList.get(i).getValue());
				bw.newLine();
			}
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}            
        }
        
        //        read option item list. Format: PrefixID \t PPID \t ItemID \t string
        public static void readOptionItemList(ArrayList<OptionItem> oiList, String filename){
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
//			for(int i = 0; i < oiList.size(); i++){
//                                bw.write("" + i + ":\t" + oiList.get(i).getPrefixID() + "\t" + oiList.get(i).getPPID() + "\t" + oiList.get(i).getOID() + "\t" + oiList.get(i).getValue());
//				bw.newLine();
//			}
                        String line;
                        while((line = br.readLine()) != null){
                            if(line.startsWith("//")){
                                continue;
                            }
                            String[] p = line.split("\t");
//                            assert(p.length == 5);
                            int prefixID = Integer.parseInt(p[1]);
                            int optionID = Integer.parseInt(p[0].substring(0, p[0].length()-1));
                            int PPID = Integer.parseInt(p[2]);
                            int indicatePP = Integer.parseInt(p[3]);
                            OptionItem oi = new OptionItem(indicatePP, optionID, p[4]);
                            oi.setPrefixID(prefixID);
                            oi.setPPID(PPID);
//                            oi.setOID(optionID);
                            oiList.add(oi);
                        }
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}            
        }
        
//       Read story ratings combined with option ratings.
        public static ArrayList<Prefix> readPrefixWOption(String prefixFile, String ratingFile){
            ArrayList<Prefix> pl = new ArrayList<Prefix>();
            		
            try{
                    BufferedReader pReader = new BufferedReader(new FileReader(prefixFile));
                    BufferedReader rReader = new BufferedReader(new FileReader(ratingFile));
                    
                    String line;
                    String line2;
                    while((line = pReader.readLine()) != null){
                            
                            String sep = "\t";
                            if(line.indexOf(sep) < 0)
                                sep = " ";
                            String[] t = line.split(sep);

                            int[] list = new int[t.length - 1];
                            for(int i = 1; i < t.length; i++){
                                    list[i-1] = Integer.parseInt(t[i]);
                            }
                            Prefix pi = new Prefix(list);
                            pi.rating = Integer.parseInt(t[0].substring(0, t[0].length()-1));
                            
                            line2 = rReader.readLine();
                            if(line2 == null){
                                System.out.println("Error! The prefix file and the rating file do not match!");
                                return pl;
                            }
                            if(!line2.isEmpty()){
                                
                                if(line2.indexOf(sep) < 0)
                                    sep = " ";
                                String[] t2 = line2.split(sep);
                                PPOptions ppo = new PPOptions(-1);
                                for(int i = 0; i < t2.length; i++){
                                    if(t2[i].isEmpty())
                                        continue;
                                    String[] tp = t2[i].split(":");
                                    int tpID = Integer.parseInt(tp[0]);
                                    int tpP = Integer.parseInt(tp[1]);
                                    ppo.add(new OptionItem(tpID, tpP));
                                    
                                }
                                pi.options = ppo;
                            }
                            
                            pl.add(pi);
                    }

            }
            catch(Exception e){
                e.printStackTrace();
            }
            return pl;
        }
	
//	type: 1 integer plot point	
	public static ArrayList<Prefix> readPrefixList(String filename, int type){
		ArrayList<Prefix> pl = new ArrayList<Prefix>();
		if(type == 1){
			try{
				BufferedReader br = new BufferedReader(new FileReader(filename));
				String line;
				while((line = br.readLine()) != null){
                                        String sep = "\t";
                                        if(line.indexOf(sep) < 0)
                                            sep = " ";
					String[] t = line.split(sep);
                                        
					int[] list = new int[t.length - 1];
					for(int i = 1; i < t.length; i++){
						list[i-1] = Integer.parseInt(t[i]);
					}
					Prefix pi = new Prefix(list);
                                        pi.rating = Integer.parseInt(t[0].substring(0, t[0].length()-1));
					pl.add(pi);
				}
	
			}
			catch(Exception e){
//				System.out.println("Cannot read file " + filename + " locally. Try to read it remotely");
                                try{
                                    URL url = new URL(ftpReadServer+filename);
                                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                                    String line;
                                    while((line = br.readLine()) != null){
                                            String sep = "\t";
                                            //System.out.println(line);
                                            if(line.indexOf(sep) < 0)
                                                sep = " ";
                                            String[] t = line.split(sep);

                                            int[] list = new int[t.length - 1];
                                            for(int i = 1; i < t.length; i++){
                                                    list[i-1] = Integer.parseInt(t[i]);
                                            }
                                            Prefix pi = new Prefix(list);
                                            pi.rating = Integer.parseInt(t[0].substring(0, t[0].length()-1));
                                            pl.add(pi);
                                    }
                                    System.out.println("Read " + filename + " successfully");
                                }
                                catch(Exception e2){
                                    e2.printStackTrace();
                                }
			}
		}
		
		return pl;
		
	}
        
        public static PlotPointLibrary readPlotPoints(String fileName){
            PlotPointLibrary ppl = new PlotPointLibrary();
            
            try{
                    BufferedReader br = new BufferedReader(new FileReader(fileName));
                    String line;
 
                    while((line = br.readLine()) != null){
                            String[] t = line.split(":::");
                            int id = Integer.parseInt(t[0]);  
                            IntegerPlotPoint ipp = new IntegerPlotPoint(id, t[1]);
                            ppl.add(ipp);
                    }
                    
            }
            catch(Exception e){
                //e.printStackTrace();
//                System.out.println("Cannot read file " + fileName + " locally. Try to read it remotely");
                    try{
                        URL url = new URL(ftpReadServer+fileName);
                        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                        String line;

                        while((line = br.readLine()) != null){
                            String[] t = line.split(":::");
                            int id = Integer.parseInt(t[0]);  
                            IntegerPlotPoint ipp = new IntegerPlotPoint(id, t[1]);
                            ppl.add(ipp);
                        }
                        System.out.println("Read " + fileName + " successfully");
                    }
                    catch(Exception e2){
                        e2.printStackTrace();
                    }
            }            
            
            
            return ppl;
                       
        }
        
        public static void addOptions2PlotPoints(PlotPointLibrary ppl, AllOptions ao){
            ArrayList<IntegerPlotPoint> ppList = ppl.getLibrary();
            for(int i = 0; i < ppList.size(); i++){
                IntegerPlotPoint tp = ppList.get(i);
                tp.setOptions(ao.getPPOptions(tp.id));
                
            }
            
        }
        
	public static void main(String args[]){
            PlotPointLibrary ppl = readPlotPoints(PrefixUtil.plotpointFile);
            AllOptions ao = readOptions(PrefixUtil.optionFile);
            addOptions2PlotPoints(ppl, ao);
            
            System.out.println(ppl.getPP(470).getOptions().getItemByIndicatedPP(475));
            
            
        }

}
