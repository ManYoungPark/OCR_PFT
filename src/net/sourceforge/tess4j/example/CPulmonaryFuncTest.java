package net.sourceforge.tess4j.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//enum HO_PFT_PRE{ACTUAL,PRED,PRED_PER};

class PFT_PRE_POST
{
	public int[] IsnumericVector = new int[] { 0, 0, 0, 0, 0, 0 };
	Double[] Pre_FVC_FEV1_FEF252 = new Double[] { 0.0, 0.0, 0.0,0.0, 0.0, 0.0 }; // 순서는
																	// actual,pred,percent
																	// 순서임.

}

class PFT_PREonly
{
	public int[] IsnumericVector = new int[] { 0, 0, 0 };
	Double[] Pre_FVC_FEV1_FEF252 = new Double[] { 0.0, 0.0, 0.0 }; // 순서는
																	// actual,pred,percent
																	// 순서임.

}



public class CPulmonaryFuncTest extends CPattern
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
	
	private static final int POST_ACTUAL = 3;
	private static final int POST_PREDICT = 4;
	private static final int POST_CHNG = 5;
	
	
	
	
	private static final int FVC = 0;
	private static final int FEV1 = 1;
	private static final int FEV1FVC = 2;
	private static final int FEF25_75 = 3;
	
	
	
	private static final int FALSE = 0;
	private static final int TRUE = 1;
	private static final int NV = 5;
	private static final int CALCULATED = 9;
	
	private Double[] Pre_FVC_FEV1_FEF25 = new Double[] { 0.0, 0.0, 0.0,0.0, 0.0, 0.0 };// 순서는
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

	public Double[][] PreValue = new Double[][] { { 0.0, 0.0, 0.0 ,0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0,0.0, 0.0, 0.0  }, { 0.0, 0.0, 0.0,0.0, 0.0, 0.0  } };

	public  int[][] PreValueCheck = new int[][] { { 0, 0, 0 ,0, 0, 0 }, { 0, 0, 0,0, 0, 0  }, { 0, 0, 0 ,0, 0, 0 } };

	//private static int[][] PreValueLastCheck = new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 }, };
	// private String rslt="";
	// private String parsedData="";

	private boolean ID_Date_parsed = false;
	private boolean FVC_parsed = false;
	private boolean FEV1_parsed = false;
	private boolean FEV1FVC_parsed = false;
	private boolean FEF25_75_parsed = false;

	private int falseCnt_pre = 0;
	private int falseCnt_post = 0;

	private int[] FEF25_75IsnumericVector = new int[] { 0, 0, 0,0, 0, 0 };

	/*
	 * private boolean []FEV1_IsnumericVector=new boolean[]{false,false,false};
	 * private boolean []FEV1FVC_IsnumericVector=new
	 * boolean[]{false,false,false}; private boolean
	 * []FEF25_75IsnumericVector=new boolean[]{false,false,false};
	 */

	CPulmonaryFuncTest(String txt)
	{

		super(txt);

	}

	public int getFalseCnt_pre()
	{
		return falseCnt_pre;
	}
	public int getFalseCnt_post()
	{
		return falseCnt_post;
	}

	public boolean parsing(String fileNM,int type)
	{
		// TODO Auto-generated method stub

		
		filename = fileNM;
		ID_Date_parsed = parsingID_Date();

		if(type==Ho_patternPFTonly) //호흡기 내과 pre만 있는거.
		{

			parsing_FVC(FVCpattern);
			parsing_FEV1(FEV1pattern);
			parsing_FEV1FVC(FVCFEV1pattern);
			FEF25_75_parsed = parsing_FEF25_75(FEF25_75pattern);
			FVC_FEV1_FEF25_dataCheck();
			System.out.println("data체크1");
			FVC_FEV1_FEF25_dataCheck();
			System.out.println("data체크2");
			
			

		}
		
		
		if(type==Ho_patternPrePost) //호흡기 내과 pre만 있는거.
		{

			parsing_FVC(FVCpatternPrePost);
			parsing_FEV1(FEV1patternPrePost);
			parsing_FEV1FVC(FVCFEV1patternPrePost);
			FEF25_75_parsed = parsing_FEF25_75(FEF25_75patternPrePost);
			FVC_FEV1_FEF25_dataCheck();
			System.out.println("data체크1");
			FVC_FEV1_FEF25_dataCheck();
			System.out.println("data체크2");
			
			
			FVC_FEV1_FEF25_Post_dataCheck();
			
			int cnt=0;
			for (int i = 0; i < 3; i++)
				for (int j = 3; j < 6; j++)
				{
					if (PreValueCheck[i][j] == 0 | PreValueCheck[i][j] == 5 )
					{
						cnt++;
					}
				}
			falseCnt_post = cnt;
			
			
		}
		
		
		
		if(type==Al_patternSPIROMETRYonly) //알레르기 내과 spirometry만 있는거.
		{

			parsing_FVC(alFVCpattern);
			parsing_FEV1(alFEV1pattern);
			
			parsing_FEV1FVC(alFEV1FVCpattern2);
			int types=2; //같은 검사라도 FEV1FVC값이 3개인게 있고, 1개인게 있다. 그것을 확인해주는 작업 
			if(PreValueCheck[2][1]==FALSE && PreValueCheck[2][2]==FALSE)
			{
				parsing_FEV1FVC(alFEV1FVCpattern1);
				types=1;
			}
			
			
			
			
			
			//알레르기 내과 데이터는 데이터가 FVC/FEV1 데이터중 actual과 pred%가 없음.. pred 만 있는데, 데이터가 actual에 들어가기 때문에 pred로 이동시킴.
			
			//호흡기 내과는 actul, pred 순서인데, 알레르기 내과는 pred, test(actual) 순서여서 바꿔줘야함.
			int tmpPreValueCheck=FALSE;
			Double tmpPreValue=0.0;
			
			tmpPreValue=PreValue[0][0];
			PreValue[0][0]=PreValue[0][1];
			PreValue[0][1]=tmpPreValue;
			
			tmpPreValueCheck=PreValueCheck[0][0];
			PreValueCheck[0][0]=PreValueCheck[0][1];
			PreValueCheck[0][1]=tmpPreValueCheck;
			

			tmpPreValue=PreValue[1][0];
			PreValue[1][0]=PreValue[1][1];
			PreValue[1][1]=tmpPreValue;
			
			tmpPreValueCheck=PreValueCheck[1][0];
			PreValueCheck[1][0]=PreValueCheck[1][1];
			PreValueCheck[1][1]=tmpPreValueCheck;

			if (types == 2)
			{
				tmpPreValue=PreValue[2][0];
				PreValue[2][0]=PreValue[2][1];
				PreValue[2][1]=tmpPreValue;
				
				tmpPreValueCheck=PreValueCheck[2][0];
				PreValueCheck[2][0]=PreValueCheck[2][1];
				PreValueCheck[2][1]=tmpPreValueCheck;
			}
			
			
			FVC_FEV1_FEF25_dataCheck();
			FVC_FEV1_FEF25_dataCheck();
			
		}
		
		
		int cnt = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			{
				if (PreValueCheck[i][j] == 0 | PreValueCheck[i][j] == 5 )
				{
					cnt++;
				}
			}
		falseCnt_pre = cnt;

		if (cnt < 2)
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

	private void FVC_FEV1_FEF25_Post_dataCheck()
	{
		// TODO Auto-generated method stub
		

		int multi = 0;
		// 하나의 행에서 그냥 계산이 맞으면, 그행에 대한 3개의 셀에는 TRUE를 만듬.
		for (int k = 0; k < 3; k++)
		{
			if (PreValueCheck[k][POST_ACTUAL] == TRUE && PreValueCheck[k][POST_PREDICT] == TRUE && PreValueCheck[k][POST_CHNG] == TRUE)
			{
				Double post_pred = PreValue[k][PRE_PREDICT_PERCENT] + (PreValue[k][PRE_PREDICT_PERCENT] * PreValue[k][POST_CHNG] / 100);

				if (post_pred > 20)
					multi = 1;
				else
					multi = 10;

				// 계산된 값이 실제로 capture된것과 +-2 정도는 인정함.
				if (post_pred * multi < (PreValue[k][POST_PREDICT] * multi + 2) && post_pred > (PreValue[k][POST_PREDICT] * multi - 2))
				{
					System.out.println("correct.");
					PreValueCheck[k][PRE_PREDICT_PERCENT] = TRUE;
					PreValueCheck[k][POST_CHNG] = TRUE;

				} else
				// 어떤게 틀린지 모르니깐, 전부 NV로 해놓음..
				{
					PreValueCheck[k][PRE_PREDICT_PERCENT] = NV;
					PreValueCheck[k][POST_CHNG] = NV;

				}
			}
			
			
			Double FVCtmp=PreValue[FEV1][POST_ACTUAL] / PreValue[FEV1FVC][POST_ACTUAL] * 100;
			if (FVCtmp * multi < (PreValue[FVC][POST_ACTUAL] * multi + 2) && FVCtmp > (PreValue[FVC][POST_ACTUAL] * multi - 2))
			{
				System.out.println("correct.");
				PreValueCheck[FVC][POST_ACTUAL] = TRUE;
				PreValueCheck[FEV1][POST_ACTUAL] = TRUE;
				PreValueCheck[FEV1FVC][POST_ACTUAL] = TRUE;
				
			} else
			{
				PreValueCheck[FVC][POST_ACTUAL] = NV;
				PreValueCheck[FEV1][POST_ACTUAL] = NV;
				PreValueCheck[FEV1FVC][POST_ACTUAL] = NV;
			}

		}

		if (FEF25_75IsnumericVector[POST_ACTUAL] == TRUE && FEF25_75IsnumericVector[POST_PREDICT] == TRUE
				&& FEF25_75IsnumericVector[POST_CHNG] == TRUE)
		{
			Double post_pred = Pre_FVC_FEV1_FEF25[PRE_PREDICT_PERCENT]
					+ (Pre_FVC_FEV1_FEF25[PRE_PREDICT_PERCENT] * Pre_FVC_FEV1_FEF25[POST_CHNG] / 100);

			if (post_pred > 20)
				multi = 1;
			else
				multi = 10;

			// 계산된 값이 실제로 capture된것과 +-2 정도는 인정함.
			if (post_pred * multi < (Pre_FVC_FEV1_FEF25[POST_PREDICT] * multi + 2) && post_pred > (Pre_FVC_FEV1_FEF25[POST_PREDICT] * multi - 2))
			{
				System.out.println("FEF 25-75 correct.");

				FEF25_75IsnumericVector[PRE_PREDICT_PERCENT] = TRUE;
				FEF25_75IsnumericVector[POST_CHNG] = TRUE;

			} else
			// 어떤게 틀린지 모르니깐, 전부 NV로 해놓음..
			{
				System.out.println("FEF 25-75 is wrong!!!!!");
				FEF25_75IsnumericVector[PRE_PREDICT_PERCENT] = NV;
				FEF25_75IsnumericVector[POST_CHNG] = NV;

			}
		}
		
	}

	public void parsing_FVC(String fVCpattern)
	{

		Matcher mcer = Pattern.compile(fVCpattern).matcher(rslt);

//		PFT_PREonly pp = new PFT_PREonly();
		PFT_PRE_POST pp = new PFT_PRE_POST();

		pp = valuecheck_and_insert(mcer, FVC);
		
		if (pp != null)
		{

			PreValue[FVC] = pp.Pre_FVC_FEV1_FEF252;
			PreValueCheck[FVC] = pp.IsnumericVector;

		}
	}

	//
	private PFT_PRE_POST valuecheck_and_insert(Matcher mcer, int fvc2)
	{
		String[] dataTmp = null;
		// TODO Auto-generated method stub

		PFT_PRE_POST ppr = null;

		if (mcer.find())
		{
			ppr = new PFT_PRE_POST();

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

			/* if(dataTmp.length>4)  //pre post 데이터일때,
			 {
				 PFT_PRE_POST ppr=null;
				 
			 }
			*/
			for (int i = 0; i < dataTmp.length; i++) // 숫자형 데이터만을 넣기 위해서 아래와 같이
														// 배열 및 벡터 형식으로 저장.
			{

				ppr.IsnumericVector[i] = isNumeric(dataTmp[i]);
				if (ppr.IsnumericVector[i] == TRUE) //
					ppr.Pre_FVC_FEV1_FEF252[i] = Double.parseDouble(dataTmp[i]);

			}

			// 값의 범위를 넘어서면, FALSE로 처리
			if (fvc2 != FEV1FVC)
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

		int multi = 0;
		// 하나의 행에서 그냥 계산이 맞으면, 그행에 대한 3개의 셀에는 TRUE를 만듬.
		for (int k = 0; k < 3; k++)
		{
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
					{
						PreValueCheck[k][i] = TRUE;
					}

				} else
				// 어떤게 틀린지 모르니깐, 전부 NV로 해놓음..
				{
					for (int i = 0; i < 3; i++)
						PreValueCheck[k][i] = NV;
				}
			}

		}

		// 전부 true 이면, 바로 리턴 해서 끝내면 됨.

		// 여기서부터는 값이 하나라도 없었을때의 처리.

		Double tmp = 0.0, tmp3 = 0.0;

		multi = 0;

		// 행으로 먼저 체크함.
		for (int i = 0; i < 3; i++)
		{
			if (PreValueCheck[i][PRE_ACTUAL] != NV && PreValueCheck[i][PRE_PREDICT] != NV && PreValueCheck[i][PRE_PREDICT_PERCENT] != NV)
			{

				// actual이 값이 없을경우 , pred, pred%가 값이 있는데,
				if ((PreValueCheck[i][PRE_ACTUAL] == FALSE | PreValueCheck[i][PRE_ACTUAL] == CALCULATED | PreValueCheck[i][PRE_ACTUAL] == NV)
						&& PreValueCheck[i][PRE_PREDICT] != FALSE && PreValueCheck[i][PRE_PREDICT_PERCENT] != FALSE)
				{
					tmp3 = PreValue[i][PRE_PREDICT] * PreValue[i][PRE_PREDICT_PERCENT] * 0.01;
					PreValueCheck[i][PRE_ACTUAL] = CALCULATED;

					if (i == FVC)// FVC=FEV1/FEV1FVC*100
						tmp = PreValue[FEV1][PRE_ACTUAL] / PreValue[FEV1FVC][PRE_ACTUAL] * 100;
					if (i == FEV1)// FEV1=FVC*FEV1FVC/100
						tmp = PreValue[FVC][PRE_ACTUAL] * PreValue[FEV1FVC][PRE_ACTUAL] / 100;
					if (i == FEV1FVC)// FEV1FVC=FEV1/FVC*100
						tmp = PreValue[FEV1][PRE_ACTUAL] / PreValue[FVC][PRE_ACTUAL] * 100;

					if (tmp > 20)
						multi = 1;
					else
						multi = 10;

					if (tmp3 * multi < (tmp * multi + 2) && tmp3 * multi > (tmp * multi - 2))
					{// validation이
						// 맞으면, calculated에서 TRUE로 변경.
						PreValue[i][PRE_ACTUAL] = tmp3;

						for (int k = 0; k < 3; k++)
						{
							PreValueCheck[i][k] = TRUE;
							// PreValueCheck[k][0] = TRUE;
							PreValueCheck[k][PRE_ACTUAL] = TRUE;
						}
					} else
					{ // 계산이 맞지 않는다면, 두개 계산한것중에서 reference range 안에들어가는거 입력
						if (i == 0 | i == 1)
						{
							if (tmp3 > 1.0 && tmp3 < 12.0)
							{
								PreValue[i][PRE_ACTUAL] = tmp3;
								PreValueCheck[i][PRE_ACTUAL] = NV;
							} else if (tmp > 1.0 && tmp < 12.0)
							{
								PreValue[i][PRE_ACTUAL] = tmp;
								PreValueCheck[i][PRE_ACTUAL] = NV;
							} else
							{

								PreValue[i][PRE_ACTUAL] = 0.0;
								PreValueCheck[i][PRE_ACTUAL] = FALSE;
							}
						}
						if (i == 2)
						{
							if (tmp3 > 50 && tmp3 < 150.0)
							{
								PreValue[i][PRE_ACTUAL] = tmp3;
								PreValueCheck[i][PRE_ACTUAL] = NV;
							} else if (tmp > 50.0 && tmp < 150.0)
							{
								PreValue[i][PRE_ACTUAL] = tmp;
								PreValueCheck[i][PRE_ACTUAL] = NV;
							} else
							{
								PreValue[i][PRE_ACTUAL] = 0.0;
								PreValueCheck[i][PRE_ACTUAL] = FALSE;
							}

						}
					}

				}

				// predict 이 없는경우
				if (PreValueCheck[i][PRE_ACTUAL] != FALSE
						&& (PreValueCheck[i][PRE_PREDICT] == FALSE | PreValueCheck[i][PRE_PREDICT] == CALCULATED | PreValueCheck[i][PRE_PREDICT] == NV)
						&& PreValueCheck[i][PRE_PREDICT_PERCENT] != FALSE)
				{

					tmp3 =PreValue[i][PRE_ACTUAL] / PreValue[i][PRE_PREDICT_PERCENT] * 100;
					PreValueCheck[i][PRE_PREDICT] = CALCULATED;

					if (i == FVC)// FVC=FEV1/FEV1FVC*100
						tmp = PreValue[FEV1][PRE_PREDICT] / PreValue[FEV1FVC][PRE_PREDICT] * 100;
					if (i == FEV1)// FEV1=FVC*FEV1FVC/100
						tmp = PreValue[FVC][PRE_PREDICT] * PreValue[FEV1FVC][PRE_PREDICT] / 100;
					if (i == FEV1FVC)// FEV1FVC=FEV1/FVC*100
						tmp = PreValue[FEV1][PRE_PREDICT] / PreValue[FVC][PRE_PREDICT] * 100;

					if (tmp > 20)
						multi = 1;
					else
						multi = 10;

					if (tmp3 * multi < (tmp * multi + 2) && tmp3 * multi > (tmp * multi - 2))
					{// validation이
						// 맞으면, calculated에서 TRUE로 변경.
						for (int k = 0; k < 3; k++)
						{
							PreValueCheck[i][k] = TRUE;
							// PreValueCheck[k][1] = TRUE;
							PreValueCheck[k][PRE_PREDICT] = TRUE;

						}
					} else
					{ // 계산이 맞지 않는다면, 두개 계산한것중에서 reference range 안에들어가는거 입력
						if (i == 0 | i == 1)
						{
							if (tmp3 > 1.0 && tmp3 < 12.0)
							{
								PreValue[i][PRE_PREDICT] = tmp3;
								PreValueCheck[i][PRE_PREDICT] = NV;
							} else if (tmp > 1.0 && tmp < 12.0)
							{
								PreValue[i][PRE_PREDICT] = tmp;
								PreValueCheck[i][PRE_PREDICT] = NV;
							} else
							{
								PreValue[i][PRE_PREDICT] = 0.0;
								PreValueCheck[i][PRE_PREDICT] = FALSE;

							}

						}
						if (i == 2)
						{
							if (tmp3 > 50 && tmp3 < 150.0)
							{
								PreValue[i][PRE_PREDICT] = tmp3;
								PreValueCheck[i][PRE_PREDICT] = NV;
							} else if (tmp > 50.0 && tmp < 150.0)
							{
								PreValue[i][PRE_PREDICT] = tmp;
								PreValueCheck[i][PRE_PREDICT] = NV;
							} else
							{
								PreValue[i][PRE_PREDICT] = 0.0;
								PreValueCheck[i][PRE_PREDICT] = FALSE;

							}

						}

					}
				}
				//
				// predict percent가 없는경우..
				if (PreValueCheck[i][PRE_ACTUAL] != FALSE
						&& PreValueCheck[i][PRE_PREDICT] != FALSE
						&& (PreValueCheck[i][PRE_PREDICT_PERCENT] == FALSE | PreValueCheck[i][PRE_PREDICT_PERCENT] == CALCULATED | PreValueCheck[i][PRE_PREDICT_PERCENT] == NV))
				{

					tmp3 = PreValue[i][PRE_ACTUAL] / PreValue[i][PRE_PREDICT] * 100;
					PreValueCheck[i][PRE_PREDICT_PERCENT] = CALCULATED;

					if (i == FVC)// FVC=FEV1/FEV1FVC*100
						tmp = PreValue[FEV1][PRE_PREDICT_PERCENT] / PreValue[FEV1FVC][PRE_PREDICT_PERCENT] * 100;
					if (i == FEV1)// FEV1=FVC*FEV1FVC/100
						tmp = PreValue[FVC][PRE_PREDICT_PERCENT] * PreValue[FEV1FVC][PRE_PREDICT_PERCENT] / 100;
					if (i == FEV1FVC)// FEV1FVC=FEV1/FVC*100
						tmp = PreValue[FEV1][PRE_PREDICT_PERCENT] / PreValue[FVC][PRE_PREDICT_PERCENT] * 100;

					if (tmp > 20)
						multi = 1;
					else
						multi = 10;

					if (tmp3 * multi < (tmp * multi + 2) && tmp3 * multi > (tmp * multi - 2))
					{// validation이
						// 맞으면, calculated에서 TRUE로 변경.
						for (int k = 0; k < 3; k++)
						{
							PreValueCheck[i][k] = TRUE;
							// PreValueCheck[k][i] = TRUE;
							PreValueCheck[k][PRE_PREDICT_PERCENT] = TRUE;
						}
					} else
					{ // 계산이 맞지 않는다면, 두개 계산한것중에서 reference range 안에들어가는거 입력

						if (tmp3 > 50 && tmp3 < 150.0)
						{
							PreValue[i][PRE_PREDICT_PERCENT] = tmp3;
							PreValueCheck[i][PRE_PREDICT_PERCENT] = NV;
						} else if (tmp > 50.0 && tmp < 150.0)
						{
							PreValue[i][PRE_PREDICT_PERCENT] = tmp;
							PreValueCheck[i][PRE_PREDICT_PERCENT] = NV;
						} else
						{
							PreValue[i][PRE_PREDICT_PERCENT] = 0.0;
							PreValueCheck[i][PRE_PREDICT_PERCENT] = FALSE;

						}

					}
				}

			}// end of if

		}// end of for

	

		double tmp2 = 0;
		for (int i = 0; i < 3; i++)
		{
			// actual이 값이 없을경우 , pred, pred%가 값이 있는데,
			// i==0일때는 actual 값이 없는 경우 i==1이면 pred 값이 없는 경우 , i==2 이면, pred% 가
			// 없는경우
			if ((PreValueCheck[FVC][i] == FALSE | PreValueCheck[FVC][i] == CALCULATED | PreValueCheck[FVC][i] == NV)
					&& PreValueCheck[FEV1][i] != FALSE && PreValueCheck[FEV1FVC][i] != FALSE)
			{
				tmp2 = PreValue[FEV1][i] / PreValue[FEV1FVC][i] * 100;
				PreValueCheck[FVC][i] = CALCULATED;

				if (i == PRE_ACTUAL)
					tmp = PreValue[FVC][PRE_PREDICT_PERCENT] * PreValue[FVC][PRE_PREDICT] / 100;
				if (i == PRE_PREDICT)
					tmp = PreValue[FVC][PRE_ACTUAL] / PreValue[FVC][PRE_PREDICT_PERCENT] * 100;
				if (i == PRE_PREDICT_PERCENT)
					tmp = PreValue[FVC][PRE_ACTUAL] / PreValue[FVC][PRE_PREDICT] * 100;

				if (tmp > 20)
					multi = 1;
				else
					multi = 10;

				if ((tmp2 * multi < tmp * multi + 2) && (tmp2 * multi > tmp * multi - 2))
				{

					for (int k = 0; k < 3; k++)
					{
						PreValue[FVC][i] = tmp2;
						PreValueCheck[i][k] = TRUE;
						PreValueCheck[k][i] = TRUE;
					}

				} else if (tmp2 * multi < (tmp * multi + 2) && tmp2 * multi > (tmp * multi - 2))
				{// validation이
					// 맞으면, calculated에서 TRUE로 변경.
					for (int k = 0; k < 3; k++)
					{
						// PreValueCheck[i][k] = TRUE;
						PreValueCheck[FVC][i] = TRUE;
						PreValueCheck[k][i] = TRUE;
					}
				} else
				{

					// 계산이 맞지 않는다면, 두개 계산한것중에서 reference range 안에들어가는거 입력
					if (i == 0 | i == 1)
					{
						if (tmp2 > 1.0 && tmp2 < 12.0)
						{
							PreValue[FVC][i] = tmp2;
							PreValueCheck[FVC][i] = NV;
						} else if (tmp > 1.0 && tmp < 12.0)
						{
							PreValue[FVC][i] = tmp;
							PreValueCheck[FVC][i] = NV;
						} else
						{

							PreValue[FVC][i] = 0.0;
							PreValueCheck[FVC][i] = FALSE;
						}
					}

					if (i == 2)
					{
						if (tmp2 > 50 && tmp2 < 150.0)
						{
							PreValue[FVC][i] = tmp2;
							PreValueCheck[FVC][i] = NV;

						} else if (tmp > 50.0 && tmp < 150.0)
						{
							PreValue[FVC][i] = tmp;
							PreValueCheck[FVC][i] = NV;
						} else
						{
							PreValue[FVC][i] = 0.0;
							PreValueCheck[FVC][i] = FALSE;
						}

					}

				}
			}
			if (PreValueCheck[FVC][i] != FALSE
					&& (PreValueCheck[FEV1][i] == FALSE | PreValueCheck[FEV1][i] == CALCULATED | PreValueCheck[FEV1][i] == NV)
					&& PreValueCheck[FEV1FVC][i] != FALSE)
			{
				// FVC=FEV1/FEV1FVC*100

				tmp2 = PreValue[FVC][i] * PreValue[FEV1FVC][i] / 100;
				PreValueCheck[FEV1][i] = CALCULATED;

				if (i == PRE_ACTUAL)
					tmp = PreValue[FEV1][PRE_PREDICT_PERCENT] * PreValue[FEV1][PRE_PREDICT] / 100;
				if (i == PRE_PREDICT)
					tmp = PreValue[FEV1][PRE_ACTUAL] / PreValue[FEV1][PRE_PREDICT_PERCENT] * 100;
				if (i == PRE_PREDICT_PERCENT)
					tmp = PreValue[FEV1][PRE_ACTUAL] / PreValue[FEV1][PRE_PREDICT] * 100;

				if (tmp > 20)
					multi = 1;
				else
					multi = 10;

				if ((tmp2 * multi < tmp * multi + 2) && (tmp2 * multi > tmp * multi - 2))
				{

					for (int k = 0; k < 3; k++)
					{
						PreValue[FEV1][i] = tmp2;
						PreValueCheck[i][k] = TRUE;
						PreValueCheck[k][i] = TRUE;
					}

				} else if (tmp2 * multi < (tmp * multi + 2) && tmp2 * multi > (tmp * multi - 2)) // 계산하지
																																// 않고,
																																// 그냥
																																// 들어가
																																// 있는
																																// 값과
																																// 일치하면,
				{// validation이
					// 맞으면, calculated에서 TRUE로 변경.
					for (int k = 0; k < 3; k++)
					{
						// PreValueCheck[i][k] = TRUE;
						PreValueCheck[FEV1][i] = TRUE;
						PreValueCheck[k][i] = TRUE;
					}
				} else
				{
					// 계산이 맞지 않는다면, 두개 계산한것중에서 reference range 안에들어가는거 입력
					if (i == 0 | i == 1)
					{
						if (tmp2 > 1.0 && tmp2 < 12.0)
						{
							PreValue[FEV1][i] = tmp2;
							PreValueCheck[FEV1][i] = NV;
						} else if (tmp > 1.0 && tmp < 12.0)
						{
							PreValue[FEV1][i] = tmp;
							PreValueCheck[FEV1][i] = NV;
						} else
						{

							PreValue[FEV1][i] = 0.0;
							PreValueCheck[FEV1][i] = FALSE;
						}
					}

					if (i == 2)
					{
						if (tmp2 > 50 && tmp2 < 150.0)
						{
							PreValue[FEV1][i] = tmp2;
							PreValueCheck[FEV1][i] = NV;

						} else if (tmp > 50.0 && tmp < 150.0)
						{
							PreValue[FEV1][i] = tmp;
							PreValueCheck[FEV1][i] = NV;
						} else
						{
							PreValue[FEV1][i] = 0.0;
							PreValueCheck[FEV1][i] = FALSE;
						}

					}

					// PreValue[FEV1][i] = tmp2;
				}
			}

			if (PreValueCheck[FVC][i] != FALSE && PreValueCheck[FEV1][i] != FALSE
					&& (PreValueCheck[FEV1FVC][i] == FALSE | PreValueCheck[FEV1FVC][i] == CALCULATED | PreValueCheck[FEV1FVC][i] == NV))
			{

				tmp2 = PreValue[FEV1][i] / PreValue[FVC][i] * 100;
				PreValueCheck[FEV1FVC][i] = CALCULATED;

				if (i == PRE_ACTUAL)
					tmp = PreValue[FEV1FVC][PRE_PREDICT_PERCENT] * PreValue[FEV1FVC][PRE_PREDICT] / 100;
				if (i == PRE_PREDICT)
					tmp = PreValue[FEV1FVC][PRE_ACTUAL] / PreValue[FEV1FVC][PRE_PREDICT_PERCENT] * 100;
				if (i == PRE_PREDICT_PERCENT)
					tmp = PreValue[FEV1FVC][PRE_ACTUAL] / PreValue[FEV1FVC][PRE_PREDICT] * 100;

				if (tmp > 20)
					multi = 1;
				else
					multi = 10;

				if ((tmp2 * multi < tmp * multi + 2) && (tmp2 * multi > tmp * multi - 2))
				{

					for (int k = 0; k < 3; k++)
					{
						PreValue[FEV1FVC][i] = tmp2;
						PreValueCheck[i][k] = TRUE;
						PreValueCheck[k][i] = TRUE;
					}

				} else if (tmp2* multi < (tmp * multi + 2) && tmp2* multi > (tmp * multi - 2))
				// 계산하지// 않고,// 그냥// 들어가// 있는// 값과// 일치하면,
				{// validation이
					// 맞으면, calculated에서 TRUE로 변경.
					for (int k = 0; k < 3; k++)
					{
						// PreValueCheck[i][k] = TRUE;
						PreValueCheck[FEV1FVC][i] = TRUE;
						PreValueCheck[k][i] = TRUE;
					}
				} else
				{
					if (tmp2 > 50 && tmp2 < 150.0)
					{
						PreValue[FEV1FVC][i] = tmp2;
						PreValueCheck[FEV1FVC][i] = NV;

					} else if (tmp > 50.0 && tmp < 150.0)
					{
						PreValue[FEV1FVC][i] = tmp;
						PreValueCheck[FEV1FVC][i] = NV;
					} else
					{
						PreValue[FEV1FVC][i] = 0.0;
						PreValueCheck[FEV1FVC][i] = FALSE;
					}

					// PreValue[FEV1FVC][i] = tmp2;
				}
			}
		}
		
		
		
		// 마지막 값체크.. reference range 확인.. 벗어난거 있으면, 전부 false로
		for (int i = 0; i < 3; i++)
		{
			for (int k = 0; k < 3; k++)
			{

				if (k == 2 | i == 2)
				{
					if (PreValue[i][k] < 40 | PreValue[i][k] > 150.0)
					{
						PreValueCheck[i][k] = FALSE;
					}
				}else if (PreValue[i][k] < 1.0 | PreValue[i][k] > 15.0)
				{
					PreValueCheck[i][k] = FALSE;
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
				for (int i = 0; i <= PRE_PREDICT_PERCENT; i++)
				{
					FEF25_75IsnumericVector[i] =NV;
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

	public void parsing_FEV1(String fEV1pattern)
	{

		Matcher mcer = Pattern.compile(fEV1pattern).matcher(rslt);

		PFT_PRE_POST pp = new PFT_PRE_POST();

		pp = valuecheck_and_insert(mcer, FVC);
		if (pp != null)
		{

			PreValue[FEV1] = pp.Pre_FVC_FEV1_FEF252;
			PreValueCheck[FEV1] = pp.IsnumericVector;

		}

	}

	public void parsing_FEV1FVC(String fVCFEV1pattern)
	{

		Matcher mcer = Pattern.compile(fVCFEV1pattern).matcher(rslt);

		PFT_PRE_POST pp = new PFT_PRE_POST();

		pp = valuecheck_and_insert(mcer, FEV1FVC);
		if (pp != null)
		{

			PreValue[FEV1FVC] = pp.Pre_FVC_FEV1_FEF252;
			PreValueCheck[FEV1FVC] = pp.IsnumericVector;

		}
		

	}

	// FEF25_75per_actual
	public boolean parsing_FEF25_75(String fEF25_75pattern)
	{
		Matcher mcer = Pattern.compile(fEF25_75pattern).matcher(rslt);
		PFT_PRE_POST pp = new PFT_PRE_POST();
		

		pp = valuecheck_and_insert(mcer, FEF25_75);
		if (pp != null)
		{

			FEF25_75IsnumericVector = pp.IsnumericVector;
			Pre_FVC_FEV1_FEF25 = pp.Pre_FVC_FEV1_FEF252;

		}

	
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
			}else 		return false;
	}

	public void SaveResult(String dst, String txtfile, int type)
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

		String data = "";
	//	if(type==Pre)
		
		int tmp=0;
		tmp=PreValueCheck[0].length;
		
		
		
		
		
		if(tmp<=3)
		{
		data=filename + "," + PreValueCheck[0][0] + PreValueCheck[0][1] + PreValueCheck[0][2] + "," + szpatientID + "," + szdate + ","
				+ "FVC," + PreValue[0][0] + "," + PreValue[0][1] + "," + PreValue[0][2] + "\r\n" + filename + "," + PreValueCheck[1][0]
				+ PreValueCheck[1][1] + PreValueCheck[1][2] + "," + szpatientID + "," + szdate + "," + "FEV1," + PreValue[1][0] + ","
				+ PreValue[1][1] + "," + PreValue[1][2] + "\r\n" + filename + "," + PreValueCheck[2][0] + PreValueCheck[2][1] + PreValueCheck[2][2]
				+ "," + szpatientID + "," + szdate + "," + "FEV1FVC," + PreValue[2][0] + "," + PreValue[2][1] + "," + PreValue[2][2] + "\r\n"
				+ filename + "," + FEF25_75IsnumericVector[0] + FEF25_75IsnumericVector[1] + FEF25_75IsnumericVector[2] + "," + szpatientID + ","
				+ szdate + "," + "FEF25_75," + Pre_FVC_FEV1_FEF25[0] + "," + Pre_FVC_FEV1_FEF25[1] + "," + Pre_FVC_FEV1_FEF25[2];
		}else 
		{
			data=filename + "," + PreValueCheck[0][0] + PreValueCheck[0][1] + PreValueCheck[0][2] +PreValueCheck[0][3] +PreValueCheck[0][4] +PreValueCheck[0][5] + "," + szpatientID + "," + szdate + ","
					+ "FVC," + PreValue[0][0] + "," + PreValue[0][1] + "," + PreValue[0][2] +"," +PreValue[0][3] +"," +PreValue[0][4] +"," +PreValue[0][5] + "\r\n" + filename + "," + PreValueCheck[1][0]
					+ PreValueCheck[1][1] + PreValueCheck[1][2] +PreValueCheck[1][3] +PreValueCheck[1][4] +PreValueCheck[1][5] + "," + szpatientID + "," + szdate + "," + "FEV1," + PreValue[1][0] + ","
					+ PreValue[1][1] + "," + PreValue[1][2] +"," + PreValue[1][3] +"," +PreValue[1][4] +"," +PreValue[1][5] +"\r\n" + filename + "," + PreValueCheck[2][0] + PreValueCheck[2][1] + PreValueCheck[2][2]+ PreValueCheck[2][3]+ PreValueCheck[2][4]+ PreValueCheck[2][5]
					+ "," + szpatientID + "," + szdate + "," + "FEV1FVC," + PreValue[2][0] + "," + PreValue[2][1] + "," + PreValue[2][2] +"," + PreValue[2][3] +"," + PreValue[2][4] +"," + PreValue[2][5] + "\r\n"
					+ filename + "," + FEF25_75IsnumericVector[0] + FEF25_75IsnumericVector[1] + FEF25_75IsnumericVector[2]+ FEF25_75IsnumericVector[3]+ FEF25_75IsnumericVector[4]+ FEF25_75IsnumericVector[5] + "," + szpatientID + ","
					+ szdate + "," + "FEF25_75," + Pre_FVC_FEV1_FEF25[0] + "," + Pre_FVC_FEV1_FEF25[1] + "," + Pre_FVC_FEV1_FEF25[2]+ "," + Pre_FVC_FEV1_FEF25[3]+ "," + Pre_FVC_FEV1_FEF25[4]+ "," + Pre_FVC_FEV1_FEF25[5];
		}

		BufferedWriter writer = null;

		// String filedir = dst + "\\PFT_data.txt";

		
		String txtfileSaving = "";

        txtfileSaving =  TesseractExample.savedir + txtfile;
        //txtfileSaving =  savedir + txtfile;

		try
		{
			File directory = new File(txtfileSaving);

			if (directory.exists() == false)
			{
				writer = new BufferedWriter(new FileWriter(txtfileSaving));
				if(tmp<=3)
				writer.write("FILENAME,SUCCESSFLAG,ID,EXECDATE,ACTUAL,PRED,PRED_PER\r\n");
				else
					writer.write("FILENAME,SUCCESSFLAG,ID,EXECDATE,ACTUAL,PRED,PRED_PER,POST_ACTUAL,POST_PRED_PER,CHNG_PER\r\n");
					
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
