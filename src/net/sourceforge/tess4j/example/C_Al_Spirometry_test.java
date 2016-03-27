package net.sourceforge.tess4j.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//enum HO_PFT_PRE{ACTUAL,PRED,PRED_PER};

class PREonly
{
	public int[] IsnumericVector = new int[] { 0, 0, 0 };
	Double[] Pre_FVC_FEV1_FEF252 = new Double[] { 0.0, 0.0, 0.0 }; // 순서는
																	// actual,pred,percent
																	// 순서임.

}

public class C_Al_Spirometry_test extends CPattern
{

	static enum HO_PFT_PRE
	{
		PRE_ACTUAL, PRE_PREDICT, PRE_PREDICT_PERCENT
	};

	static enum HO_PFT_Names
	{
		FVC, FEV1, FVC_FVE1
	}

	static enum Result_Status
	{
		PERFECT, MIDDLE, POOR
	}

	private static final int PRE_ACTUAL = 0;
	private static final int PRE_PREDICT = 1;
	private static final int PRE_PREDICT_PERCENT = 2;
	
	private static final int FVC = 0;
	private static final int FEV1 = 1;
	private static final int FVCFEV1 = 2;
	private static final int FEF25_75 = 3;
	
	private static final int TRUE = 1;
	private static final int FALSE = 0;

	private Double[] Pre_FVC_FEV1_FEF25 = new Double[] { 0.0, 0.0, 0.0 };// 순서는
																			// actual,pred,percent
																			// 순서임.
	private Double[] PrePost_FVC_FEV1_FEF25 = new Double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	private Double[] PreMethacolinePost_FVC_FEV1_FEF25 = new Double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

	private Double[] FVC_Actual_Pred_Predperc = new Double[] { 0.0, 0.0, 0.0 };// 순서는
																				// actual,pred,percent
																				// 순서임.
	private Double[] FEV1_Actual_Pred_Predperc = new Double[] { 0.0, 0.0, 0.0 }; // 순서는
																					// actual,pred,percent
																					// 순서임.
	private Double[] FEV1FVC_Actual_Pred_Predperc = new Double[] { 0.0, 0.0, 0.0 }; // 순서는
																					// actual,pred,percent
																					// 순서임.
	private Double[] FEF25_75_Actual_Pred_Predperc = new Double[] { 0.0, 0.0, 0.0 }; // 순서는
																						// actual,pred,percent
																						// 순서임.

	private Double[][] PreValue = new Double[][] { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };

	private int[][] PreValueCheck = new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } };

	private static int[][] PreValueLastCheck = new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, };
	// private String rslt="";
	// private String parsedData="";

	private boolean ID_Date_parsed = false;
	private boolean FVC_parsed = false;
	private boolean FEV1_parsed = false;
	private boolean FEV1FVC_parsed = false;
	private boolean FEF25_75_parsed = false;

	private int falseCnt = 0;

	private int[] FEF25_75IsnumericVector = new int[] { 0, 0, 0 };

	/*
	 * private boolean []FEV1_IsnumericVector=new boolean[]{false,false,false};
	 * private boolean []FEV1FVC_IsnumericVector=new
	 * boolean[]{false,false,false}; private boolean
	 * []FEF25_75IsnumericVector=new boolean[]{false,false,false};
	 */

	C_Al_Spirometry_test(String txt)
	{

		super(txt);

	}

	public int getFalseCnt()
	{
		return falseCnt;
	}

	public boolean parsing(String fileNM)
	{
		// TODO Auto-generated method stub

		filename = fileNM;
		ID_Date_parsed = parsingID_Date();

		parsing_FVC();
		parsing_FEV1();
		parsing_FEV1FVC();

		FEF25_75_parsed = parsing_FEF25_75();

		FVC_FEV1_FEF25_dataCheck();

		int cnt = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			{
				if (PreValueCheck[i][j] == 0)
				{
					cnt++;
				}
			}
		falseCnt = cnt;

		if (cnt < 4)
		{

			System.out.println("-------파싱 이상없이 완료------------");
			System.out.println("ID : " + szpatientID + " date : " + szdate);

			System.out.print("FVC :");
			for (int i = 0; i < 3; i++)
			{
				System.out.print(PreValue[0][i] + " ");
			}
			System.out.println("");

			System.out.print("FEV1 :");
			for (int i = 0; i < 3; i++)
			{
				System.out.print(PreValue[1][i] + " ");
			}
			System.out.println("");
			System.out.print("FEV1/FVC :");
			for (int i = 0; i < 3; i++)
			{
				System.out.print(PreValue[2][i] + " ");
			}
			System.out.println("");
			System.out.print("FEF25_75 :");

			for (int i = 0; i < 3; i++)
			{
				System.out.print(Pre_FVC_FEV1_FEF25[i] + " ");
			}
		}
		return true;

		// 각 벡터들에 값이 전부 true인지 살펴보고 false 이면, 다른 값들과 비교해서 true 로 만들어주고,
		// 파일 옮기고, 파일에 데이터 쓰기까지 해야함.

	}

	public void parsing_FVC()
	{

		Matcher mcer = Pattern.compile(FVCpattern).matcher(rslt);

		PFT_PREonly pp = new PFT_PREonly();

		pp = valuecheck_and_insert(mcer, FVC);
		if (pp != null)
		{

			PreValue[FVC] = pp.Pre_FVC_FEV1_FEF252;
			PreValueCheck[FVC] = pp.IsnumericVector;

		}
	}

	//
	private PFT_PREonly valuecheck_and_insert(Matcher mcer, int fvc2)
	{
		String[] dataTmp = null;
		// TODO Auto-generated method stub

		PFT_PREonly ppr = null;

		if (mcer.find())
		{
			ppr = new PFT_PREonly();

			int groupCnt = mcer.groupCount() - 1;
			dataTmp = new String[groupCnt];
			for (int j = 2; j < groupCnt + 2; j++) // group 0은 전체 문자열, group 1은
													// FVC 같은 문자열, group 2부터가
													// 파싱하기 위한 데이터임.
			{
				System.out.println(mcer.group(j));
				dataTmp[j - 2] = replaceChar(mcer.group(j));
				if (j == groupCnt + 1)// when last for
				{
					String tmp = dataTmp[j - 2];

					tmp = checklastvalue(tmp);
					dataTmp[j - 2] = tmp;
				}
			}

			
			for (int i = 0; i < dataTmp.length; i++) // 숫자형 데이터만을 넣기 위해서 아래와 같이
														// 배열 및 벡터 형식으로 저장.
			{

				ppr.IsnumericVector[i] = isNumeric(dataTmp[i]);
				if (ppr.IsnumericVector[i] == TRUE) //
					ppr.Pre_FVC_FEV1_FEF252[i] = Double.parseDouble(dataTmp[i]);

			}

			// 값의 범위를 넘어서면, FALSE로 처리
			if (fvc2 != FVCFEV1)
			{

				if (ppr.Pre_FVC_FEV1_FEF252[PRE_ACTUAL] > 20)
					ppr.Pre_FVC_FEV1_FEF252[PRE_ACTUAL]=valueCheck_below100(ppr.Pre_FVC_FEV1_FEF252[PRE_ACTUAL]);
					//ppr.IsnumericVector[PRE_ACTUAL] = FALSE;

				if (ppr.Pre_FVC_FEV1_FEF252[PRE_PREDICT] > 20)
					ppr.Pre_FVC_FEV1_FEF252[PRE_PREDICT]=valueCheck_below100(ppr.Pre_FVC_FEV1_FEF252[PRE_PREDICT]);
					//ppr.IsnumericVector[PRE_PREDICT] = FALSE;

				if (ppr.Pre_FVC_FEV1_FEF252[PRE_PREDICT_PERCENT] < 20)
					ppr.IsnumericVector[PRE_PREDICT_PERCENT] = FALSE;

			} else
			{ // FVC/FEV1 은 최소 20부터 300사이의 값을 가진다.
				if (ppr.Pre_FVC_FEV1_FEF252[PRE_ACTUAL] < 20 || ppr.Pre_FVC_FEV1_FEF252[PRE_ACTUAL] > 300)
					ppr.IsnumericVector[PRE_ACTUAL] = FALSE;

				if (ppr.Pre_FVC_FEV1_FEF252[PRE_PREDICT] < 20 || ppr.Pre_FVC_FEV1_FEF252[PRE_PREDICT] > 300)
					ppr.IsnumericVector[PRE_PREDICT] = FALSE;

				if (ppr.Pre_FVC_FEV1_FEF252[PRE_PREDICT_PERCENT] < 20 || ppr.Pre_FVC_FEV1_FEF252[PRE_PREDICT_PERCENT] > 300)
					ppr.IsnumericVector[PRE_PREDICT_PERCENT] = FALSE;

			}

		}

		return ppr;
	}

	private String checklastvalue(String tmp)
	{
		// TODO Auto-generated method stub

		String lastvalue = "";
		Matcher mcer = Pattern.compile(lastValuePattern).matcher(tmp);
		if (mcer.find())
			lastvalue = mcer.group(1);

		return lastvalue;
	}

	public void FVC_FEV1_FEF25_dataCheck()
	{

		double[][] ctmpValue = new double[][] { { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };

		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			{
				ctmpValue[i][j] = PreValue[j][i];
			}

		int multi = 0;

		// 하나의 행에서 그냥 계산이 맞으면, 그행에 대한 3개의 셀에는 TRUE를 만듬.
		for (int k = 0; k < 3; k++)
		{
			// if (PreValue[k][0] == TRUE && PreValue[k][1] == TRUE &&
			// PreValue[k][2] == TRUE)
			if (PreValueCheck[k][0] == TRUE && PreValueCheck[k][1] == TRUE && PreValueCheck[k][2] == TRUE)
			{

				Double percent_cal = (PreValue[k][0] / PreValue[k][1]) * 100;

				if (percent_cal > 20)
					multi = 1;
				else
					multi = 10;

				// 계산된 값이 실제로 capture된것과 +-2 정도는 인정함.
				if (percent_cal * multi < (PreValue[k][2] * multi + 2) && percent_cal > (PreValue[k][2] * multi - 2))
				{
					System.out.println("correct.");

					for (int i = 0; i < 3; i++)
						PreValueCheck[k][i] = TRUE;

				} else
				{
					for (int i = 0; i < 3; i++)
						PreValueCheck[k][i] = FALSE;
				}
			}

		}

		// 전부 true 이면, 바로 리턴 해서 끝내면 됨.

		// 여기서부터는 값이 하나라도 없었을때의 처리.

		Double tmp = 0.0;
		multi = 0;
		// actual이 값이 없을경우 , pred, pred%가 값이 있는데,
		for (int i = 0; i < 3; i++)
		{
			if (PreValueCheck[i][PRE_ACTUAL] == FALSE && PreValueCheck[i][PRE_PREDICT] == TRUE && PreValueCheck[i][PRE_PREDICT_PERCENT] == TRUE)
			{
				PreValue[i][PRE_ACTUAL] = PreValue[i][PRE_PREDICT] * PreValue[i][PRE_PREDICT_PERCENT] * 0.01;

				if (i == 0)
				{
					ctmpValue[PRE_ACTUAL][i] = ctmpValue[PRE_ACTUAL][1] / ctmpValue[PRE_ACTUAL][2] * 100;
				}
				if (i == 1)
				{
					ctmpValue[PRE_ACTUAL][i] = ctmpValue[PRE_ACTUAL][0] * ctmpValue[PRE_ACTUAL][2] * 0.01;
				}

				if (i == 2)
				{
					ctmpValue[PRE_ACTUAL][i] = ctmpValue[PRE_ACTUAL][1] / ctmpValue[PRE_ACTUAL][0] * 100;
				}

				tmp = ctmpValue[PRE_ACTUAL][i];

				if (tmp > 20)
				{
					multi = 1;

				} else
					multi = 10;

				if (PreValue[i][PRE_ACTUAL] * multi < (tmp * multi + 2) && PreValue[i][PRE_ACTUAL] * multi > (tmp * multi - 2))
				{// validation이
					// 맞으면,
					for (int k = 0; k < 3; k++)
						PreValueCheck[i][k] = TRUE;
				}
			}

			if (PreValueCheck[i][PRE_ACTUAL] == TRUE && PreValueCheck[i][PRE_PREDICT] == FALSE && PreValueCheck[i][PRE_PREDICT_PERCENT] == TRUE)
			{

				PreValue[i][PRE_PREDICT] = PreValue[i][PRE_ACTUAL] / PreValue[i][PRE_PREDICT_PERCENT] * 100;

				if (i == 0)
				{
					ctmpValue[PRE_PREDICT][i] = ctmpValue[PRE_PREDICT][1] / ctmpValue[PRE_PREDICT][2] * 100;
				}
				if (i == 1)
				{

					ctmpValue[PRE_PREDICT][i] = ctmpValue[PRE_PREDICT][0] * ctmpValue[PRE_PREDICT][2] * 0.01;
				}

				if (i == 2)
				{
					ctmpValue[PRE_PREDICT][i] = ctmpValue[PRE_PREDICT][1] / ctmpValue[PRE_PREDICT][0] * 100;
				}

				tmp = ctmpValue[PRE_PREDICT][i];

				if (tmp > 20)
				{
					multi = 1;

				} else

					multi = 10;

				if (PreValue[i][PRE_PREDICT] * multi < (tmp * multi + 2) && PreValue[i][PRE_PREDICT] * multi > (tmp * multi - 2))
				{// validation이
					// 맞으면,
					for (int k = 0; k < 3; k++)
						PreValueCheck[i][k] = TRUE;
				}
			}

			if (PreValueCheck[i][PRE_ACTUAL] == TRUE && PreValueCheck[i][PRE_PREDICT] == TRUE && PreValueCheck[i][PRE_PREDICT_PERCENT] == FALSE)
			{

				PreValue[i][PRE_PREDICT_PERCENT] = PreValue[i][PRE_ACTUAL] / PreValue[i][PRE_PREDICT] * 100;
				if (i == 0)
				{
					ctmpValue[PRE_PREDICT_PERCENT][i] = ctmpValue[PRE_PREDICT_PERCENT][1] / ctmpValue[PRE_PREDICT_PERCENT][2] * 100;
				}
				if (i == 1)
				{
					ctmpValue[PRE_PREDICT_PERCENT][i] = ctmpValue[PRE_PREDICT_PERCENT][0] * ctmpValue[PRE_PREDICT_PERCENT][2] * 0.01;
				}

				if (i == 2)
				{
					ctmpValue[PRE_PREDICT_PERCENT][i] = ctmpValue[PRE_PREDICT_PERCENT][1] / ctmpValue[PRE_PREDICT_PERCENT][0] * 100;
				}

				tmp = ctmpValue[PRE_PREDICT_PERCENT][i];

				if (tmp > 20)
				{
					multi = 1;

				} else
					multi = 10;

				if (PreValue[i][PRE_PREDICT_PERCENT] * multi < (tmp * multi + 2) && PreValue[i][PRE_PREDICT_PERCENT] * multi > (tmp * multi - 2))
				{// validation이
					// 맞으면,
					for (int k = 0; k < 3; k++)
						PreValueCheck[i][k] = TRUE;
				}
			}

		}

		System.out.print("FVC :");
		for (int i = 0; i < 3; i++)
		{
			System.out.print(PreValue[0][i] + " ");
		}
		System.out.println("");

		System.out.print("FEV1 :");
		for (int i = 0; i < 3; i++)
		{
			System.out.print(PreValue[1][i] + " ");
		}
		System.out.println("");
		System.out.print("FEV1/FVC :");
		for (int i = 0; i < 3; i++)
		{
			System.out.print(PreValue[2][i] + " ");
		}
		System.out.println("");

		System.out.print("FVC :");
		for (int i = 0; i < 3; i++)
		{
			System.out.print(PreValueCheck[0][i] + " ");
		}
		System.out.println("");

		System.out.print("FEV1 :");
		for (int i = 0; i < 3; i++)
		{
			System.out.print(PreValueCheck[1][i] + " ");
		}
		System.out.println("");
		System.out.print("FEV1/FVC :");
		for (int i = 0; i < 3; i++)
		{
			System.out.print(PreValueCheck[2][i] + " ");
		}
		System.out.println("");

	}

	private Double[] FVC_FEV1_FEF25_dataCheck(int[] IsnumericVector2, Double[] pre_FVC_FEV1_FEF252, int Num)
	{

		// actual, pred, pred%의 3개 전부값이 있다면.. 계산해보고, 맞으면 ok 아니면, 어떤게 잘못된건지 모르니깐,
		// PreValueLastCheck 에 전부 false를 넣는다.
		if (IsnumericVector2[0] == TRUE && IsnumericVector2[1] == TRUE && IsnumericVector2[2] == TRUE)
		{

			Double percent_cal = (pre_FVC_FEV1_FEF252[0] / pre_FVC_FEV1_FEF252[1]) * 100;
			// 계산된 값이 실제로 capture된것과 +-2 정도는 인정함.
			if (percent_cal < (pre_FVC_FEV1_FEF252[2] + 2) && percent_cal > (pre_FVC_FEV1_FEF252[2] - 2))
			{
				System.out.println("correct.");
				for (int i = 0; i < PRE_PREDICT_PERCENT; i++)
					FEF25_75IsnumericVector[i] = TRUE;

			} else
			{
				for (int i = 0; i < PRE_PREDICT_PERCENT; i++)
				{
					FEF25_75IsnumericVector[i] = FALSE;
				}
			}
			return pre_FVC_FEV1_FEF252;
		}

		// 3개의 변수중 한개만 없을때, 계산이 가능하기 때문에 계산해서 넣는다.
		// 하지만, 나머지 두개가 정확히 뽑혔다고 확신 할수 없기때문에 마지막에 추가 검증으로 마무리.

		// actual이 값이 없을경우 , pred, pred%가 값이 있는데,
		if (IsnumericVector2[0] == FALSE && IsnumericVector2[1] == TRUE && IsnumericVector2[2] == TRUE)
		{
			pre_FVC_FEV1_FEF252[0] = pre_FVC_FEV1_FEF252[1] * pre_FVC_FEV1_FEF252[2] * 0.01;
			FEF25_75IsnumericVector = IsnumericVector2;
		} else if (IsnumericVector2[0] == TRUE && IsnumericVector2[1] == FALSE && IsnumericVector2[2] == TRUE)
		{ // pred 가 값이 없을경우, actual, pred% 값이 있는데,
			pre_FVC_FEV1_FEF252[1] = (pre_FVC_FEV1_FEF252[0] / pre_FVC_FEV1_FEF252[2]) * 100;
			FEF25_75IsnumericVector = IsnumericVector2;
		} else if (IsnumericVector2[0] == TRUE && IsnumericVector2[1] == TRUE && IsnumericVector2[2] == FALSE)
		{ // pred % 가 값이 없을경우, actual, pred가 값이 있는데,
			pre_FVC_FEV1_FEF252[2] = (pre_FVC_FEV1_FEF252[0] / pre_FVC_FEV1_FEF252[1]) * 100;
			FEF25_75IsnumericVector = IsnumericVector2;
		} else
		{

			FEF25_75IsnumericVector = IsnumericVector2;
		}
		// 위의 경우가 아닌경우는 2개이상이 값이 없는 경우이기 때문에 여기서 계산이 되지가 않음.

		return pre_FVC_FEV1_FEF252;
	}

	public void parsing_FEV1()
	{

		Matcher mcer = Pattern.compile(FEV1pattern).matcher(rslt);

		PFT_PREonly pp = new PFT_PREonly();

		pp = valuecheck_and_insert(mcer, FVC);
		if (pp != null)
		{

			PreValue[FEV1] = pp.Pre_FVC_FEV1_FEF252;
			PreValueCheck[FEV1] = pp.IsnumericVector;

		}

	}

	public void parsing_FEV1FVC()
	{

		Matcher mcer = Pattern.compile(FVCFEV1pattern).matcher(rslt);

		PFT_PREonly pp = new PFT_PREonly();

		pp = valuecheck_and_insert(mcer, FVCFEV1);
		if (pp != null)
		{

			PreValue[FVCFEV1] = pp.Pre_FVC_FEV1_FEF252;
			PreValueCheck[FVCFEV1] = pp.IsnumericVector;

		}

	}

	// FEF25_75per_actual
	public boolean parsing_FEF25_75()
	{
		Matcher mcer = Pattern.compile(FEF25_75pattern).matcher(rslt);

		PFT_PREonly pp = new PFT_PREonly();

		pp = valuecheck_and_insert(mcer, FEF25_75);
		if (pp != null)
		{

			FEF25_75IsnumericVector = pp.IsnumericVector;
			Pre_FVC_FEV1_FEF25 = pp.Pre_FVC_FEV1_FEF252;

		}

		if (mcer.groupCount() <= (3 + 1))
		{
			// 데이터가 하나만 없을때 계산해서 넣을수 있기때문에 계산해서 넣기.
			Pre_FVC_FEV1_FEF25 = FVC_FEV1_FEF25_dataCheck(FEF25_75IsnumericVector, Pre_FVC_FEV1_FEF25, FEF25_75);

			// 이때 실제 값들을 넣자..
			FVC_Actual_Pred_Predperc = Pre_FVC_FEV1_FEF25;
			Double percent_cal = (Pre_FVC_FEV1_FEF25[0] / Pre_FVC_FEV1_FEF25[1]) * 100;
			// 계산된 값이 실제로 capture된것과 +-2 정도는 인정함.
			if (percent_cal < (Pre_FVC_FEV1_FEF25[2] + 2) && percent_cal > (Pre_FVC_FEV1_FEF25[2] - 2))
			{
				System.out.println("FVC is correct.");
				return true;
			}

		}
		return false;
	}

	public void SaveResult(String dst, String txtfile, int successflag)
	{

		System.out.println("-------파싱 완료------------");
		System.out.println("ID : " + szpatientID + " date : " + szdate);
		System.out.print(filename + "," + PreValueCheck[0][0] + PreValueCheck[0][1] + PreValueCheck[0][2] + "," + szpatientID + "," + szdate + ","
				+ "FVC," + PreValue[0][0] + "," + PreValue[0][1] + "," + PreValue[0][2]);
		System.out.print(filename + "," + PreValueCheck[1][0] + PreValueCheck[1][1] + PreValueCheck[1][2] + "," + szpatientID + "," + szdate + ","
				+ "FEV1," + PreValue[1][0] + "," + PreValue[1][1] + "," + PreValue[1][2]);
		System.out.print(filename + "," + PreValueCheck[2][0] + PreValueCheck[2][1] + PreValueCheck[2][2] + "," + szpatientID + "," + szdate + ","
				+ "FEV1FVC," + PreValue[2][0] + "," + PreValue[2][1] + "," + PreValue[2][2]);
		System.out.print(filename + "," + FEF25_75IsnumericVector[0] + FEF25_75IsnumericVector[1] + FEF25_75IsnumericVector[2] + "," + szpatientID
				+ "," + szdate + "," + "FEV1FVC," + Pre_FVC_FEV1_FEF25[0] + "," + Pre_FVC_FEV1_FEF25[1] + "," + Pre_FVC_FEV1_FEF25[2]);

		String data = filename + "," + PreValueCheck[0][0] + PreValueCheck[0][1] + PreValueCheck[0][2] + "," + szpatientID + "," + szdate + ","
				+ "FVC," + PreValue[0][0] + "," + PreValue[0][1] + "," + PreValue[0][2] + "\r\n" + filename + "," + PreValueCheck[1][0]
				+ PreValueCheck[1][1] + PreValueCheck[1][2] + "," + szpatientID + "," + szdate + "," + "FEV1," + PreValue[1][0] + ","
				+ PreValue[1][1] + "," + PreValue[1][2] + "\r\n" + filename + "," + PreValueCheck[2][0] + PreValueCheck[2][1] + PreValueCheck[2][2]
				+ "," + szpatientID + "," + szdate + "," + "FEV1FVC," + PreValue[2][0] + "," + PreValue[2][1] + "," + PreValue[2][2] + "\r\n"
				+ filename + "," + FEF25_75IsnumericVector[0] + FEF25_75IsnumericVector[1] + FEF25_75IsnumericVector[2] + "," + szpatientID + ","
				+ szdate + "," + "FEF25_75," + Pre_FVC_FEV1_FEF25[0] + "," + Pre_FVC_FEV1_FEF25[1] + "," + Pre_FVC_FEV1_FEF25[2];

		BufferedWriter writer = null;

		// String filedir = dst + "\\PFT_data.txt";

		String txtfileSaving = "";

        txtfileSaving =  "c:\\" + txtfile;

		try
		{
			File directory = new File(txtfileSaving);

			if (directory.exists() == false)
			{
				writer = new BufferedWriter(new FileWriter(txtfileSaving));
				writer.write("FILENAME,SUCCESSFLAG,ID,EXECDATE,ACTUAL,PRED,PRED_PER\r\n");
				writer.close();
			}

			writer = new BufferedWriter(new FileWriter(txtfileSaving, true));

			writer.write(data);
			writer.newLine();
			writer.close();

		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Double valueCheck_below100(Double tmp)
	{
		//20이하일 값들이 100이상인것은 분명 소수점이 빠진것이다. 그래서 소수점 첫째자리를 만들어주고, 넣어보는것. 그리고 계산해보면 아니깐..
		
		String tmp2=Double.toString(tmp);
				tmp2=tmp2.replace(".", "");
		tmp2=tmp2.substring(0,1)+"."+tmp2.substring(1, tmp2.length());
		 
		return  Double.parseDouble(tmp2);
		
		
	}
	
	
}
