.data 
	true: .asciiz "true"
	false : .asciiz "false"

	global_main_a : .word 0
	global_main_b : .word 0

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
		li $v0, 5
		syscall
		move $t0, $v0

		la $a0, global_main_b
		sw $t0, 0($a0)
		la $a0, global_main_a
		lw $t0, 0($a0)
		move $t1, $t0
		la $a0, global_main_b
		lw $t0, 0($a0)
		seq $t0, $t0, $t1
		li $v0, 1
		beq $t0, $zero, printFalseL1
		la $t0, true
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		b endPrintFalseL1
	printFalseL1:
		la $t0, false
		li $v0, 4
		add $a0, $t0, $zero
		syscall
	endPrintFalseL1:
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		lw $ra,0($sp)
		jr $ra
