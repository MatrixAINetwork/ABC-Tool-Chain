import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;
import java.util.HashMap;
import java.util.Stack;



public class reCon{
	static private FileReader fin;
	static private FileWriter fout;
	static private BufferedReader sin;
	static private BufferedWriter sout;
	private int [][] branch,path;
	private Stack<Integer> Pstack;
	private Pattern bra,pre,num1,num2,ex;
	private int braNum;
	private HashMap<String,Integer> Bno1;
	private HashMap<Integer,String> Bno2;
	private HashMap<String,String> PP,PP2;
	public reCon(){
		bra=Pattern.compile("(bra)");
		pre=Pattern.compile("@%p");
		num1=Pattern.compile("^([01][01][01][01][01][01][01][01])");
		num2=Pattern.compile(";([01][01][01][01][01][01][01][01])");
		ex=Pattern.compile("^(11111111)");
		String InString;
		Matcher match,match1,match2,match3;
		braNum=0;
		Pstack=new Stack<Integer>();
		Bno1=new HashMap<String,Integer>();
		Bno2=new HashMap<Integer,String>();
		PP=new HashMap<String,String>();
		PP2=new HashMap<String,String>();
		int www=0;
		try{
			fin=new FileReader(new File("isa.out"));
			//fout=new FileWriter(new File("isare.out"));
			sin=new BufferedReader(fin);
			//sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		try{
			while((InString=sin.readLine())!=null){
				
				System.out.println(InString);
				match=bra.matcher(InString);
				match3=pre.matcher(InString);
				String Get,To;
				if(www==1){
					match1=num1.matcher(InString);
					if (match1.find()){
						Get=match1.group(1);
						To=PP.get(Get);
						if (!"1".equals(To)){
							braNum=braNum+1;
							PP.put(Get, "1");
						}
					}
				}
				www=0;
				if(match.find()){
					match1=num1.matcher(InString);
					match2=num2.matcher(InString);
					if (match1.find()){
						Get=match1.group(1);
						To=PP.get(Get);
						if (!"1".equals(To)){
							braNum=braNum+1;
							PP.put(Get, "1");
						}
					}
					if (match2.find()){
						Get=match2.group(1);
						To=PP.get(Get);
						if (!"1".equals(To)){
							braNum=braNum+1;
							PP.put(Get, "1");
						}
					}
					if(match3.find()) www=1;
					
				}
				match=ex.matcher(InString);
				if (match.find()){
					Get="11111111";
					To=PP.get(Get);
					if (!"1".equals(To)){
						braNum=braNum+1;
						PP.put(Get, "1");
					}
				}
				
				
				
			}
			sin.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		branch=new int[braNum+1][12];
		path=new int[braNum+1][12];
		for(int i=1;i<=braNum;i++){
			branch[i][0]=i;
			path[i][0]=0;
			for(int j=1;j<=11;j++){
				branch[i][j]=0;
				path[i][j]=0;
			}
		}
		int bNum=0;
		try{
			fin=new FileReader(new File("isa.out"));
			//fout=new FileWriter(new File("isare.out"));
			sin=new BufferedReader(fin);
			//sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		try{
			while((InString=sin.readLine())!=null){
				System.out.println(InString);
				match=bra.matcher(InString);
				String Get,To;
				if(www==1){
					match1=num1.matcher(InString);
					if(match1.find()){
						Get=match1.group(1);
						To=PP2.get(Get);
						if (!"1".equals(To)){
							bNum=bNum+1;
							PP2.put(Get, "1");
							Bno1.put(Get, bNum);
							Bno2.put(bNum, Get);
							int a=0;
							for(int i=0;i<8;i++){
								char bit=Get.charAt(i);
								a = (a << 1) | (bit - '0');
							}
							branch[bNum][1]=a;
							
						}
					}
				}
				www=0;
				if(match.find()){
					match1=num1.matcher(InString);
					match2=num2.matcher(InString);
					match3=pre.matcher(InString);
					if (match3.find()){
						www=1;
					}
					if (match1.find()){
						Get=match1.group(1);
						To=PP2.get(Get);
						if (!"1".equals(To)){
							bNum=bNum+1;
							PP2.put(Get, "1");
							Bno1.put(Get, bNum);
							Bno2.put(bNum, Get);
							int a=0;
							for(int i=0;i<8;i++){
								char bit=Get.charAt(i);
								a = (a << 1) | (bit - '0');
							}
							branch[bNum][1]=a;
							
						}
						
						
					}
					if (match2.find()){
						Get=match2.group(1);
						To=PP2.get(Get);
						if (!"1".equals(To)){
							bNum=bNum+1;
							PP2.put(Get, "1");
							Bno1.put(Get, bNum);
							Bno2.put(bNum, Get);
							int a=0;
							for(int i=0;i<8;i++){
								char bit=Get.charAt(i);
								a = (a << 1) | (bit - '0');
							}
							branch[bNum][1]=a;
							
						}
						
						
					}
					
				}
				
				match=ex.matcher(InString);
				if (match.find()){
					match1=num1.matcher(InString);
					if (match1.find()){
						bNum=bNum+1;
						String L=match1.group(1);
						To=PP2.get(L);
						if (!"1".equals(To)){
							Bno1.put(L,bNum);
							Bno2.put(bNum,L);
							int a=0;
							for(int i=0;i<8;i++){
								char bit=L.charAt(i);
								a = (a << 1) | (bit - '0');
							}
							branch[bNum][1]=a;
						}
						
					}
				}
				
				
				
			}
			sin.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		
		
	}
	
	public void Count(){
		String InString;
		
		
		Matcher match1,match2,match3,match4;
		
		try{
			fin=new FileReader(new File("isa.out"));
			fout=new FileWriter(new File("isare.out"));
			sin=new BufferedReader(fin);
			sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		try{
			while((InString=sin.readLine())!=null){
				match1=bra.matcher(InString);
				match2=pre.matcher(InString);
				match3=num1.matcher(InString);
				match4=num2.matcher(InString);
				int preNum=0,jumNum=0;
				if (match1.find()){
					System.out.println(InString);
					if (match3.find()){
						String L=match3.group(1);
						preNum=Bno1.get(L);
					}
					if (match4.find()){
						String LLL=match4.group(1);
						jumNum=Bno1.get(LLL);
					}
					if (match2.find()){
						match4=num2.matcher(InString);
						
						if (match4.find()){
							
							int leNum=braNum,riNum=braNum;
							int jj=65534;
							for (int i=1;i<=braNum;i++){
								int de=branch[i][1]-jumNum;
								if (de>0&&de<jj){
									riNum=i;
									jj=de;
									
								}
							}
							//branch[preNum][3]=riNum;
							//path[preNum][3]=1;
							branch[preNum][3]=jumNum;
							path[preNum][3]=1;
							branch[jumNum][2]=riNum;
							path[jumNum][2]=1;
							for(int i=4;i<10;i++){
								if(branch[jumNum][i]==0){
									branch[jumNum][i]=preNum;
									path[jumNum][i]=1;
									break;
								}
							}
							for(int i=4;i<10;i++){
								if(branch[riNum][i]==0){
									branch[riNum][i]=jumNum;
									path[riNum][i]=1;
									break;
								}
							}
							jj=65534;
							for (int i=1;i<=braNum;i++){
								int de=branch[i][1]-branch[preNum][1];
								if (de>0&&de<jj){
									leNum=i;
									jj=de;
									
								}
							}
							int wer=branch[preNum][2]+2;
							branch[preNum][2]=wer;
							path[preNum][2]=1;
							branch[wer][2]=leNum;
							path[wer][2]=1;
							for(int i=4;i<10;i++){
								if(branch[leNum][i]==0){
									branch[leNum][i]=wer;
									path[leNum][i]=1;
									break;
								}
							}
						}
						
					}
					else{
						match4=num2.matcher(InString);
						if (match4.find()){
							String L=match4.group(1);
							int a=0;
							for(int i=0;i<8;i++){
								char bit=L.charAt(i);
								a = (a << 1) | (bit - '0');
							}
							int leNum=0;
							int jj=65534;
							for (int i=1;i<=braNum;i++){
								int de=branch[i][1]-a;
								if (de>0&&de<jj){
									leNum=i;
									jj=de;
									
								}
							}
							int wer=branch[preNum][2]+2;
							branch[preNum][2]=wer;
							path[preNum][2]=1;
							branch[wer][2]=leNum;
							path[wer][2]=1;
							for(int i=4;i<10;i++){
								if(branch[leNum][i]==0){
									branch[leNum][i]=wer;
									path[leNum][i]=1;
									break;
								}
							}
							
						}
					}
				}
			}
			sin.close();
			rec(braNum);
					
		}catch (IOException e){
			e.printStackTrace();
		}
		try{
			fin=new FileReader(new File("isa.out"));
			//fout=new FileWriter(new File("isare.out"));
			sin=new BufferedReader(fin);
			//sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
		try{
			while((InString=sin.readLine())!=null){
				sout.write(InString);
				match1=pre.matcher(InString);
				match2=num1.matcher(InString);
				if(match1.find()&&match2.find()){
					String LL=match2.group(1);
					int LLA=Bno1.get(LL);
					String LLb=Bno2.get(branch[LLA][10]);
					sout.write("   ");
					sout.write(LLb);
					sout.newLine();
				}
				else sout.newLine();
			}
			sout.close();
			sin.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		
	}
	private int rec(int PNum){
		//if (branch[PNum][11]==1){
			//if (!Pstack.empty()) Pstack.pop();
			//return 0;
		//}
		if (branch[PNum][3]!=0&&!Pstack.empty()){
			int a=Pstack.pop();
			branch[PNum][10]=a;
			
		}
		if (branch[PNum][5]!=0){
			Pstack.push(PNum);
		}
		int a=0;
		if(!Pstack.empty()){
			a=Pstack.pop();
			Pstack.push(a);
		}
	
		int b=0,c=1;
		for (int i=4;i<10;i++){
			if(branch[PNum][i]!=0&&path[PNum][i]==0&&!Pstack.empty()&&c==1){
				Pstack.pop();
				c=0;
			}	
			
			if (branch[PNum][i]!=0&&path[PNum][i]!=0&&branch[PNum][i]<PNum){
				//int aa=branch[PNum][i];
				//if(branch[aa][11]==0){
					path[PNum][i]=0;
					rec (branch[PNum][i]);
					Pstack.push(a);
					b=1;
				//}
				
			}
			
			
		
		}
		//branch[PNum][11]=1;
		if(b==1&&!Pstack.empty()) Pstack.pop();
		
		if (branch[PNum][4]==0)
			return 0;
		return 1;
	}
}
