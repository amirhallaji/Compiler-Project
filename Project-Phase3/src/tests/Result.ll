.data 
<<<<<<< HEAD
	global_a	.word	0
=======
	global_main_a	.word	0
	global_main_b	.word	0
>>>>>>> origin/master

.text 
	.globl main
	main:
<<<<<<< HEAD
	bne	$global_main	$global_main		jr $ra
=======
		li $v0,10
		syscall
>>>>>>> origin/master
