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
		li $t0, 1
		add $t1, $t1, $t0
		li $t0, 2
		move $t1, $t0
		li $t0, 3
		mul $t1, $t1, $t0
		move $t0, $t1
		add $t1, $t1, $t0
		move $t0, $t1
		la $a0, global_d
		sw $t0, 0($a0)
		lw $ra,0($sp)
		jr $ra
