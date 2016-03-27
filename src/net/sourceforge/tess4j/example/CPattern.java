package net.sourceforge.tess4j.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CPattern { //공통적으로 들어가는 부분만을 담은 클래스
	
	static public int Ho_patternPFTonly=1;
	static public int Al_patternSPIROMETRYonly=2;
	static public int Ho_patternPrePost=3;
	
	
	static public String patternID="(\\d{7})";
	static public String patternID2="(\\d{1}[0-9a-zA-Z]{6})";
	
	
	static public String patternDate="(\\d{4}-\\d{1,2}[\\s]?\\d{1}?-\\d{1,2}[\\s]?\\d{1}?|\\d{1,2}[\\s]?\\/\\d{1,2}[\\s]?\\/\\d{4}|\\d{4}\\/\\d{1,2}[\\s]?\\d{1}?\\/\\d{1,2}[\\s]?\\d{1}?)";
	static public String patternDate2="(..?\\/..?.?\\/.....?|....[\\/-]..?[\\/-]..?)";
	
	

	static public String Ho_patternMetacholine="(P\\s?R\\s?E|P\\s?R\\s?O)-BR[A|O].{3}[\\s|\\t]+(CHA|OHA)"; //이 자료는 metacholine challenge test 임. pre challenge post
	static public String Ho_patternPFTonlyPre="(P\\s?R\\s?E|P\\s?R\\s?O)[\\?.-]BR[AO].{3}[\\s\\t]+POST.BR.{3}"; //이 자료는 pulmonary function test 임. 아직 싱글인지는 모름.

	

	static public String Al_patternPFTonlyPre="(M\\s?M\\s?E\\s?F|P\\s?E\\s?F)[^\\n:]"; //이 자료는 알레르기 내과 이미지 임. 아직 싱글인지는 post까지 있는지 모름.
		    	
	
	//호흡기 내과 패턴
	static public String FVCpattern="([-FP]\\s?V\\s?C|F\\s?.\\s?C|[-FP]\\s?V\\s?.)[\\s\\t]+_L_[\\s\\t-']+(.{3,5})[\\s\\t](.{3,5})[\\s\\t](.{2,4})[\\s\\t]";//FVC가 있는 라인을 가지고 오기위한 패턴
	static public String FEV1pattern="([-FP]\\s?E\\s?V|F\\s?F\\s?V|F\\s?.\\s?V)[1LI][\\s\\t]+_L_[\\s\\t]?(.{3,5})[\\s\\t](.{3,5})[\\s\\t](.{2,4})";//FEV1가 있는 라인을 가지고 오기위한 패턴FVC�� �ִ� ������ ������ �������� ����
	static public String FVCFEV1pattern="([-FP]\\s?E\\s?V\\s?[L1I].?[\\/I1l]?[PF]\\s?V\\s?[CO])[\\s\\t]+_P_[\\s\\t]?(.{2,3})[\\s\\t](.{2,3})[\\s\\t](.{2,4})";//FVC�� �ִ� ������ ������ �������� ����
	static public String  FEF25_75pattern="(.?E\\s?[EF]\\s?[\\s\\t]?2.+[75].+S\\s?.\\s?[CO]\\s?_)[\\s\\t]?(.{2,4})[\\s\\t](.{2,4})[\\s\\t](.{2,3})[\\s\\t]";
	
	static public String FVCpatternPrePost ="(.?\\s?V\\s?C|F\\s?.\\s?C|[FP]\\s?V\\s?.)[\\s\\t]?_L_[\\s\\t-']+(.{3,5})[\\s\\t](.{3,5})[\\s\\t](.{2,4})[\\s\\t](.{2,4})[\\s\\t](.{2,3})[\\s\\t](.{1,3})[\\s\\t]";
	static public String FEV1patternPrePost="(.?\\s?E\\s?V|F\\s?F\\s?V|F\\s?.\\s?V)[\\s\\t]?[1LI][\\s\\t]?_L_[\\s\\t-']+(.{3,5})[\\s\\t](.{3,5})[\\s\\t](.{2,4})[\\s\\t](.{2,4})[\\s\\t](.{2,3})[\\s\\t](.{1,3})[\\s\\t]";
	static public String FVCFEV1patternPrePost="(.?\\s?E\\s?V\\s?[L1I].?[\\/I1l]?[PF]\\s?V\\s?[CO])[\\s\\t]?_P_[\\s\\t]?(.{2,3})[\\s\\t](.{2,3})[\\s\\t](.{2,4})[\\s\\t](.{2,4})[\\s\\t](.{2,3})[\\s\\t](.{1,3})[\\s\\t]";
	static public String FEF25_75patternPrePost="(.?E\\s?[EF]\\s?[\\s\\t]?2.+[75].+S\\s?.\\s?[A-Z]\\s?_)[\\s\\t]?(.{3,5})[\\s\\t](.{3,5})[\\s\\t](.{2,4})[\\s\\t](.{2,4})[\\s\\t](.{2,3})[\\s\\t](.{1,3})[\\s\\t]";
	
	//알레르기 내과 패턴
	//static public String alFVCpattern="([FP]\\s?V\\s?C|F\\s?.\\s?C|[FP]\\s?V\\s?.).+[I|]L[\\s\\t-']+(.{3,5})[\\s\\t](.{3,5})[\\s\\t](.{2,4})";
	static public String alFVCpattern="([-FP]\\s?V\\s?C.+IL|F\\s?.\\s?C.+IL|[-FP]\\s?V\\s?..+IL|[-FP]\\s?V\\s?C.+LL|F\\s?.\\s?C.+LL|[-FP]\\s?V\\s?..+LL)[\\s\\t-']+(.{3,5})[\\s\\t](.{3,5})[\\s\\t](.{2,4})";
	static public String alFEV1pattern="([-FP]\\s?E\\s?V|F\\s?F\\s?V|F\\s?.\\s?V)\\s?[1LI].+[I|L]L[\\s\\t-']+[\\s\\t]?(.{3,5})[\\s\\t](.{3,5})[\\s\\t](.{2,4}\\d?)";
	static public String alFEV1FVCpattern1="([-FPE]\\s?E\\s?V\\s?)\\s?[Ii1].+[MP]L[\\t\\s]+(.{2,4})";
	static public String alFEV1FVCpattern2="([-FPE]\\s?E\\s?V\\s?)\\s?[Ii1].+[MP]L[\\t\\s]+(.{2,5}).?[\\s\\t]+(.{2,5}).?[\\s\\t]+(.{2,5})";
	
	static public String  lastValuePattern="(-?\\d{1,3}[\\t\\s]?)";
	
	public  String rslt="";
	
	public String szpatientID="";
	public String szdate="";
	public String filename="";
		
	private boolean ID_parsed=false;
	private boolean Date_parsed=false;
	
	
	CPattern(String txt)
	{
		rslt=txt;
	}
		
	public boolean parsingID_Date() {
		// TODO Auto-generated method stub
		
		Matcher  m;
		
		m=Pattern.compile(patternID).matcher(rslt);
    	if(m.find())
    	{
    		System.out.println(m.group(0));
    		szpatientID = m.group(0);
    		ID_parsed=true;
    	}else if ((m=Pattern.compile(patternID2).matcher(rslt)).find())
    	{
    		System.out.println(m.group(0));
    		szpatientID = m.group(0);
    		ID_parsed=true;
    	}else
    	{
    		ID_parsed=false;
    	}
    	
    	
    	
    	
    	m=Pattern.compile(patternDate).matcher(rslt);
    	if(m.find())
    	{
    		System.out.println(m.group(0));
    		szdate = m.group(0);
    		Date_parsed=true;
    	}else if ((m=Pattern.compile(patternDate2).matcher(rslt)).find())
    	{
    		System.out.println(m.group(0));
    		szdate = m.group(0);
    		Date_parsed=true;
    	}else 
    	{	
    		Date_parsed=true;
    	}
    	    	
    	if(ID_parsed==true && Date_parsed==true)
    			return true;
    	else return false;
		
	}
	
	public  int isNumeric(String s) {
		  try {
		      Double.parseDouble(s);
		      return 1;
		  } catch(NumberFormatException e) {
		      return 0;
		  }
		}
	

	
	public String replaceChar(String str)
	{
		
		
		str=str.replace("l", "1");
		str=str.replace("q", "9");
		str=str.replace("H", "11");
		str=str.replace("'", "");
		str=str.trim();		
		str=str.replace(" ", ".");
		str=str.replace("..", ".");
		str=str.replace("O", "0");
		str=str.replace("o", "0");
		
		return str;	
		
	}
}
