package prefix;

public class IntegerPlotPoint extends PlotPoint{
	private String value;
	private PPOptions options;
        
	public IntegerPlotPoint(int id){
		this.id = id;
        }
        public IntegerPlotPoint(int id, String value){
            this.id = id;
            this.value = value;
        }
        
	public IntegerPlotPoint(IntegerPlotPoint p){
		this.id = p.id;
	}
        
	public int compareTo(PlotPoint pi){
		if(this.id < ((IntegerPlotPoint)pi).id)
			return -1;
		else if(this.id > ((IntegerPlotPoint)pi).id)
			return 1;
		else
			return 0;
	}
        
	public String toString(){
		return ""+id;
	}
        
        public String value(){
            return value;
        }
        
        public void setOptions(PPOptions ppo){
            options = ppo;
        }
        
        public PPOptions getOptions(){
            return options;
        }
}