enum player_state {
	idle,
	grapple,
}
pState = player_state.idle;
grv = 0.5; // gravity
hsp = 0; // current horizontal speed
vsp = 0; // current vertical speed
hspWalk = 5; // walk speed
vspJump = -11; // jump speed
canJump = 0; // are we touching the ground

//show_debug_message(view_wport[]);