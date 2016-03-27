package net.sourceforge.tess4j.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.tess4j.*;

public class TesseractExample extends CPattern
{

	static public String savedir = "g:\\";

	TesseractExample(String txt)
	{
		super(txt);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
		// File imageFile = new File("eurotext.tif");
		String filefullname = null;
		String filename = null;

		Boolean TF = false;
		cImageSizeCheck isc = new cImageSizeCheck();
		File[] fileLists = null;

		// String filesrc = "d:\\PFT";
		String filesrc = "g:\\All_pft_txt";
		
		//String filesrc ="G:\\PFT_image\\medinfo_emr_after2_notxt";
		

		String dest_trash = "\\trash";
		String dest_real_al_metacholine = "\\real_al_metacholine";
		String dest_real_al_pulmonary = "\\real_al_pulmonary";
		String dest_real_al_pulmonaryPrePost = "\\real_al_pulmonaryPrePost";
		String dest_real_al_pulmonary2th = "\\real_al_pulmonary2th";
		String dest_real_al_pulmonarySaline_Chg = "\\real_al_pulmonarySaline_Chg";

		String dest_real_ho_pulmonaryPre = "\\real_ho_pulmonaryPre";
		String dest_real_ho_pulmonaryPrePost = "\\real_ho_pulmonaryPrePost";
		String dest_real_ho_pulmonary2th = "\\real_ho_pulmonary2th";
		String dest_real_ho_pulmonaryConfused = "\\real_ho_pulmonaryConfused";

		String dest_real_ho_metacholine = "\\real_ho_metacholine";
		String dest_real_ho_metacholine2th = "\\real_ho_metacholine2th";

		String successdir = "\\success";
		String faildir = "\\fail";

		String dest_real_confused = "\\real_confused";

		fileLists = cImageSizeCheck.getFileList(filesrc);
		String result = "";
		// String result = getImageFileName(filesrc);

		for (int i = 0; i < fileLists.length; i++)
		{

			long startTime = System.nanoTime();

			filefullname = filesrc + File.separator + fileLists[i].getName();
			filename = fileLists[i].getName();

			if (filename.indexOf("txt") < 0) // 0보다 작으면, 이미지파일이라고 생각하면 됨.
			{
				File imageFile = new File(filefullname);

				System.out.println(imageFile);

				Tesseract instance = Tesseract.getInstance(); // JNA Interface
																// Mapping
				// Tesseract1 instance = new Tesseract1(); // JNA Direct
				// Mappingl

				try
				{
					result = instance.doOCR(imageFile);
				} catch (TesseractException e)
				{
					System.err.println(e.getMessage());
				}

			} else if (filename.indexOf("txt") > 0)
			{
				result = "";

				File f = new File(filefullname);
				FileReader reader = null;
				try
				{
					reader = new FileReader(f);

					BufferedReader buffer = new BufferedReader(reader);

					String rl = "";
					while ((rl = buffer.readLine()) != null)
					{

						result = result + rl + "\n";
					}
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					reader.close();

				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// System.out.println(result);

			result = result.replace(System.getProperty("line.separator"), "");

			// result=result.replace("\n"," ");
			result = result.toUpperCase();

			result = result.replace("—", "-");
			result = result.replace("(", "_");
			result = result.replace(")", "_");
			result = result.replace("]", "L");
			result = result.replace("[", "L");
			result = result.replace("J", "L");
			result = result.replace("|", "I");
			result = result.replace("%", "P");

			System.out.println(result);

			// 이건 호흡기 내과꺼..
			String Ho_patternMetacholine = "(PRE|PRO)-BR[A|O].{3}[\\s|\\t]+(CHA|OHA)"; // 이
																						// 자료는
																						// metacholine
																						// challenge
																						// test
																						// 임.
																						// pre
																						// challenge
																						// post
			String Ho_patternPFTonlyPre = "(PRE|PRO)[\\?.-](BR.{3}.?|.+)[\\s\\t~]+(POST|P...).B.{3}.?"; // 이
																										// 자료는
																										// pulmonary
																										// function
																										// test
																										// 임.
																										// 아직
																										// 싱글인지는
																										// 모름.

			// 알레르기
			String Al_patternPFTonlyPre = "(MMEF|PEF)[^\\n:]"; // 이 자료는 알레르기 내과
																// 이미지 임. 아직
																// 싱글인지는 post까지
																// 있는지 모름.

			String FVCpattern = "(F\\s?V\\s?C|F\\s?.\\s?C|F\\s?V\\s?.|.VC)[\\s\\t]+[_L]L[L_][\\s\\t]+(.+)";// FVC가
			// 있는
			// 라인을
			// 가지고
			// 오기위한
			// 패턴

			Matcher mcer = Pattern.compile(Ho_patternMetacholine).matcher(result);
			System.out.println(result);

			if (mcer.find())
			{
				System.out.println(mcer.group(0));
				System.out.println("이 자료는 호흡기 내과의 metacholine challenge test 임");
				cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_metacholine);
				cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_ho_metacholine + "\\" + fileLists[i].getName());
				continue;
			}

			mcer = Pattern.compile(Ho_patternPFTonlyPre).matcher(result);
			if (mcer.find())
			{

				System.out.println(mcer.group(0));
				System.out.println("이 자료는  호흡기 내과의 pulmonary function test 임");

				mcer = Pattern.compile(FVCpattern).matcher(result);
				if (mcer.find())
				{
					String tmp = mcer.group(2);
					tmp=tmp.replace(" ", "\t");
					//String[] tmp2 = tmp.split(" ");
					String[] tmp2 = tmp.split("\t");
					

					// 객체를 만들어서 txt 전체를 생성자에 넣어버림
					CPulmonaryFuncTest pft = new CPulmonaryFuncTest(result);

				
					if (tmp2.length <= 4 )
					{
						System.out.println("pre만 있는 데이터");

						// 파일을 옮기기전에 파싱이 가능하면, 파싱하고 완료된 폴더로 이동시킨다. 그래야, 시간이
						// 절약되니깐,
						pft.parsing(filename, Ho_patternPFTonly);

						if (pft.getFalseCnt_pre() < 2) // 실패한것이 3개 이하이면, 성공으로 봐서
													// 성공
													// 디렉토리로 이동.
						{
							pft.SaveResult(filesrc, "ho_preonly_success.txt", 1);
							cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryPre);
							cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryPre + successdir);
							cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_ho_pulmonaryPre + successdir + "\\" + fileLists[i].getName());
							continue;

						} else
						{ // 실패 디렉토리로 이동..

							pft.SaveResult(filesrc, "ho_preonly_failed.txt", 0);
							cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryPre);
							cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryPre + faildir);
							cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_ho_pulmonaryPre + faildir + "\\" + fileLists[i].getName());
							continue;
						}
					} else
					{
						pft.parsing(filename, Ho_patternPrePost);
						
						
						if(pft.getFalseCnt_pre()<2 && pft.getFalseCnt_post()<2)
						{
							pft.SaveResult(filesrc, "ho_pre_post_success.txt", 1);
							cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryPrePost);
							cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryPrePost + successdir);
							cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_ho_pulmonaryPrePost + successdir + "\\" + fileLists[i].getName());
							continue;

							
						}else
						{ // 실패 디렉토리로 이동..

							pft.SaveResult(filesrc, "ho_pre_post_failed.txt", 0);
							cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryPrePost);
							cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryPrePost + faildir);
							cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_ho_pulmonaryPrePost + faildir + "\\" + fileLists[i].getName());
							continue;
						}

						
					}

				}
				// 호흡기 내과 자료중 trash들..
				cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonaryConfused);
				cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_ho_pulmonaryConfused + "\\" + fileLists[i].getName());

				System.out.println("쓰레기 데이터");

				continue;

			}

			mcer = Pattern.compile(Al_patternPFTonlyPre).matcher(result);
			if (mcer.find())
			{
				Matcher mcer2 = Pattern.compile("(POST|PO8T)").matcher(result);

				if (!mcer2.find())
				{

					System.out.println(mcer.group(0));
					System.out.println("이 자료는 알레르기 내과 pulmonary function test PRE 만 있는거 임");

					CPulmonaryFuncTest alSMT = new CPulmonaryFuncTest(result);

					alSMT.parsing(filename, Al_patternSPIROMETRYonly);

					if (alSMT.getFalseCnt_pre() < 2) // 실패한것이 3개 이하이면, 성공으로 봐서 성공
													// 디렉토리로
													// 이동.
					{
						alSMT.SaveResult(filesrc, "AL_SPIRO_preonly_success.txt", 1);
						cImageSizeCheck.makeDirectory(filesrc + dest_real_al_pulmonary);
						cImageSizeCheck.makeDirectory(filesrc + dest_real_al_pulmonary + successdir);
						cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_al_pulmonary + successdir + "\\" + fileLists[i].getName());

						continue;
					} else
					{ // 실패 디렉토리로 이동..

						alSMT.SaveResult(filesrc, "AL_SPIRO_preonly_failed.txt", 0);
						cImageSizeCheck.makeDirectory(filesrc + dest_real_al_pulmonary);
						cImageSizeCheck.makeDirectory(filesrc + dest_real_al_pulmonary + faildir);
						cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_al_pulmonary + faildir + "\\" + fileLists[i].getName());
						continue;

					}

				} else
				//
				{
					System.out.println("이 자료는 알레르기 내과 pulmonary function test PRE POST 둘다 있는거 임");

					cImageSizeCheck.makeDirectory(filesrc + dest_real_al_pulmonaryPrePost);
					cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_al_pulmonaryPrePost + "\\" + fileLists[i].getName());
					continue;
				}

			}

			mcer = Pattern.compile("(SPIR)").matcher(result); // 그러면 이건 호흡기 내과에서
																// 찍은 PFT인데,
																// 두번째장..
																// 필요없는거임.
			if (mcer.find())
			{
				System.out.println(mcer.group(0));
				System.out.println("이 자료는 호흡기 내과 pulmonary function test  두번째거임 필요없는거임");

				cImageSizeCheck.makeDirectory(filesrc + dest_real_ho_pulmonary2th);
				cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_ho_pulmonary2th + "\\" + fileLists[i].getName());

				continue;
			}

			mcer = Pattern.compile("(ME[TL1]HA)").matcher(result); // 그러면 이건
																	// 알레르기 내과에서
																	// 찍은 PFT인데,
																	// 두번째장..
																	// 필요없는거임.
			if (mcer.find())
			{
				System.out.println(mcer.group(0));
				System.out.println("이 자료는 알레르기 내과 METHACHOLINE 두번째장 필요없는거임");

				cImageSizeCheck.makeDirectory(filesrc + dest_real_al_pulmonary2th);
				cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_al_pulmonary2th + "\\" + fileLists[i].getName());

				continue;
			}

			String Al_patternPFT2th = "SALINE|CHG";// 이패턴은 필요없는 데이터.. 2번째꺼

			mcer = Pattern.compile(Al_patternPFT2th).matcher(result); // 그러면 두번째
																		// 장거라
																		// 필요없는
																		// 자료임.
			// 자료임
			if (mcer.find())
			{
				System.out.println(mcer.group(0));
				System.out.println("이 자료는 분류가 안된는 자료임");

				cImageSizeCheck.makeDirectory(filesrc + dest_real_al_pulmonarySaline_Chg);
				cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_al_pulmonarySaline_Chg + "\\" + fileLists[i].getName());

				continue;
			}

			mcer = Pattern.compile("FVC|F.C").matcher(result); // 그러면 두번째 장거라
																// 필요없는 자료임.
																// 자료임
			if (mcer.find())
			{
				System.out.println(mcer.group(0));
				System.out.println("이 자료는 분류가 안된는 자료임");

				cImageSizeCheck.makeDirectory(filesrc + dest_real_confused);
				cImageSizeCheck.moveFile(filefullname, filesrc + dest_real_confused + "\\" + fileLists[i].getName());

				continue;
			}

			// 여기까지 안걸렸으면, 쓰레기 파일임.
			cImageSizeCheck.makeDirectory(filesrc + dest_trash);
			cImageSizeCheck.moveFile(filefullname, filesrc + dest_trash + "\\" + fileLists[i].getName());

		}

		/*
		 * long endTime = System.nanoTime();
		 * 
		 * long lTime = endTime - startTime;
		 * 
		 * System.out.println("Time:" + lTime / 1000000.0 + "(ms)");
		 */

	}

	private static String getImageFileName(String filesrc)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
