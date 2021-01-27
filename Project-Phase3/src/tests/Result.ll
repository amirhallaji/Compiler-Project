.data 
	true: .asciiz "true"
	false : .asciiz "false"

	global_main_b :	.word	0
	global_main_a : .word 0

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		li $t1, 1
		li $t2, 2
		add $t0, $t0, $t1
		add $t0, $t0, $t2
		la $a0, global_main_a
		lw $t0, 0($a0)
		lw $t1, a
		add $t0, $t0, $t1
		la $a0, global_main_a
		sw $t0, 0($a0)
		lw $ra,0($sp)
		jr $ra
