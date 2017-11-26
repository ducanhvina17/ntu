`include "define.v"

module forward(
	input  clk,  rst,
	input[`ASIZE-1:0] rd_EXE_MEM, rd_MEM_WB, rs_ID_EXE, rt_ID_EXE,
	input wen_EXE_MEM, wen_MEM_WB,
	output reg [1:0] ForwardA, ForwardB);


//Forward A
always @ (wen_EXE_MEM or rd_EXE_MEM or rs_ID_EXE or wen_MEM_WB or rd_MEM_WB)
begin
	if ((wen_EXE_MEM == 1) && (rd_EXE_MEM != 0) && (rd_EXE_MEM == rs_ID_EXE))
		ForwardA = 2'b10;
	else if ((wen_MEM_WB == 1) && (rd_MEM_WB != 0) && (rd_MEM_WB == rs_ID_EXE))
		ForwardA = 2'b01;
	else  if (wen_MEM_WB && (rd_MEM_WB != 0) && !(wen_EXE_MEM && (rd_EXE_MEM != 0) && (rd_EXE_MEM != rs_ID_EXE)) && (rd_MEM_WB == rs_ID_EXE))
		ForwardA = 2'b01;
	else
		ForwardA = 2'b00;
end

//Forward B
always @ (wen_MEM_WB or rd_MEM_WB or rt_ID_EXE or rd_EXE_MEM or wen_EXE_MEM)
begin
	if ((wen_EXE_MEM == 1) && (rd_EXE_MEM != 0) && (rd_EXE_MEM == rt_ID_EXE))
		ForwardB = 2'b10;
	else if ((wen_MEM_WB == 1) && (rd_MEM_WB != 0) && (rd_MEM_WB == rt_ID_EXE))
		ForwardB = 2'b01;
	else if (wen_MEM_WB && (rd_MEM_WB != 0) && !(wen_EXE_MEM && (rd_EXE_MEM != 0) && (rd_EXE_MEM != rs_ID_EXE)) && (rd_MEM_WB == rt_ID_EXE))
		ForwardB = 2'b01;
	else
		ForwardB = 2'b00;
end


endmodule
