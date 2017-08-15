
import java.util.regex.*;
import java.util.HashMap;

public class isaConvert {
	private Pattern reg,pred,addi,addc,addf,subi,subc,subf,muli,mulf,madi,madf,divi,divf;
	private Pattern setp,mov,ld,st,bra,reg2,reg3,reg4,preg3;
	private HashMap<String,String> typeUS6Hash,typeUS2Hash,addcHash,rndHash;
	private HashMap<String,String> ftzHash,satHash,typeF2Hash,subcHash,hiloHash;
	private HashMap<String,String> rnd2Hash,rnd3Hash,cmpopHash,boolopHash,typeBUSF11Hash;
	private int regNum;
	public isaConvert(){
		regNum=0;
		reg=Pattern.compile("(.reg .[usf]\\d{1,2} \\%[a-zA-Z]+<(\\d+)>;)");
		pred=Pattern.compile("(@(!)?%p(\\d+))");
		addi=Pattern.compile("(add(.sat)?.([us]\\d{2}))");
		addc=Pattern.compile("((add.cc|addc|addc.cc).([us])32)");
		addf=Pattern.compile("(add(.r[mnpz])?(.ftz)?(.sat)?.f(32|64))");
		subi=Pattern.compile("(sub(.sat)?.([us]\\d{2}))");
		subc=Pattern.compile("((sub.cc|subc|subc.cc).([us])32)");
		subf=Pattern.compile("(sub(.r[mnpz])?(.ftz)?(.sat)?.f(32|64))");
		muli=Pattern.compile("(mul(.hi|.lo|.wide)?.([us]\\d{2}))");
		mulf=Pattern.compile("(mul(.r[mnpz])?(.ftz)?(.sat)?.f(32|64))");
		madi=Pattern.compile("(mad(.hi|.lo|.wide)?(.sat)?.([us]\\d{2}))");
		madf=Pattern.compile("(mad(.r[nzmp])?(.ftz)?(.sat)?.f(32|64))");
		divi=Pattern.compile("(div.([us]\\d{2}))");
		divf=Pattern.compile("(div(.approx|.full|.r[nzmp])(.ftz)?.f(32|64))");
		setp=Pattern.compile("(setp(.eq|.ne|.lt|.gt|.ge|.lo|.ls|.hi|.hs|.equ|.neu|.ltu|.leu|.gtu|.geu|.num|.nan)(.and|.or|.xor)?(.ftz)?(.[busf]\\d{2}))");
		mov=Pattern.compile("(mov.([busf]\\d{2}||pred))");
		ld=Pattern.compile("(ld.(global|shared|local|const))");
		st=Pattern.compile("(st.(global|shared|local|const))");
		bra=Pattern.compile("(bra)");
		reg3=Pattern.compile("(\\%r(\\d+), \\%r(\\d+), \\%r(\\d+))");
		reg4=Pattern.compile("(\\%r(\\d+), \\%r(\\d+), \\%r(\\d+), \\%r(\\d+))");
		preg3=Pattern.compile("(\\%p(\\d+), \\%r(\\d+), \\%r(\\d+))");
		
		//used in addi subi muli madi divi
		typeUS6Hash=new HashMap<String,String>();
		typeUS6Hash.put("u16","000");
		typeUS6Hash.put("u32","001");
		typeUS6Hash.put("u64","010");
		typeUS6Hash.put("s16","100");
		typeUS6Hash.put("s32","101");
		typeUS6Hash.put("s64","110");
		
		//used in addc subc
		typeUS2Hash=new HashMap<String,String>();
		typeUS2Hash.put("u","0");
		typeUS2Hash.put("s", "1");
		
		//used in addc subc
		addcHash=new HashMap<String,String>();
		addcHash.put("add.cc", "00");
		addcHash.put("addc", "10");
		addcHash.put("addc.cc", "11");
		
		//used in addf subf
		rndHash=new HashMap<String,String>();
		rndHash.put(".rn", "100");
		rndHash.put(".rz", "101");
		rndHash.put(".rm", "110");
		rndHash.put(".rp", "111");
		rndHash.put(null, "010");
		
		//used in addf subf mulf madf divf
		ftzHash=new HashMap<String,String>();
		ftzHash.put(null,"0");
		ftzHash.put(".ftz","1");
		
		//used in addf subf mulf madf divf
		satHash=new HashMap<String,String>();
		satHash.put(null, "0");
		satHash.put(".sat", "1");
		
		//used in addf subf
		typeF2Hash=new HashMap<String,String>();
		typeF2Hash.put("32", "0");
		typeF2Hash.put("64", "1");
		
		//used in subc
		subcHash=new HashMap<String,String>();
		subcHash.put("sub.cc", "00");
		subcHash.put("subc", "10");
		subcHash.put("subc.cc", "11");
		
		//used in muli madi
		hiloHash=new HashMap<String,String>();
		hiloHash.put(null, "00");
		hiloHash.put(".hi", "01");
		hiloHash.put(".lo", "10");
		hiloHash.put(".wide", "11");
		
		//used in mulf madf
		rnd2Hash=new HashMap<String,String>();
		rnd2Hash.put(null, "100");
		rnd2Hash.put(".rn", "000");
		rnd2Hash.put(".rz", "001");
		rnd2Hash.put(".rm", "010");
		rnd2Hash.put(".rp", "011");
		
		//used in divf
		rnd3Hash=new HashMap<String,String>();
		rnd3Hash.put(".rn", "00");
		rnd3Hash.put(".rz", "01");
		rnd3Hash.put(".rm", "10");
		rnd3Hash.put(".rp", "11");
		
		//used in setp
		cmpopHash=new HashMap<String,String>();
		cmpopHash.put(".eq", "01010");
		cmpopHash.put(".ne", "01011");
		cmpopHash.put(".lt", "01100");
		cmpopHash.put(".gt", "01101");
		cmpopHash.put(".ge", "01110");
		cmpopHash.put(".lo", "01111");
		cmpopHash.put(".ls", "10000");
		cmpopHash.put(".hi", "10001");
		cmpopHash.put(".hs", "10010");
		cmpopHash.put(".equ", "10011");
		cmpopHash.put(".neu", "10100");
		cmpopHash.put(".ltu", "10101");
		cmpopHash.put(".leu", "10110");
		cmpopHash.put(".gtu", "10111");
		cmpopHash.put(".geu", "11000");
		cmpopHash.put(".num", "11001");
		cmpopHash.put(".nan", "11010");
		
		//used in setp
		boolopHash=new HashMap<String,String>();
		boolopHash.put(null, "00");
		boolopHash.put(".and", "01");
		boolopHash.put("or", "10");
		boolopHash.put("xor", "11");
		
		//used in setp
		typeBUSF11Hash=new HashMap<String,String>();
		typeBUSF11Hash.put(".b16","0000");
		typeBUSF11Hash.put(".b32","0001");
		typeBUSF11Hash.put(".b64","0010");
		typeBUSF11Hash.put(".u16","0100");
		typeBUSF11Hash.put(".u32","0101");
		typeBUSF11Hash.put(".u64","0110");
		typeBUSF11Hash.put(".s16","1000");
		typeBUSF11Hash.put(".s32","1001");
		typeBUSF11Hash.put(".s64","1010");
		typeBUSF11Hash.put(".f32","1100");
		typeBUSF11Hash.put(".f64","1101");
		
		
		
	}
	public String Convert(String InString){
		StringBuilder OutString;
		Matcher Match; 
		
		//reg
		Match=reg.matcher(InString);
		if(Match.find()){
			System.out.println("reg Yes!");
			String G1=Match.group(2);
			regNum=regNum+Integer.parseInt(G1);
		}
		
		//pred
		Match=pred.matcher(InString);
		if(Match.find()){
			System.out.println("pred Yes!");
			String G1=Match.group(2);
			String G2=Match.group(3);
			if ("!".equals(G1))
				OutString=new StringBuilder("00000000000");
			else
				OutString=new StringBuilder("00000000001");
			int pN=regNum+Integer.parseInt(G2);
			String G3=Integer.toBinaryString(pN);
			int LL=G3.length();
			for (int i=0;i<5-LL;i++)
				OutString.append("0");
			OutString.append(G3);
		}
		else
			OutString=new StringBuilder("0000000000000000");
		
		//add
		Match=addi.matcher(InString);
		if (Match.find()){
			System.out.println("add Yes!");
			/*for (int i=1;i<=2;i++){
				System.out.println(addM.group(i));
			}*/
			OutString.append("0000000000000");
			String G1=Match.group(2);
			if ((".sat").equals(G1))
				OutString.append("111");
			else {
				String G2=Match.group(3);
				OutString.append(typeUS6Hash.get(G2));
			}
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//addc
		Match=addc.matcher(InString);
		if (Match.find()){
			System.out.println("adc Yes!");
			OutString.append("0000000000001");
			String G1=Match.group(2);
			OutString.append(addcHash.get(G1));
			String G2=Match.group(3);
			OutString.append(typeUS2Hash.get(G2));
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//addf
		Match=addf.matcher(InString);
		if(Match.find()){
			System.out.println("adf Yes!");
			OutString.append("0000000000");
			String G1=Match.group(2);  
			String G2=Match.group(3);
			String G3=Match.group(4);
			String G4=Match.group(5);
			OutString.append(rndHash.get(G1));
			OutString.append(ftzHash.get(G2));
			OutString.append(satHash.get(G3));
			OutString.append(typeF2Hash.get(G4));
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//subi
		Match=subi.matcher(InString);
		if (Match.find()){
			System.out.println("sub Yes!");
			/*for (int i=1;i<=2;i++){
				System.out.println(addM.group(i));
			}*/
			OutString.append("0000000001000");
			String G1=Match.group(2);
			if ((".sat").equals(G1))
				OutString.append("111");
			else {
				String G2=Match.group(3);
				OutString.append(typeUS6Hash.get(G2));
			}
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//subc
		Match=subc.matcher(InString);
		if (Match.find()){
			System.out.println("sbc Yes!");
			OutString.append("0000000001001");
			String G1=Match.group(2);
			OutString.append(subcHash.get(G1));
			String G2=Match.group(3);
			OutString.append(typeUS2Hash.get(G2));
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//subf
		Match=subf.matcher(InString);
		if(Match.find()){
			System.out.println("sbf Yes!");
			OutString.append("0000000001");
			String G1=Match.group(2);  
			String G2=Match.group(3);
			String G3=Match.group(4);
			String G4=Match.group(5);
			OutString.append(rndHash.get(G1));
			OutString.append(ftzHash.get(G2));
			OutString.append(satHash.get(G3));
			OutString.append(typeF2Hash.get(G4));
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		
		//muli
		Match=muli.matcher(InString);
		if(Match.find()){
			System.out.println("muli Yes!");
			OutString.append("0000000010");
			String G1=Match.group(2);
			String G2=Match.group(3);
			OutString.append("0");
			OutString.append(hiloHash.get(G1));
			OutString.append(typeUS6Hash.get(G2));
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//mulf
		Match=mulf.matcher(InString);
		if(Match.find()){
			System.out.println("mulf Yes!");
			OutString.append("0000000010");
			String G1=Match.group(2);
			String G2=Match.group(3);
			String G3=Match.group(4);
			String G4=Match.group(5);
			if (G4.equals("32")){
				OutString.append("1");
				OutString.append(rnd2Hash.get(G1));
				OutString.append(ftzHash.get(G2));
				OutString.append(satHash.get(G3));
			}
			else{
				OutString.append("111");
				OutString.append(rnd2Hash.get(G1));
			}
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//madi
		Match=madi.matcher(InString);
		if(Match.find()){
			System.out.println("madi Yes!");
			OutString.append("0000000011");
			String G1=Match.group(2);
			String G2=Match.group(3);
			String G3=Match.group(4);
			if (".sat".equals(G2)){
				OutString.append("001111");
			}
			else {
				OutString.append("0");
				OutString.append(hiloHash.get(G1));
				OutString.append(typeUS6Hash.get(G3));
			}
			Match=reg4.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				String R4=Match.group(5);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				int r4=Integer.parseInt(R4);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				String B4=Integer.toBinaryString(r4);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				int l4=B4.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				for (int i=0;i<8-l4;i++)
					OutString.append("0");
				OutString.append(B4);
				
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//madf
		Match=madf.matcher(InString);
		if(Match.find()){
			System.out.println("madf Yes!");
			OutString.append("0000000011");
			String G1=Match.group(2);
			String G2=Match.group(3);
			String G3=Match.group(4);
			String G4=Match.group(5);
			if("32".equals(G4)){
				OutString.append("1");
				OutString.append(rnd2Hash.get(G1));
				OutString.append(ftzHash.get(G2));
				OutString.append(satHash.get(G3));
			}
			else {
				OutString.append("111");
				OutString.append(rnd2Hash.get(G1));
			}
			Match=reg4.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				String R4=Match.group(5);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				int r4=Integer.parseInt(R4);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				String B4=Integer.toBinaryString(r4);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				int l4=B4.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				for (int i=0;i<8-l4;i++)
					OutString.append("0");
				OutString.append(B4);
				
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//divi
		Match=divi.matcher(InString);
		if(Match.find()){
			System.out.println("divi Yes!");
			OutString.append("0000000101000");
			String G1=Match.group(2);
			OutString.append(typeUS6Hash.get(G1));
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//divf
		Match=divf.matcher(InString);
		if(Match.find()){
			System.out.println("divf Yes!");
			OutString.append("0000000101");
			String G1=Match.group(2);
			String G2=Match.group(3);
			String G3=Match.group(4);
			if (".approx".equals(G1)){
				OutString.append("00100");
				OutString.append(ftzHash.get(G2));
			}
			else if (".full".equals(G1)){
				OutString.append("00101");
				OutString.append(ftzHash.get(G2));
			}
			else {
				if("64".equals(G3)){
					OutString.append("0011");
					OutString.append(rnd3Hash.get(G1));
				}
				else {
					OutString.append("010");
					OutString.append(rnd3Hash.get(G1));
					OutString.append(ftzHash.get(G2));
				}
			}
			Match=reg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1);
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//setp
		Match=setp.matcher(InString);
		if(Match.find()){
			System.out.println("setp Yes!");
			OutString.append("00011");
			String G1=Match.group(2);
			String G2=Match.group(3);
			//String G3=Match.group(4);
			String G4=Match.group(5);
			OutString.append(cmpopHash.get(G1));
			OutString.append(boolopHash.get(G2));
			OutString.append(typeBUSF11Hash.get(G4));
			Match=preg3.matcher(InString);
			if (Match.find()){
				System.out.println("Found!");
				String R1=Match.group(2);
				String R2=Match.group(3);
				String R3=Match.group(4);
				int r1=Integer.parseInt(R1)+regNum;
				int r2=Integer.parseInt(R2);
				int r3=Integer.parseInt(R3);
				String B1=Integer.toBinaryString(r1);
				String B2=Integer.toBinaryString(r2);
				String B3=Integer.toBinaryString(r3);
				int l1=B1.length();
				int l2=B2.length();
				int l3=B3.length();
				for (int i=0;i<8-l1;i++)
					OutString.append("0");
				OutString.append(B1);
				for (int i=0;i<8-l2;i++)
					OutString.append("0");
				OutString.append(B2);
				for (int i=0;i<8-l3;i++)
					OutString.append("0");
				OutString.append(B3);
				int L=OutString.length();
				for (int i=0;i<8;i++)
					OutString.append("0");
			}
			else System.out.println("Not Found");
			return OutString.toString();
		}
		
		//mov *******************
		Match=mov.matcher(InString);
		if(Match.find()){
			System.out.println("mov Yes!");
			OutString.append("0000000000011001");
			OutString.append("00000000000000000000000000000000");
			return OutString.toString();
		}
		
		//ld  ***********************
		Match=ld.matcher(InString);
		if(Match.find()){
			System.out.println("ld Yes!");
			OutString.append("000000000010");
			String G1=Match.group(2);
			if (G1.equals("global"))
				OutString.append("1010");
			if (G1.equals("shared"))
				OutString.append("1011");
			if (G1.equals("local"))
				OutString.append("1100");
			if (G1.equals("const"))
				OutString.append("1101");
			return OutString.toString();
		}
		
		//st *******************
		Match=st.matcher(InString);
		if(Match.find()){
			System.out.println("st Yes!");
			OutString.append("00000000001");
			String G1=Match.group(2);
			if (G1.equals("global"))
				OutString.append("01110");
			if (G1.equals("shared"))
				OutString.append("01111");
			if (G1.equals("local"))
				OutString.append("10000");
			if (G1.equals("const"))
				OutString.append("10001");
			return OutString.toString();
		}
		
		//bra *********************
		Match=bra.matcher(InString);
		if(Match.find()){
			System.out.println("bra Yes!");
			OutString.append("0000000000110010");
			return OutString.toString();
		}
		
		return "XD";
		
		
		
		
	}
}
