.data 
	true: .asciiz "true"
	false : .asciiz "false"
	global_main_b :	.word	0
	global_main_a :	.word	0

.text 
	.globl main
	main:
		la	$a0, global_main_b
		lw	$t0  0($a0)
		la	$a0, global_main_a
		sw	$t0, 0($a0)
		#END OF PROGRAM
		li $v0,10
		syscall
