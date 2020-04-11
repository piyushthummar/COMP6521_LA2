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
		System.out.println("Total time for File One: " + fileOneTime + " seconds");
		
		String fileTwo = "./15K-test.txt";
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
		System.out.println("Total time for File Two: " + fileTwoTime + " seconds");
		System.out.println("Total time for both files is " + (fileOneTime + fileTwoTime) + " seconds");
		System.out.println("BMI generated");
		
		
		
//		FileReader fr;
//		Scanner scan, innerScan;
//		FileWriter fw;
//		BufferedWriter bw;
//		HashSet<String> setOfId = new HashSet<>();
//		long startTime = System.currentTimeMillis();
//		try {
//			String fileOne = "./500000.txt";
//			String fileTwo = "./1000000.txt";
//			fr = new FileReader(fileOne);
//			scan = new Scanner(fr);
//			fw = new FileWriter("./bitmapIndex.txt");
//			bw = new BufferedWriter(fw);
//			int count = 0;
//			HashMap<String, String> bitmapIndex = new HashMap<>();
//			
// 			while(scan.hasNextLine()) {
// 				long tupleTime = System.currentTimeMillis();
// 				String bitmapForTuple = "";
// 				String line = scan.nextLine();
//				String s = line.substring(0, 8);
//				setOfId.add(s);
//				Double key = Double.parseDouble(s);
//				if(!bitmapIndex.containsKey(s)) {
//					innerScan = new Scanner(new FileReader(fileOne));
//					while(innerScan.hasNextLine()) {
//						String tuple = innerScan.nextLine();
////						System.out.println(tuple);
//						if(s.equals(tuple.substring(0, 8))) {//key.equals(Double.parseDouble(tuple.substring(0,8)))
////							System.out.println("tuple match");
//							bitmapForTuple += 1;
////							System.out.println("1 added");
//						} else {
//							bitmapForTuple += 0;
////							System.out.println("0 added");
//						}
//					}
//					System.out.println("bitmapIndex for tuple " + s + " is generated with " + (s.length() + bitmapForTuple.length()) + " characters");
////					System.out.println(bitmapForTuple.length());
//					bitmapIndex.put(s, "bitmapIndex");
//					bw.write(s + bitmapForTuple);
//					bw.newLine();
//					long tupleEndTime = System.currentTimeMillis();
//					System.out.println((tupleEndTime - tupleTime) + " ms or " + (tupleEndTime - tupleTime)/1000 + " seconds");
//					bw.flush();
//				} else {
//					System.out.println("key is present for " + s);
//					continue;
//				}
//			}
// 			System.out.println("Index by hashmap : " + bitmapIndex.size());
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		long endTime = System.currentTimeMillis();
//		System.out.println("Done!!");
//		System.out.println("Unique Records -> " + setOfId.size());
//		System.out.println("Time to create bitmap index is " + (endTime - startTime) + " ms " + (endTime - startTime) / 1000 + " seconds or " + (endTime - startTime) / (1000 * 60) + " minutes");
		
	}

}
