package net.sourceforge.tess4j.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.awt.Image;

import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;



public class cImageSizeCheck {

	/**
	 * @param args
	 */
	
	
	 public static File[] getFileList(String filesrc)
	    {
	    	  File dir = new File(filesrc);  // �����������
/*	          String maxFileName = "";
	          String[] szFileName = null;
	          long maxSize = 0;*/
	          File[] fileList=null;
	          if(dir.exists() && dir.isDirectory()) { // �����ϴ���, �������� üũ
	            fileList = dir.listFiles();  //  �������� ���ϸ���Ʈ ���
	              }
	          return fileList;
	    }
	
	 
	public static boolean moveFile(String source, String dest) {
	    boolean result = false;
	         
	    FileInputStream inputStream = null;
	    FileOutputStream outputStream = null;
	         
	    try {
	        inputStream = new FileInputStream(source);
	        outputStream = new FileOutputStream(dest);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        result = false;
	    }
	         
	    FileChannel fcin = inputStream.getChannel();
	    FileChannel fcout = outputStream.getChannel();
	         
	    long size = 0;
	    try {
	        size = fcin.size();
	        fcin.transferTo(0, size, fcout);
	             
	        fcout.close();
	        fcin.close();
	        outputStream.close();
	        inputStream.close();
	             
	        result = true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        result = false;
	    }
	         
	    File f = new File(source);
	    if (f.delete()) {
	        result = true;
	    }else {
	    	File file = new File(source);

	    	File fileToMove = new File(dest);
	    	boolean isMoved = file.renameTo(fileToMove);
	    	if(isMoved==true)
	    		System.out.println("\n잘 이동함");
	    	else System.out.println("\n이동하지 못함.");
	    }
	    
	    
/*
		File file = new File("D:/PFT/00293701000000124520111101144246703.txt");

    	File fileToMove = new File("D:/PFT/real_al_pulmonary/00293701000000124520111101144246703.txt");
    	boolean isMoved = file.renameTo(fileToMove);
    	if(isMoved==true)
    		System.out.println("\n잘 이동함");
    	else System.out.println("\n이동하지 못함.");
    	*/
	    
	    return result;
	    
	    
	    
	}

	public static boolean makeDirectory(String path) {
		  
		  boolean success = false;
		  
		  File directory = new File(path);
		  
		  if(directory.exists() == false) {
		   directory.mkdirs();
		   success = true;
		  }
		  
		  return success;
		  
		 }

	
	/*
	 public static void main(String[] args) throws FileNotFoundException {
		 File[] fileLists=null;
		// String filesrc="d:\\ECG_data\\ecg_scan1"; 
		 
		 String fileimgdest1800="R:\\ecg_original\\���۾�\\ecg1_1_1800";
		 String fileimgdest1600="R:\\ecg_original\\���۾�\\ecg1_1_1600";
		 String fileimgdest1300="R:\\ecg_original\\���۾�\\ecg1_1_1300";
		 String fileimgdest1300_bg_wh="R:\\ecg_original\\���۾�\\ecg1_1_1300_bg_wh";
		 
		 

		 
		 String filesrc="R:\\ecg_original\\���۾�";
		
		 fileLists=getFileList(filesrc);
		 
		 int width=0;
		 int height=0;
		 
		 //�ӽ÷� ������ 1000���� ¥����.
	//	 split_folder(filesrc);
		// merge_folder(filesrc);
	//	 System.exit(1);
		 
		 for(int i=0; i<fileLists.length; i++)
		 {
			 
			 // ���1 �̹��� ����� EKG����� �ƴϸ�, �ٸ� ������ ����
		  Image img = new ImageIcon(filesrc+File.separator+fileLists[i].getName()).getImage();
		 
		  width=img.getWidth(null);
		  height=img.getHeight(null);
		  
		  System.out.println(fileLists[i].getName()+"�ʺ�/����:"+width + "," + height +"cnt="+i);
		  
		  if(width>1800)
		  {
			  //img �̵�
			  moveFile(filesrc+File.separator+fileLists[i].getName(),fileimgdest1800+File.separator+fileLists[i].getName());
			  //txt �̵�
		//	  moveFile("d:\\ecg_data\\ecg_scan1_txt"+File.separator+(fileLists[i].getName()).replaceAll(".jpg", ".txt"),(filetxtdest1800+File.separator+fileLists[i].getName()).replaceAll(".jpg", ".txt"));
			  System.out.println("������ �̵��մϴ�.");
		  }
		  
		  if(width>1600 && width <1800)
		  {
			  
			  moveFile(filesrc+File.separator+fileLists[i].getName(),fileimgdest1600+File.separator+fileLists[i].getName());
			  //txt �̵�
		  	//  moveFile("d:\\ecg_data\\ecg_scan1_txt"+File.separator+(fileLists[i].getName()).replaceAll(".jpg", ".txt"),(filetxtdest1600+File.separator+fileLists[i].getName()).replaceAll(".jpg", ".txt"));
			  System.out.println("������ �̵��մϴ�.");
			 
		  }
		  
		  try{
		  if(width>1000 && width <1400)
		  {
			  
			  if(height>940)
			  {
			  moveFile(filesrc+File.separator+fileLists[i].getName(),fileimgdest1300+File.separator+fileLists[i].getName());
			  System.out.println("img������ �̵�����.");
			  }else
			  {
				  moveFile(filesrc+File.separator+fileLists[i].getName(),fileimgdest1300_bg_wh+File.separator+fileLists[i].getName());
				  System.out.println("img������ �̵�����.");
				  
			  }
			  //txt �̵�
			//  moveFile("d:\\ecg_data\\ecg_scan1_txt"+File.separator+(fileLists[i].getName()).replaceAll(".jpg", ".txt"),(filetxtdest1300+File.separator+fileLists[i].getName()).replaceAll(".jpg", ".txt"));
			  System.out.println("txt������ �̵����� ");
		  }
		  }catch(NullPointerException e){}
		  
		 }
		
	 }
*/

	private static void merge_folder(String filesrc) {
		// TODO Auto-generated method stub
		DirSubFilter dsf=new DirSubFilter();
		String[] tmpDirs=null;
		
		File directory=new File(filesrc);
		tmpDirs=directory.list(dsf);
		File[] fileLists=null;
		
		for(String folder:tmpDirs)
		{
			System.out.println(folder);
			if(folder.indexOf("tmp")>=0)
			{
				fileLists=getFileList(filesrc+"\\"+folder);
				 for(int i=0; i<fileLists.length; i++)
				 {
					 String msrc=null;
					 String mdest=null;
					 msrc=filesrc+"\\"+folder+"\\"+fileLists[i].getName();
					 mdest=filesrc+"\\"+fileLists[i].getName();
					 
				
					 moveFile(msrc,mdest);
				 }
			}
		}
	}


	private static void split_folder(String filesrc) {
		// TODO Auto-generated method stub
		//�̹��� ����������� �߶� ���������� ��
		File[] fileLists=null;
		//String filesrc="D:\\2hoECG\\1300";
		String filedest="";
		fileLists=getFileList(filesrc);
		 
		int j=0;
		 for(int i=0; i<fileLists.length; i++)
		 {
			 j=(i/300)+60;
			 filedest=filesrc+"\\tmp_"+j;
			 makeDirectory(filedest);
			 moveFile(filesrc+File.separator+fileLists[i].getName(),filedest+File.separator+fileLists[i].getName());			 			 
			 
		 }
	}

}
