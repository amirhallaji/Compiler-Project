.data 
	true: .asciiz "true"
	false : .asciiz "false"

	global_temp1_c :	.word	0
	global_temp1_d :	.word	0
	global_temp2_c :	.float	0.0
	global_temp2_d :	.word	0
	global_main_a :	.word	0
	global_main_b :	.word	0
	global_main_c :	.float	0.0

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	global_temp1:
		sw $ra,0($sp)
		addi $sp,$sp,-8
		la $a1, global_temp1_c
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		la $a1, global_temp1_d
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		la $a0, global_temp1_c
		lw $t0, 0($a0)
		li $v0, 1
		add $a0, $t0, $zero
		syscall
		la $a0, global_temp1_d
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
	global_temp2:
		sw $ra,0($sp)
		addi $sp,$sp,-8
		la $a1, global_temp2_c
		l.s $f1, 0($sp)
		s.s $f1, 0($a1)
		addi $sp, $sp, 4
		la $a1, global_temp2_d
		lw $t1, 0($sp)
		sw $t1, 0($a1)
		addi $sp, $sp, 4
		la $a0, global_temp2_c
		l.s $f0, 0($a0)
		li $v0, 3
		mov.s	$f12, $f0
		syscall
		la $a0, global_temp2_d
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

		la $a0, global_main_a
		sw $t0, 0($a0)
		li $v0, 5
		syscall
		move $t0, $v0

		la $a0, global_main_b
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
		jal global_temp1
		addi $sp, $sp, -12
		la $a0, global_main_c
		l.s $f0, 0($a0)
		addi $sp, $sp, 4
		sw $t0, 0($sp)
		la $a0, global_main_b
		lw $t0, 0($a0)
		addi $sp, $sp, 4
		sw $t0, 0($sp)
		addi $sp, $sp, 4
		jal global_temp2
		addi $sp, $sp, -12
		lw $ra,0($sp)
		jr $ra
