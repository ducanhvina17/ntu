`include "define.v"

module ID_EXE_stage (
	input  clk,  rst, 
	input [`DSIZE-1:0] rdata1_in,
	input [`DSIZE-1:0] rdata2_in,
	input [`DSIZE-1:0] imm_in,
	input [3:0] opcode_in,
	input alusrc_in,
	input [`ASIZE-1:0] waddr_in,

	input branch_in, jal_in, memRead_in, memWrite_in, memtoReg_in, wen_in,
	input [`ISIZE-1:0] pc_in,

	output reg [`DSIZE-1:0] rdata1_out,
	output reg [`DSIZE-1:0] rdata2_out,
	output reg [`DSIZE-1:0] imm_out,
	output reg [3:0] opcode_out,
	output reg alusrc_out,
	output reg [`ASIZE-1:0] waddr_out,

	output reg branch_out, jal_out, memRead_out, memWrite_out, memtoReg_out, wen_out,
	output reg [`ISIZE-1:0] pc_out
	);


always @ (posedge clk)
begin
	if(rst)
	begin
		rdata1_out <= 0;
		rdata2_out <= 0;
		imm_out<=0;
		opcode_out<=0;
		alusrc_out<=0;
		waddr_out <= 0;

		branch_out <= 0;
		jal_out <= 0;
		memRead_out <= 0;
		memWrite_out <= 0;
		memtoReg_out <= 0;
		wen_out <= 0;
	end
	else
	begin
		rdata1_out <= rdata1_in;
		rdata2_out <= rdata2_in;
		imm_out <= imm_in;
		opcode_out <= opcode_in;
		alusrc_out <= alusrc_in;
		waddr_out <= waddr_in;

		branch_out <= branch_in;
		jal_out <= jal_in;
		memRead_out <= memRead_in;
		memWrite_out <= memWrite_in;
		memtoReg_out <= memtoReg_in;
		wen_out <= wen_in;
		pc_out <= pc_in;
	end
end


endmodule

