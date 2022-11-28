if (mouse_check_button_pressed(mb_left) && !instance_exists(oTongue)) {
	instance_create_layer(x,y,"Tongue",oTongue);
}

if (pState == player_state.idle) {
	// User input
	var _keyRight = keyboard_check(vk_right) || keyboard_check(ord("D"));
	var _keyLeft = keyboard_check(vk_left) ||  keyboard_check(ord("A"));
	var _keyJump = keyboard_check(vk_up) || keyboard_check(vk_space) || keyboard_check(ord("W"))

	// Work out where to move horizontally
	hsp = (_keyRight - _keyLeft) * hspWalk;

	// Work out where to move vertically
	vsp = vsp + grv;

	// Work out if we should jump
	if (canJump-- > 0) && (_keyJump) {
		vsp = vspJump;
	}

	// Collide and move
	if (place_meeting(x + hsp, y, oWall)) {
		while (abs(hsp) > 0.1) {
			hsp *= 0.5;
			if (!place_meeting(x + hsp, y, oWall)) x += hsp;
		}
		hsp = 0;
	}
	x += hsp;

	// Vertical Collision
	if (place_meeting(x, y + vsp, oWall)) {
	
		if (vsp > 0) canJump = 5;
	
		while (abs(vsp) > 0.1) {
			vsp *= 0.5;
			if (!place_meeting(x, y + vsp, oWall)) y += vsp;
		}
		vsp = 0;
	}
	y += vsp;

} else if (pState == player_state.grapple) {
	oPlayer.direction = point_direction(oPlayer.x, oPlayer.y, oTongue.x, oTongue.y);
	oPlayer.speed = oTongue.extendSpeed;
	if (distance_to_object(oTongue) < extendSpeed) {
		oPlayer.speed = 0;
	}
	
}

//Animation
if (!place_meeting(x,y+1, oWall)) {
	sprite_index = sPlayerA;
	image_speed = 0;
	if (sign(vsp) > 0) image_index = 0; else image_index = 1;
} else {
	image_speed = 1;
	if (hsp == 0) {
			sprite_index = sPlayer;
	}
	/*
	else {	// incomplete running animation
		sprite_index = sPlayerR;
	}
	*/
}

if (hsp != 0) image_xscale = (sign(hsp)*2);


