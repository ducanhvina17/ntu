`timescale 1ns / 1ps

////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer:
//
// Create Date:   11:29:23 04/10/2016
// Design Name:   pipelined_regfile_5stage
// Module Name:   Y:/Part2/Pipeline_5_Stage.v
// Project Name:  Part2
// Target Device:  
// Tool versions:  
// Description: 
//
// Verilog Test Fixture created by ISE for module: pipelined_regfile_5stage
//
// Dependencies:
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
////////////////////////////////////////////////////////////////////////////////

module Pipeline_5_Stage;

	// Inputs
	reg clk;
	reg rst;
	reg fileid;

	// Outputs
	wire [31:0] PCOUT;
	wire [31:0] branchAdd;
	wire [31:0] PCSrc_mux;
	wire [31:0] jr_mux;
	wire [31:0] jump_mux;
	wire [31:0] INST;
	wire [31:0] rdata1;
	wire [31:0] rdata2;
	wire [31:0] extended_imm;
	wire [31:0] rdata1_ID_EXE;
	wire [31:0] rdata2_ID_EXE;
	wire [31:0] imm_ID_EXE;
	wire [31:0] rdata2_EXE_MEM;
	wire [31:0] ALUSrc_mux;
	wire [31:0] aluout;
	wire [31:0] aluout_EXE_MEM;
	wire [31:0] dm_out;
	wire [31:0] memtoReg_mux;
	wire [4:0] jal_waddr_mux;
	wire [31:0] jal_wdata_mux;
	wire [31:0] jumpAddr;

	// Instantiate the Unit Under Test (UUT)
	pipelined_regfile_5stage uut (
		.clk(clk), 
		.rst(rst), 
		.fileid(fileid), 
		.PCOUT(PCOUT), 
		.branchAdd(branchAdd), 
		.PCSrc_mux(PCSrc_mux), 
		.jr_mux(jr_mux), 
		.jump_mux(jump_mux), 
		.INST(INST), 
		.rdata1(rdata1), 
		.rdata2(rdata2), 
		.extended_imm(extended_imm), 
		.rdata1_ID_EXE(rdata1_ID_EXE), 
		.rdata2_ID_EXE(rdata2_ID_EXE), 
		.imm_ID_EXE(imm_ID_EXE), 
		.rdata2_EXE_MEM(rdata2_EXE_MEM), 
		.ALUSrc_mux(ALUSrc_mux), 
		.aluout(aluout), 
		.aluout_EXE_MEM(aluout_EXE_MEM), 
		.dm_out(dm_out), 
		.memtoReg_mux(memtoReg_mux), 
		.jal_waddr_mux(jal_waddr_mux), 
		.jal_wdata_mux(jal_wdata_mux), 
		.jumpAddr(jumpAddr)
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

