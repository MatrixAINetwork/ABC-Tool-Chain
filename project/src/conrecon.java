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

public class conrecon {
	static private FileReader fin;
	static private FileWriter fout;
	static private BufferedReader sin;
	static private BufferedWriter sout;
	private int[][] branch, path;
	private Stack<Integer> Pstack;
	private Pattern bra, pre, num1, num2;
	private int braNum;
	private HashMap<String, String> waL;
	private HashMap<String, Integer> Bno1;
	private HashMap<Integer, String> Bno2;

	public conrecon() {
		bra = Pattern.compile("(bra)");
		pre = Pattern.compile("@%p");
		num1 = Pattern
				.compile("^([01][01][01][01][01][01][01][01][01][01][01][01][01][01][01][01])");
		num2 = Pattern
				.compile(";([01][01][01][01][01][01][01][01][01][01][01][01][01][01][01][01])");
		
		String InString;
		Matcher match1, match2, match3;
		braNum = 0;
		Pstack = new Stack<Integer>();
		Bno1 = new HashMap<String, Integer>();
		Bno2 = new HashMap<Integer, String>();
		waL = new HashMap<String, String>();
		

		try {
			fin = new FileReader(new File("isa.out"));
			// fout=new FileWriter(new File("isare.out"));
			sin = new BufferedReader(fin);
			// sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int OKL = 0;
		try {
			while ((InString = sin.readLine()) != null) {
				System.out.println(InString);
				if(OKL==1){
					OKL=0;
					match1=num1.matcher(InString);
					if(match1.find()){
						String G=match1.group(1);
						if(!"1".equals(waL.get(G))){
							braNum=braNum+1;
							waL.put(G, "1");
						}
					}
				}
				match1 = bra.matcher(InString);
				if (match1.find()) {
					match2=num1.matcher(InString);
					if(match2.find()){
						String G = match2.group(1);
						if(!"1".equals(waL.get(G))){
							waL.put(G, "1");
							braNum=braNum+1;
						}
					}
					
					match2=num2.matcher(InString);
					if(match2.find()){
						String GG = match2.group(1);
						if(!"1".equals(waL.get(GG))){
							braNum=braNum+1;
							waL.put(GG, "1");
						}
					}
					match3=pre.matcher(InString);
					if(match3.find()){
						OKL=1;
					}
				}
				
						
				

			}
			sin.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		branch = new int[braNum + 1][8];
		path = new int[braNum + 1][8];
		for (int i = 1; i <= braNum; i++) {
			branch[i][0] = i;
			path[i][0] = i;
			for (int j = 1; j <= 7; j++) {
				branch[i][j] = 0;
				path[i][j] = 0;
			}
		}

		OKL = 0;
		int bnum=0;
	
		try {
			fin = new FileReader(new File("isa.out"));
			// fout=new FileWriter(new File("isare.out"));
			sin = new BufferedReader(fin);
			// sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while ((InString = sin.readLine()) != null) {
				System.out.println(InString);
				match1 = num1.matcher(InString);
				String G;
				if (match1.find()) {
					G=match1.group(1);
					if("1".equals(waL.get(G))){
						bnum=bnum+1;
						Bno1.put(G, bnum);
						Bno2.put(bnum, G);
					}
						

				}
			}
			if(!"1".equals(waL.get("1111111111111111"))){
				bnum=bnum+1;
				Bno1.put("1111111111111111",braNum);
				Bno2.put(braNum,"1111111111111111");
			}
			
			sin.close();
		}catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fin = new FileReader(new File("isa.out"));
			// fout=new FileWriter(new File("isare.out"));
			sin = new BufferedReader(fin);
			// sout=new BufferedWriter(fout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while ((InString = sin.readLine()) != null) {
				String G;
				match1=bra.matcher(InString);
				if(match1.find()){
					int sn=0,dn1=0,dn2=0;
					
					match2=num1.matcher(InString);
					if(match2.find()){
						G=match2.group(1);
						sn=Bno1.get(G);
					}
					
					match2=num2.matcher(InString);
					if(match2.find()){
						G=match2.group(1);
						dn1=Bno1.get(G);
						branch[sn][1]=dn1;
						path[sn][1]=1;
						for(int i=3;i<=6;i++){
							if (branch[dn1][i]==0){
								branch[dn1][i]=sn;
								path[dn1][i]=1;
								break;
							}
						}
					}
					match2=pre.matcher(InString);
					if(match2.find()){
						int a=0;
						
						match3=num1.matcher(InString);
						if(match3.find())
							G=match3.group(1);
						else G="0";
						for(int i=0;i<16;i++){
							char bit=G.charAt(i);
							a = (a << 1) | (bit - '0');
						}
						a=a+1;
						StringBuilder t=new StringBuilder();
						String LL=Integer.toBinaryString(a);
						int L=LL.length();
						for(int i=0;i<16-L;i++){
							t.append("0");
						}
						t.append(LL);
						String Q=t.toString();
						if("1".equals(waL.get(Q))){
							dn2=Bno1.get(Q);
						}
						else dn2=Bno1.get("1111111111111111");
						branch[sn][2]=dn2;
						path[sn][2]=1;
						for(int i=3;i<=6;i++){
							if (branch[dn2][i]==0){
								branch[dn2][i]=sn;
								path[dn2][i]=1;
								break;
							}
						}
					}
				}

			}
			sin.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=1;i<braNum;i++)
			if(branch[i][1]==0){
				branch[i][1]=i+1;
				path[i][1]=1;
				for(int j=3;j<=6;j++){
					if(branch[i+1][j]==0){
						branch[i+1][j]=i;
						path[i+1][j]=1;
						break;
					}
				}
			}
		
		
	}
	public void count(){
		String InString;
		rec(braNum);
		//branch[10][7]=13;
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
				sout.write(InString);
				Matcher match1=pre.matcher(InString);
				Matcher match2=num1.matcher(InString);
				if(match1.find()&&match2.find()){
					String LL=match2.group(1);
					int LLA=Bno1.get(LL);
					String LLb=Bno2.get(branch[LLA][7]);
					sout.write("   ");
					if(branch[LLA][7]!=braNum){
						int a=0;
						for(int i=0;i<16;i++){
							char bit=LLb.charAt(i);
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
						sout.write(mp.toString());
						sout.newLine();
					}
					else{
						sout.write("1111111111111111");
						sout.newLine();
					}
						
						
					
				}
				else sout.newLine();
			}
			sout.close();
			sin.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		
		
	}
	private int rec(int Num){
		int a=0,b=0,c=0;
		if(!Pstack.empty()){
			b=Pstack.pop();
			Pstack.push(b);
		}
		if(branch[Num][2]!=0&&!Pstack.empty()){
			int x=Pstack.pop();
			branch[Num][7]=x;
			a=1;
		}
		if(Num==1){
			if(a==1&&b!=0){
				Pstack.push(b);
			}
			return 0;
		}
		if(branch[Num][4]!=0){
			int ln=0;
			for(int i=3;i<=6;i++){
				if(branch[Num][i]<Num&&branch[Num][i]!=0)
					ln=ln+1;
			}
			if(ln>1){
				Pstack.push(Num);
				c=1;
			}
			
		}
		for(int i=3;i<=6;i++){
			if(branch[Num][i]!=0&&path[Num][i]!=0&&branch[Num][i]<Num){
				rec(branch[Num][i]);
				path[Num][i]=0;
			}
		}
		if(a==1&&b!=0){
			Pstack.push(b);
		}
		if(c==1&&!Pstack.empty()){
			Pstack.pop();
		}
		return 1;
	}

}
