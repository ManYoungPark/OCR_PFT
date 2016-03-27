package net.sourceforge.tess4j.example;

import java.io.FilenameFilter;
import java.io.File;

public class DirSubFilter implements FilenameFilter{

	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		String tmp;
		tmp=dir+"\\"+name;
		return new File(tmp).isDirectory();
	}

}
