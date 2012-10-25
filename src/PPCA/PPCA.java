package PPCA;
import tools.CommonUtil;
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.sparse.*;
import  java.text.SimpleDateFormat;
import java.util.*;
//import x.na.SparseMatrixBuilder;
//import java.io.*;

public class PPCA {
	
	private static double getStats(Matrix data, Matrix test, boolean dotest, Matrix mean, Matrix E, Matrix TX, Matrix TXX, Matrix TE, int[] missing){
		int N = data.numColumns();
		int D = data.numRows();
		CompColMatrix Cdata = (CompColMatrix)data;
		Matrix Eoi;
		Matrix muo;
		DenseMatrix Xi;
		Matrix Ti;
		int num = 0;
//		int[] rows = new int[D];
//		for(int i = 0; i < D; i++){
//			rows[i] = i;
//		}
		double RMSE = 0;
		int tnum = 0;
		
		for(int ind = 0; ind < N; ind++){
			if(ind % 10 == 0)
				System.out.print("It: "+ind+"  ");
			
			if(D-missing[ind] == 0){				
				continue;
			}
			num ++;
			int[] obs = new int[D-missing[ind]];			
			int[] jc = Cdata.getColumnPointers();
			int[] ir = Cdata.getRowIndices();
			int[] tjc = null;
			int[] tir = null;
			if(dotest){
				tjc = ((CompColMatrix)test).getColumnPointers();
				tir = ((CompColMatrix)test).getRowIndices();
			}
			int cm = 0;
			for(int i = jc[ind]; i < jc[ind+1]; i++){
				obs[cm++] = ir[i];
			}
			
//			Compute Eoi = Eo^{-1}
			Eoi = new DenseMatrix(D-missing[ind], D-missing[ind]);		
			DenseMatrix Eo = new DenseMatrix(Matrices.getSubMatrix(E, obs, obs));
			Eo.solve(Matrices.identity(D-missing[ind]), Eoi);

//			Compute TE. getSubMatrix return reference to TE
			Matrix temp = Matrices.getSubMatrix(TE, obs, obs);
			temp.add(Eoi);
			
//			Compute TX
			Ti = new DenseMatrix(Matrices.getSubMatrix(data, obs, new int[]{ind}));
			muo = new DenseMatrix(Matrices.getSubMatrix(mean, obs, new int[]{0}));
			Ti.add(-1, muo);
			Xi = new DenseMatrix(D-missing[ind], 1);
			Eoi.mult(Ti, Xi);
			temp = Matrices.getSubMatrix(TX, obs,new int[]{0});
			temp.add(Xi);
			
//			Compute TXX
			Matrix XiXit = new DenseMatrix(D-missing[ind], D-missing[ind]);
			Xi.transBmult(Xi, XiXit);
			temp = Matrices.getSubMatrix(TXX, obs, obs);
			temp.add(XiXit);
			
			if(dotest){
				int[] tobs = new int[tjc[ind+1] - tjc[ind]];
				tnum += tobs.length;
				for(int i = 0; i < tobs.length; i++){
					tobs[i] = tir[tjc[ind]+i];
				}
				Matrix mut = new DenseMatrix(Matrices.getSubMatrix(mean, tobs, new int[]{0}));
				Matrix tempmut = new DenseMatrix(tobs.length, 1);
				Matrix Eto = new DenseMatrix(Matrices.getSubMatrix(E, tobs, obs));
				Eto.mult(Xi, tempmut);
				mut.add(tempmut);
				Matrix testi = new DenseMatrix(Matrices.getSubMatrix(test, tobs, new int[]{ind}));
				mut.add(-1, testi);
				RMSE += Math.pow(mut.norm(Matrix.Norm.Frobenius), 2);
			}		
		}
		TX.scale(1.0/num);
		TXX.scale(1.0/num);
		TE.scale(1.0/num);
		if(dotest){
			RMSE /= tnum;
			RMSE = Math.pow(RMSE, 0.5);
		}
		return RMSE;
		
	}
	
//	ppca_test: PPCA test
//	data is a D by 1 vector containing missing values
	public static Matrix ppca_test(Matrix cov, Matrix mean, Matrix data){
		DenseMatrix predict = new DenseMatrix(data);
		
		int D = data.numRows();
		int numMissing = 0;
		for(int i = 0; i < D; i++){
			if(data.get(i, 0) == 0)
				numMissing++;
		}
		int[] hid = new int[numMissing];
		int[] obs = new int[D-numMissing];
		int indh = 0;
		int indo = 0;
		for(int i = 0; i < D; i++){
			if(data.get(i, 0) == 0){
				hid[indh++] = i;
			}
			else{
				obs[indo++] = i;
			}
		}
		Matrix th = Matrices.getSubMatrix(predict, hid, new int[]{0});
		Matrix meano = Matrices.getSubMatrix(mean, obs, new int[]{0});
		DenseMatrix temp = new DenseMatrix(Matrices.getSubMatrix(predict, obs, new int[]{0}));
		DenseMatrix temp2 = new DenseMatrix(D-numMissing, 1);
		DenseMatrix temp3 = new DenseMatrix(numMissing, 1);
		//th.set(Matrices.getSubMatrix(mean, hid, new int[]{0}));
		Matrix Eho = Matrices.getSubMatrix(cov, hid, obs);
		Matrix Eo = new DenseMatrix(Matrices.getSubMatrix(cov, obs, obs));
		Matrix Eoi = new DenseMatrix(D-numMissing, D-numMissing);
		Eo.solve(Matrices.identity(D-numMissing), Eoi);
		
		th.set(Matrices.getSubMatrix(mean, hid, new int[]{0}));
		temp.add(-1, meano);
		Eoi.mult(temp, temp2);
		Eho.mult(temp2, temp3);
		th.add(temp3);
		
		for(int i = 0; i < predict.numRows(); i++){
			if(predict.get(i, 0) > 5){
				predict.set(i, 0, 5);
			}
			else if(predict.get(i, 0) < 1){
				predict.set(i, 0, 1);
			}
		}
		
		return predict;
			
	}
	
//  ppca_train: PPCA for data
//	Input:
//		data: t_n, dimension D*N.
//		d: dimension of latent space.
//		W: D*d
//	Return: 
//		PPCAModel		
	public static PPCAModel ppca_train(Matrix data, Matrix test, Matrix mask) throws NotConvergedException{
		
		PPCAModel model = new PPCAModel();
		int maxiter = 100;
		int D = data.numRows();
		int N = data.numColumns();
//		double sigmasq = Math.abs(Math.random());
//		sigmasq = 1;
//		if(D == d){
//			sigmasq = 0;
//		}
		double threshold = 0.0001;
		boolean dotest = test == null ? false : true;
		
//		Initialization Process	
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		System.out.println("Initialization... "+sdf.format(new Date()));
//		Initialize mean 
		int[] missing = new int[N];
		DenseMatrix mean = new DenseMatrix(D, 1);
		DenseMatrix newmean = new DenseMatrix(D, 1);
		double tvalue;
		for(int i = 0; i < D; i++){
			double sum = 0; 
			int count = 0;
			for(int j = 0; j < N; j++){
				tvalue = data.get(i, j);
				if(tvalue != 0){
					count++;
					sum += tvalue;
				}
				else{
					missing[j] ++;
				}
				
			}
			if(count != 0)
				sum /= count;
			else
				sum = 0;
			mean.set(i, 0, sum);
		}
		newmean.set(mean);

		DenseMatrix E = new DenseMatrix(D, D);			
		DenseMatrix C = new DenseMatrix(D, D);
		DenseMatrix recov = new DenseMatrix(D, D);
		DenseMatrix temp2 = new DenseMatrix(D, D);
		DenseMatrix temp1 = new DenseMatrix(D, D);	
//		DenseMatrix TTt = new DenseMatrix(D, D);
		DenseMatrix ETX = new DenseMatrix(D, 1);
		
		DenseMatrix TX = new DenseMatrix(D, 1);
		DenseMatrix TXX = new DenseMatrix(D, D);
		DenseMatrix TE = new DenseMatrix(D, D);
		
//		Compute initial variance matrix E = W*W^T + sigmasq*I
//		W.transBmult(W, E);
//		MyUtil.addIdentity(sigmasq, E);
		
//		Compute initial log likelyhood
		double likold, lik;
		likold = 1000;
		

		
//		C is set to identity; E = C;
		CommonUtil.addIdentity(1, C);
		E.set(C);
		
		System.out.println("Start Iteration... " + sdf.format(new Date()));
		for(int i = 0; i < maxiter; i++){
			
//*********************		E Step  ******************************************
			System.out.println("E step: " + i + " " + sdf.format(new Date()));
			
			TX.zero();
			TXX.zero();
			TE.zero();
//			Compute liklyhood
			lik = getStats(data, test, dotest, newmean, C, TX, TXX, TE, missing);
//			
			
//*******************	M Step	************************************************
			System.out.println("\nM Step: " + i + " " + sdf.format(new Date()));
						
			if(dotest){
				System.out.println("RMSE on test set is: " + lik + "\n");
			}
			
			if(dotest){						
				if(lik > likold || Math.abs((lik-likold)/likold) < threshold){
					System.out.println("\nStop since new RMSE is: " + lik + "\n");
					break;
				}
				likold = lik;
			}
			
//			Compute E
			E.set(C);
			mean.set(newmean);
			

//			Compute newmean
			E.mult(TX, ETX);
			newmean.set(ETX);
			newmean.add(mean);
			
//		    Compute C
			TXX.mult(E, temp1);
			E.mult(temp1, C);
//			ETX.transBmult(ETX, temp1);
//			C.add(-1, temp1);
			C.add(E);
			E.mult(TE, temp1);
			temp1.mult(E, temp2);
			C.add(-1, temp2);
                        if(mask != null){
                            C.solve(Matrices.identity(D), temp1);
                            recov.zero();
                            for(int m = 0; m < D; m++){
                                    for(int mm = 0; mm < D; mm++){
                                            recov.set(m, mm, mask.get(m, mm)==0?0:temp1.get(m, mm));
                                    }
                            }
                            recov.solve(Matrices.identity(D), C);
                        }
						
		}
		
//		model.W = W;
//		model.sigma2 = sigmasq;
		model.mean = mean;
//		E.solve(Matrices.identity(D), recov);
		model.cov = E;
		
		return model;
		
	}
}
