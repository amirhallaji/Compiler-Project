.data 
	true: .asciiz "true"
	false : .asciiz "false"
	global_a :	.word	0

.text 
	.globl main
	main:
		beq $t0, 0, L1
L1:
		#END OF PROGRAM
		li $v0,10
		syscall
