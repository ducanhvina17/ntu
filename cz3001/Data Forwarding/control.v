//control unit for write enable and ALU control
`include "define.v"

module control(
	input [5:0] inst_cntrl,
	output reg wen_cntrl,	// Write enabled
	output reg alusrc_cntrl,	// ALUsrc
	output reg [3:0] aluop_cntrl,	// ALUop
	output reg branch,	// Whether branch is needed
	output reg jal,	// Jump and Link
	output reg jr,	// Jump register
	output reg jump,	// Jump
	output reg memWrite,	// MemWrite for Data Memory
	output reg memRead,	// MemRead for Date Memory
	output reg memtoReg,	// 
	output reg regDst	// Register Destination
	);


always@(inst_cntrl)
begin
	case(inst_cntrl)
		`ADD:
		begin
			wen_cntrl=1;
			alusrc_cntrl=0;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 1;
			regDst = 1;
		end
		`ADDI:
		begin
			wen_cntrl=1;
			alusrc_cntrl=1;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 1;
			regDst = 0;
		end
		`AND:
		begin
			wen_cntrl=1;
			alusrc_cntrl=0;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 1;
			regDst = 0;
		end
		`BEQ: begin
			wen_cntrl=0;
			alusrc_cntrl = 0;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 1;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 1;
			regDst = 1;
		end
		`COM:
		begin
			wen_cntrl=1;
			alusrc_cntrl=0;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 1;
			regDst = 0;
		end
		`J:
		begin
			wen_cntrl=1;
			alusrc_cntrl=1;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 1;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 0;
			regDst = 0;
		end
		`JAL:
		begin
			wen_cntrl=1;
			alusrc_cntrl=1;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 1;
			jal = 1;
			memWrite = 0;
			memRead = 0;
			memtoReg = 0;
			regDst = 0;
		end
		`JR:
		begin
			wen_cntrl=1;
			alusrc_cntrl=1;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 1;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 1;
			memtoReg = 0;
			regDst = 0;
		end
		`LW:
		begin
			wen_cntrl = 1;
			alusrc_cntrl = 1;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 1;
			memtoReg = 0;
			regDst = 0;		
		end
		`MUL:
		begin
			wen_cntrl=1;
			alusrc_cntrl=0;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 1;
			regDst = 0;
		end
		`SW:
		begin
			wen_cntrl = 0;
			alusrc_cntrl = 1;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 1;
			memRead = 0;	// Not used
			memtoReg = 0;
			regDst = 1;
		end
		`SUB:
		begin
			wen_cntrl=1;
			alusrc_cntrl=0;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 1;
			regDst = 1;
		end
		`XOR:
		begin
			wen_cntrl=1;
			alusrc_cntrl=0;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 1;
			regDst = 0;
		end
		default:
		begin
			wen_cntrl = 0;
			alusrc_cntrl=0;
			aluop_cntrl=inst_cntrl[3:0];
			branch = 0;
			jr = 0;
			jump = 0;
			jal = 0;
			memWrite = 0;
			memRead = 0;
			memtoReg = 0;
			regDst = 0;
		end	
	endcase
end


endmodule
