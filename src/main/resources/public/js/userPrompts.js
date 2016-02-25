function promptPassword(lobbyId, lobbyName) {
	var password = prompt("Please enter the Lobby password for "+lobbyName+":", "bear-o-dactyl");
	
	console.info(password);
	
	if (password != null) {
		window.location = "/play?id=" + lobbyId + "&pw=" + password;
	}
}


function validate(evt) {
	var theEvent = evt || window.event;
	var key = theEvent.keyCode || theEvent.which;
	key = String.fromCharCode( key );
	var regex = /[0-9]|\./;
	if( !regex.test(key) ) {
		theEvent.returnValue = false;
		if(theEvent.preventDefault) theEvent.preventDefault();
	}
}