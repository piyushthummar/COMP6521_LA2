package com.bitmap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class RemoveDuplicationAndMerge {

	public RemoveDuplicationAndMerge() {

	}

	public void startRemovingDuplication(String bitmapPath, String recordPath, String outputPath) {
		long startRemovalTime = System.currentTimeMillis();
		try {
			FileReader bitmapFile = new FileReader(new File(bitmapPath));
			// FileReader recordFile = new FileReader(new File("./10K-test.txt"));
			FileWriter outputFile = new FileWriter(new File(outputPath));
			BufferedWriter outputWriter = new BufferedWriter(outputFile);
			Scanner bitmapScan = new Scanner(bitmapFile);
			// Scanner recordScan;
			ArrayList<String> duplicates;
			int recordCount = 0;
			while (bitmapScan.hasNextLine()) {
				System.gc();
				String bitmapRecord = bitmapScan.nextLine();
				String bitmapIndex = bitmapRecord.substring(8);
				Scanner recordScan = new Scanner(new FileReader(new File(recordPath)));
				int index = 0;
				duplicates = new ArrayList<>();
				// System.out.println("Reading bitmap of " + bitmapRecord.substring(0, 8) + "
				// with index " + index);
				while (recordScan.hasNextLine()) {
					// System.out.print(index + "-");
					if (bitmapIndex.charAt(index) == '1') {
						String record = recordScan.nextLine();
						duplicates.add(record);
						// System.out.println("duplicate found for " + duplicates);

					} else {
						recordScan.nextLine();
					}
					index++;
				}
				String latestRecord = removeDuplicates(duplicates);
				outputWriter.write(latestRecord);
				outputWriter.newLine();
				outputWriter.flush();
				recordCount++;
				// System.out.println(recordCount + " - " + latestRecord.substring(0, 18) + " is
				// written from " + duplicates.size() + " duplicates");
			}
			System.out.println("Duplicates removed!!");
			long endRemovalTime = System.currentTimeMillis();
			System.out.println("Time for removal is " + (endRemovalTime - startRemovalTime) + " ms");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String removeDuplicates(ArrayList<String> duplicates) {
		// System.out.println("function->" + duplicates);
		String latestRecord = "";
		Collections.sort(duplicates, Collections.reverseOrder());
		latestRecord = duplicates.get(0);
		// if(duplicates.size() > 1) {
		// System.out.println(duplicates.get(0).substring(0, 18) + " has " +
		// duplicates.size() + " duplicate records");
		// }
		return latestRecord;
	}

	public void startMerging() {
		long startMergeTime = System.currentTimeMillis();
		File folder = new File("./IO_Files");
		ArrayList<File> filePointers = new ArrayList<>();
		System.out.println("Listing files...");
		for (File file : folder.listFiles()) {
			if (file.getName().startsWith("without-duplicates")) {
				System.out.println(file.getName());
				filePointers.add(file);
			}
		}
//		System.out.println(filePointers.size());

		// Start Merging...
		Scanner scanOne;
		// FileWriter fw;
		BufferedWriter bw = null;
		try {
			// fw = new FileWriter(new File("./merged-file.txt"));
			bw = new BufferedWriter(new FileWriter(new File("./IO_Files/merged-file.txt")));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			int recordCount = 0;
			scanOne = new Scanner(filePointers.get(0));
			HashSet<String> uniqueRecords = new HashSet();
			ArrayList<String> duplicatesBetweenFiles;
			// Checking for File 1 records...
			while (scanOne.hasNextLine()) {
				System.gc();
				String s = scanOne.nextLine();
				uniqueRecords.add(s.substring(0, 8));
				Scanner scanTwo = new Scanner(new FileReader(filePointers.get(1)));
				duplicatesBetweenFiles = new ArrayList<>();
				duplicatesBetweenFiles.add(s);
				while (scanTwo.hasNextLine()) {
					String line = scanTwo.nextLine();
					if (s.substring(0, 8).equals(line.substring(0, 8))) {
						duplicatesBetweenFiles.add(line);
					}
				}
				Collections.sort(duplicatesBetweenFiles, Collections.reverseOrder());
				bw.write(duplicatesBetweenFiles.get(0));
				bw.newLine();
				bw.flush();
				recordCount++;
//				System.out.println(recordCount);
			}
			// Checking for File 2 records...
			scanOne = new Scanner(filePointers.get(1));
			while (scanOne.hasNextLine()) {
				System.gc();
				String s = scanOne.nextLine();
				// duplicatesBetweenFiles = new ArrayList<>();
				if (!uniqueRecords.contains(s.substring(0, 8))) {
					uniqueRecords.add(s.substring(0, 8));
					bw.write(s);
					bw.newLine();
					bw.flush();
					recordCount++;
//					System.out.println(recordCount);
				}
			}

			System.out.println("Merging done!!!");
			System.out.println("Unique Records : " + uniqueRecords.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long endMergeTime = System.currentTimeMillis();
		System.out.println("Merge Time is " + (endMergeTime - startMergeTime) + " ms");
		System.gc();
		
		//Sorting the merged file...
		try {
			Scanner scan = new Scanner(new FileReader(new File("./IO_Files/merged-file.txt")));
			BufferedWriter sortedFile = new BufferedWriter(new FileWriter(new File("./IO_Files/sorted-file.txt")));
			ArrayList<String> records = new ArrayList<>();
			while(scan.hasNextLine()) {
				String line = scan.nextLine();
				records.add(line);
			}
			Collections.sort(records);
			
			for(String line : records) {
				sortedFile.write(line);
				sortedFile.newLine();
				sortedFile.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
