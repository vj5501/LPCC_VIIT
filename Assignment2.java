import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
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
		int size;
		
		symbolTable(String symbol, int loc, int size){
			this.symbol=symbol;
			this.loc=loc;
			this.size=size;
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
	
	public static class poolTable{
		int loc;
		
		poolTable(int loc){
			this.loc=loc;
		}
	}
	
	public static int findInEmot(String s, ArrayList<emot> emotTable) {
		
		for(int i=0; i<emotTable.size(); i++) {
			if(s.equals(emotTable.get(i).Mnemonic)) {
				return 1;
			}
		}
		
		return 0;
	}
	
	public static int findInSymbolTable(String symbol, ArrayList<symbolTable> table) {
		for(int i=0; i<table.size(); i++) {
			if(table.get(i).symbol.equals(symbol)) {
				return table.get(i).loc;
			}
		}
		return -1;
	}
	
	public static void replaceInSymbolTable(String symbol, int loc, ArrayList<symbolTable> table) {
		for(int i=0; i<table.size(); i++) {
			if(table.get(i).symbol.equals(symbol)) {
				table.get(i).loc=loc;
			}
		}
	}
	
	public static ArrayList<symbolTable> generateSymbolTable(ArrayList<emot> emotTable) throws IOException{
		
		ArrayList<symbolTable> symbolTable=new ArrayList<>();
		
		//open file
		File file=new File("A2_PROG-1.txt");
				
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
					symbolTable.add(new symbolTable(sub[0], lc, 1));
				}
				
			}
			
			i++;
		}
		
		//print symbol table
		System.out.println("Symbol  Location  Size");
		System.out.println("===========================");
		for(int j=0; j<symbolTable.size(); j++) {
			System.out.println(symbolTable.get(j).symbol+"       "+symbolTable.get(j).loc+"      "+symbolTable.get(j).size);
		}
		
		return symbolTable;
	}
	
	
	
	public static void generateLiteralTable(ArrayList<emot> emotTable) throws IOException {
		

		ArrayList<literalTable> literalTable=new ArrayList<>();
		
		//open file
		File file=new File("A2_PROG-2.txt");
				
		//open for read mode
		BufferedReader bf=new BufferedReader(new FileReader(file));
		int totalLines=Integer.parseInt((bf.lines().count()-2)+"");
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
				if(sub[sub.length-1].startsWith("='") && sub[sub.length-1].endsWith("'")) {
					String last=sub[sub.length-1].replace("='", "").replace("'", "");
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
	
	public static void generateLiteralAndPoolTable(ArrayList<emot> emotTable) throws IOException{
		ArrayList<literalTable> literalTable=new ArrayList<>();
		ArrayList<Integer> literals=new ArrayList<>();
		ArrayList<poolTable> poolTable=new ArrayList<>();
		poolTable.add(new poolTable(0));
		
		//open file
		File file=new File("A2_PROG-3.txt");
				
		//open for read mode
		BufferedReader bf=new BufferedReader(new FileReader(file));
				
		String s="";
		int i=0;
		int lc=0;
		int start=0;
		
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
				
				//LTORG encountered
				int loc=lc;
				if(sub[0].trim().equals("LTORG")) {
					for(int j=0; j<literals.size(); j++) {
						literalTable.add(new literalTable(literals.get(j)+"", loc));
						loc++;
						i++;
					} 
					i--;
					poolTable.add(new poolTable(literalTable.size()));
					literals.clear();
				}
				else if(sub[sub.length-1].startsWith("='") && sub[sub.length-1].endsWith("'")) {
					//check if curr symbol is literal
					
					String last=sub[sub.length-1].replace("='", "").replace("'", "");
					int literal=Integer.parseInt(last);
					
					//store encountered literals temporarily
					literals.add(literal);
				}
			}
			i++;
		}
		
		//print literal table
		System.out.println("Literal  Location");
		System.out.println("====================");
		for(int j=0; j<literalTable.size(); j++) {
			System.out.println(literalTable.get(j).literal+"       "+literalTable.get(j).loc);
		}
		
		System.out.println("\n");
		
		System.out.println("Pool Table");
		System.out.println("====================");
		System.out.println("Literal Number");
		for(int j=0; j<poolTable.size(); j++) {
			System.out.println(poolTable.get(j).loc);
		}
		
	}
	
	public static void generateSTAndLTWithOriginAndEqu(ArrayList<emot> emotTable) throws IOException{
		
		ArrayList<symbolTable> symbolTable=new ArrayList<>();
		
		ArrayList<literalTable> literalTable=new ArrayList<>();
		ArrayList<Integer> literals=new ArrayList<>();
		ArrayList<poolTable> poolTable=new ArrayList<>();
		poolTable.add(new poolTable(0));
		
		//open file
		File file=new File("A2_PROG-4.txt");
				
		//open for read mode
		BufferedReader bf=new BufferedReader(new FileReader(file));
				
		String s="";
		int i=0;
		int lc=0;
		int start=0;
		
		while((s=bf.readLine())!=null) {
			//System.out.println(lc);
			if(lc==0) {
				s=s.replace(",", " ");
				String[] sub=s.split(" ");
				start=Integer.parseInt(sub[sub.length-1]);
				lc=start-1;
			}
			else {
				//split string
				s=s.replace(",", " ");
				String[] sub=s.split(" ");
				
				//LTORG encountered
				int loc=lc;
				//for ltorg
				if(sub[0].trim().equals("LTORG")) {
					for(int j=0; j<literals.size(); j++) {
						literalTable.add(new literalTable(literals.get(j)+"", loc));
						loc++;
					} 
					poolTable.add(new poolTable(literalTable.size()));
					literals.clear();
					lc=loc;
					lc--;
				}
				//for origin
				if(sub[0].trim().equals("ORIGIN")) {
					//get symbol
					String symbol=sub[sub.length-1];
					
					symbol=symbol.replace("+", " ");
					String[] operators=symbol.split(" ");
					
					if(operators.length==0) {
						//no plus operator
					}
					else {
						String sym=operators[0].trim();
						int inc=Integer.parseInt(operators[1].trim());
						
						int symLoc=findInSymbolTable(sym, symbolTable);
						
						//update lc
						lc=symLoc+inc;
						lc=lc-1;
					}
				}
				//for literal
				if(sub[sub.length-1].startsWith("='") && sub[sub.length-1].endsWith("'")) {
					//check if curr symbol is literal
					String last=sub[sub.length-1].replace("='", "").replace("'", "");
					int literal=Integer.parseInt(last);
					
					//store encountered literals temporarily
					literals.add(literal);
				}
				//for symbol table
				if(findInEmot(sub[0].trim(), emotTable)==0) {
					//current mneumonic is symbol
					symbolTable.add(new symbolTable(sub[0], lc, 1));
				}
				//for EQU
				if(sub.length==3 && sub[1].equals("EQU")) {
					//symbol whose location is required
					String sym=sub[2];
					int symLoc=findInSymbolTable(sym.trim(), symbolTable);
					
					//symbol whose location to be equated
					String symToReplace=sub[0];
					replaceInSymbolTable(symToReplace, symLoc, symbolTable);
					
				}
			}
			lc++;
		}
		
		//print literal table
		System.out.println("Literal  Location");
		System.out.println("====================");
		for(int j=0; j<literalTable.size(); j++) {
			System.out.println(literalTable.get(j).literal+"       "+literalTable.get(j).loc);
		}
		
		System.out.println("\n");
		
		//print pool table
		System.out.println("Pool Table");
		System.out.println("====================");
		System.out.println("Literal Number");
		for(int j=0; j<poolTable.size(); j++) {
			System.out.println(poolTable.get(j).loc);
		}
		
		System.out.println("\n");
		
		//print symbol table
		System.out.println("Symbol Table");
		System.out.println("======================");
		System.out.println("Symbol   Location");
		for(int j=0; j<symbolTable.size(); j++) {
			System.out.println(symbolTable.get(j).symbol+"      "+symbolTable.get(j).loc);
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
		
		System.out.println("Symbol Table");
		generateSymbolTable(emotTable);
		System.out.println();
		
		
		//que2
		System.out.println("Question-2");
		System.out.println("===========================");
		System.out.println();
		
		generateLiteralTable(emotTable);
		System.out.println();
		
		
		//que3
		System.out.println("Question-3");
		System.out.println("===========================");
		System.out.println();
		
		generateLiteralAndPoolTable(emotTable);
		System.out.println();
		
		
		//que4
		System.out.println("Question-4");
		System.out.println("===========================");
		System.out.println();
		
		generateSTAndLTWithOriginAndEqu(emotTable);
		System.out.println();

	}

}
