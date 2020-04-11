package com.bitmap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CompressBitmapIndex {

	String sourcePath;
	int startIndex = 0, endIndex = 0;
	public CompressBitmapIndex() {
		
	}
	
	public void compressBitmap(String sourceFolder) {
		sourcePath = sourceFolder;
		
		File folder = new File(sourcePath);
		for(File file : folder.listFiles()) {
			if(file.isFile()) {
				String fileName = file.getName();
//				System.out.println(fileName);
				
				if(fileName.substring(11, 15).equals("empi")) {
					startIndex = 0;
					endIndex = 8;
				} else if(fileName.substring(11, 15).equals("gend")) {
					startIndex = 0;
					endIndex = 1;
				} else if(fileName.substring(11, 15).equals("dept")) {
					startIndex = 0;
					endIndex = 3;
				}
				String outputFile = sourcePath + "\\C" + fileName.substring(3);
				BufferedReader reader;
				BufferedWriter writer;
				try {
					reader = new BufferedReader(new FileReader(file));
					FileWriter FileWriter = new FileWriter(outputFile, true);
		            writer = new BufferedWriter(FileWriter);
					String line = reader.readLine();
					
					while (line != null) {
						String outputvector = getOutput(line);
						writer.write(outputvector);
			            writer.newLine();
			            writer.flush();
						// read next line
						line = reader.readLine();
					}
					reader.close();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("File " + fileName + " is compressed");
			}
		}
		
	}
	
	private String binaryvalue(int zerocount) {
		String str = new String();
		while(zerocount > 0)
	      {
	         int y = zerocount % 2;
	         str = y + str;
	         zerocount = zerocount / 2;
	      }
		return str;
	}
	
	private String getOutput(String line) {
		String output = new String();
		output = line.substring(startIndex, endIndex);
		char input[]= line.substring(endIndex).toCharArray();
		int zerocount=0;
		for(char i:input) {
			if(i=='0') {
				zerocount++;
			}
			else if(i=='1') {
				String str= new String();
				if(zerocount > 1 ) {
					str=binaryvalue(zerocount);
					for(int j=0;j<str.length()-1;j++) {
						output = output + '1';
					}
					output = output + '0' + str;
				}
				else if(zerocount == 1) {
					output = output + '0' + '1';
				}
				else if(zerocount == 0) {
					output = output + '0' + '0';
				}
				
				
				zerocount=0;
			}
		}
		return output;
	}
}
