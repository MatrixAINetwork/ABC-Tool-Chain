import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.*;

public class res {
	int regNum;
	private Pattern ISA,reg0,reg1,reg2,reg3,reg4,reg5,ld,st,ldp,cvt;
	static private FileReader fin;
	static private FileWriter fout;
	static private BufferedReader sin;
	static private BufferedWriter sout;
	
	public res(){
		ISA=Pattern.compile("(((add|sub|mul|mad|div|rem|abs|neg|min|max|setp|numeric|mov|selp|cvt|ld|st|tex|atom|bra|call|ret|set|and)\\.)|\\@\\%p[1234567890]+ bra)");
		reg0=Pattern.compile("([a-z0123456789\\.]* 	\\%)([rpf])([0123456789]+), ([0123456789\\_a-zA-Z]+);");
		reg1=Pattern.compile("([a-z0123456789\\.]* 	\\%)([rpf])([0123456789]+), \\%([rpf])([0123456789]+);");
		reg2=Pattern.compile("([a-z0123456789\\.]* 	\\%)([rpf])([0123456789]+), \\%([rpf])([0123456789]+), \\%([rpf])([0123456789]+);");
		reg3=Pattern.compile("([a-z0123456789\\.]* 	\\%)([rpf])([0123456789]+), \\%([rpf])([0123456789]+), ([0123456789]+);");
		reg4=Pattern.compile("([a-z0123456789\\.]* 	\\%)([rpf])([0123456789]+), \\%([rpf])([0123456789]+), \\%([rfp])([0123456789]+), \\%([rpf])([0123456789]+);");
		reg5=Pattern.compile("([a-z0123456789\\.]* 	\\%)([rpf])([0123456789]+), \\%([rpf])([0123456789]+), ([0123456789]+), \\%([rpf])([0123456789]+);");
		ld=Pattern.compile("(ld[a-z0123456789\\.]*) 	\\%([rf])([0123456789]+), \\[\\%([rf])([0123456789]*)\\+([0123456789]+)\\];");
		st=Pattern.compile("(st[a-z0123456789\\.]* 	\\[\\%)([rf])([0123456789]+)(\\+[0123456789]+\\], \\%)([rf])([0123456789]+);");
		ldp=Pattern.compile("(ld[a-z0123456789\\.]* 	\\%)([rf])([0123456789]+)(, \\[[a-zA-Z0123456789\\_]+\\];)");
		cvt=Pattern.compile("(cvt.[a-zA-Z0123456789.]*) 	(\\%r[0123456789]+, )\\%(tid.x|tid.y|ctaid.x|ctaid.y|ntid.x|ntid.y);");
		regNum=27;
	}
	
	public void Convert(){
		try{
			fin=new FileReader(new File("isa.in"));
			fout=new FileWriter(new File("isa0.in"));
			sin=new BufferedReader(fin);
			sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		String InString;		
		//StringBuilder OutString;
		Matcher match;
		
		try{
			while((InString=sin.readLine())!=null){
				
				match=reg1.matcher(InString);
				if(match.find()){
					String G1=match.group(2);
					String G2=match.group(3);
					if(G1.equals("r")){
						int num=Integer.parseInt(G2);
						if(num>regNum)
							regNum=num;
					}
				}
				match=reg2.matcher(InString);
				if(match.find()){
					String G1=match.group(2);
					String G2=match.group(3);
					if(G1.equals("r")){
						int num=Integer.parseInt(G2);
						if(num>regNum)
							regNum=num;
					}
				}
				match=reg3.matcher(InString);
				if(match.find()){
					String G1=match.group(2);
					String G2=match.group(3);
					if(G1.equals("r")){
						int num=Integer.parseInt(G2);
						if(num>regNum)
							regNum=num;
					}
				}
				match=reg4.matcher(InString);
				if(match.find()){
					String G1=match.group(3);
					String G2=match.group(4);
					if(G1.equals("r")){
						int num=Integer.parseInt(G2);
						if(num>regNum)
							regNum=num;
					}
				}
				match=ld.matcher(InString);
				if(match.find()){
					String G1=match.group(2);
					String G2=match.group(3);
					if(G1.equals("r")){
						int num=Integer.parseInt(G2);
						if(num>regNum)
							regNum=num;
					}
					G1=match.group(4);
					G2=match.group(5);
					if(G1.equals("r")){
						int num=Integer.parseInt(G2);
						if(num>regNum)
							regNum=num;
					}
				}
				match=st.matcher(InString);
				if(match.find()){
					String G1=match.group(2);
					String G2=match.group(3);
					if(G1.equals("r")){
						int num=Integer.parseInt(G2);
						if(num>regNum)
							regNum=num;
					}
					G1=match.group(5);
					G2=match.group(6);
					if(G1.equals("r")){
						int num=Integer.parseInt(G2);
						if(num>regNum)
							regNum=num;
					}
				}
			}
			sin.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		
		
		try{
			fin=new FileReader(new File("isa.in"));
			sin=new BufferedReader(fin);
		}catch (FileNotFoundException e){
			e.printStackTrace();
		} 
		
		
		try{
			while((InString=sin.readLine())!=null){
				//OutString=new StringBuilder();
				System.out.println(InString);
				match=ISA.matcher(InString);
				if(!match.find()){
					sout.write(InString);
					sout.newLine();
				
				}
				else{
					match=cvt.matcher(InString);
					if(match.find()){
						//String G1=match.group(2);
						String G2=match.group(3);
						StringBuilder p=new StringBuilder();
						p.append(InString);
						
						if(G2.equals("tid.x")){
							p.append("001");
						}
						if(G2.equals("tid.y")){
							p.append("010");
						}
						if(G2.equals("ctaid.x")){
							p.append("011");
						}
						if(G2.equals("ctaid.y")){
							p.append("100");
						}
						if(G2.equals("ntid.x")){
							p.append("101");
						}
						if(G2.equals("ntid.y")){
							p.append("110");
						}
						InString=p.toString();
					}
					Matcher match1,match2,match3;
					match1=ld.matcher(InString);
					match2=st.matcher(InString);
					match3=ldp.matcher(InString);
					if(match1.find()){
						String G1=match1.group(1);
						String G2=match1.group(2);
						String G3=match1.group(3);
						String G4=match1.group(4);
						String G5=match1.group(5);
						String G6=match1.group(6);
						int srcNum,srcto,desNum,desto;
						String src,des;
						srcto=0;
						desto=0;
						srcNum=Integer.parseInt(G5);
						if (G4.equals("f")||srcNum>27){
							srcto=1;
							if(G4.equals("f")){
								srcNum=regNum-28+srcNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write(src);
								sout.newLine();
								
							}
							else if(srcNum>27){
								srcNum=srcNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src);
								sout.write("]");
								sout.newLine();
							}
							
						}
						desNum=Integer.parseInt(G3);
						if(G2.equals("f")||desNum>27){
							desto=1;
						}
						sout.write("	");
						sout.write(G1);
						sout.write(" 	%");
						if(desto==0){
							sout.write(G2);
							sout.write(G3);
							sout.write(", [%");
						}
						else{
							sout.write("r29, [%");
							
						}
						if(srcto==0){
							sout.write(G4);
							sout.write(G5);
							sout.write("+");
							sout.write(G6);
							sout.write("];");
							sout.newLine();
						}
						else{
							sout.write("r28+");
							sout.write(G6);
							sout.write("];");
							sout.newLine();
							srcto=0;
						}
						if(G2.equals("f")){
							desNum=regNum-28+desNum;
							StringBuilder t=new StringBuilder("111111");
							String l=Integer.toBinaryString(desNum);
							int L=l.length();
							for(int i=0;i<10-L;i++)
								t.append("0");
							t.append(l);
							des=t.toString();
							sout.write("	st.local.f32 	[");
							sout.write(des);
							sout.write("], %r29");
							sout.newLine();
						}
						else if(desNum>27){
							desNum=desNum-28;
							StringBuilder t=new StringBuilder("111111");
							String l=Integer.toBinaryString(desNum);
							int L=l.length();
							for(int i=0;i<10-L;i++)
								t.append("0");
							t.append(l);
							des=t.toString();
							sout.write("	st.local.f32 	[");
							sout.write(des);
							sout.write("], %r29");
							sout.newLine();
						}
					}
					else if(match3.find()){
						String G1=match3.group(1);
						String G2=match3.group(2);
						String G3=match3.group(3);
						String G4=match3.group(4);
						int desto,desNum;
						String des;
						desto=0;
						desNum=Integer.parseInt(G3);
						if(G2.equals("f")||desNum>27){
							desto=1;
						}
						if(desto==0){
							sout.write(InString);
							sout.newLine();
						}
						else{
							sout.write("	");
							sout.write(G1);
							sout.write("r28");
							sout.write(G4);
							sout.newLine();
						}
						if(G2.equals("f")){
							desNum=regNum-28+desNum;
							StringBuilder t=new StringBuilder("111111");
							String l=Integer.toBinaryString(desNum);
							int L=l.length();
							for(int i=0;i<10-L;i++)
								t.append("0");
							t.append(l);
							des=t.toString();
							sout.write("	st.local.f32 	[");
							sout.write(des);
							sout.write("], %r28");
							sout.newLine();
						}
						else if(desNum>27){
							desNum=desNum-28;
							StringBuilder t=new StringBuilder("111111");
							String l=Integer.toBinaryString(desNum);
							int L=l.length();
							for(int i=0;i<10-L;i++)
								t.append("0");
							t.append(l);
							des=t.toString();
							sout.write("	st.local.f32 	[");
							sout.write(des);
							sout.write("], %r28");
							sout.newLine();
						}
					}
					else if(match2.find()){
						String G1=match2.group(1);
						String G2=match2.group(2);
						String G3=match2.group(3);
						String G4=match2.group(4);
						String G5=match2.group(5);
						String G6=match2.group(6);
						int srcNum,srcto,desNum,desto;
						String src,des;
						srcto=0;
						desto=0;
						srcNum=Integer.parseInt(G6);
						if(G5.equals("f")){
							srcto=1;
							srcNum=regNum-28+srcNum;
							StringBuilder t=new StringBuilder("111111");
							String l=Integer.toBinaryString(srcNum);
							int L=l.length();
							for(int i=0;i<10-L;i++)
								t.append("0");
							t.append(l);
							src=t.toString();
							sout.write("	ld.local.f32 	%r28, ");
							sout.write(src);
							sout.newLine();
						}
						else if(srcNum>27){
							srcto=1;
							srcNum=srcNum-28;
							StringBuilder t=new StringBuilder("111111");
							String l=Integer.toBinaryString(srcNum);
							int L=l.length();
							for(int i=0;i<10-L;i++)
								t.append("0");
							t.append(l);
							src=t.toString();
							sout.write("	ld.local.f32 	%r28, ");
							sout.write("[");
							sout.write(src);
							sout.write("]");
							sout.newLine();
						}
						desNum=Integer.parseInt(G3);
						if(G2.equals("f")||desNum>27){
							desto=1;
						}
						sout.write("	");
						sout.write(G1);
						if(desto==0){
							sout.write(G2);
							sout.write(G3);
							
						}
						else{
							sout.write("r29");
						}
						sout.write(G4);
						if(srcto==0){
							sout.write(G5);
							sout.write(G6);
							
						}
						else{
							sout.write("r28");
							
						}
						sout.write(";");
						sout.newLine();
						if(G2.equals("f")){
							desNum=regNum-28+desNum;
							StringBuilder t=new StringBuilder("111111");
							String l=Integer.toBinaryString(desNum);
							int L=l.length();
							for(int i=0;i<10-L;i++)
								t.append("0");
							t.append(l);
							des=t.toString();
							sout.write("	st.local.f32 	[");
							sout.write(des);
							sout.write("], %r29");
							sout.newLine();
						}else if(desNum>27){
							desNum=desNum-28;
							StringBuilder t=new StringBuilder("111111");
							String l=Integer.toBinaryString(desNum);
							int L=l.length();
							for(int i=0;i<10-L;i++)
								t.append("0");
							t.append(l);
							des=t.toString();
							sout.write("	st.local.f32 	[");
							sout.write(des);
							sout.write("], %r29");
							sout.newLine();
						}
					}
					else{
						Matcher Reg0,Reg1,Reg2,Reg3,Reg4,Reg5;
						Reg0=reg0.matcher(InString);
						Reg1=reg1.matcher(InString);
						Reg2=reg2.matcher(InString);
						Reg3=reg3.matcher(InString);
						Reg4=reg4.matcher(InString);
						Reg5=reg5.matcher(InString);
						if(Reg0.find()){
							String G1=Reg0.group(1);
							String G2=Reg0.group(2);
							String G3=Reg0.group(3);
							String G4=Reg0.group(4);
							int desto,desNum;
							String des;
							desto=0;
							desNum=Integer.parseInt(G3);
							if(G2.equals("f")||desNum>27)
								desto=1;
							sout.write("	");
							sout.write(G1);
							if(desto==1){
								sout.write("r28, ");
							}
							else{
								sout.write(G2);
								sout.write(G3);
								sout.write(", ");
							}
							sout.write(G4);
							sout.write(";");
							sout.newLine();
							if(G2.equals("f")){
								desNum=regNum-28+desNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r29");
								sout.newLine();
							}
							else if(desNum>27){
								desNum=desNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r29");
								sout.newLine();
							}
						}
						else if(Reg1.find()){
							String G1=Reg1.group(1);
							String G2=Reg1.group(2);
							String G3=Reg1.group(3);
							String G4=Reg1.group(4);
							String G5=Reg1.group(5);
							int srcto,srcNum,desto,desNum;
							String src,des;
							srcto=0;
							desto=0;
							srcNum=Integer.parseInt(G5);
							desNum=Integer.parseInt(G3);
							if(G2.equals("f")||desNum>27)
								desto=1;
							if(G4.equals("f")){
								srcto=1;
								srcNum=regNum-28+srcNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum>27){
								srcto=1;
								srcNum=srcNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src);
								sout.write("]");
								sout.newLine();
							}
							sout.write("	");
							sout.write(G1);
							if(desto==1){
								sout.write("r29, %");
							}
							else{
								sout.write(G2);
								sout.write(G3);
								sout.write(", %");
							}
							if(srcto==1){
								sout.write("r28;");
							}
							else{
								sout.write(G4);
								sout.write(G5);
								sout.write(";");
								
							}
							sout.newLine();
							if(G2.equals("f")){
								desNum=regNum-28+desNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r29");
								sout.newLine();
							}
							else if(desNum>27){
								desNum=desNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r29");
								sout.newLine();
							}
						}
						else if(Reg2.find()){
							String G1=Reg2.group(1);
							String G2=Reg2.group(2);
							String G3=Reg2.group(3);
							String G4=Reg2.group(4);
							String G5=Reg2.group(5);
							String G6=Reg2.group(6);
							String G7=Reg2.group(7);
							int srcto1,srcNum1,srcto2,srcNum2,desto,desNum;
							String src1,src2,des;
							srcto1=0;
							srcto2=0;
							desto=0;
							srcNum1=Integer.parseInt(G5);
							srcNum2=Integer.parseInt(G7);
							desNum=Integer.parseInt(G3);
							if(G2.equals("f")||desNum>27)
								desto=1;
							if(G4.equals("f")){
								srcto1=1;
								srcNum1=regNum-28+srcNum1;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum1);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src1=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src1);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum1>27){
								srcto1=1;
								srcNum1=srcNum1-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum1);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src1=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src1);
								sout.write("]");
								sout.newLine();
							}
							
							if(G6.equals("f")){
								srcto2=1;
								srcNum2=regNum-28+srcNum2;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum2);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src2=t.toString();
								sout.write("	ld.local.f32 	%r29, ");
								sout.write("[");
								sout.write(src2);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum2>27){
								srcto2=1;
								srcNum2=srcNum2-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum2);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src2=t.toString();
								sout.write("	ld.local.f32 	%r29, ");
								sout.write("[");
								sout.write(src2);
								sout.write("]");
								sout.newLine();
							}
							sout.write("	");
							sout.write(G1);
							if(desto==1){
								sout.write("r30, %");
							}
							else{
								sout.write(G2);
								sout.write(G3);
								sout.write(", %");
							}
							if(srcto1==1){
								sout.write("r28, %");
							}
							else{
								sout.write(G4);
								sout.write(G5);
								sout.write(", %");
								
							}
							if(srcto2==1){
								sout.write("r29;");
							}
							else{
								sout.write(G6);
								sout.write(G7);
								sout.write(";");
								
							}
							sout.newLine();
							if(G2.equals("f")){
								desNum=regNum-28+desNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r30");
								sout.newLine();
							}
							else if(desNum>27){
								desNum=desNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r30");
								sout.newLine();
							}
						}
						else if(Reg3.find()){
							String G1=Reg3.group(1);
							String G2=Reg3.group(2);
							String G3=Reg3.group(3);
							String G4=Reg3.group(4);
							String G5=Reg3.group(5);
							String G6=Reg3.group(6);
							int srcto,srcNum,desto,desNum;
							String src,des;
							srcto=0;
							desto=0;
							srcNum=Integer.parseInt(G5);
							desNum=Integer.parseInt(G3);
							if(G2.equals("f")||desNum>27)
								desto=1;
							if(G4.equals("f")){
								srcto=1;
								srcNum=regNum-28+srcNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum>27){
								srcto=1;
								srcNum=srcNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src);
								sout.write("]");
								sout.newLine();
							}
							sout.write("	");
							sout.write(G1);
							if(desto==1){
								sout.write("r29, %");
							}
							else{
								sout.write(G2);
								sout.write(G3);
								sout.write(", %");
							}
							if(srcto==1){
								sout.write("r28, ");
							}
							else{
								sout.write(G4);
								sout.write(G5);
								sout.write(", ");
								
							}
							sout.write(G6);
							sout.write(";");
							sout.newLine();
							if(G2.equals("f")){
								desNum=regNum-28+desNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r29");
								sout.newLine();
							}
							else if(desNum>27){
								desNum=desNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r29");
								sout.newLine();
							}
							
						}
						else if(Reg4.find()){
							String G1=Reg4.group(1);
							String G2=Reg4.group(2);
							String G3=Reg4.group(3);
							String G4=Reg4.group(4);
							String G5=Reg4.group(5);
							String G6=Reg4.group(6);
							String G7=Reg4.group(7);
							String G8=Reg4.group(8);
							String G9=Reg4.group(9);
							int srcto1,srcNum1,srcto2,srcNum2,srcto3,srcNum3,desto,desNum;
							String src1,src2,src3,des;
							srcto1=0;
							srcto2=0;
							srcto3=0;
							desto=0;
							srcNum1=Integer.parseInt(G5);
							srcNum2=Integer.parseInt(G7);
							srcNum3=Integer.parseInt(G9);
							desNum=Integer.parseInt(G3);
							if(G2.equals("f")||desNum>27)
								desto=1;
							if(G4.equals("f")){
								srcto1=1;
								srcNum1=regNum-28+srcNum1;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum1);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src1=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src1);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum1>27){
								srcto1=1;
								srcNum1=srcNum1-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum1);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src1=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src1);
								sout.write("]");
								sout.newLine();
							}
							
							if(G6.equals("f")){
								srcto2=1;
								srcNum2=regNum-28+srcNum2;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum2);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src2=t.toString();
								sout.write("	ld.local.f32 	%r29, ");
								sout.write("[");
								sout.write(src2);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum2>27){
								srcto2=1;
								srcNum2=srcNum2-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum2);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src2=t.toString();
								sout.write("	ld.local.f32 	%r29, ");
								sout.write("[");
								sout.write(src2);
								sout.write("]");
								sout.newLine();
							}
							if(G8.equals("f")){
								srcto3=1;
								srcNum3=regNum-28+srcNum3;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum3);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src3=t.toString();
								sout.write("	ld.local.f32 	%r30, ");
								sout.write("[");
								sout.write(src3);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum2>27){
								srcto3=1;
								srcNum3=srcNum3-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum3);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src3=t.toString();
								sout.write("	ld.local.f32 	%r30, ");
								sout.write("[");
								sout.write(src3);
								sout.write("]");
								sout.newLine();
							}
							sout.write("	");
							sout.write(G1);
							if(desto==1){
								sout.write("r31, %");
							}
							else{
								sout.write(G2);
								sout.write(G3);
								sout.write(", %");
							}
							if(srcto1==1){
								sout.write("r28, %");
							}
							else{
								sout.write(G4);
								sout.write(G5);
								sout.write(", %");
								
							}
							if(srcto2==1){
								sout.write("r29, %");
							}
							else{
								sout.write(G6);
								sout.write(G7);
								sout.write(", %");
								
							}if(srcto3==1){
								sout.write("r30;");
							}
							else{
								sout.write(G6);
								sout.write(G7);
								sout.write(";");
								
							}
							sout.newLine();
							if(G2.equals("f")){
								desNum=regNum-28+desNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r31");
								sout.newLine();
							}
							else if(desNum>27){
								desNum=desNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r31");
								sout.newLine();
							}
						}
						else if(Reg5.find()){
							String G1=Reg5.group(1);
							String G2=Reg5.group(2);
							String G3=Reg5.group(3);
							String G4=Reg5.group(4);
							String G5=Reg5.group(5);
							String G6=Reg5.group(6);
							String G7=Reg5.group(7);
							String G8=Reg5.group(8);
							int srcto1,srcNum1,srcto2,srcNum2,desto,desNum;
							String src1,src2,des;
							srcto1=0;
							srcto2=0;
							desto=0;
							srcNum1=Integer.parseInt(G5);
							srcNum2=Integer.parseInt(G8);
							desNum=Integer.parseInt(G3);
							if(G2.equals("f")||desNum>27)
								desto=1;
							if(G4.equals("f")){
								srcto1=1;
								srcNum1=regNum-28+srcNum1;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum1);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src1=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src1);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum1>27){
								srcto1=1;
								srcNum1=srcNum1-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum1);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src1=t.toString();
								sout.write("	ld.local.f32 	%r28, ");
								sout.write("[");
								sout.write(src1);
								sout.write("]");
								sout.newLine();
							}
							
							if(G7.equals("f")){
								srcto2=1;
								srcNum2=regNum-28+srcNum2;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum2);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src2=t.toString();
								sout.write("	ld.local.f32 	%r29, ");
								sout.write("[");
								sout.write(src2);
								sout.write("]");
								sout.newLine();
							}
							else if(srcNum2>27){
								srcto2=1;
								srcNum2=srcNum2-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(srcNum2);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								src2=t.toString();
								sout.write("	ld.local.f32 	%r29, ");
								sout.write("[");
								sout.write(src2);
								sout.write("]");
								sout.newLine();
							}
							sout.write("	");
							sout.write(G1);
							if(desto==1){
								sout.write("r30, %");
							}
							else{
								sout.write(G2);
								sout.write(G3);
								sout.write(", %");
							}
							if(srcto1==1){
								sout.write("r28, ");
							}
							else{
								sout.write(G4);
								sout.write(G5);
								sout.write(", ");
								
							}
							sout.write(G6);
							sout.write(", %");
							if(srcto2==1){
								sout.write("r29;");
							}
							else{
								sout.write(G7);
								sout.write(G8);
								sout.write(";");
								
							}
							sout.newLine();
							if(G2.equals("f")){
								desNum=regNum-28+desNum;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r30");
								sout.newLine();
							}
							else if(desNum>27){
								desNum=desNum-28;
								StringBuilder t=new StringBuilder("111111");
								String l=Integer.toBinaryString(desNum);
								int L=l.length();
								for(int i=0;i<10-L;i++)
									t.append("0");
								t.append(l);
								des=t.toString();
								sout.write("	st.local.f32 	[");
								sout.write(des);
								sout.write("], %r30");
								sout.newLine();
							}
						}
						else{
							sout.write(InString);
							sout.newLine();
						}
						
					}
					
				}
				
				
			}
			sin.close();
			sout.close();		
		}catch (IOException e){
			e.printStackTrace();
		}
	}

}
