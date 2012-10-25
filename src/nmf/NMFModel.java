package nmf;
import no.uib.cipr.matrix.*;

public class NMFModel {
	public Matrix W;
	public Matrix H;
	public int numConst = 0;
	public Matrix contW;
	public int dim = 5;
	public  int maxRating = 5;
	public  int minRating = 1;
}
