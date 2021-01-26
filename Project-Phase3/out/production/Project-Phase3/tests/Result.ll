.data 
	global_main_a	.word	0
	global_main_b	.word	0

.text 
	.globl main
	main:
		lw $t1, a
		lw $t2, a
		add $t0, $t0, $t1
		sub $t0, $t0, $t2
		li $v0,10
		syscall
