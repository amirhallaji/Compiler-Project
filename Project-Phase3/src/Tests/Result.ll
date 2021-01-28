.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error: .asciiz "runtime ERROR"
	global_f_a : .word 0
	global_f_b : .word 0
	global_main_a : .word 0

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	runtime_error:
		li $v0, 4
		la $a0, error
		syscall
		#END OF PROGRAM
		li $v0,10
		syscall
	global_f:
		sw $ra,0($sp)
		addi $sp,$sp,-8
		la $a1, global_f_a
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		la $a1, global_f_b
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		addi $sp,$sp,4
		la $a0, global_f_a
		lw $t0, 0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_f_a
		lw $t0, 0($a0)
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
	global_main:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, global_main_a
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 5
		syscall
		move $t0, $v0

		sw $t0, 0($a3)
		la $a0, global_main_a
		lw $t0, 0($a0)
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		la $a0, global_main_a
		lw $t0, 0($a0)
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal global_f
		addi $sp, $sp, -8
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
