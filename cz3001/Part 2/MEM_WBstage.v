`include "define.v"

module MEM_WB (
	input clk, rst,
	input [`DSIZE-1:0] alu_in,
	input [`ASIZE-1:0] waddr_in,
	
	input jal_in, memtoReg_in, wen_in,
	input [`ISIZE-1:0] pc_in,

	output reg [`DSIZE-1:0] alu_out,
	output reg [`ASIZE-1:0] waddr_out,
	output reg jal_out, memtoReg_out, wen_out,
	output reg [`ISIZE-1:0] pc_out
	);

//here we have not taken write enable (wen) as it is always 1 for R and I type instructions.
//ID_EXE register to save the values.

always @ (posedge clk) begin
	if(rst)
		begin
			alu_out <= 0;
			waddr_out <= 0;
			jal_out <= 0;
			memtoReg_out <= 0;
			wen_out <= 0;
			pc_out <= 0;
		end
	else
		begin
			alu_out <= alu_in;
			waddr_out <= waddr_in;
			jal_out <= jal_in;
			memtoReg_out <= memtoReg_in;
			wen_out <= wen_in;
			pc_out <= pc_in;
		end
	end
endmodule
