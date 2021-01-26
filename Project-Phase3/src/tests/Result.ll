.data 
	global_main_a	.word	0

.text 
	.globl main
	main:
		bne	, $t2, resultL1
		li $v0,10
		syscall
