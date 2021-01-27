.data 
	true: .asciiz "true"
	false : .asciiz "false"

	global_main_a : .word 0
	StringLiteral_in else: .asciiz "in else"

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		li $v0, 5
		syscall
		move $t0, $v0

		la $a0, global_main_a
		sw $t0, 0($a0)
		la $a0, global_main_a
		sw $t0, 0($a0)
		beq $t0,1, L1, L2
L1:
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
L2:
		la $t0, StringLiteral_in else
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		lw $ra,0($sp)
		jr $ra
