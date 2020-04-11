package com.bitmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

import com.bitmap.Main;

public class CreateBitmapIndex {

	private static String filePath, destinationFolder;
	ArrayList<String> subFilesPath;
	ArrayList<String> dataList;
	public static int total_current_subFile = Main.totalSubFiles;
	public static int subFileCount = 0, records = 0, startIndex = 0, endIndex = 0, sumOfRecords = 0;
	public static String indexOn = "";
	public static String zeros = "";
	public static ArrayList<String> padding = new ArrayList<>();
	
	public CreateBitmapIndex(String filePath, String destinationFolder) {
		this.filePath = filePath;
		this.destinationFolder = destinationFolder;
		subFilesPath = new ArrayList<>();
	}

	public long extractDetails(String indexOn) {
		subFileCount = 0;
		records = 0; 
		startIndex = 0;
		endIndex = 0; 
		sumOfRecords = 0;
//		indexOn = "";
		zeros = "";
		padding.clear();

		long count = 0;
		subFileCount = 0;
		System.out.println("extracting sublists...");
		FileReader fr = null;
		BufferedReader br = null;
		Scanner sc = null;
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			sc = new Scanner(br);

			long availableMemory;

			availableMemory = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()
					+ Runtime.getRuntime().freeMemory();
			double maxTuples = Math.floor(availableMemory / 100);
			dataList = new ArrayList<>();
			// System.out.println(maxTuples);

			if (indexOn.equalsIgnoreCase("empId")) {
				indexOn = "empid";
				startIndex = 0;
				endIndex = 8;
			} else if (indexOn.equalsIgnoreCase("gender")) {
				indexOn = "gender";
				startIndex = 43;
				endIndex = 44;
			} else if (indexOn.equalsIgnoreCase("dept")) {
				indexOn = "dept";
				startIndex = 44;
				endIndex = 47;
			}

			long startSublist = System.currentTimeMillis();
			while (sc.hasNextLine()) {
				System.gc();
				records++;
				for (int index = 0; index < maxTuples; index++) {
					String line = sc.nextLine();
					count++;
					dataList.add(line);
					zeros += 0;
					// System.out.println("tuple-count:" + index + ", tuple-size : " +
					// line.getBytes().length + ", available-memory : "+
					// Runtime.getRuntime().freeMemory() + ", total-tuple:" + count);
					if (!sc.hasNextLine()
							|| Runtime.getRuntime().freeMemory() < (Runtime.getRuntime().maxMemory() * 0.74)) {
						break;
					}
				}
				
				sumOfRecords += dataList.size();
				System.out.println(dataList.size() + " scanned records = " + sumOfRecords);
				padding.add(zeros);
				zeros = "";
				createPartialBitmap(dataList, startIndex, endIndex, indexOn);
				dataList.clear();
				Runtime.getRuntime().gc();
				Main.totalSubFiles = total_current_subFile;
			}
			long endSublist = System.currentTimeMillis();
			System.out.println("partial index on " + indexOn + " is generated in " + (endSublist - startSublist)/1000 + " seconds");
			// System.out.println(records + " records");
			System.out.println(sumOfRecords);
			System.out.println("Merging partial indexes...");

			mergePartialBitmap(destinationFolder + "\\" + indexOn, "./IO_Files", indexOn);

			System.out.println("Bitmap index on " + indexOn + " is generated");
			fr.close();
			br.close();
			sc.close();
			Runtime.getRuntime().gc();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return count;
	}

	public boolean createPartialBitmap(ArrayList<String> data, int startIndex, int endIndex, String type) {
		PrintWriter pw;
		HashSet<String> setOfIndexOn = new HashSet<>();
		boolean writeSuccess = true;
		total_current_subFile++;
		subFileCount++;
		String subfilePath = destinationFolder + "\\" + type + "\\partial-bitmap-" + Integer.toString(subFileCount)
				+ ".txt";
		subFilesPath.add(subfilePath);
		
		
		try {
//			dataList.clear();
			System.gc();
			pw = new PrintWriter(new File(subfilePath));
			for (String tuple : data) {
				String s = tuple.substring(startIndex, endIndex);
				if (!setOfIndexOn.contains(s)) {
					setOfIndexOn.add(s);
					for (int index = 0; index < data.size(); index++) {
						if (tuple.substring(startIndex, endIndex)
								.equals(data.get(index).substring(startIndex, endIndex))) {
							s += 1;
						} else {
							s += 0;
						}
					}
				} else {
//					System.out.println("duplicate->" + s);
					// System.out.println("tuple already present");
					continue;
				}
				pw.println(s);
				pw.flush();
			}
			System.out.println("unique : " + setOfIndexOn.size());
			dataList.clear();
			// System.out.println("file " + current_subFile + " is generated");
			pw.close();
		} catch (FileNotFoundException e) {
			subFileCount--;
			total_current_subFile--;
			writeSuccess = false;
			e.printStackTrace();
		}
		return writeSuccess;
	}

	public void mergePartialBitmap(String sourcePathFolder, String destFolderForMergedIndex, String indexOn) {
		String destinationPath = destFolderForMergedIndex + "\\uncompressed-" + indexOn + "-index-" + sumOfRecords + ".txt";
		String mergedIndex = sourcePathFolder + "\\merged-index.txt";
		PrintWriter pw;
		FileReader fr;
		Scanner sc;
		ArrayList<BufferedReader> listOfFilePointers = new ArrayList<>();
		int count = 0;
		
		if (indexOn.equalsIgnoreCase("empId")) {
			indexOn = "empid";
			startIndex = 0;
			endIndex = 8;
		} else if (indexOn.equalsIgnoreCase("gender")) {
			indexOn = "gender";
			startIndex = 0;
			endIndex = 1;
		} else if (indexOn.equalsIgnoreCase("dept")) {
			indexOn = "dept";
			startIndex = 0;
			endIndex = 3;
		}
		ArrayList<HashSet<String>> recordOfEachSublist = new ArrayList<>();
		
		try {
			System.gc();
			pw = new PrintWriter(new File(mergedIndex));
			HashSet<String> setOfUnique;
			HashSet<String> duplicates = new HashSet<>();
			for (int index = 0; index < subFileCount; index++) {
				setOfUnique = new HashSet<>();
				zeros = "";
				File file = new File(sourcePathFolder + "\\partial-bitmap-" + (int)(index + 1) + ".txt");
				fr = new FileReader(file);
				sc = new Scanner(fr);
//				System.out.println(sourcePathFolder + "/" +file.getName());
//				System.out.println("merging " + (index+1) + " file...");
				
				while (sc.hasNextLine()) {
					String s = sc.nextLine();
					
					if (!setOfUnique.contains(s.subSequence(startIndex, endIndex))) {
						setOfUnique.add(s.substring(startIndex, endIndex));
					}
				}
//				System.out.println(setOfUnique);
				recordOfEachSublist.add(setOfUnique);
			}
//			System.out.println(recordOfEachSublist);
			// Creating pointer to each subfiles
			for (int index = 0; index < subFileCount; index++) {
				zeros = "";
				File file = new File(sourcePathFolder + "\\partial-bitmap-" + (int)(index + 1) + ".txt");
				fr = new FileReader(file);
				sc = new Scanner(fr);
//				System.out.println(sourcePathFolder + "/" +file.getName());
//				System.out.println("merging " + (index+1) + " file...");
				
				while (sc.hasNextLine()) {
					String s = sc.nextLine();
					String print = s.substring(startIndex, endIndex);
					int paddingIndex = 0;
					for(HashSet<String> data : recordOfEachSublist) {
						if(!data.contains(s.substring(startIndex, endIndex))) {
							print += padding.get(paddingIndex);
						} else {
							File file2 = new File(sourcePathFolder + "\\partial-bitmap-" + (int)(paddingIndex + 1) + ".txt");
							FileReader fr2 = new FileReader(file2);
							Scanner sc2 = new Scanner(fr2);
							while(sc2.hasNextLine()) {
								String line = sc2.nextLine();
								if(s.substring(startIndex, endIndex).equals(line.substring(startIndex, endIndex))) {
									print += line.substring(endIndex);
									break;
								}
							}
						}
						paddingIndex++;
					}
					pw.println(print);
					pw.flush();
				}	
			}

			System.gc();
			//Removing suplicate entry
			setOfUnique = new HashSet<>();
			pw = new PrintWriter(new File(destinationPath));
			sc = new Scanner(new File(mergedIndex));
			while(sc.hasNextLine()) {
				String s = sc.nextLine();
				if(!setOfUnique.contains(s.substring(startIndex, endIndex))) {
					setOfUnique.add(s.substring(startIndex, endIndex));
					pw.println(s);
					pw.flush();
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
