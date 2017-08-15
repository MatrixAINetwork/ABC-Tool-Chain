
import java.util.regex.*;
import java.util.HashMap;

public class isaconvert2 {
	private Pattern op2,op3,op4,fs32,sf32,pred,bra,cvtin,movs,iff,reg3,reg3i,regp3,regp3i,reg4,setp,b,bp,codNum,reg2,reg2i,hilo,mvc,syn,exit,ld,st,ldr,ldp,str;
	private HashMap<String,String> Op2,Op3,Op3i,Op4,Setp,Setpi;
	
	public isaconvert2(){
		iff=Pattern.compile("(add|sub|mul|div|min|max|rem|and|or|xor|shl|hi|lo|shr).[ful]*[.]*(s|u|b|f)([0123456789]+)");
		syn=Pattern.compile("bar.sync");
		hilo=Pattern.compile("mul.(hi|lo|[bsfu]\\d+)");
		op3=Pattern.compile("(add|sub|mul|div|min|max|rem|and|or|xor|shl|shr).[ful]*[.]?([subf]\\d+|hi|lo)");
		op4=Pattern.compile("(mad|slep).");
		op2=Pattern.compile("(abs|not|neg|cnot).");
		pred=Pattern.compile("(@(!)?%p(\\d+))");
		bra=Pattern.compile("(bra)");
		//ldst=Pattern.compile("((ld|st).(global|shared|local|const))");
		reg3=Pattern.compile("(\\%r(\\d+), \\%r(\\d+), \\%r(\\d+))");
		reg3i=Pattern.compile("(\\%r(\\d+), \\%r(\\d+), (\\d+))");
		regp3=Pattern.compile("(\\%[rp](\\d+), \\%r(\\d+), \\%r(\\d+))");
		regp3i=Pattern.compile("(\\%[rp](\\d+), \\%r(\\d+), (\\d+))");
		setp=Pattern.compile("set[p]?.(eq|ne|lt|le|gt|ge)");
		bp=Pattern.compile(";([01][01][01][01][01][01][01][01][01][01][01][01][01][01][01][01])   ([01][01][01][01][01][01][01][01][01][01][01][01][01][01][01][01])");
		b=Pattern.compile(";([01][01][01][01][01][01][01][01][01][01][01][01][01][01][01][01])");
		codNum=Pattern.compile("^([01][01][01][01][01][01][01][01][01][01][01][01][01][01][01][01])");
		reg2=Pattern.compile("(\\%r(\\d+), \\%r(\\d+))");
		reg2i=Pattern.compile("(\\%r(\\d+), (\\d+))");
		mvc=Pattern.compile("(mov|cvt).");
		exit=Pattern.compile("exit");
		reg4=Pattern.compile("(\\%r(\\d+), \\%r(\\d+), \\%r(\\d+), \\%r(\\d+));");
		movs=Pattern.compile("\\%r(\\d+), \\_\\_cuda[a-zA-Z0123456789\\_]+;  ([01]+)");
		cvtin=Pattern.compile("\\%r(\\d+), \\%[a-zA-Z.]+;([01]+)");
		ld=Pattern.compile("ld.(global|local|shared|param)");
		st=Pattern.compile("st.(global|local|shared|param)");
		ldr=Pattern.compile("\\%r(\\d+), \\[\\%r(\\d+)\\+(\\d+)\\];");
		ldp=Pattern.compile("\\%r(\\d+), \\[[a-zA-Z0123456789\\_]+(\\+\\d+)?\\];  ([01]+)");
		str=Pattern.compile("\\[\\%r(\\d+)\\+(\\d+)\\], \\%r(\\d+);");
		fs32=Pattern.compile(".f32.[su]32");
		sf32=Pattern.compile(".[su]32.f32");
		//stp=Pattern.compile("");
		
		Op3=new HashMap<String,String>();
		Op3.put("add", "0000001");
		Op3.put("sub", "0000010");
		Op3.put("mul", "0000");
		Op3.put("div", "0000110");
		Op3.put("rem", "0000111");
		//Op3.put("abs", "00001000");
		//Op3.put("neg", "00001001");
		Op3.put("min", "0001010");
		Op3.put("max", "0001011");
		Op3.put("and", "0100101");
		Op3.put("or", "0100110");
		Op3.put("xor", "0100111");
		
		Op3.put("shl", "0101010");
		Op3.put("shr", "0101011");
		
		
		Op3i=new HashMap<String,String>();
		Op3i.put("and", "1100101");
		Op3i.put("or", "1100110");
		Op3i.put("xor", "1100111");
		Op3i.put("shl", "1101010");
		Op3i.put("shr", "1101011");
		Op3i.put("add", "1000001");
		Op3i.put("sub", "1000010");
		Op3i.put("mul", "1000");
		Op3i.put("div", "1000110");
		Op3i.put("rem", "1000111");
		Op3i.put("min", "1001010");
		Op3i.put("max", "1001011");
				
		
		
		
		
		Op2=new HashMap<String,String>();
		Op2.put("abs", "0000000010001000");
		Op2.put("neg", "0000000010001001");
		Op2.put("not", "0000000010101000");
		Op2.put("cnot", "0000000010101001");
		
		
		
		
				
		Op4=new HashMap<String,String>();
		Op4.put("mad", "0000000010000101");
		Op4.put("selp", "0000000010100010");
		
		Setp=new HashMap<String,String>();
		Setp.put("eq","0000000010010000");
		Setp.put("ne","0000000010010001");
		Setp.put("lt","0000000010010010");
		Setp.put("le","0000000010010011");
		Setp.put("gt","0000000010010100");
		Setp.put("ge","0000000010010101");
		
		
		Setpi=new HashMap<String,String>();
		Setpi.put("eq","0000000101010000");
		Setpi.put("ne","0000000101010001");
		Setpi.put("lt","0000000101010010");
		Setpi.put("le","0000000101010011");
		Setpi.put("gt","0000000101010100");
		Setpi.put("ge","0000000101010100");
		
		
	}
	public String Convert(String Instring){
		StringBuilder OutString;
		Matcher match;
		
		OutString=new StringBuilder();
		System.out.println(Instring);
		/*match=codNum.matcher(Instring);
		if(match.find()){
			String G=match.group(1);
			OutString.append(G);
			OutString.append("    ");
		}*/
		
		match=pred.matcher(Instring);
		if(match.find()){
			System.out.println("pred Yes!");
			String G1=match.group(2);
			String G2=match.group(3);
			if ("!".equals(G1))
				OutString.append("00000000001");
			else
				OutString.append("00000000000");
			int pN=Integer.parseInt(G2);
			String G3=Integer.toBinaryString(pN);
			int LL=G3.length();
			for (int i=0;i<5-LL;i++)
				OutString.append("0");
			OutString.append(G3);
		}
		else
			OutString.append("0000000000000000");
		
		
		match=op3.matcher(Instring);
		
		if(match.find()){
			OutString.append("00000");
			String G1=match.group(1);
			//String G2=match.group(2);
			String G2;
			Matcher mo=iff.matcher(Instring);
			if(mo.find())
				G2=mo.group(2);
			else G2=(":P");
			Matcher match1=reg3.matcher(Instring);
			Matcher match2=reg3i.matcher(Instring);
			Matcher match3=regp3.matcher(Instring);
			if(match1.find()||match3.find()){
				if(!"f".equals(G2)){
					OutString.append("0001");
				}
				else OutString.append("0011");
			}
			if(match2.find()){
				if(!"f".equals(G2)){
					OutString.append("0010");
				}
				else OutString.append("0100");
			}
			
			
			match1=reg3.matcher(Instring);
			match2=reg3i.matcher(Instring);
			match3=regp3.matcher(Instring);
			
			if(match1.find()){
				OutString.append(Op3.get(G1));
				if("mul".equals(G1)){
					Matcher mm=hilo.matcher(Instring);
					if(mm.find()){
						String tg=mm.group(1);
						if("lo".equals(tg))
							OutString.append("011");
						else if("hi".equals(tg))
							OutString.append("100");
						else 
							OutString.append("011");
						
					}
				}
				String r1=match1.group(2);
				String r2=match1.group(3);
				String r3=match1.group(4);
				int L;
				int rr2=Integer.parseInt(r2);
				String R2=Integer.toBinaryString(rr2);
				L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				int rr3=Integer.parseInt(r3);
				String R3=Integer.toBinaryString(rr3);
				L=R3.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R3);
				int rr1=Integer.parseInt(r1);
				String R1=Integer.toBinaryString(rr1);
				L=R1.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R1);
				OutString.append("00000000");
			}
			else if(match2.find()){
				OutString.append(Op3.get(G1));
				
				if("mul".equals(G1)){
					Matcher mm=hilo.matcher(Instring);
					if(mm.find()){
						String tg=mm.group(1);
						if("lo".equals(tg))
							OutString.append("011");
						else if("hi".equals(tg))
							OutString.append("100");
					}
				}
				String r1=match2.group(2);
				String r2=match2.group(3);
				String r3=match2.group(4);
				int L;
				int rr2=Integer.parseInt(r2);
				String R2=Integer.toBinaryString(rr2);
				L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				int rr1=Integer.parseInt(r1);
				String R1=Integer.toBinaryString(rr1);
				L=R1.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R1);
				int rr3=Integer.parseInt(r3);
				String R3=Integer.toBinaryString(rr3);
				L=R3.length();
				for (int i=0;i<16-L;i++)
					OutString.append("0");
				OutString.append(R3);
			}
			else if(match3.find()){
				OutString.append(Op3.get(G1));
				
				if("mul".equals(G1)){
					Matcher mm=hilo.matcher(Instring);
					if(mm.find()){
						String tg=mm.group(1);
						if("lo".equals(tg))
							OutString.append("011");
						else if("hi".equals(tg))
							OutString.append("100");
					}
				}
				String r1=match3.group(2);
				String r2=match3.group(3);
				String r3=match3.group(4);
				int L;
				int rr2=Integer.parseInt(r2);
				String R2=Integer.toBinaryString(rr2);
				L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				int rr3=Integer.parseInt(r3);
				String R3=Integer.toBinaryString(rr3);
				L=R3.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R3);
				int rr1=Integer.parseInt(r1);
				String R1=Integer.toBinaryString(rr1);
				L=R1.length();
				OutString.append("001");
				for (int i=0;i<5-L;i++)
					OutString.append("0");
				OutString.append(R1);
				OutString.append("00000000");
			}
			
			return OutString.toString();
		}
		
		match=op2.matcher(Instring);
		if(match.find()){
			String G1=match.group(1);
			
			Matcher match1=reg2.matcher(Instring);
			//Matcher match2=reg2i.matcher(Instring);
			if(match1.find()){
				OutString.append(Op2.get(G1));
				String G2=match1.group(2);
				String G3=match1.group(3);
				int r2=Integer.parseInt(G2);
				int r3=Integer.parseInt(G3);
				String R2=Integer.toBinaryString(r2);
				String R3=Integer.toBinaryString(r3);
				int L;
				L=R3.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R3);
				OutString.append("00000000");
				L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				OutString.append("00000000");
			}
			return OutString.toString();
			
		}
		
		
		match=mvc.matcher(Instring);
		if(match.find()){
			String G1=match.group(1);
			Matcher match1=reg2.matcher(Instring);
			Matcher match2=reg2i.matcher(Instring);
			Matcher match3=movs.matcher(Instring);
			Matcher match4=cvtin.matcher(Instring);
			Matcher match5=fs32.matcher(Instring);
			Matcher match6=sf32.matcher(Instring);
			if("mov".equals(G1)&&match1.find()){
				OutString.append("0000000100100000");
				String G2=match1.group(2);
				String G3=match1.group(3);
				int r2=Integer.parseInt(G2);
				int r3=Integer.parseInt(G3);
				String R2=Integer.toBinaryString(r2);
				String R3=Integer.toBinaryString(r3);
				int L;
				L=R3.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R3);
				
				L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				OutString.append("00000000");
				OutString.append("00000000");
			}
			else if("mov".equals(G1)&&match2.find()){
				OutString.append("000000010110000000000000");
				String G2=match2.group(2);
				String G3=match2.group(3);
				int r2=Integer.parseInt(G2);
				int r3=Integer.parseInt(G3);
				String R2=Integer.toBinaryString(r2);
				String R3=Integer.toBinaryString(r3);
				int L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				L=R3.length();
				//OutString.append("1");
				for(int i=0;i<16-L;i++)
					OutString.append("0");
				OutString.append(R3);
				
			}
			else if("mov".equals(G1)&&match3.find()){
				OutString.append("000000010110000000000000");
				String G2=match3.group(1);
				String G3=match3.group(2);
				int r2=Integer.parseInt(G2);
				String R2=Integer.toBinaryString(r2);
				int L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				OutString.append(G3);
			}
			else if("cvt".equals(G1)&&match1.find()){
				if(match5.find()){
					OutString.append("0000000100100011");
				}
				else if(match6.find()){
					OutString.append("0000000100100100");
				}
				else OutString.append("0000000010100000");
				String G2=match1.group(2);
				String G3=match1.group(3);
				int r2=Integer.parseInt(G2);
				int r3=Integer.parseInt(G3);
				String R2=Integer.toBinaryString(r2);
				String R3=Integer.toBinaryString(r3);
				int L;
				L=R3.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R3);
				
				L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				OutString.append("00000000");
				OutString.append("00000000");
			}
			else if("cvt".equals(G1)&&match4.find()){
				OutString.append("0000000100111000");
				String G2=match4.group(1);
				String G3=match4.group(2);
				OutString.append("000");
				OutString.append(G3);
				int r2=Integer.parseInt(G2);
				String R2=Integer.toBinaryString(r2);
				int L;
				L=R2.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				OutString.append("00000000");
				OutString.append("00000000");
			}
			return OutString.toString();
		}
			
		
		match=bra.matcher(Instring);
		if (match.find()){
			OutString.append("0000010000110101");
			Matcher mmm;
			
			
			mmm=bp.matcher(Instring);
			if(mmm.find()){
				String G2=mmm.group(1);
				String G3=mmm.group(2);
				
				OutString.append(G3);
				
				OutString.append(G2);
			}
			else{
				mmm=b.matcher(Instring);
				if(mmm.find()){
					String G2=mmm.group(1);
					int a=0;
					for(int i=0;i<16;i++){
						char bit=G2.charAt(i);
						a = (a << 1) | (bit - '0');
					}
					a=a+1;
					String ax=Integer.toBinaryString(a);
					int ll=ax.length();
					StringBuilder mp=new StringBuilder();
					for(int i=0;i<16-ll;i++){
						mp.append("0");
					}
					mp.append(ax);
					OutString.append(mp.toString());
					OutString.append(G2);
					
				}
				
				
			}
			return OutString.toString();
		}
		match=syn.matcher(Instring);
		if(match.find()){
			OutString.append("000001000111111000000000000000000000000000000000");
			return OutString.toString();
		}
		match=exit.matcher(Instring);
		if(match.find()){
			OutString.append("000001000111111100000000000000000000000000000000");
			return OutString.toString();
		}
		
		match=op4.matcher(Instring);
		if(match.find()){
			String G1=match.group(1);
			OutString.append(Op4.get(G1));
			Matcher mm=reg4.matcher(Instring);
			String r1,r2,r3,r4;
			if(mm.find()){
				r1=mm.group(2);
				r2=mm.group(3);
				r3=mm.group(4);
				r4=mm.group(5);
			}
			else{
				r1="d";r2="d";r3="d";r4="d";
			}
			
			int L;
			int rr2=Integer.parseInt(r2);
			String R2=Integer.toBinaryString(rr2);
			L=R2.length();
			for (int i=0;i<8-L;i++)
				OutString.append("0");
			OutString.append(R2);
			int rr3=Integer.parseInt(r3);
			String R3=Integer.toBinaryString(rr3);
			L=R3.length();
			for (int i=0;i<8-L;i++)
				OutString.append("0");
			OutString.append(R3);
			int rr1=Integer.parseInt(r1);
			String R1=Integer.toBinaryString(rr1);
			L=R1.length();
			for (int i=0;i<8-L;i++)
				OutString.append("0");
			OutString.append(R1);
			int rr4=Integer.parseInt(r4);
			String R4=Integer.toBinaryString(rr4);
			L=R4.length();
			for (int i=0;i<8-L;i++)
				OutString.append("0");
			OutString.append(R4);
			return OutString.toString();
		}
		
		match=setp.matcher(Instring);
		if(match.find()){
			String G=match.group(1);
			Matcher match1=regp3.matcher(Instring);
			Matcher match2=regp3i.matcher(Instring);
			
			if(match1.find()){
				OutString.append(Setp.get(G));
				String r1=match1.group(2);
				String r2=match1.group(3);
				String r3=match1.group(4);
				int L;
				int rr2=Integer.parseInt(r2);
				String R2=Integer.toBinaryString(rr2);
				L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				int rr3=Integer.parseInt(r3);
				String R3=Integer.toBinaryString(rr3);
				L=R3.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R3);
				int rr1=Integer.parseInt(r1);
				String R1=Integer.toBinaryString(rr1);
				L=R1.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R1);
				OutString.append("00000000");
			}
			else if(match2.find()){
				OutString.append(Setpi.get(G));
				String r1=match2.group(2);
				String r2=match2.group(3);
				String r3=match2.group(4);
				int L;
				int rr2=Integer.parseInt(r2);
				String R2=Integer.toBinaryString(rr2);
				L=R2.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R2);
				int rr1=Integer.parseInt(r1);
				String R1=Integer.toBinaryString(rr1);
				L=R1.length();
				for (int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(R1);
				int rr3=Integer.parseInt(r3);
				String R3=Integer.toBinaryString(rr3);
				L=R3.length();
				for (int i=0;i<16-L;i++)
					OutString.append("0");
				OutString.append(R3);
			}
			return OutString.toString();
		}
		match=ld.matcher(Instring);
		if(match.find()){
			String G=match.group(1);
			if("shared".equals(G)){
				OutString.append("0000001110110001");
			}
			else
				OutString.append("0000001110110001");
			Matcher match1=ldr.matcher(Instring);
			Matcher match2=ldp.matcher(Instring);
			if(match1.find()){
				String G1=match1.group(1);
				String G2=match1.group(2);
				String G3=match1.group(3);
				int r1=Integer.parseInt(G1);
				int r2=Integer.parseInt(G2);
				int r3=Integer.parseInt(G3);
				String rr1=Integer.toBinaryString(r1);
				String rr2=Integer.toBinaryString(r2);
				String rr3=Integer.toBinaryString(r3);
				int L;
				L=rr2.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(rr2);
				L=rr1.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(rr1);
				L=rr3.length();
				for(int i=0;i<16-L;i++)
					OutString.append("0");
				OutString.append(rr3);
				
			}
			else if(match2.find()){
				String G1=match2.group(1);
				String G2=match2.group(3);
				OutString.append("00000000");
				int r1=Integer.parseInt(G1);
				String rr1=Integer.toBinaryString(r1);
				int L;
				L=rr1.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(rr1);
				OutString.append(G2);
			}
			return OutString.toString();
		}
		match=st.matcher(Instring);
		if(match.find()){
			String G=match.group(1);
			if("shared".equals(G)){
				OutString.append("0000001110110011");
			}
			else
				OutString.append("0000001110110011");
			
			Matcher match1=str.matcher(Instring);
			if(match1.find()){
				String G1=match1.group(1);
				String G2=match1.group(2);
				String G3=match1.group(3);
				int r1=Integer.parseInt(G1);
				int r2=Integer.parseInt(G2);
				int r3=Integer.parseInt(G3);
				String rr1=Integer.toBinaryString(r1);
				String rr2=Integer.toBinaryString(r2);
				String rr3=Integer.toBinaryString(r3);
				int L;
				L=rr1.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(rr1);
				L=rr3.length();
				for(int i=0;i<8-L;i++)
					OutString.append("0");
				OutString.append(rr3);
				L=rr2.length();
				for(int i=0;i<16-L;i++)
					OutString.append("0");
				OutString.append(rr2);
			}
			return OutString.toString();
		}
		
		OutString.append("XD");
		return OutString.toString();
		
	}

}
