`timescale 1ns / 1ps
`include "define.v"

module pipelined_regfile_4stage(clk, rst, fileid, PCOUT, branchAdd, PCSrc_mux, INST, aluop_ID_EXE, rdata1, rdata2, rdata1_ID_EXE, rdata2_ID_EXE, imm_ID_EXE, ALUSrc_mux, rdata2_EXE_MEM, alusrc_ID_EXE, aluout, aluout_EXE_MEM, dm_out, waddr_ID_EXE, waddr_EXE_MEM, waddr_MEM_WB, memtoReg_mux);

/*------------------------------ Input ------------------------------*/
input clk;	
input fileid; 												
input rst;


/*------------------------------ Output ------------------------------*/
// ALU source
output alusrc_ID_EXE;

// ALU operation
output [3:0] aluop_ID_EXE;

// Write address
output [`ASIZE-1:0] waddr_ID_EXE;
output [`ASIZE-1:0] waddr_EXE_MEM;
output [`ASIZE-1:0] waddr_MEM_WB;

// ALU
output [`DSIZE-1:0] ALUSrc_mux;	// ALU input b
output [`DSIZE-1:0] aluout;
output [`DSIZE-1:0] aluout_EXE_MEM;

output [`DSIZE-1:0] dm_out;	// Read data from DM

output [`DSIZE-1:0] imm_ID_EXE;	// Immediate value

output [`DSIZE-1:0] INST;	// Instruction

output [`DSIZE-1:0] memtoReg_mux;	// Check value of write data

// RData1
output [`DSIZE-1:0] rdata1;
output [`DSIZE-1:0] rdata1_ID_EXE;

// RData2
output [`DSIZE-1:0] rdata2;
output [`DSIZE-1:0] rdata2_ID_EXE;
output [`DSIZE-1:0] rdata2_EXE_MEM;

// PC
output [`DSIZE-1:0] branchAdd;
output [`ISIZE-1:0] PCOUT;
output [`DSIZE-1:0] PCSrc_mux;


/*------------------------------ Wire ------------------------------*/
wire branch;	// Whether branch is needed
wire jr;	// Jump register
wire jump;	// Jump
wire jal;	// Jump and Link
wire memRead;	// MemRead for Date Memory
wire memWrite;	// MemWrite for Data Memory
wire memtoReg;	// Memory to Register
wire PCSrc;
wire regDst;	// Register Destination
wire wen;
wire zero;	// from ALU

wire [3:0] aluop;

wire [`ASIZE-1:0] waddr_mux;

wire [`DSIZE-1:0] extended_imm;
wire [`DSIZE-1:0] ALUSrc_mux;

// PC
wire [`ISIZE-1:0] PCIN;
wire [`ISIZE-1:0] nPC;

// ID_EXE Pipeline
wire branch_ID_EXE;
wire memRead_ID_EXE;
wire memWrite_ID_EXE;
wire memtoReg_ID_EXE;
wire wen_ID_EXE;
wire [`ISIZE-1:0] nPC_ID_EXE;

// EXE_MEM Pipeline
wire memRead_EXE_MEM;
wire memWrite_EXE_MEM;
wire memtoReg_EXE_MEM;
wire wen_EXE_MEM;
wire [`ISIZE-1:0] PCIN_EXE_MEM;

// MEM_WB Pipeline
wire memtoReg_MEM_WB;
wire wen_MEM_WB;
wire [`DSIZE-1:0] aluout_MEM_WB;
wire [`ISIZE-1:0] PCIN_MEM_WB;


/*------------------------------ Assign Wires ------------------------------*/
assign extended_imm = ({{16{INST[15]}},INST[15:0]});	//sign extension for immediate needs to be done for I type instuction.
assign PCSrc = branch_ID_EXE & zero;		// Branch & zero

/*------------------------------ Adder ------------------------------*/
assign branchAdd = imm_ID_EXE + nPC_ID_EXE;	// Offset PC
assign nPC = PCOUT + 32'b1;	//i Increments PC to PC +1

/*------------------------------ Multiplexer ------------------------------*/
assign PCSrc_mux = PCSrc ? branchAdd : nPC;		// Mux to select next PC value
assign waddr_mux = regDst ? INST[15:11] : INST[20:16];		// Selecting waddr value
assign ALUSrc_mux = alusrc_ID_EXE ? imm_ID_EXE : rdata2_ID_EXE;		// Selecting immedaite or the rdata2 value
assign memtoReg_mux = memtoReg_MEM_WB ? aluout_MEM_WB : dm_out;	// mux for selecting immedaite or the rdata2 value


// Program counter
PC1 pc(.clk(clk),.rst(rst),.nextPC(PCSrc_mux),.currPC(PCOUT));	//PCOUT is your PC value and PCIN is your next PC

// Instruction memory
memory im( .clk(clk), .rst(rst), .wen(1'b0), .addr(PCOUT), .data_in(32'b0), .fileid(4'b0),.data_out(INST));//note that memory read is having one clock cycle delay as memory is a slow operation

// Initialization of regfiles is done as hardcoding here
control C0 (.inst_cntrl(INST[31:26]), .wen_cntrl(wen),.alusrc_cntrl(alusrc), .aluop_cntrl(aluop), .branch(branch), .jal(jal), .jr(jr), .jump(jump), .memRead(memRead),.memWrite(memWrite),.memtoReg(memtoReg),.regDst(regDst));

// Register File
regfile  RF0 ( .clk(clk), .rst(rst), .wen(wen_MEM_WB), .raddr1(INST[25:21]), .raddr2(INST[20:16]), .waddr(waddr_MEM_WB), .wdata(memtoReg_mux), .rdata1(rdata1), .rdata2(rdata2));

// Pipeline 1
ID_EXE_stage PIPE1(.clk(clk), .rst(rst), .rdata1_in(rdata1), .rdata2_in(rdata2), .imm_in(extended_imm), .opcode_in(aluop), .alusrc_in(alusrc), .waddr_in(waddr_mux), .branch_in(branch), .memRead_in(memRead), .memWrite_in(memWrite), .memtoReg_in(memtoReg), .wen_in(wen), .pc_in(nPC), .imm_out(imm_ID_EXE), .rdata1_out(rdata1_ID_EXE), .rdata2_out(rdata2_ID_EXE), .alusrc_out(alusrc_ID_EXE), .opcode_out(aluop_ID_EXE), .waddr_out(waddr_ID_EXE), .branch_out(branch_ID_EXE), .memRead_out(memRead_ID_EXE), .memWrite_out(memWrite_ID_EXE), .memtoReg_out(memtoReg_ID_EXE), .wen_out(wen_ID_EXE), .pc_out(nPC_ID_EXE));

// ALU takes its input from pipeline register and the output of mux.
alu ALU0 (.a(rdata1_ID_EXE), .b(ALUSrc_mux), .op(aluop_ID_EXE), .zero(zero), .out(aluout));

// Pipeline 2
EXE_MEM_stage PIPE2(.clk(clk), .rst(rst), .wen_in(wen_ID_EXE), .alu_in(aluout), .waddr_in(waddr_ID_EXE), .rdata2_in(rdata2_ID_EXE), .memRead_in(memRead_ID_EXE), .memWrite_in(memWrite_ID_EXE), .memtoReg_in(memtoReg_ID_EXE), .wen_out(wen_EXE_MEM), .alu_out(aluout_EXE_MEM), .waddr_out(waddr_EXE_MEM), .rdata2_out(rdata2_EXE_MEM), .memRead_out(memRead_EXE_MEM), .memWrite_out(memWrite_EXE_MEM), .memtoReg_out(memtoReg_EXE_MEM));

// Data Memory
dataMem dm(.clk(clk), .rst(rst), .wen(memWrite_EXE_MEM), .addr(aluout_EXE_MEM), .data_in(rdata2_EXE_MEM), .fileid(4'd8), .data_out(dm_out));

// Pipeline 3
MEM_WB PIPE3(.clk(clk), .rst(rst), .alu_in(aluout_EXE_MEM), .waddr_in(waddr_EXE_MEM), .memtoReg_in(memtoReg_EXE_MEM), .wen_in(wen_EXE_MEM), .alu_out(aluout_MEM_WB), .waddr_out(waddr_MEM_WB), .memtoReg_out(memtoReg_MEM_WB), .wen_out(wen_MEM_WB));


endmodule
