`include "define.v"

module forwardMux(
	input [1:0] sel,
   input [`DSIZE-1:0] rdata_ID_EXE, memtoReg_mux, aluout_EXE_MEM,
	
   output reg [`DSIZE-1:0] forward_out
	);


always @ (sel, rdata_ID_EXE, memtoReg_mux, aluout_EXE_MEM)
begin
	case (sel)
		2'b00 : forward_out <= rdata_ID_EXE;
		2'b01 : forward_out <= memtoReg_mux;
		2'b10 : forward_out <= aluout_EXE_MEM;
	endcase
end


endmodule
