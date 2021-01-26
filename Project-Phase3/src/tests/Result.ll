.data 
	true: .asciiz "true"
	false : .asciiz "false"
	global_main_a :	.word	0

.text 
	.globl main
	main:
		li $v0, 5
		syscall
		move $t0, $v0

		la	$a0, global_main_a
		sw	$t0  0($a0)
		beq $t0, 0, L1
L1:
		la	$a0, 0_a
		lw	$t0  0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#END OF PROGRAM
		li $v0,10
		syscall
