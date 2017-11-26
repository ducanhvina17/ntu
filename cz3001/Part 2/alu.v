`include "define.v"	// Defines DSIZE, ADD, SUB etc

module alu(
	a,	// 1st operand
	b,	// 2nd operand
	op,	// 4-bit operation
	zero,	// Result of a == b
	out	// Output
	);


// Input
input [`DSIZE-1:0] a, b;
input [3:0] op;

// Output
output reg zero;
output reg [`DSIZE-1:0] out;

// Whenever any input changes, check for the opcode's instruction
always @(a or b or op )
begin
	case(op)
		`ADD	:	out = a + b;
		`SUB	:	out = a - b;
		`AND	:	out = a & b;
		`XOR	:	out = a ^ b;
		`COM	:	out = a <= b;
		`MUL	:	out = a * b;
		`ADDI	:	out = a + b;
		`LW	:	out = a + b;
		`SW	:	out = a + b;
		`BEQ	:	zero = a == b;
		default:
			begin
				zero = 0;
				out = 0;
			end
	endcase
end


endmodule
