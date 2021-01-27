.data 
	true: .asciiz "true"
	false : .asciiz "false"

	global_main_a : .word 0
	global_main_b : .word 0
	global_main_NEW_ARRAY_0 .space 4
	global_main_NEW_ARRAY_1 .space 8
	global_main_NEW_ARRAY_2 .space 8

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		la $a0, global_main_b
		lw $t0, 0($a0)
		la $a0, global_main_a
		sw $t0, 0($a0)
		la $t0, global_main_NEW_ARRAY_0
		la $a0, global_main_a
		sw $t0, 0($a0)
		la $t0, global_main_NEW_ARRAY_1
		la $a0, global_main_b
		sw $t0, 0($a0)
		la $t0, global_main_NEW_ARRAY_2
		la $a0, global_main_a
		sw $t0, 0($a0)
		lw $ra,0($sp)
		jr $ra
