.data 
	global_main_a	.word	0

.text 
	.globl main
	main:
<<<<<<< HEAD
		bne	, $t2, resultL1
=======
		lw $t1, a
		lw $t2, a
		add $t0, $t0, $t1
		sub $t0, $t0, $t2
>>>>>>> origin/master
		li $v0,10
		syscall
