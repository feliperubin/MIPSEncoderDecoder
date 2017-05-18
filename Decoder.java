import java.util.*;
public class Decoder{
	private Information information;
	public Decoder(){
		information = new Information();
	}
	/*
		Decodifica a instrucao
	*/
	public String decode(Map<String,String> textMemory, String currMemory, String instruction) throws Exception{
		
		String binInstruction = Calculator.hexToBinString(instruction,32);

		String opCode = binInstruction.substring(0,6);


		switch(information.getTypeByOpCode(opCode)){
			case J: return decodeTypeJ(textMemory, binInstruction);
			case BRANCH: return decodeTypeBRANCH(textMemory,currMemory,binInstruction);
			case LOADSTORE: return decodeTypeLOADSTORE(binInstruction);
			case I: return decodeTypeI(binInstruction);
			case R: 
			case SHIFT: return (information.getTypeByFuncCode(binInstruction.substring(26)) == InstructionType.R ? decodeTypeR(binInstruction) : decodeTypeSHIFT(binInstruction));
			case LUI: return decodeTypeLUI(binInstruction);
			default: throw new Exception("Error encode("+instruction+")");
		}

		
	}


	/*

	*/
	private String decodeTypeJ(Map<String,String> textMemory, String binInstruction){
		//Tem q criar a label
		String decoded = "";

		decoded+= "j ";

		String jumpAddr = binInstruction.substring(6);

		jumpAddr = "0000"+jumpAddr+"00";

		decoded+= Calculator.binToHexString(jumpAddr); 


		return decoded;
	}

	/*

	*/
	private String decodeTypeBRANCH(Map<String,String> textMemory, String currMemory, String binInstruction) throws Exception{
		String decoded = "";

		String opCode = binInstruction.substring(0,6);

		//beq ou bne
		decoded+=information.getNameByOpCode(opCode)+" ";

		//rs
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(6,11)))+",";

		//rt
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(11,16)))+",";		

		//offset
		String offset = binInstruction.substring(16,32);

		int labelDistance = 0;

		if(offset.charAt(0) == '1'){ //Negativo
			labelDistance = Calculator.binToNegativeInt(offset);
		}else{ //positivo
			labelDistance = Calculator.binToInt(offset);
		}

		//Endereco da label
		String labelMemory = Calculator.addIntToHex((4 *(labelDistance +1)),currMemory);

		decoded+=labelMemory;


		return decoded;
	}

	/*
	*/
	private String decodeTypeLOADSTORE(String binInstruction) throws Exception{
		String decoded = "";
		String opCode = binInstruction.substring(0,6);

		//beq ou bne
		decoded+=information.getNameByOpCode(opCode)+" ";
		//rt
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(11,16)))+",";

		//offset
		String offset = binInstruction.substring(16,32);
		int offsetVal = 0;
		if(offset.charAt(0) == '1'){ //Negativo
			offsetVal = Calculator.binToNegativeInt(offset);
		}else{ //positivo
			offsetVal = Calculator.binToInt(offset);
		}

		decoded+=offsetVal;
		//rs
		decoded+="("+information.getRegisterName(Calculator.binToInt(binInstruction.substring(6,11)))+")";

		return decoded;
	}

	/*

	*/
	private String decodeTypeI(String binInstruction) throws Exception{
		String decoded = "";

		String opCode = binInstruction.substring(0,6);

		//beq ou bne
		decoded+=information.getNameByOpCode(opCode)+" ";
		//rt
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(11,16)))+",";
		//rs
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(6,11)))+",";

		//imm
		String imm = binInstruction.substring(16,32);
		int immVal = 0;
		if(imm.charAt(0) == '1'){ //Negativo
			immVal = Calculator.binToNegativeInt(imm);
		}else{ //positivo
			immVal = Calculator.binToInt(imm);
		}

		decoded+=immVal;


		return decoded;
	}

	/*

	*/
	private String decodeTypeR(String binInstruction) throws Exception{
		String decoded = "";

		String funcCode = binInstruction.substring(26,32);

		//uma instr do tipo R
		decoded+=information.getNameByFuncCode(funcCode)+" ";
		//rd
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(16,21)))+",";
		//rs
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(6,11)))+",";
		//rt
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(11,16)));


		return decoded;
	}
	
	/*
	*/
	private String decodeTypeSHIFT(String binInstruction) throws Exception{
		String decoded = "";
		String funcCode = binInstruction.substring(26,32);

		//sll ou srl
		decoded+=information.getNameByFuncCode(funcCode)+" ";
		//rd
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(16,21)))+",";
		//rt
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(11,16)))+",";
		//shamt
		decoded+= Calculator.binToInt(binInstruction.substring(21,26));

		return decoded;
	}

	/*
	*/
	private String decodeTypeLUI(String binInstruction) throws Exception{
		String decoded = "";
		
		String opCode = binInstruction.substring(0,6);

		//lui mesmo, soh por padronizar
		decoded+=information.getNameByOpCode(opCode)+" ";
		//rt
		decoded+=information.getRegisterName(Calculator.binToInt(binInstruction.substring(11,16)))+",";
		
		decoded+= Calculator.binToInt(binInstruction.substring(16,32));


		return decoded;
	}


}