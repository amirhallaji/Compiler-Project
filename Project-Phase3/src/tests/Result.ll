.data 
	true: .asciiz "true"
	false : .asciiz "false"

	global_a : .word 0
	global_b : .word 0
	global_c : .word 0
	global_d : .word 0
	global_f : .word 0

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		li $t0, 16
		move $s1 $t1
		move $t1, $t0
		li $t0, 4
		div $t1, $t1, $t0
		move $t0, $t1
		move $t0 $s1
		li $t0, 3
		div $t1, $t1, $t0
		mfhi $t1
		move $t0, $t1
		move $t0 $s1

		li $t0, 16
		move $s1 $t1
		move $t1, $t0
		li $t0, 4
		div $t1, $t1, $t0
		move $t0, $t1
		move $t0 $s1
		neg $t0, $t0

		move $t0 $t1

		la $a0, global_d
		sw $t0, 0($a0)
		lw $ra,0($sp)
		jr $ra
