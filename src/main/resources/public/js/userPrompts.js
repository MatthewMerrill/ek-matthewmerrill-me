function promptPassword(lobbyId, lobbyName) {
	var password = prompt("Please enter the Lobby password for "+lobbyName+":", "bear-o-dactyl");
	
	console.info(password);
	
	if (password != null) {
		window.location = "/play?id=" + lobbyId + "&pw=" + password;
	}
}