`include "define.v"

module PC1(
	clk,
	rst,
	nextPC,
	currPC
	);


// Input
input clk,rst;
input [`ISIZE - 1:0]nextPC;	//instruction address is 16 bit as it can address more memory locations

// Output
output reg [`ISIZE - 1:0]currPC;

// Update the pc value if reset is false
always @( posedge clk)
begin
	if (rst)
		begin
			currPC <= 32'h00000000;
		end
	else
		begin
			currPC <= nextPC;
		end
end


endmodule	
