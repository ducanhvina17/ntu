`include "define.v"

module EXE_MEM_stage (
	input  clk,  rst, wen_in,
	input [`DSIZE-1:0] alu_in,
	input [`ASIZE-1:0] waddr_in,
	input [`DSIZE-1:0] rdata2_in,
	input jal_in, memRead_in, memWrite_in, memtoReg_in,
	input [`ISIZE-1:0] pc_in,

	output reg wen_out,
	output reg [`DSIZE-1:0] alu_out,
	output reg [`ASIZE-1:0] waddr_out,
	output reg [`DSIZE-1:0] rdata2_out,
	output reg jal_out, memRead_out, memWrite_out, memtoReg_out,
	output reg [`ISIZE-1:0] pc_out
	);


always @ (posedge clk)
begin
	if(rst)
		begin
			wen_out <= 0;
			alu_out <= 0;
			waddr_out <= 0;
			rdata2_out <= 0;

			jal_out <= 0;
			memRead_out <= 0;
			memWrite_out <= 0;
			memtoReg_out <= 0;
			pc_out <= 0;
		end
	else
		begin
			wen_out <= wen_in;
			alu_out <= alu_in;
			waddr_out <= waddr_in;
			rdata2_out <= rdata2_in;
			jal_out <= jal_in;
			memRead_out <= memRead_in;
			memWrite_out <= memWrite_in;
			memtoReg_out <= memtoReg_in;
			pc_out <= pc_in;
		end
end


endmodule
