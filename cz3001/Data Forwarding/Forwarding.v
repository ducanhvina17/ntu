`timescale 1ns / 1ps

////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer:
//
// Create Date:   12:10:05 04/10/2016
// Design Name:   pipelined_5_stage_fowarding
// Module Name:   Y:/DataForwarding/Forwarding.v
// Project Name:  DataForwarding
// Target Device:  
// Tool versions:  
// Description: 
//
// Verilog Test Fixture created by ISE for module: pipelined_5_stage_fowarding
//
// Dependencies:
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
////////////////////////////////////////////////////////////////////////////////

module Forwarding;

	// Inputs
	reg clk;
	reg rst;
	reg fileid;

	// Outputs
	wire [31:0] PCOUT;
	wire [31:0] INST;
	wire [31:0] rdata1;
	wire [31:0] rdata2;
	wire [31:0] extended_imm;
	wire [31:0] rdata1_ID_EXE;
	wire [31:0] rdata2_ID_EXE;
	wire [31:0] imm_ID_EXE;
	wire [1:0] forwardA;
	wire [1:0] forwardB;
	wire [31:0] rdata1_forward;
	wire [31:0] rdata2_forward;
	wire [31:0] ALUSrc_mux;
	wire [31:0] aluout;
	wire [31:0] aluout_EXE_MEM;
	wire [31:0] dm_out;
	wire [31:0] memtoReg_mux;

	// Instantiate the Unit Under Test (UUT)
	pipelined_5_stage_fowarding uut (
		.clk(clk), 
		.rst(rst), 
		.fileid(fileid), 
		.PCOUT(PCOUT), 
		.INST(INST), 
		.rdata1(rdata1), 
		.rdata2(rdata2), 
		.extended_imm(extended_imm), 
		.rdata1_ID_EXE(rdata1_ID_EXE), 
		.rdata2_ID_EXE(rdata2_ID_EXE), 
		.imm_ID_EXE(imm_ID_EXE), 
		.forwardA(forwardA), 
		.forwardB(forwardB), 
		.rdata1_forward(rdata1_forward), 
		.rdata2_forward(rdata2_forward), 
		.ALUSrc_mux(ALUSrc_mux), 
		.aluout(aluout), 
		.aluout_EXE_MEM(aluout_EXE_MEM), 
		.dm_out(dm_out), 
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

