`timescale 1ns / 1ps
`include "define.v"

module pipelined_5_stage_fowarding(clk, rst, fileid, PCOUT, INST, rdata1, rdata2, extended_imm, rdata1_ID_EXE, rdata2_ID_EXE, imm_ID_EXE, forwardA, forwardB, rdata1_forward, rdata2_forward, ALUSrc_mux, aluout, aluout_EXE_MEM, dm_out, memtoReg_mux);

/*------------------------------ Input ------------------------------*/
input clk;	
input fileid; 												
input rst;


/*------------------------------ Output ------------------------------*/
output [`ISIZE-1:0] PCOUT;

output [`DSIZE-1:0] INST;
output [`DSIZE-1:0] rdata1;
output [`DSIZE-1:0] rdata2;
output [`DSIZE-1:0] extended_imm;

output [`DSIZE-1:0] rdata1_ID_EXE;
output [`DSIZE-1:0] rdata2_ID_EXE;
output [`DSIZE-1:0] imm_ID_EXE;
output [1:0] forwardA;
output [1:0] forwardB;
output [`DSIZE-1:0] rdata2_forward;
output [`DSIZE-1:0] rdata1_forward;
output [`DSIZE-1:0] ALUSrc_mux;
output [`DSIZE-1:0] aluout;

output [`DSIZE-1:0] aluout_EXE_MEM;
output [`DSIZE-1:0] dm_out;
output [`DSIZE-1:0] memtoReg_mux;


/*------------------------------ Wire ------------------------------*/
wire [`DSIZE-1:0] branchAdd;
wire [`ASIZE-1:0] jal_waddr_mux;
wire [`DSIZE-1:0] jal_wdata_mux;
wire [`DSIZE-1:0] jumpAddr;
wire [`DSIZE-1:0] jump_mux;
wire [`DSIZE-1:0] jr_mux;
wire [`ASIZE-1:0] waddr_mux;
wire [`DSIZE-1:0] PCSrc_mux;
wire [`ISIZE-1:0] PCIN;
wire [`ISIZE-1:0] nPC;

// Control Signals
wire alusrc;
wire branch;
wire jr;
wire jump;
wire jal;
wire memRead;
wire memWrite;
wire memtoReg;
wire PCSrc;
wire regDst;
wire wen;
wire zero;
wire [3:0] aluop;

// ID_EXE Pipeline
wire alusrc_ID_EXE;
wire branch_ID_EXE;
wire jal_ID_EXE;
wire memRead_ID_EXE;
wire memWrite_ID_EXE;
wire memtoReg_ID_EXE;
wire wen_ID_EXE;
wire [3:0] aluop_ID_EXE;
wire [`ASIZE-1:0] rs_ID_EXE;
wire [`ASIZE-1:0] rd_ID_EXE;
wire [`ASIZE-1:0] rt_ID_EXE;
wire [`ASIZE-1:0] waddr_ID_EXE;
wire [`ISIZE-1:0] nPC_ID_EXE;

// EXE_MEM Pipeline
wire jal_EXE_MEM;
wire memRead_EXE_MEM;
wire memWrite_EXE_MEM;
wire memtoReg_EXE_MEM;
wire wen_EXE_MEM;
wire [`ASIZE-1:0] waddr_EXE_MEM;
wire [`DSIZE-1:0] rdata2_EXE_MEM;
wire [`ISIZE-1:0] PCIN_EXE_MEM;
wire [`ISIZE-1:0] nPC_EXE_MEM;

// MEM_WB Pipeline
wire jal_MEM_WB;
wire memtoReg_MEM_WB;
wire wen_MEM_WB;
wire [`ASIZE-1:0] waddr_MEM_WB;
wire [`DSIZE-1:0] aluout_MEM_WB;
wire [`ISIZE-1:0] nPC_MEM_WB;


/*------------------------------ Assign Wires ------------------------------*/
assign extended_imm = ({{16{INST[15]}},INST[15:0]});	//sign extension for immediate needs to be done for I type instuction.
assign jumpAddr = ({nPC[31:26], INST[25:0]});	// PC with offset
assign PCSrc = branch_ID_EXE & zero;		// Branch & zero

/*------------------------------ Adder ------------------------------*/
assign branchAdd = imm_ID_EXE + nPC_ID_EXE;	// Offset PC
assign nPC = PCOUT + 32'b1;	// i Increments PC to PC +1

/*------------------------------ Multiplexer ------------------------------*/
assign PCSrc_mux = PCSrc ? branchAdd : nPC;		// Mux to select next PC value
assign waddr_mux = regDst ? INST[15:11] : INST[20:16];		// Selecting waddr value
assign ALUSrc_mux = alusrc_ID_EXE ? imm_ID_EXE : rdata2_forward;		// Selecting immedaite or the rdata2 value
assign memtoReg_mux = memtoReg_MEM_WB ? aluout_MEM_WB : dm_out;	// mux for selecting immedaite or the rdata2 value
assign jal_waddr_mux = jal_MEM_WB ? 5'd31 : waddr_MEM_WB;
assign jal_wdata_mux = jal_MEM_WB ? nPC_MEM_WB : memtoReg_mux;
assign jump_mux = jump ? jumpAddr : PCSrc_mux;
assign jr_mux = jr ? rdata1 : jump_mux;
forwardMux fm_A (.sel(forwardA), .rdata_ID_EXE(rdata1_ID_EXE), .memtoReg_mux(jal_wdata_mux), .aluout_EXE_MEM(aluout_EXE_MEM), .forward_out(rdata1_forward));
forwardMux fm_B (.sel(forwardB), .rdata_ID_EXE(rdata2_ID_EXE), .memtoReg_mux(jal_wdata_mux), .aluout_EXE_MEM(aluout_EXE_MEM), .forward_out(rdata2_forward));

// Program counter
PC1 pc(.clk(clk),.rst(rst),.nextPC(jr_mux),.currPC(PCOUT));	//PCOUT is your PC value and PCIN is your next PC

// Instruction memory
memory im( .clk(clk), .rst(rst), .wen(1'b0), .addr(PCOUT), .data_in(32'b0), .fileid(4'b0),.data_out(INST));//note that memory read is having one clock cycle delay as memory is a slow operation

// Control unit
control C0 (.inst_cntrl(INST[31:26]), .wen_cntrl(wen),.alusrc_cntrl(alusrc), .aluop_cntrl(aluop), .branch(branch), .jal(jal), .jr(jr), .jump(jump), .memRead(memRead),.memWrite(memWrite),.memtoReg(memtoReg),.regDst(regDst));

// Forwarding unit
forward fu (.clk(clk), .rst(rst), .rd_EXE_MEM(waddr_EXE_MEM), .rd_MEM_WB(waddr_MEM_WB), .rs_ID_EXE(rs_ID_EXE), .rt_ID_EXE(rt_ID_EXE), .wen_EXE_MEM(wen_EXE_MEM), .wen_MEM_WB(wen_MEM_WB), .ForwardA(forwardA), .ForwardB(forwardB));

// Register File
regfile  RF0 ( .clk(clk), .rst(rst), .wen(wen_MEM_WB), .raddr1(INST[25:21]), .raddr2(INST[20:16]), .waddr(jal_waddr_mux), .wdata(jal_wdata_mux), .rdata1(rdata1), .rdata2(rdata2));

// Pipeline 1
ID_EXE_stage PIPE1 (.clk(clk), .rst(rst), .rs_in(INST[25:21]), .rd_in(INST[15:11]), .rt_in(INST[20:16]), .rdata1_in(rdata1), .rdata2_in(rdata2), .imm_in(extended_imm), .opcode_in(aluop), .alusrc_in(alusrc), .waddr_in(waddr_mux), .branch_in(branch), .jal_in(jal), .memRead_in(memRead), .memWrite_in(memWrite), .memtoReg_in(memtoReg), .wen_in(wen), .pc_in(nPC), .rs_out(rs_ID_EXE), .rd_out(rd_ID_EXE), .rt_out(rt_ID_EXE), .rdata1_out(rdata1_ID_EXE), .rdata2_out(rdata2_ID_EXE), .imm_out(imm_ID_EXE), .opcode_out(aluop_ID_EXE), .alusrc_out(alusrc_ID_EXE), .waddr_out(waddr_ID_EXE), .branch_out(branch_ID_EXE), .jal_out(jal_ID_EXE), .memRead_out(memRead_ID_EXE), .memWrite_out(memWrite_ID_EXE), .memtoReg_out(memtoReg_ID_EXE), .wen_out(wen_ID_EXE), .pc_out(nPC_ID_EXE));

// ALU
alu ALU0 (.a(rdata1_forward), .b(ALUSrc_mux), .op(aluop_ID_EXE), .zero(zero), .out(aluout));

// Pipeline 2
EXE_MEM_stage PIPE2(.clk(clk), .rst(rst), .wen_in(wen_ID_EXE), .alu_in(aluout), .waddr_in(waddr_ID_EXE), .rdata2_in(rdata2_ID_EXE), .jal_in(jal_ID_EXE), .memRead_in(memRead_ID_EXE), .memWrite_in(memWrite_ID_EXE), .memtoReg_in(memtoReg_ID_EXE), .pc_in(nPC_ID_EXE), .wen_out(wen_EXE_MEM), .alu_out(aluout_EXE_MEM), .waddr_out(waddr_EXE_MEM), .rdata2_out(rdata2_EXE_MEM), .jal_out(jal_EXE_MEM), .memRead_out(memRead_EXE_MEM), .memWrite_out(memWrite_EXE_MEM), .memtoReg_out(memtoReg_EXE_MEM), .pc_out(nPC_EXE_MEM));

// Data Memory
dataMem dm(.clk(clk), .rst(rst), .wen(memWrite_EXE_MEM), .addr(aluout_EXE_MEM), .data_in(rdata2_forward), .fileid(4'd8), .data_out(dm_out));

// Pipeline 3
MEM_WB PIPE3(.clk(clk), .rst(rst), .alu_in(aluout_EXE_MEM), .waddr_in(waddr_EXE_MEM), .jal_in(jal_EXE_MEM), .memtoReg_in(memtoReg_EXE_MEM), .wen_in(wen_EXE_MEM), .pc_in(nPC_EXE_MEM), .alu_out(aluout_MEM_WB), .waddr_out(waddr_MEM_WB), .jal_out(jal_MEM_WB), .memtoReg_out(memtoReg_MEM_WB), .wen_out(wen_MEM_WB), .pc_out(nPC_MEM_WB));


endmodule
