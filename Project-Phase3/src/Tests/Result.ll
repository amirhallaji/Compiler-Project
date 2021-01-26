.data 
	true: .asciiz "true"
	false : .asciiz "false"

	global_temp_a : .word 0
	global_temp_b : .word 0
	global_main_b : .word 0
	global_main_a : .word 0
	StringLiteral_salam: .asciiz "salam"

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	global_temp:
		sw $ra,0($sp)
		addi $sp,$sp,-8
		la $a1, global_temp_a
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		la $a1, global_temp_b
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		la $a0, global_temp_a
		lw $t0, 0($a0)
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		la $a0, global_temp_b
		lw $t0, 0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		lw $ra,0($sp)
		jr $ra
	global_main:
		sw $ra,0($sp)
		li $v0, 5
		syscall
		move $t0, $v0

		la $a0, global_main_b
		sw $t0, 0($a0)
		la $t0, StringLiteral_salam
		la $a0, global_main_a
		sw $t0, 0($a0)
		la $a0, global_main_a
		lw $t0, 0($a0)
		addi $sp, $sp, 4
		sw $t0, 0($sp)
		la $a0, global_main_b
		lw $t0, 0($a0)
		addi $sp, $sp, 4
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal global_temp
		addi $sp, $sp, -12
		lw $ra,0($sp)
		jr $ra
