.data 
	global_main_a	.word	0
	global_main_b	.word	0

.text 
	.globl main
	main:
		li $t1, 1
		sub $t0, $t0, $t1
		lw $t1, a
		sub $t0, $t0, $t1
		lw $t1, a
		sub $t0, $t0, $t1
		li $t1, 5
		add $t0, $t0, $t1
		lw $t1, a
		sub $t0, $t0, $t1
		li $t1, 2
		add $t0, $t0, $t1
		li $t1, 1
		add $t0, $t0, $t1
		lw $t1, a
		sub $t0, $t0, $t1
		li $v0,10
		syscall
