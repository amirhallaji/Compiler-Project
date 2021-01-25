.data 
	global_a	.word	0

.text 
	.globl main
	main:
	bne	$global_main	$global_main		jr $ra
