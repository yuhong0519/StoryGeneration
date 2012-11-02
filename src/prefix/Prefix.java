package prefix;
import java.util.*;




public class Prefix implements Comparable<Prefix>{
	public ArrayList<PlotPoint> itemList = new ArrayList<PlotPoint>();
        public int rating = -1;
        public PPOptions options = null;
//        for data process purpose. QuestionProcess.DataProcess
//        public int count = 1;
	public Prefix(int[] s){
		for(int i = 0; i < s.length; i++){
			itemList.add(new IntegerPlotPoint(s[i]));
		}
	}
	public Prefix(){
		
	}
        public Prefix(int s, PPOptions p){
		
		itemList.add(new IntegerPlotPoint(s));
		options = p;
	}
        public Prefix(double[] s){
		for(int i = 0; i < s.length; i++){
			itemList.add(new IntegerPlotPoint((int)s[i]));
		}
	}
//        deep copy
	public Prefix(Prefix p){
		if(p.itemList.size() > 0 && (p.itemList.get(0)) instanceof IntegerPlotPoint){
			for(int i = 0; i < p.itemList.size(); i++){
				this.itemList.add(new IntegerPlotPoint(((IntegerPlotPoint)p.itemList.get(i)).id));
			}
                        this.options = new PPOptions(p.options);
		}
		else{
			System.err.println("Unsupported Prefix construction error!!");
		}
	}
        
        
        
        public PlotPoint getLast(){
            return itemList.get(itemList.size()-1);
        }
        

        
        public void append(int s, PPOptions p){
            itemList.add(new IntegerPlotPoint(s));
            this.options = p;
        }
	
	public int compareTo(Prefix pi){
		if(this.itemList.size() < pi.itemList.size()){
			return -1;
		}
		else if(this.itemList.size() > pi.itemList.size()){
			return 1;
		}
		else{
			for(int i = 0; i < itemList.size(); i++){
				if(itemList.get(i).compareTo(pi.itemList.get(i)) != 0)
					return itemList.get(i).compareTo(pi.itemList.get(i));
			}
				
			return 0;
		}
	}
        
        public int numOfPlotPoints(){
            return itemList.size();
        }
	
}
