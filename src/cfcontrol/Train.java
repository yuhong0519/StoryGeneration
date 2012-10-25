package cfcontrol;


import tools.CommonUtil;
import java.io.FileReader;
import PPCA.*;
import no.uib.cipr.matrix.sparse.*;
import no.uib.cipr.matrix.*;
import x.na.SparseMatrixBuilder;
import nmf.*;

public class Train {

	/**
	 * @param args
	 */
	
	static double getPPCATestRMSE(Matrix cov, Matrix mean, Matrix data, CompColMatrix test){
		int N = data.numColumns();
		int dim = data.numRows();
		int[] rowind = new int[dim];
		for(int i = 0; i < dim; i++){
			rowind[i] = i;
		}
		int[] jc = test.getColumnPointers();
		int[] ir = test.getRowIndices();
		double[] testData = test.getData();	
		double mse = 0;
		int num = 0;
		for(int i = 0; i < N; i++){
			Matrix predict = PPCA.ppca_test(cov, mean, new DenseMatrix(Matrices.getSubMatrix(data, rowind, new int[]{i})));
			for(int j = jc[i]; j < jc[i+1]; j++){
				
					mse += Math.pow(testData[j] - predict.get(ir[j], 0), 2);
					num++;
				
			}
		}
		mse = mse / num;
		mse = Math.pow(mse, 0.5);
		return mse;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		algorithm=1: pPCA; algorithm=2: NMF
		int algorithm = 2;
		System.out.println("Loading data....");
		boolean addPrior = true;

		try{
			for(int i = 1; i < 2; i++){
//				CompColMatrix train = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("train"+i+".txt")));
//				CompColMatrix validate = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("validate"+i+".txt")));
//				CompColMatrix test1 = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("test1"+i+".txt")));
//				CompColMatrix test2 = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("test2"+i+".txt")));
				CompColMatrix train = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("train.txt")));
				CompColMatrix validate = new CompColMatrix(SparseMatrixBuilder.readCoordMatrix(new FileReader("validate.txt")));
				
				if(algorithm == 1){
					DenseMatrix mask = new DenseMatrix(CommonUtil.readData("Mask.txt", false));
					PPCAModel  model = PPCA.ppca_train(train, validate, null) ;
					CommonUtil.printObject(model.cov, "cov"+i+".txt");
					CommonUtil.printObject(model.mean, "mean"+i+".txt");
					
	//				DenseMatrix cov = new DenseMatrix(MyUtil.readData("cov1.txt", false));
	//				DenseMatrix mean = new DenseMatrix(MyUtil.readData("mean1.txt", false));
	//				System.out.println("\nTest error: "+getTestRMSE(cov, mean, test1, test2)+"\n");
					
//					System.out.println("\nTest error: "+getPPCATestRMSE(model.cov, model.mean, test1, test2)+"\n");
				}
				
				else if(algorithm == 2){
					NMFModel nmfModel = new NMFModel();
					nmfModel.dim = 5;
					if(addPrior){
						nmfModel.contW = new DenseMatrix(CommonUtil.readData("Prior.txt", false));
						nmfModel.numConst = nmfModel.contW.numColumns();
					}
					NMF.nmf_train(train, validate, nmfModel);
					CommonUtil.printObject(nmfModel.H, "H"+i+".txt");
					CommonUtil.printObject(nmfModel.W, "W"+i+".txt");
					
//                                        DenseMatrix predict = NMF.nmf_test(nmfModel.W, test1);
					
//					DenseMatrix W = new DenseMatrix(MyUtil.readData("W1.txt", false));
//					DenseMatrix predict = NMF.nmf_test(W, test1);

//					System.out.println("\nTest error: " + NMF.computeRMSE(test2, predict) + "\n");
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
