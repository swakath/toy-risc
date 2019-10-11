package processor.pipeline;

import java.util.Scanner;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		containingProcessor.setEX_MA_Nop(EX_MA_Latch.getIsNop());
		
		//Special Handling for "end" instruction
        if(EX_MA_Latch.getInstruction() == -402653184){
            // Update OF_EX Latch
            MA_RW_Latch.setPC(EX_MA_Latch.getPC());
            MA_RW_Latch.setControlUnit(EX_MA_Latch.getControlUnit());    
            MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
        }

		//if isNop
		if(EX_MA_Latch.getIsNop()){

			EX_MA_Latch.setIsNop(false);
			MA_RW_Latch.setIsNop(true);
			System.out.println("MA got NOP");
		}

		if(EX_MA_Latch.isMA_enable()) {
			ControlUnit cu = EX_MA_Latch.getControlUnit();
			cu.setOpCode(EX_MA_Latch.getInstruction());

			if(cu.isLd()) {
				int ldResult;
				ldResult = containingProcessor.getMainMemory().getWord(EX_MA_Latch.getALUResult());
				MA_RW_Latch.setLdResult(ldResult);
				System.out.println("getALUResult: " + Integer.toString(EX_MA_Latch.getALUResult()));
				System.out.println("ldResult: " + Integer.toString(ldResult));
			} else if(cu.isSt()) {
				int location = EX_MA_Latch.getALUResult();
				int data = EX_MA_Latch.getOp2();
				System.out.println("location: " + Integer.toString(location));
				System.out.println("data: " + Integer.toString(data));
				containingProcessor.getMainMemory().setWord(location, data);

			}

			
			// // debug
			// Scanner input = new Scanner(System.in);
			// System.out.print("Enter an MA integer: ");
			// int number = input.nextInt();

			// Update MA_RW Latch
			MA_RW_Latch.setPC(EX_MA_Latch.getPC());
			MA_RW_Latch.setInstruction(EX_MA_Latch.getInstruction());
			MA_RW_Latch.setALUResult(EX_MA_Latch.getALUResult());
			System.out.println("ALUResult: " + Integer.toString(EX_MA_Latch.getALUResult()));
			System.out.println("CU_OPCODE: " + cu.getOpCode());
			MA_RW_Latch.setControlUnit(cu);

			if(EX_MA_Latch.isMA_enable()){ //isBranchTaken is false
				MA_RW_Latch.setRW_enable(true);
			} else {//isBranchTaken is true
				MA_RW_Latch.setRW_enable(false);
			}
			EX_MA_Latch.setMA_enable(false);


			// debug
			Scanner input = new Scanner(System.in);
			System.out.println("Enter an integer: ");
			int number = input.nextInt();

		}
				
	}

}
