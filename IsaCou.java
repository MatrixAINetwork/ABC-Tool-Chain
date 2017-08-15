import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;

public class IsaCou {
	private Pattern ISA;
	private int isaNum;
	static private FileReader fin;
	static private FileWriter fout;
	static private BufferedReader sin;
	static private BufferedWriter sout;
	
	public IsaCou(){
		isaNum=0;
		ISA=Pattern.compile("(((add|sub|mul|mad|div|rem|abs|neg|min|max|setp|set|and|bar|numeric|mov|selp|cvt|ld|st|tex|atom|bra|call|ret)\\.)|\\@\\%p[1234567890]+ bra)");
	}
	public void count(){
		try{
			fin=new FileReader(new File("isa00.out"));
			fout=new FileWriter(new File("isa000.out"));
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
				match=ISA.matcher(InString);
				if(match.find()){
					isaNum=isaNum+1;
					String IN=Integer.toBinaryString(isaNum);
					int L=IN.length();
					for (int i=0;i<16-L;i++)
						OutString.append("0");
					OutString.append(IN);
					OutString.append(": ");
					OutString.append(InString);
					sout.write(OutString.toString());
					sout.newLine();
				}
				else{
					OutString.append(InString);
					sout.write(OutString.toString());
					sout.newLine();
				}
			}
			sin.close();
			sout.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
