// update length every step
tLength = point_distance(oTongue.x,oTongue.y,oPlayer.x,oPlayer.y);
oTongue.image_angle = oTongue.direction;

if (oTongue.tState == tongue_state.extending) {
	oTongue.direction = point_direction(oTongue.x, oTongue.y, mouse_x, mouse_y);
	oTongue.speed = oTongue.extendSpeed;
	
	if (place_meeting(x,y,oWall))
		tState = tongue_state.hooked;
	
	if (mouse_check_button_released(mb_left) 
		|| tLength >= range
		|| tLength >= point_distance(mouse_x,mouse_y,oPlayer.x,oPlayer.y)) {
			
		tState = tongue_state.retracting;
	}

} else if (oTongue.tState == tongue_state.hooked) {
	speed = 0;
	
	if (mouse_check_button_released(mb_left))
		pState = player_state.grapple;
		
	if (mouse_check_button_pressed(mb_left))
		tState = tongue_state.retracting;
		oPlayer.pState = player_state.idle;
		show_debug_message("rec");
	
} else if (oTongue.tState == tongue_state.retracting) {
	oTongue.direction = point_direction(oTongue.x, oTongue.y, oPlayer.x, oPlayer.y);
	oTongue.speed = oTongue.retractSpeed;
	
	if (place_meeting(oTongue.x, oTongue.y, oPlayer))
		instance_destroy();
}
	
 
/*if (place_meeting(oTongue.x, oTongue.y, oWall)) {
		oPlayer.direction = point_direction(oPlayer.x, oPlayer.y, oTongue.x, oTongue.y);
		oPlayer.speed = oTongue.extendSpeed;
		oPlayer.state = "hooked";
}

/*if (mouse_check_button(mb_left) || distance_to_object(oPlayer) >= oTongue.range) {
		oTongue.active = false;
		oPlayer.state = "idle";
	}
	
/*if (oTongue.active == false) {
	oTongue.direction = point_direction(oTongue.x, oTongue.y, oPlayer.x, oPlayer.y);
	oTongue.speed = oTongue.retractSpeed;
}

/*if (place_meeting(oTongue.x, oTongue.y, oPlayer) && oTongue.active == false) {
	instance_destroy();
}

/*if (mouse_check_button_pressed(mb_left) || point_distance(oTongue.x,oTongue.y,oPlayer.x,oPlayer.y) >= range) {
	active = false;
	show_debug_message("achieved")
}

if (!active) {
	oTongue.direction = point_direction(oPlayer.x,oPlayer.y,mouse_x,mouse_y);
	oTongue.speed = retractSpeed;
}

if(place_meeting(oTongue.x, oTongue.y, oPlayer) && !active) {
	instance_destroy();
}