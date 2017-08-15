import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class assembler {
	static private FileReader fin;
	static private FileWriter fout;
	static private BufferedReader sin;
	static private BufferedWriter sout;
	
	public static void main(String[] args){
		//res Res=new res();
		//Res.Convert();
		
		textConvert textconvert=new textConvert();
		int memNum=textconvert.Convert();
		
		regAllo regallo=new regAllo();
		regallo.allocation();
		
		IsaCou isacou=new IsaCou();
		isacou.count();
		
		labalConvert labalconvert=new labalConvert(memNum);
		labalconvert.Convert();
		
		conrecon recon=new conrecon();
		recon.count();
		
		//reconvCount reconvcount=new reconvCount();
		//reconvcount.Count();
		//reCon reconvcount=new reCon();
		//reconvcount.Count();
		
		String InString,OutString;
		isaconvert2 isaconvert2=new isaconvert2();
		try{
			fin=new FileReader(new File("isare.out"));
			fout=new FileWriter(new File("isafinal.out"));
			sin=new BufferedReader(fin);
			sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		try{
			while((InString=sin.readLine())!=null){
				System.out.println(InString);
				OutString=isaconvert2.Convert(InString);
				
					sout.write(OutString);
					sout.newLine();
				
			}
			sin.close();
			sout.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		
		try{
			fin=new FileReader(new File("isafinal.out"));
			fout=new FileWriter(new File("isadebug.out"));
			sin=new BufferedReader(fin);
			sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		try{
			while((InString=sin.readLine())!=null){
				System.out.println(InString);
				StringBuilder A=new StringBuilder();
				for(int i=0;i<64;i++){
					char AA=InString.charAt(i);
					A.append(AA);
					if((i+1)%8==0&&i!=63)
						A.append("_");
				}
				sout.write(A.toString());
				sout.newLine();
				
			}
			for(int i=0; i<20;i++){
				sout.write("00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000000");
				sout.newLine();
			}
			sin.close();
			sout.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
}
