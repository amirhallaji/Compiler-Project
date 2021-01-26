.data 
	global_a	.word	0

.text 
	.globl main
	main:
		beq $t0, 0  L1
L1:
		li $v0,10
		syscall
