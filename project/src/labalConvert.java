import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;
import java.util.HashMap;

public class labalConvert {
	private int LineNum;//,memNum;
	//private int [] memDiv;
	private Pattern Labal1,Labal2,Labal3,Mem,addr;
	private HashMap<String,String> labal,mem;
	static private FileReader fin,ffin;
	static private FileWriter fout;
	static private BufferedReader sin,ssin;
	static private BufferedWriter sout;
	
	public labalConvert(int i){
		LineNum=0;
		//memNum=i;
		/*memDiv=new int [memNum+1];
		for(int ii=0;ii<=memNum;ii++){
			memDiv[ii]=0;
		}*/
		labal=new HashMap<String,String>();
		mem=new HashMap<String,String>();
		Mem=Pattern.compile("	\\.(shared|local|global|const)");
		addr=Pattern.compile("(\\_\\_cuda\\_\\_[a-zA-Z0123456789\\_]+)\\[([0123456789]+)\\];");
		Labal1=Pattern.compile("((\\$L[a-zA-Z0123456789\\_]+)\\;)");
		Labal2=Pattern.compile("((\\$L[a-zA-Z0123456789\\_]+)\\:)");
		Labal3=Pattern.compile("([01][01][01][01][01][01][01][01][01][01][01][01][01][01][01][01]):");
	}
	
	public void Convert(){
		try{
			fin=new FileReader(new File("isa000.out"));
			fout=new FileWriter(new File("isa.out"));
			sin=new BufferedReader(fin);
			sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		String InString;
		Matcher match1,match2;
		int mn=0;
		try{
			while((InString=sin.readLine())!=null){
				System.out.println(InString);
				LineNum=LineNum+1;
				match1=Mem.matcher(InString);
				match2=addr.matcher(InString);
				if(match1.find()){
					mn=mn+1;
					if(match2.find()){
						String G0=match2.group(1);
						mem.put(G0, Integer.toString(mn));
					}
				}
				
				
				match1=Labal1.matcher(InString);
				match2=Labal2.matcher(InString);
				if(match1.find()){
					System.out.println(InString);
					String LL=match1.group(2);
					String IInString;
					
					try{
						ffin=new FileReader(new File("isa000.out"));
						ssin=new BufferedReader(ffin);
					} catch (FileNotFoundException e){
						e.printStackTrace();
					} 
					try{
						while((IInString=ssin.readLine())!=null){
							Matcher mm=Labal2.matcher(IInString);
							if (mm.find()){
								System.out.println(IInString);
								String GetL=mm.group(2);
								if (GetL.equals(LL)){
									while((IInString=ssin.readLine())!=null){
										Matcher mm2=Labal3.matcher(IInString);
										if (mm2.find()){
											String LL2=mm2.group(1);
											labal.put(LL, LL2);
											break;
										}
									}
								}
							}
							
						}
					} catch (IOException e){
						e.printStackTrace();
					}
					ssin.close();
					
					sout.write(InString);
					sout.write(labal.get(LL));
					sout.newLine();
				
				}	
				else if (!match2.find()){
					sout.write(InString);
					sout.newLine();
				}
			}
			sin.close();
			sout.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
