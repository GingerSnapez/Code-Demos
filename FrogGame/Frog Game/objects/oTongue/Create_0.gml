enum tongue_state {
	extending,
	hooked,
	retracting,
}

range = 400;
extendSpeed = 15;
retractSpeed = 30;
tState = tongue_state.extending;
tLength = point_distance(oTongue.x,oTongue.y,oPlayer.x,oPlayer.y);;