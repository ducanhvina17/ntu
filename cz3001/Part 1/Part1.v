`timescale 1ns / 1ps

////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer:
//
// Create Date:   16:21:36 03/17/2016
// Design Name:   pipelined_regfile_4stage
// Module Name:   Y:/CZ3001/Assignment/Part1.v
// Project Name:  Assignment
// Target Device:  
// Tool versions:  
// Description: 
//
// Verilog Test Fixture created by ISE for module: pipelined_regfile_4stage
//
// Dependencies:
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
////////////////////////////////////////////////////////////////////////////////

module Part1;

	// Inputs
	reg clk;
	reg rst;
	reg fileid;

	// Outputs
	wire [31:0] PCOUT;
	wire [31:0] branchAdd;
	wire [31:0] PCSrc_mux;
	wire [31:0] INST;
	wire [3:0] aluop_ID_EXE;
	wire [31:0] rdata1;
	wire [31:0] rdata2;
	wire [31:0] rdata1_ID_EXE;
	wire [31:0] rdata2_ID_EXE;
	wire [31:0] imm_ID_EXE;
	wire [31:0] ALUSrc_mux;
	wire [31:0] rdata2_EXE_MEM;
	wire alusrc_ID_EXE;
	wire [31:0] aluout;
	wire [31:0] aluout_EXE_MEM;
	wire [31:0] dm_out;
	wire [4:0] waddr_ID_EXE;
	wire [4:0] waddr_EXE_MEM;
	wire [4:0] waddr_MEM_WB;
	wire [31:0] memtoReg_mux;

	// Instantiate the Unit Under Test (UUT)
	pipelined_regfile_4stage uut (
		.clk(clk), 
		.rst(rst), 
		.fileid(fileid), 
		.PCOUT(PCOUT), 
		.branchAdd(branchAdd), 
		.PCSrc_mux(PCSrc_mux), 
		.INST(INST), 
		.aluop_ID_EXE(aluop_ID_EXE), 
		.rdata1(rdata1), 
		.rdata2(rdata2), 
		.rdata1_ID_EXE(rdata1_ID_EXE), 
		.rdata2_ID_EXE(rdata2_ID_EXE), 
		.imm_ID_EXE(imm_ID_EXE), 
		.ALUSrc_mux(ALUSrc_mux), 
		.rdata2_EXE_MEM(rdata2_EXE_MEM), 
		.alusrc_ID_EXE(alusrc_ID_EXE), 
		.aluout(aluout), 
		.aluout_EXE_MEM(aluout_EXE_MEM), 
		.dm_out(dm_out), 
		.waddr_ID_EXE(waddr_ID_EXE), 
		.waddr_EXE_MEM(waddr_EXE_MEM), 
		.waddr_MEM_WB(waddr_MEM_WB), 
		.memtoReg_mux(memtoReg_mux)
	);

always #15 clk = ~clk;
	initial begin
		// Initialize Inputs
		clk = 0;
		rst = 0;
		fileid = 0;


		// Wait 100 ns for global reset to finish
		#100;
        
		// Add stimulus here
#25 rst =1;
#25 rst=0;

	end
      
endmodule

