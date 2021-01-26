.data 
	global_main_a :	.word	0
	global_main_b :	.word	0

.text 
	.globl main
	main:
		la	$a0, global_main_b
		lw	$t0  0($a0)
		la	$a0, global_main_a
		sw	$t0  0($a0)
		la	$a0, global_main_a
		lw	$t0  0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		la	$a0, global_main_b
		lw	$t0  0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#END OF PROGRAM
		li $v0,10
		syscall
