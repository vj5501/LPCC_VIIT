package com.lpcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Assign1 {

	public static class emot{
		String Mnemonic;
		int Class;
		String Opcode;
		
		emot(String Mnemonic, int Class, String Opcode){
			this.Mnemonic=Mnemonic;
			this.Class=Class;
			this.Opcode=Opcode;
		}
	}
	
	public static class symbolTable{
		String symbol;
		int loc;
		
		symbolTable(String symbol, int loc){
			this.symbol=symbol;
			this.loc=loc;
		}
		
		@Override
		public String toString() {
			return "symbolTable [symbol=" + symbol + ", loc=" + loc + "]";
		}	
	}
	
	public static class literalTable{
		String literal;
		int loc;
		
		literalTable(String literal, int loc){
			this.literal=literal;
			this.loc=loc;
		}

		@Override
		public String toString() {
			return "literalTable [literal=" + literal + ", loc=" + loc + "]";
		}
		
	}
	
	public static void acceptALP() {
		Scanner s=new Scanner(System.in);
		String ALP="";
		 
		System.out.println("Enter 1 line of ALP: ");
		ALP=s.nextLine();
		
		//replace comma with space
		ALP=ALP.replace(",", " ");
		
		//split into substrings
		String[] substr=ALP.split(" ");
		
		//substrings count
		int count=substr.length;
		
		//print substrings 
		System.out.println("Substrings: ");
		for(int i=0; i<substr.length; i++) {
			System.out.println(substr[i]);
		}
		System.out.println();
		
		//print count
		System.out.println("Count of substrings: "+count);
	}
	
	public static void printALP() throws IOException {
		//open file
		File file=new File("a.txt");
		
		//open for read mode
		BufferedReader bf=new BufferedReader(new FileReader(file));
		
		String s="";
		int i=1;
		
		//read string
		while((s=bf.readLine())!=null) {
			System.out.println(i+". "+s);
			i++;
		}
		
		//close read mode
		bf.close();
	}
	
	public static int findInEmot(String s, ArrayList<emot> emotTable) {
		
		for(int i=0; i<emotTable.size(); i++) {
			if(s.equals(emotTable.get(i).Mnemonic)) {
				return 1;
			}
		}
		
		return 0;
	}
	
	public static void generateSymbolTable(ArrayList<emot> emotTable) throws IOException{
		
		ArrayList<symbolTable> symbolTable=new ArrayList<>();
		
		//open file
		File file=new File("a.txt");
				
		//open for read mode
		BufferedReader bf=new BufferedReader(new FileReader(file));
				
		String s="";
		int i=0;
		int lc=0;
		int start=0;
				
		//read string
		while((s=bf.readLine())!=null) {
			if(i==0) {
				s=s.replace(",", " ");
				String[] sub=s.split(" ");
				start=Integer.parseInt(sub[sub.length-1]);
			}
			else {
				//update location counter
				lc=start+i-1;
				
				//split string
				String[] sub=s.split(" ");
				
				//check if emot table contains current symbol
				int res=findInEmot(sub[0], emotTable);
				
				if(res==0) {
					//not found in emot table, means it is a symbol
					symbolTable.add(new symbolTable(sub[0], lc));
				}
				
			}
			
			i++;
		}
		
		//print symbol table
		System.out.println("Symbol  Location");
		System.out.println("====================");
		for(int j=0; j<symbolTable.size(); j++) {
			System.out.println(symbolTable.get(j).symbol+"       "+symbolTable.get(j).loc);
		}
	}
	
	public static void generateLiteralTable1(ArrayList<emot> emotTable) throws IOException {
		
		generateSymbolTable(emotTable);
		System.out.println();
		
		ArrayList<literalTable> literalTable=new ArrayList<>();
		
		//open file
		File file=new File("a.txt");
				
		//open for read mode
		BufferedReader bf=new BufferedReader(new FileReader(file));
		int totalLines=Integer.parseInt((bf.lines().count()-1)+"");
		bf=new BufferedReader(new FileReader(file));
				
		String s="";
		int i=0;
		int lc=0;
		int start=0;
		
		//read string
		while((s=bf.readLine())!=null) {
			if(i==0) {
				s=s.replace(",", " ");
				String[] sub=s.split(" ");
				start=Integer.parseInt(sub[sub.length-1]);
			}
			else {
				//update location counter
				lc=start+i-1;
				
				//split string
				s=s.replace(",", " ");
				String[] sub=s.split(" ");
				
				//check if current symbol is literal
				if(sub[sub.length-1].startsWith("'") && sub[sub.length-1].endsWith("'")) {
					String last=sub[sub.length-1].replace("'", "").replace("'", "");
					int literal=Integer.parseInt(last);
					
					//add into literal table
					if(literalTable.size()==0) {
						literalTable.add(new literalTable(literal+"", totalLines+start));
					}
					else {
						int prevLoc=literalTable.get(literalTable.size()-1).loc;
						literalTable.add(new literalTable(literal+"", prevLoc+1));
					}
				}
				
			}
			
			i++;
		}
		
		//print literalTable
		System.out.println("Literal  Location");
		System.out.println("====================");
		for(int j=0; j<literalTable.size(); j++) {
			System.out.println(literalTable.get(j).literal+"       "+literalTable.get(j).loc);
		}
	}
	
	public static void generateLiteralTable2(ArrayList<emot> emotTable) throws IOException {
		
		ArrayList<Integer> literalTable=new ArrayList<>();
		
		//open file
		File file=new File("p5.txt");
				
		//open for read mode
		BufferedReader bf=new BufferedReader(new FileReader(file));
		
		String s="";
		int i=0;
		int lc=0;
		int start=0;
		
		//read string
		while((s=bf.readLine())!=null) {
			if(i==0) {
				s=s.replace(",", " ");
				String[] sub=s.split(" ");
				start=Integer.parseInt(sub[sub.length-1]);
			}
			else {
				//update location pointer
				lc=start+i-1;
				
				//split string
				s=s.replace(",", " ");
				String[] sub=s.split(" ");
				
				//check if current symbol is literal
				if(sub[sub.length-1].startsWith("='") && sub[sub.length-1].endsWith("'")) {
					String last=sub[sub.length-1].replace("='", "").replace("'", "");
					int literal=Integer.parseInt(last);
					
					//add into literal table
					literalTable.add(literal);
				}
				
			}
			
			i++;
		}
		
		//print literalTable
		System.out.println("Literal");
		System.out.println("====================");
		for(int j=0; j<literalTable.size(); j++) {
			System.out.println(literalTable.get(j));
		}
	}
	
	
	
	public static void main(String[] args) throws IOException {
		ArrayList<emot> emotTable=new ArrayList<>();
		emotTable.add(new emot("STOP", 1, "00"));
		emotTable.add(new emot("ADD", 1, "01"));
		emotTable.add(new emot("SUB", 1, "02"));
		emotTable.add(new emot("MULT", 1, "03"));
		emotTable.add(new emot("MOVER", 1, "04"));
		emotTable.add(new emot("MOVEM", 1, "05"));
		emotTable.add(new emot("COMP", 1, "06"));
		emotTable.add(new emot("BC", 1, "07"));
		emotTable.add(new emot("DIV", 1, "08"));
		emotTable.add(new emot("READ", 1, "09"));
		emotTable.add(new emot("PRINT", 1, "10"));
		emotTable.add(new emot("START", 3, "01"));
		emotTable.add(new emot("END", 3, "02"));
		emotTable.add(new emot("ORIGIN", 3, "03"));
		emotTable.add(new emot("EQU", 3, "04"));
		emotTable.add(new emot("LTORG", 3, "05"));
		emotTable.add(new emot("DS", 2, "01"));
		emotTable.add(new emot("DC", 2, "02"));
		emotTable.add(new emot("AREG", 4, "01"));
		emotTable.add(new emot("BREG", 4, "02"));
		emotTable.add(new emot("CREG", 4, "03"));
		emotTable.add(new emot("EQ", 5, "01"));
		emotTable.add(new emot("LT", 5, "02"));
		emotTable.add(new emot("GT", 5, "03"));
		emotTable.add(new emot("NE", 5, "04"));
		emotTable.add(new emot("LE", 5, "05"));
		emotTable.add(new emot("GT", 5, "06"));
		emotTable.add(new emot("ANY", 5, "07"));
		
		
		//que1
		System.out.println("Question-1");
		System.out.println("===========================");
		System.out.println();
		
		acceptALP();
		System.out.println('\n');
		
		//que2
		System.out.println("Question-2");
		System.out.println("===========================");
		System.out.println();
		
		printALP();
		System.out.println('\n');
		
		//que3
		System.out.println("Question-3");
		System.out.println("===========================");
		System.out.println();
		
		generateSymbolTable(emotTable);
		System.out.println('\n');
		
		//que4
		System.out.println("Question-4");
		System.out.println("===========================");
		System.out.println();
		generateLiteralTable1(emotTable);
		System.out.println('\n');
		
		//que5
		System.out.println("Question-5");
		System.out.println("===========================");
		System.out.println();
		generateLiteralTable2(emotTable);
		System.out.println('\n');
		
	}

}
