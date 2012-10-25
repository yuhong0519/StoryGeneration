package prefix;

public abstract class PlotPoint implements Comparable<PlotPoint>{
	public int id;
	public abstract int compareTo(PlotPoint pi);
	public abstract String toString();
}

