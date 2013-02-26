package nmf;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import tools.CommonUtil;

import no.uib.cipr.matrix.*;
public class NMF {
	
	public static double computeRMSE(Matrix A, Matrix B){
		double rmse = 0;
		int num = 0;
		for(int i = 0; i < A.numRows(); i++){
			for(int j = 0; j < A.numColumns(); j++){
				double t = A.get(i, j);
				if(t != 0){
					num++;
					rmse += (t-B.get(i, j))*(t-B.get(i, j));
				}
				
			}			
		}
		rmse /= num;
		rmse = Math.sqrt(rmse);
		return rmse;
	}
	
//	Train the NMF model, V = WH
	public static void nmf_train(Matrix V, Matrix test, NMFModel model){
		int maxIt = 400;
		int innerMaxIt = 200;
		DenseMatrix newFullV = new DenseMatrix(V);
		double rmse = 1000;
		double newrmse = 1000;
		double thresholdNMF = 0.000001;
//	Initialization	

		
		int[] rowind = new int[V.numRows()];
		for(int i = 0; i < V.numRows(); i++){
			rowind[i] = i;
		}
		
		if(model.H == null){
			model.H = Matrices.random( model.dim, V.numColumns());
			model.H.scale(2.0/model.H.numRows());
			
		}
		if(model.W == null){
			
			model.W = Matrices.random( V.numRows(), model.dim);
			model.W.scale(model.maxRating);
			if(model.contW != null){	
	
				for(int i = 0; i < model.contW.numRows(); i++){
					for(int j = 0; j < model.contW.numColumns(); j++){
						model.W.set(i, j, model.contW.get(i, j));
						
					}
				}
			}
			
		}
		DenseMatrix newW = new DenseMatrix(model.W);
		DenseMatrix newH = new DenseMatrix(model.H);
//		DenseMatrix Wt = new DenseMatrix(model.W.numColumns(), model.W.numRows());
//		DenseMatrix Ht = new DenseMatrix(model.H.numColumns(), model.H.numRows());
		DenseMatrix WH = new DenseMatrix(V.numRows(), V.numColumns());
		DenseMatrix WHHt = new DenseMatrix(model.W.numRows(), model.W.numColumns());
		DenseMatrix VHt = new DenseMatrix(model.W.numRows(), model.W.numColumns());
		DenseMatrix tempW = new DenseMatrix(model.W.numRows(), model.W.numColumns());
		DenseMatrix tempH = new DenseMatrix(model.H.numRows(), model.H.numColumns());
		DenseMatrix WtV = new DenseMatrix(model.H.numRows(), model.H.numColumns());
		DenseMatrix WtWH = new DenseMatrix(model.H.numRows(), model.H.numColumns());
		
//		Start iterations
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                int it = 0;
		for(it = 0; it < maxIt; it++){
//			E step
						
			System.out.println("E Step "+ it + "... "+sdf.format(new Date()) + "");
			
			newW.mult(newH, newFullV);
			double trainError = computeRMSE(V, newFullV);
			System.out.println("Training RMSE: " + trainError + ", at " + sdf.format(new Date()) + "");
			for(int i = 0; i < V.numColumns(); i++){
				for(int j = 0; j < V.numRows(); j++){
					
					if(V.get(j, i) != 0){
						newFullV.set(j, i, V.get(j,i));
					}
					else{
						double t = newFullV.get(j, i);
						if( t > model.maxRating){
							newFullV.set(j, i, model.maxRating);
							
						}else if( t < model.minRating){
							newFullV.set(j, i, model.minRating);
						}
					}
				}				
			}
			if(test != null){
//                            If the test matrix dimension is different, compute only the differenece in the test matrix
                            if(test.numRows() == newFullV.numRows()){
				newrmse = computeRMSE(test, newFullV);
                            }
                                    
                            else{
                                int[] rowi = new int[test.numRows()];
                                int[] coli  = new int[test.numColumns()];
                                for(int i = 0; i < rowi.length; i++){
                                    rowi[i] = i;
                                }
                                for(int i = 0; i < coli.length; i++){
                                    coli[i] = i;
                                }
                                newrmse = computeRMSE(test, Matrices.getSubMatrix(newFullV, rowi, coli));
                            }
                            System.out.println("Testing RMSE: " + newrmse + ", at " + sdf.format(new Date()) + "");
                            if(newrmse > rmse){
                                    System.out.println("Stop iteration since rmse is increasing!");
                                    break;
                            }
                            rmse = newrmse;
                          
			}
			model.W.set(newW);
			model.H.set(newH);
			
//			M Step....................
			System.out.println("M Step "+ it + "... "+sdf.format(new Date()) + "");
			for(int iit = 0; iit < innerMaxIt; iit++){
				
				newW.transAmult(newFullV, WtV);
				newW.mult(newH, WH);
				newW.transAmult(WH, WtWH);
				CommonUtil.dotMult(newH, WtV, tempH);
				CommonUtil.dotDiv(tempH, WtWH, tempH);

				
				
				newW.mult(newH, WH);
				WH.transBmult(newH, WHHt);
				newFullV.transBmult(newH, VHt);
				CommonUtil.dotMult(newW, VHt, tempW);
				CommonUtil.dotDiv(tempW, WHHt, tempW);
				if(model.contW != null){	
					for(int i = 0; i < model.contW.numRows(); i++){
						for(int j = 0; j < model.contW.numColumns(); j++){
							tempW.set(i, j, model.contW.get(i, j));
							
						}
					}
				}
				
				
//				if(MyUtil.euclidianDist(tempW, newW) <= thresholdNMF){
//					System.out.println("NMF converge in iteration "+ iit + ", at "+sdf.format(new Date()) + "");
//					break;
//				}
				if(CommonUtil.euclidianDist(tempH, newH) <= thresholdNMF){
					System.out.println("NMF converge in iteration "+ iit + ", at "+sdf.format(new Date()) + "");
					break;
				}
				newH.set(tempH);
				newW.set(tempW);
				
			}
			
		}

		if(it >= maxIt){
                    System.out.println("Max iteration number reached");
                }
		
		
	}

	public static DenseMatrix nmf_test(NMFModel model, Matrix test){
		DenseMatrix predict = new DenseMatrix(test.numRows(), test.numColumns());
		DenseMatrix newpredict = new DenseMatrix(predict);
		DenseMatrix temppredict = new DenseMatrix(predict);
		Matrix W = model.W;
		DenseMatrix H = new DenseMatrix(W.numColumns(), test.numColumns());
		DenseMatrix tempH = new DenseMatrix(W.numColumns(), test.numColumns());
		DenseMatrix WtV = new DenseMatrix(W.numColumns(), test.numColumns());
		DenseMatrix WtW = new DenseMatrix(W.numColumns(), W.numColumns());
		DenseMatrix WtWH = new DenseMatrix(W.numColumns(), test.numColumns());
		Matrices.random(H);
		predict.scale(2.0/H.numRows());
		DenseMatrix newH = new DenseMatrix(H);
		
		int maxIt = 400;
		int maxIIt = 200;
		double rmse = 1000;
		double newrmse = rmse;
		boolean debug = false;
		if(debug){
			System.out.println("Start Iteration...");
		}
		for(int it = 0; it < maxIt; it++){
			if(debug){
				if(it % 10 == 0)
					System.out.print("\nOuter Iteration: " + it + "\n");
			}
			W.mult(newH, newpredict);
			newrmse = computeRMSE(test, newpredict);
			
			for(int i = 0; i < test.numRows(); i++){
				for(int j = 0; j < test.numColumns(); j++){
					if(test.get(i, j) != 0){
						newpredict.set(i, j, test.get(i,j));
					}
					else{
						double t = newpredict.get(i, j);
						if (t > model.maxRating){
							newpredict.set(i, j, model.maxRating);
						}
						else if( t < model.minRating){
							newpredict.set(i, j, model.minRating);
						}
					}
				}
			}
			if(newrmse > rmse){
				break;
			}
			rmse = newrmse;
			H.set(newH);
			predict.set(newpredict);
//			Compute newH
			tempH.set(newH);
			double threshold = 0.0001;
			int iit;
			for(iit = 0; iit < maxIIt; iit++){
//				if(debug){
//					if(iit %10 == 0)
//						System.out.print("Inner Iteration: " + iit + "\t");
//				}
				W.transAmult(newpredict, WtV);
				W.transAmult(W, WtW);
				WtW.mult(newH, WtWH);
				CommonUtil.dotMult(newH, WtV, tempH);
				CommonUtil.dotDiv(tempH, WtWH, tempH);
				if(CommonUtil.euclidianDist(tempH, newH) < threshold){
					break;
				}
				newH.set(tempH);
			}
			if(debug && it % 10 == 0){
				System.out.print("" + iit + "\t");
			}
			
		}
		
		return predict;
		
	}
        
        public static void main(String[] args){
            DenseMatrix V = new DenseMatrix(10, 20);
            double missing = 0.8;
            Random r = new Random();
            for(int i = 0; i < V.numColumns(); i++){
                

                if(r.nextBoolean() == true){
                    for(int j = 0; j < V.numRows(); j++){
                        double t = r.nextDouble();
                        if(t < missing){
                            V.set(j, i, 0);
                        }
                        else if(j > V.numRows()/2.0){
                            V.set(j, i, 5);
                        }
                        else{
                            V.set(j, i, 1);
                        }
                    }
                }
                else{
                    for(int j = 0; j < V.numRows(); j++){
                        double t = r.nextDouble();
                        if(t < missing){
                            V.set(j, i, 0);
                        }
                        else if(j % 2 == 0){
                            V.set(j, i, 5);
                        }
                        else
                            V.set(j, i, 1);
                    }
                }
            }
            DenseMatrix test = new DenseMatrix(10,2);
            for(int i = 0; i < test.numColumns(); i++){
                if(i == 0){
                    for(int j = 0; j < test.numRows(); j++){
                        if(r.nextDouble() < missing){
                            test.set(j, i, 0);
                        }
                        if(j > test.numRows()/2.0){
                            test.set(j, i, 5);
                        }
                        else
                            test.set(j, i, 1);
                    }
                }
                else{
                    for(int j = 0; j < test.numRows(); j++){
                        if(r.nextDouble() < missing){
                            test.set(j, i, 0);
                        }
                        if(j % 2 == 0){
                            test.set(j, i, 5);
                        }
                        else
                            test.set(j, i, 1);
                    }
                }
                
            }
            NMFModel nmfm = new NMFModel();
            nmfm.dim = 2;
            nmf_train(V,  test,  nmfm);
            tools.CommonUtil.printObject(V, "v.txt");
            tools.CommonUtil.printObject(nmfm.W, "w.txt");
            tools.CommonUtil.printObject(nmfm.H, "H.txt");
            
            test = (new DenseMatrix(10,1));
            DenseMatrix test2 = (new DenseMatrix(10,1));
            for(int i = 0; i < test.numColumns(); i++){
                
                    for(int j = 0; j < test.numRows(); j++){
                        if(r.nextDouble() < missing){
                            test.set(j, i, 0);
                        }
                        else if(j > test.numRows()/2.0){
                            test.set(j, i, 5);
                        }
                        else
                            test.set(j, i, 1);
                    }
                   for(int j = 0; j < test.numRows(); j++){
                        if(r.nextDouble() < missing){
                            test2.set(j, i, 0);
                        }
                        else if(j % 2 == 0){
                            test2.set(j, i, 5);
                        }
                        else
                            test2.set(j, i, 1);
                    }
                
                
            }
            Matrix t1 = nmf_test(nmfm, test);
            Matrix t2 = nmf_test(nmfm, test2);
            tools.CommonUtil.printObject(t1, "t1.txt");
            tools.CommonUtil.printObject(t2, "t2.txt");
        }
}
