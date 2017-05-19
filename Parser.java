/*
	Fix the initial file 

*/
import java.util.*;
import java.io.*;
import java.nio.*;
public class Parser{
	private String file; //Arquivo a ser lido
	private String currentMemory; //Contador indica a PosMemTexto atual
	private String currentMemoryData; //Contador indica a PosMemDados atual
	private Map<String,String> dataMemory; //<PosMemoria,Dado>
	private Map<String,String> textMemory; //<PosMemoria,Instr>
	private Map<String,String> labelMemory; //<Label,PosMemoria>

	public Parser(String file){
		this.file = file;
		currentMemory = "0x00400000";
		currentMemoryData = "0x10010000";
		dataMemory = new LinkedHashMap<>();
		textMemory = new LinkedHashMap<>();
		labelMemory = new LinkedHashMap<>();
	}

	/*
		Le o arquivo .asm, nesse caso file eh uma string
		que contem tudo
	*/
	public String parseASMString() throws Exception{
		String resp = "";
		String line = "";

		try{
			
			BufferedReader br = new BufferedReader(new StringReader(file));


			while((line = br.readLine()) != null){

				if(line.matches("[\\s]*")) continue; //Se n tiver nada so pula

				if(line.contains(".globl")) continue; //Ignora

				if(line.contains(".text")){
					
					//pula o .text
					while((line = br.readLine()) != null){
						line = beautifyInstruction(line);						
						if(line.matches("[\\s]*")) continue; // Se n tiver nada pula
						if(line.contains(".globl")) continue; //Ignora
						if(line.contains(".data")) break; //Acabou as instr
						

						if(line.contains(":")){

							String labelSplit[] = line.split("\\s*:\\s*");//divide label de instr
							// <Label_1,0x004...>
							labelMemory.put(labelSplit[0],currentMemory);
							if(labelSplit.length > 1){
								line = labelSplit[1];
							}else continue;
						}
						// <0x004..., Instr>
						textMemory.put(currentMemory,line);

						//Currmemory++;
						currentMemory = Calculator.addIntToHex(4,currentMemory);
						
					}

				}

				if(line.contains(".data")){

					while((line = br.readLine()) != null){
						if(line.matches("[\\s]*")) continue;
						if(line.contains(".globl")) continue;
						if(line.contains(".text")) break;

						//TO DO , TALVEZ N PRECISE USAR O .data
					}
					//aqui a line pode ser nula 

				}
				if(line == null) break;

				if(line.contains(".text")){
					
					//pula o .text
					while((line = br.readLine()) != null){
						line = beautifyInstruction(line);						
						if(line.matches("[\\s]*")) continue; // Se n tiver nada pula
						if(line.contains(".globl")) continue; //Ignora
						if(line.contains(".data")) break; //Acabou as instr
						

						if(line.contains(":")){

							String labelSplit[] = line.split("\\s*:\\s*");//divide label de instr
							// <Label_1,0x004...>
							labelMemory.put(labelSplit[0],currentMemory);
							if(labelSplit.length > 1){
								line = labelSplit[1];
							}else continue;
						}
						// <0x004..., Instr>
						textMemory.put(currentMemory,line);

						//Currmemory++;
						currentMemory = Calculator.addIntToHex(4,currentMemory);
						
					}

				}
			}


		}catch(Exception e){
			throw new Exception("Error Reading "+ line);
		}

		Encoder encoder = new Encoder();

		try{
			for(Map.Entry<String,String> ks : textMemory.entrySet()){
				resp+=encoder.encode(labelMemory,ks.getKey(),ks.getValue())+"\n";
			}

		}catch(Exception e){
			throw new Exception("Eror while Encoding\n"+e.getMessage());
			//System.out.println("Error while Encoding");
			//System.out.println(e.getMessage());
		}

		return resp;		
	}

/*
		Le o arquivo .asm
*/
	public void parseASM() throws Exception{
		try{
			
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line = "";

			while((line = br.readLine()) != null){

				if(line.matches("[\\s]*")) continue; //Se n tiver nada so pula

				if(line.contains(".globl")) continue; //Ignora

				if(line.contains(".text")){
					
					//pula o .text
					while((line = br.readLine()) != null){
						line = beautifyInstruction(line);						
						if(line.matches("[\\s]*")) continue; // Se n tiver nada pula
						if(line.contains(".globl")) continue; //Ignora
						if(line.contains(".data")) break; //Acabou as instr
						

						if(line.contains(":")){

							String labelSplit[] = line.split("\\s*:\\s*");//divide label de instr
							// <Label_1,0x004...>
							labelMemory.put(labelSplit[0],currentMemory);
							if(labelSplit.length > 1){
								line = labelSplit[1];
							}else continue;
						}
						// <0x004..., Instr>
						textMemory.put(currentMemory,line);

						//Currmemory++;
						currentMemory = Calculator.addIntToHex(4,currentMemory);
						
					}

				}

				if(line.contains(".data")){

					while((line = br.readLine()) != null){
						if(line.matches("[\\s]*")) continue;
						if(line.contains(".globl")) continue;
						if(line.contains(".text")) break;

						//TO DO , TALVEZ N PRECISE USAR O .data
					}
					//aqui a line pode ser nula 

				}
				if(line == null) break;

				if(line.contains(".text")){
					
					//pula o .text
					while((line = br.readLine()) != null){
						line = beautifyInstruction(line);						
						if(line.matches("[\\s]*")) continue; // Se n tiver nada pula
						if(line.contains(".globl")) continue; //Ignora
						if(line.contains(".data")) break; //Acabou as instr
						

						if(line.contains(":")){

							String labelSplit[] = line.split("\\s*:\\s*");//divide label de instr
							// <Label_1,0x004...>
							labelMemory.put(labelSplit[0],currentMemory);
							if(labelSplit.length > 1){
								line = labelSplit[1];
							}else continue;
						}
						// <0x004..., Instr>
						textMemory.put(currentMemory,line);

						//Currmemory++;
						currentMemory = Calculator.addIntToHex(4,currentMemory);
						
					}

				}
			}


		}catch(Exception e){
			System.out.println(e.getMessage());
		}


		for(Map.Entry<String,String> ks : textMemory.entrySet()){
			System.out.print("["+ks.getKey()+ "] ");
			System.out.println(ks.getValue());

		}

		for(Map.Entry<String,String> ks : labelMemory.entrySet()){
			System.out.print("["+ks.getKey()+ "] ");
			System.out.println(ks.getValue());
		}

		Encoder encoder = new Encoder();

		try{

			for(Map.Entry<String,String> ks : textMemory.entrySet()){
				System.out.println(encoder.encode(labelMemory,ks.getKey(),ks.getValue()));
			}

		}catch(Exception e){
			System.out.println("Error while Encoding");
			System.out.println(e.getMessage());
		}
	}

	/*
		Le o arquivo .txt, nesse caso file eh uma string
		que contem tudo
	*/
	public String parseCodeString() throws Exception{
		String resp = "";
		String line =  "";
		try{
			BufferedReader br = new BufferedReader(new StringReader(file));
			while((line = br.readLine()) != null){

				if(line.matches("[\\s]*")) continue; //Se n tiver nada so pula
				
				if(!line.contains("0x")){  //Para o caso de testar valores direto do mars, n sai com 0x..
					textMemory.put(currentMemory,"0x"+line);
				}else{
					textMemory.put(currentMemory,line);
				}

				currentMemory = Calculator.addIntToHex(4,currentMemory);
			}
		}catch(Exception e){
			throw new Exception("Error Reading "+ line);
			//System.out.println(e.getMessage());
		}

		Decoder decoder = new Decoder();
		//Para a memoria inicial
		labelMemory.put("0x00400000","main");
		resp+=".text\n\n";
		resp+=".globl main\n";
		try{
			//Antes textMemory<PosMem,HexInstr> agora textMemory<PosMem,Instr>
			for(Map.Entry<String,String> ks : textMemory.entrySet()){
				textMemory.put(ks.getKey(),decoder.decode(labelMemory,ks.getKey(),ks.getValue()));
				//System.out.println(decoder.decode(labelMemory,ks.getKey(),ks.getValue()));
			}

			String label = "";
			for(Map.Entry<String,String> ks : textMemory.entrySet()){
				if((label = labelMemory.get(ks.getKey())) != null){
					resp+= label +":\n";

				}
				resp+=ks.getValue()+"\n";

			}
			resp+=".data";



		}catch(Exception e){
			System.out.println("Error while Decoding\n"+e.getMessage());
			//System.out.println(e.getMessage());
		}

		return resp;		

	}	

	/*
		Le o arquivo p/ decodificara as instrucoes

		O label memory sera usado ao contrario aqui!
		labelMemory<PosMemoria,Label>
	*/
	public void parseCode(){
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line =  "";
			while((line = br.readLine()) != null){

				if(line.matches("[\\s]*")) continue; //Se n tiver nada so pula
				
				if(!line.contains("0x")){  //Para o caso de testar valores direto do mars, n sai com 0x..
					textMemory.put(currentMemory,"0x"+line);
				}else{
					textMemory.put(currentMemory,line);
				}

				currentMemory = Calculator.addIntToHex(4,currentMemory);
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		for(Map.Entry<String,String> ks : textMemory.entrySet()){
			System.out.print("["+ks.getKey()+ "] ");
			System.out.println(ks.getValue());

		}

		Decoder decoder = new Decoder();
		//Para a memoria inicial
		labelMemory.put("0x00400000","main");
		try{
			//Antes textMemory<PosMem,HexInstr> agora textMemory<PosMem,Instr>
			for(Map.Entry<String,String> ks : textMemory.entrySet()){
				textMemory.put(ks.getKey(),decoder.decode(labelMemory,ks.getKey(),ks.getValue()));
				//System.out.println(decoder.decode(labelMemory,ks.getKey(),ks.getValue()));
			}

			System.out.println(".text");
			System.out.println(".globl main");
			String label = "";
			for(Map.Entry<String,String> ks : textMemory.entrySet()){
				if((label = labelMemory.get(ks.getKey())) != null){
					System.out.println(label+":");

				}
				System.out.println(ks.getValue());

			}
			System.out.println(".data");



		}catch(Exception e){
			System.out.println("Error while Decoding");
			System.out.println(e.getMessage());
		}		

	}


	/*
		Recebe uma instrucao ma formatada(muitos tabs, espacos...)
		Retorna ela formatada
	*/
	public String beautifyInstruction(String horrible){
		horrible = horrible.replaceAll("[#].*","");
		horrible = horrible.replaceAll("\\s+$","");
 		horrible = horrible.replaceAll("^[\\t\\s]*","");
		horrible = horrible.replaceAll("\\s*,",",");
		horrible = horrible.replaceAll(",\\s*",",");
		horrible = horrible.replaceAll("\\s*\\(\\s*","\\(");
		horrible = horrible.replaceAll("\\(\\s*","\\(");
		horrible = horrible.replaceAll("\\s*\\)","\\)");
		horrible = horrible.replaceAll("\\s+"," ");
		return horrible;
	}
}