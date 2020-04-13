package com.bitmap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import javax.xml.stream.events.StartDocument;

public class Main {

	public static int totalSubFiles = 0;
	public Main() {
		
	}

	public static void main(String[] args) {
		long startExecutionTime = System.currentTimeMillis();
		
		String fileOne = "./10000.txt";
		String destinationFolderOne = "./IO_Files/PartialBitmapResult/fileOne";
		CreateBitmapIndex create = new CreateBitmapIndex(fileOne, destinationFolderOne);
		long startTime = 0, endTime = 0;
		startTime = System.currentTimeMillis();
		create.extractDetails("empid");
		endTime = System.currentTimeMillis();
		long timeForEmpIdBitmap = endTime - startTime;
		System.out.println(timeForEmpIdBitmap + " ms");
		
		startTime = System.currentTimeMillis();
		create.extractDetails("gender");
		endTime = System.currentTimeMillis();
		long timeForGenderBitmap = endTime - startTime;
		System.out.println(timeForGenderBitmap + " ms");
		
		startTime = System.currentTimeMillis();
		create.extractDetails("dept");
		endTime = System.currentTimeMillis();
		long timeForDeptBitmap = endTime - startTime;
		System.out.println(timeForDeptBitmap + " ms");
		long fileOneTime = (timeForEmpIdBitmap + timeForGenderBitmap + timeForDeptBitmap)/1000;
		System.out.println("\nTotal time for File One: " + fileOneTime + " seconds\n");
		
		String fileTwo = "./15000.txt";
		String destinationFolderTwo = "./IO_Files/PartialBitmapResult/fileTwo";
		create = new CreateBitmapIndex(fileTwo, destinationFolderTwo);
		
		startTime = System.currentTimeMillis();
		create.extractDetails("empid");
		endTime = System.currentTimeMillis();
		timeForEmpIdBitmap = endTime - startTime;
		System.out.println(timeForEmpIdBitmap + " ms");
		
		startTime = System.currentTimeMillis();
		create.extractDetails("gender");
		endTime = System.currentTimeMillis();
		timeForGenderBitmap = endTime - startTime;
		System.out.println(timeForGenderBitmap + " ms");
		
		startTime = System.currentTimeMillis();
		create.extractDetails("dept");
		endTime = System.currentTimeMillis();
		timeForDeptBitmap = endTime - startTime;
		System.out.println(timeForDeptBitmap + " ms");
		
		long fileTwoTime = (timeForEmpIdBitmap + timeForGenderBitmap + timeForDeptBitmap)/1000;
		System.out.println("\nTotal time for File Two: " + fileTwoTime + " seconds");
		System.out.println("\n\nTotal time for both files is " + (fileOneTime + fileTwoTime) + " seconds");
		System.out.println("BMI generated");
		
		System.out.println("Starting compression...");
		System.gc();
		long compressionStartTime = System.currentTimeMillis();
		CompressBitmapIndex compress = new CompressBitmapIndex();
		compress.compressBitmap("./IO_Files");
		long compressionEndTime = System.currentTimeMillis();
		long compressionTotalTime = compressionEndTime - compressionStartTime;
		System.out.println("Compression Done!!");
		System.out.println("Compression Total Time : " + (compressionTotalTime/1000) + " seconds");
		
		
//		//Starting the process of duplication removal and merging...
		System.out.println("Starting Removal of duplication...");
		RemoveDuplicationAndMerge removeAndMerge = new RemoveDuplicationAndMerge();
		long startTotalTime = System.currentTimeMillis();
		
		//Removing from File 1 duplicate records...
		String bitmapFilePath = "", recordFilePath = "", outputFilePath = "";
		File folder = new File("./IO_Files");
		int fileCount = 0;
		for (File file : folder.listFiles()) {
			if (file.getName().startsWith("uncompressed-empid")) {
				bitmapFilePath = "./IO_Files/" + file.getName();
				 String []split = file.getName().split("-");
//				 System.out.println(split[split.length - 1]);
//				 System.out.println(fileOne + " --- " + fileTwo);
				 if(split[split.length - 1].equals(fileOne.substring(2))) {
					 recordFilePath = fileOne;
					 outputFilePath = "./IO_Files/without-duplicates-" + fileOne.substring(2);
				 } else if(split[split.length - 1].equals(fileTwo.substring(2))) {
					 recordFilePath = fileTwo;
					 outputFilePath = "./IO_Files/without-duplicates-" + fileTwo.substring(2);
				 }
				 fileCount++;
				 System.out.println("Removing duplicates from " + recordFilePath.substring(2) + " file...");
				 removeAndMerge.startRemovingDuplication(bitmapFilePath, recordFilePath, outputFilePath);
//				 System.out.println("outputPath - " + outputFilePath);
			}
			
		}
		
		//Merging of both files...
		System.out.println("Starting merging...");
		removeAndMerge.startMerging();
		long endTotalTime = System.currentTimeMillis();
		System.out.println("Total time for duplication removal for both files and merging into one is " + (endTotalTime - startTotalTime)/1000 + " seconds");
		
		long endExecutionTime = System.currentTimeMillis();
		int totalExecutionTime = (int)(endExecutionTime - startExecutionTime)/ 1000;
		System.out.println("Total execution time is " + totalExecutionTime + " seconds or " + (int)(totalExecutionTime/60) + " minutes"); 
		System.out.println("Done!!!");
	}

}
