import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;
import java.util.HashMap;

public class regAllo {
	
	private Pattern reg;
	private HashMap <String, Integer> alloreg1,allomem;
	private HashMap <Integer, String> alloreg2;
	private HashMap <String, String> regstate;
	private int regocu[];
	static private FileReader fin,ffin;
	static private FileWriter fout;
	static private BufferedReader sin,ssin;
	static private BufferedWriter sout;
	
	public regAllo(){
		alloreg1=new HashMap <String, Integer>();
		allomem=new HashMap <String,Integer>();
		alloreg2=new HashMap <Integer,String>();
		regstate=new HashMap <String,String>();
		regocu=new int[17];
		reg=Pattern.compile("(%[rf][0123456789]+)([,;+])");
		for(int i=1;i<=16;i++)
			regocu[i]=0;
	}
	public void allocation(){
		int lineNum=0;
		int MemNum=0;
		
		try{
			fin=new FileReader(new File("isa0.out"));
			fout=new FileWriter(new File("isa00.out"));
			sin=new BufferedReader(fin);
			sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		String InString;		
		StringBuffer OutString;
		Matcher match;
		try{
			while((InString=sin.readLine())!=null){
				for(int i=1;i<=16;i++){
					if (regocu[i]==2)
						regocu[i]=0;
				}
				lineNum=lineNum+1;
				//OutString=new StringBuffer();
				System.out.println(InString);
				match=reg.matcher(InString);
				while(match.find()){
					String reNm=match.group(1);
					System.out.println(reNm);
					boolean allsec=false;
					if(!"1".equals(regstate.get(reNm))  && !"2".equals(regstate.get(reNm))){
						
						for(int i=1;i<=16;i++){
							if(regocu[i]==0){
								allsec=true;
								regocu[i]=1;
								alloreg1.put(reNm, i);
								alloreg2.put(i, reNm);
								regstate.put(reNm, "1");
								break;
							}
						}			
					}
					
					if("2".equals(regstate.get(reNm))){
						Integer sl=0;
						for(int i=1;i<=16;i++){
							if(regocu[i]==0){
								allsec=true;
								regocu[i]=1;
								alloreg1.put(reNm, i);
								alloreg2.put(i, reNm);
								regstate.put(reNm, "1");
								sl=i;
								break;
							}
						}
						if(allsec){
							OutString=new StringBuffer();
							OutString.append("	ld.global.s32 	%r");
							String ew=sl.toString();
							OutString.append(ew);
							OutString.append(", [__cuda__];  ");
							sl=allomem.get(reNm);
							ew=Integer.toBinaryString(sl);
							int lenl=ew.length();
							for(int i=1;i<=16-lenl;i++)
								OutString.append("0");
							OutString.append(ew);
							System.out.println(OutString.toString());
							sout.write(OutString.toString());
							sout.newLine();
						}
						
					}
					if(!allsec && !"1".equals(regstate.get(reNm))){
						try{
							ffin=new FileReader(new File("isa0.out"));
							ssin=new BufferedReader(ffin);
						} catch(FileNotFoundException e){
							e.printStackTrace();
						} 
						String IInString;
						boolean regu[]=new boolean[17];
						for(int i=1;i<=16;i++){
							regu[i]=true;
						}
						int llineNum=0;
						try{
							while((IInString=ssin.readLine())!=null){
								llineNum=llineNum+1;
								if(llineNum>=lineNum){
									Matcher mmatch=reg.matcher(IInString);
									boolean same=false;
									while(mmatch.find()){
										String cp=mmatch.group(1);
										if (cp.equals(reNm)){
											same=true;
											break;
										}
									}
									if(same){
										mmatch=reg.matcher(IInString);
										while(mmatch.find()){
											String s=mmatch.group(1);
											if (!s.equals(reNm) && ("0".equals(regstate.get(s)) || "1".equals(regstate.get(s)) || "2".equals(regstate.get(s)))){
												int nn=alloreg1.get(s);
												regu[nn]=false;
											}
												
										}
									}
								}
							}
						} catch(IOException e){
							e.printStackTrace();
						}
						ssin.close();
						llineNum=0;
						try{
							ffin=new FileReader(new File("isa0.out"));
							ssin=new BufferedReader(ffin);
						} catch(FileNotFoundException e){
							e.printStackTrace();
						} 
						try{
							while((IInString=ssin.readLine())!=null){
								llineNum=llineNum+1;
								if(llineNum>lineNum){
									int fdmm=0;
									for(int i=1;i<=16;i++){
										if(regu[i])
											fdmm=fdmm+1;
									}
									if(fdmm==1)
										break;
									Matcher mmatch=reg.matcher(IInString);
									while(mmatch.find()){
										for(int i=1;i<=16;i++){
											if(regu[i]){
												String rrenm=alloreg2.get(i);
												if(rrenm.equals(mmatch.group(1)))
													regu[i]=false;
											}
											
										}
										int ffdm=0;
										for(int i=1;i<=16;i++){
											if(regu[i])
												ffdm=ffdm+1;
										}
										if(ffdm==1)
											break;
									}
									if(fdmm==1)
										break;
								}
							}
							ssin.close();
						} catch (IOException e){
							e.printStackTrace();
						}
						int rn=0;
						for(int i=1;i<=16;i++){
							if(regu[i])
								rn=i;
						}
						String tomem=alloreg2.get(rn);
						MemNum=MemNum+1;
						allomem.put(tomem, MemNum);
						regstate.put(tomem, "2");
						alloreg1.put(reNm, rn);
						alloreg2.put(rn, reNm);
						regstate.put(reNm, "1");
						OutString=new StringBuffer();
						if("2".equals(regstate.get(reNm))){
							OutString.append("	ld.global.s32 	%r");
							Integer sl=rn;
							String ew=sl.toString();
							OutString.append(ew);
							OutString.append(", [__cuda__];  ");
							sl=allomem.get(reNm);
							ew=Integer.toBinaryString(sl);
							int lenl=ew.length();
							for(int i=1;i<=16-lenl;i++)
								OutString.append("0");
							OutString.append(ew);
							System.out.println(OutString.toString());
							sout.write(OutString.toString());
							sout.newLine();
						}
						
						OutString.append("	st.global.s32 	[%r0+");
						Integer MN=MemNum;
						String t=MN.toString();
						OutString.append(t);
						OutString.append("], %r");
						MN=rn;
						t=MN.toString();
						OutString.append(t);
						OutString.append(";");
						System.out.println(OutString.toString());
						sout.write(OutString.toString());
						sout.newLine();
						
					}
					if ("1".equals(regstate.get(reNm))){
						try{
							ffin=new FileReader(new File("isa0.out"));
							ssin=new BufferedReader(ffin);
						} catch(FileNotFoundException e){
							e.printStackTrace();
						} 
						String IInString;
						int llineNum=0;
						boolean regend=true;
						try{
								while((IInString=ssin.readLine())!=null){
								llineNum=llineNum+1;
								
								if(llineNum>lineNum){
									Matcher mmatch=reg.matcher(IInString);
									while(mmatch.find()){
										String rreNm=mmatch.group(1);
										if (rreNm.equals(reNm)){
											regend=false;
											break;
											
										}
									}
								}
								if (regend==false){
									ssin.close();
									break;
								}
							}
						} catch (IOException e){
							e.printStackTrace();
						}
						if (regend==true){
							regstate.put(reNm, "0");
							regocu[alloreg1.get(reNm)]=2;
						}
					}
					
				}
				
				match=reg.matcher(InString);
				boolean mat=false;
				OutString=new StringBuffer();
				while(match.find()){
					mat=true;
					String con1=match.group(1);
					String con2=match.group(2);
					Integer N=alloreg1.get(con1);
					String NN=N.toString();
					StringBuffer Ot=new StringBuffer();
					Ot.append("%r");
					Ot.append(NN);
					Ot.append(con2);
					String con3=Ot.toString();
					
					match.appendReplacement(OutString, con3);
				}
				match.appendTail(OutString);
				System.out.println(OutString.toString());
				sout.write(OutString.toString());
				sout.newLine();
			} 
			sin.close();
			sout.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
