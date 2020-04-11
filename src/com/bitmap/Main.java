package com.bitmap;

import java.io.BufferedWriter;
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
		String fileOne = "./10K-test.txt";
		String destinationFolderOne = "./IO_Files/PartialBitmapResult/fileOne";
		CreateBitmapIndex create = new CreateBitmapIndex(fileOne, destinationFolderOne);
		long startTime = 0, endTime = 0;
//		startTime = System.currentTimeMillis();
//		create.extractDetails("empid");
//		endTime = System.currentTimeMillis();
//		long timeForEmpIdBitmap = endTime - startTime;
//		System.out.println(timeForEmpIdBitmap + " ms");
//		
//		startTime = System.currentTimeMillis();
//		create.extractDetails("gender");
//		endTime = System.currentTimeMillis();
//		long timeForGenderBitmap = endTime - startTime;
//		System.out.println(timeForGenderBitmap + " ms");
//		
//		startTime = System.currentTimeMillis();
//		create.extractDetails("dept");
//		endTime = System.currentTimeMillis();
//		long timeForDeptBitmap = endTime - startTime;
//		System.out.println(timeForDeptBitmap + " ms");
//		long fileOneTime = (timeForEmpIdBitmap + timeForGenderBitmap + timeForDeptBitmap)/1000;
//		System.out.println("\nTotal time for File One: " + fileOneTime + " seconds\n");
//		
//		String fileTwo = "./15K-test.txt";
//		String destinationFolderTwo = "./IO_Files/PartialBitmapResult/fileTwo";
//		create = new CreateBitmapIndex(fileTwo, destinationFolderTwo);
//		
//		startTime = System.currentTimeMillis();
//		create.extractDetails("empid");
//		endTime = System.currentTimeMillis();
//		timeForEmpIdBitmap = endTime - startTime;
//		System.out.println(timeForEmpIdBitmap + " ms");
//		
//		startTime = System.currentTimeMillis();
//		create.extractDetails("gender");
//		endTime = System.currentTimeMillis();
//		timeForGenderBitmap = endTime - startTime;
//		System.out.println(timeForGenderBitmap + " ms");
//		
//		startTime = System.currentTimeMillis();
//		create.extractDetails("dept");
//		endTime = System.currentTimeMillis();
//		timeForDeptBitmap = endTime - startTime;
//		System.out.println(timeForDeptBitmap + " ms");
//		
//		long fileTwoTime = (timeForEmpIdBitmap + timeForGenderBitmap + timeForDeptBitmap)/1000;
//		System.out.println("\nTotal time for File Two: " + fileTwoTime + " seconds");
//		System.out.println("\n\nTotal time for both files is " + (fileOneTime + fileTwoTime) + " seconds");
//		System.out.println("BMI generated");
		
		System.out.println("Starting compression...");
		System.gc();
		long compressionStartTime = System.currentTimeMillis();
		CompressBitmapIndex compress = new CompressBitmapIndex();
		compress.compressBitmap("./IO_Files");
		long compressionEndTime = System.currentTimeMillis();
		long compressionTotalTime = compressionEndTime - compressionStartTime;
		System.out.println("Compression Done!!");
		System.out.println("Compression Total Time : " + (compressionTotalTime / 1000) + " seconds");
		
	}

}
