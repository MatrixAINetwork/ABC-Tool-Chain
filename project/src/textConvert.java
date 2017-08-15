import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;
import java.util.HashMap;

public class textConvert {
	private int isaNum,memNum,entryNum;
	private Pattern ISA,Labal,Mem,entry,share,local,cvt;
	private HashMap<String,String> Entry,Share,Inreg; 
	static private FileReader fin;
	static private FileWriter fout;
	static private BufferedReader sin;
	static private BufferedWriter sout;
	
	public textConvert(){
		isaNum=0;
		memNum=0;
		entryNum=0;
		Entry=new HashMap<String,String>();
		Share=new HashMap<String,String>();
		Inreg=new HashMap<String,String>();
		ISA=Pattern.compile("(((add|sub|mul|mad|div|rem|abs|neg|min|max|setp|set|and|bar|numeric|mov|selp|cvt|ld|st|tex|atom|bra|call|ret)\\.)|\\@\\%p[1234567890]+ bra)");
		Labal=Pattern.compile("(\\$L[a-zA-Z0123456789\\_]+\\:)");
		Mem=Pattern.compile("	\\.(shared)");
		entry=Pattern.compile(".param .[usf]32 ([a-zA-Z0123456789\\_]*)");
		share=Pattern.compile("\\[?(\\_\\_cuda\\_[a-zA-Z0123456789\\_]*)(\\+\\d+\\])?[\\[\\;]");
		local=Pattern.compile("\\[([a-zA-Z0123456789\\_]*)\\];");
		cvt=Pattern.compile("(cvt.[a-zA-Z0123456789.]*) 	(\\%r[0123456789]+, )\\%(tid.x|tid.y|tid.z|ntid.x|ntid.y|ntid.z|ctaid.x|ctaid.y|ctaid.z|nctaid.x|nctaid.y|nctaid.z);");
		
		
		Inreg.put("tid.x", "00001");
		Inreg.put("tid.y", "00010");
		Inreg.put("tid.z", "00011");
		Inreg.put("ntid.x", "00100");
		Inreg.put("ntid.y", "00101");
		Inreg.put("ntid.z", "00110");
		Inreg.put("ctaid.x", "00111");
		Inreg.put("ctaid.y", "01000");
		Inreg.put("ctaid.z", "01001");
		Inreg.put("nctaid.x", "01010");
		Inreg.put("nctaid.y", "01011");
		Inreg.put("nctaid.z", "01100");
	}
	
	public int Convert(){
		try{
			fin=new FileReader(new File("isa.in"));
			fout=new FileWriter(new File("isa0.out"));
			sin=new BufferedReader(fin);
			sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		String InString;		
		StringBuilder OutString;
		Matcher match;
		
		
		try{
			while((InString=sin.readLine())!=null){
				OutString=new StringBuilder();
				System.out.println(InString);
				match=entry.matcher(InString);
				if(match.find()){
					String G=match.group(1);
					StringBuilder en=new StringBuilder("000000");
					String n=Integer.toBinaryString(entryNum);
					int L=n.length();
					for(int i=0;i<10-L;i++){
						en.append("0");
					}
					en.append(n);
					Entry.put(G, en.toString());
					entryNum=entryNum+1;
				}
				match=ISA.matcher(InString);
				if(match.find()){
					isaNum=isaNum+1;
					/*String IN=Integer.toBinaryString(isaNum);
					int L=IN.length();
					for (int i=0;i<16-L;i++)
						OutString.append("0");
					OutString.append(IN);
					OutString.append(": ");*/
					OutString.append(InString);
					Matcher match2;
					match2=share.matcher(InString);
					if(match2.find()){
						String G2=match2.group(1);
						String tp=Share.get(G2);
						OutString.append("  ");
						OutString.append(tp);
					}
					match2=local.matcher(InString);
					if(match2.find()){
						String G2=match2.group(1);
						String tp=Entry.get(G2);
						OutString.append("  ");
						OutString.append(tp);
					}
					match2=cvt.matcher(InString);
					if(match2.find()){
						String G2=match2.group(3);
						
						OutString.append(Inreg.get(G2));
						
					}
					sout.write(OutString.toString());
					sout.newLine();
				}
				match=Mem.matcher(InString);
				if(match.find()){
					Matcher match2=share.matcher(InString);
					if(match2.find()){
						String G=match2.group(1);
						StringBuilder en=new StringBuilder();
						String n=Integer.toBinaryString(memNum);
						int L=n.length();
						for(int i=0;i<6-L;i++){
							en.append("0");
						}
						en.append(n);
						en.append("0000000000");
						Share.put(G, en.toString());
						memNum=memNum+1;
					}
					
					
				}
					
				match=Labal.matcher(InString);
				if(match.find()){
					sout.write(InString);
					sout.newLine();
					
				}
				
			}
			sout.write("1111111111111111: 	exit");
			sout.newLine();
			sout.close();
			sin.close();
		}catch (IOException e){
			e.printStackTrace();
		}
				
		return memNum;		
		
	}

}
