package PPCA;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrix;

public class PPCAModel {
	public DenseMatrix mean;
	public DenseMatrix W;
	public double sigma2;
	public Matrix recov;
	public Matrix cov;
	public int maxRating = 5;
	public int minRating = 1;
}