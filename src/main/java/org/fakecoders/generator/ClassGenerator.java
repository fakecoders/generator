package org.fakecoders.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ClassGenerator {
	public static void main(final String[] args) throws IOException, URISyntaxException {
		System.out.println("@Author Fakecoders");
		System.out.println("Welcome to Code generator service :)");
		final Scanner scanner = new Scanner(System.in);
		System.out.println("Destination Folder Path Without Package (For Example /home/fakecoders/)");
		String destBaseUrl = scanner.next();
		System.out.println("Enter package name (Example com.inn.fakecoders)");
		final String basePackageName = scanner.next();
		System.out.println("Enter class name to be generate");
		String myclassName = scanner.next();
		System.out.println("Enter table name");
		String tableName = scanner.next();
		String tableNameForTableGenerator = tableName.toUpperCase();
		
		final String appType = "Resources/";
		final String basePackageNameWithslash = basePackageName.replaceAll("\\.", "/");
		destBaseUrl = String.valueOf(destBaseUrl) + "/" + basePackageNameWithslash + "/";
		final File folderPath = new File(destBaseUrl);
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		final String restpath = folderPath + "/rest/impl";
		final String servicepath = folderPath + "/service/impl";
		final String daopath = folderPath + "/repo/impl";
		final String modelpath = folderPath + "/model";
		new File(restpath).mkdirs();
		new File(servicepath).mkdirs();
		new File(daopath).mkdirs();
		new File(modelpath).mkdirs();
		
		setPathForClasses(appType, myclassName, destBaseUrl, basePackageName, tableName, tableNameForTableGenerator);
		showProgressBar();
		System.out.println("\nCode generated successfully, Do you wish to continue with same configuration? (Yes/No)");
		String continueFlag;
		for (continueFlag = scanner.next(); !continueFlag.equalsIgnoreCase("yes"); continueFlag = scanner.next()) {
			if (continueFlag.equalsIgnoreCase("no")) {
				System.out.println("\n Please Define your package in your class");
				break;
			}
			System.out.println("Please choose valid option (Yes/No)");
		}
		while (continueFlag.equalsIgnoreCase("Yes")) {
			System.out.println("Enter Class Name to be generate");
			myclassName = scanner.next();
			System.out.println("Enter Table Name (Example task_detail)");
			tableName = scanner.next();
			tableNameForTableGenerator = tableName.toUpperCase();
			setPathForClasses(appType, myclassName, destBaseUrl, basePackageName, tableName, tableNameForTableGenerator);
			showProgressBar();
			System.out.println("\nCode Generated Successfully,Do you wish to continue with same configuration? (Yes/No)");
			for (continueFlag = scanner.next(); !continueFlag.equalsIgnoreCase("yes") && !continueFlag.equalsIgnoreCase("no"); continueFlag = scanner.next()) {
				System.out.println("Please choose valid option (Yes/No)");
			}
		}
		System.out.println("Bye!Goodluck");
		scanner.close();
	}
	
		private static void showProgressBar() {
			System.out.println("..");
			System.out.println("Wait class generation in progress");
			printDot(3, 10);
			printDot(8, 20);
			printDot(15, 40);
			printDot(20, 60);
			printDot(25, 80);
			printDot(30, 100);
		}
		
		static void printDot(final int length, final int percentage) {
			for (int i = 0; i < length; ++i) {
				try {
					Thread.sleep(60L);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.print(".");
			}
			System.out.println(String.valueOf(percentage) + "%");
			try {
				Thread.sleep(150L);
			}
			catch (InterruptedException e2) {
				e2.printStackTrace();
			}
		}
		
		static void setPathForClasses(final String appType, final String myclassName, final String destBaseUrl, final String basePackageName, final String tableName, final String tableNameForTableGenerator) throws IOException {
			final String sourcePojoPath = String.valueOf(appType) + "Example.txt";
			final String sourceDaoImplPath = String.valueOf(appType) + "ExampleRepoImpl.txt";
			final String sourceIDaoPath = String.valueOf(appType) + "IExampleRepo.txt";
			final String sourceIServicePath = String.valueOf(appType) + "IExampleService.txt";
			final String sourceServiceImplPath = String.valueOf(appType) + "ExampleServiceImpl.txt";
			final String sourceRestImplPath = String.valueOf(appType) + "ExampleRestImpl.txt";
			final String pojoName = String.valueOf(myclassName) + ".java";
			final String restName = String.valueOf(myclassName) + "RestImpl.java";
			final String serviceName = String.valueOf(myclassName) + "ServiceImpl.java";
			final String IserviceName = "I" + myclassName + "Service.java";
			final String daoName = String.valueOf(myclassName) + "RepoImpl.java";
			final String IdaoName = "I" + myclassName + "Repo.java";
			final String destDaoImplPath = String.valueOf(destBaseUrl) + "repo/impl/" + daoName;
			
			final String destIDaoPath = String.valueOf(destBaseUrl) + "repo/" + IdaoName;
			final String destServiceImplPath = String.valueOf(destBaseUrl) + "service/impl/" + serviceName;
			final String destRestImplPath = String.valueOf(destBaseUrl) + "rest/impl/" + restName;
			final String destPojoPath = String.valueOf(destBaseUrl) + "model/" + pojoName;
			final String destIServicePath = String.valueOf(destBaseUrl) + "service/" + IserviceName;
			String content = "";
			final List<String> sourceArrayList = new ArrayList<String>();
			sourceArrayList.add(sourcePojoPath);
			sourceArrayList.add(sourceRestImplPath);
			sourceArrayList.add(sourceServiceImplPath);
			sourceArrayList.add(sourceIServicePath);
			sourceArrayList.add(sourceIDaoPath);
			final List<String> destArrayList = new ArrayList<String>();
			destArrayList.add(destPojoPath);
			destArrayList.add(destRestImplPath);
			destArrayList.add(destServiceImplPath);
			destArrayList.add(destIServicePath);
			destArrayList.add(destIDaoPath);
			
			sourceArrayList.add(sourceDaoImplPath);
			destArrayList.add(destDaoImplPath);
			int index = -1;
			for (final String sourceElement : sourceArrayList) {
				++index;
				content = getStringFormOfFile(sourceElement);
				content = getReplacedStringFor(content, myclassName, basePackageName, tableName, tableNameForTableGenerator);
				createFile(content, destArrayList.get(index));
			}

		}
		
		public static String getStringFormOfFile(final String fileReadPath) throws IOException {
			final StringBuilder builder = new StringBuilder();
			final InputStream fin = ClassGenerator.class.getResourceAsStream(fileReadPath);
			int ch;
			while ((ch = fin.read()) != -1) {
				builder.append((char)ch);
			}
			final String content = builder.toString();
			return content;
		}

		public static String getReplacedStringFor(final String daoImplStringForm, final String MyclassName, final String basePackageName, final String tableName, final String tableNameForTableGenerator) {
			return daoImplStringForm.replaceAll("Example", MyclassName).replaceAll("example", MyclassName.toLowerCase()).replaceAll("base_pkg", basePackageName).replaceAll("table_name", tableName).replaceAll("tn_caps", tableNameForTableGenerator);
		}

		public static void createFile(final String content, final String path) throws IOException {
			final FileOutputStream fos = new FileOutputStream(path);
			fos.write(content.getBytes());
			fos.close();
		}
}
