//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port
		+ "/chat/" + location.search);
webSocket.onmessage = function(msg) {
	/*
	console.info(msg);
	console.info(msg.data);
	console.info(msg.data.key);
	 */
	var data = JSON.parse(msg.data);
	console.info("Received: " + msg.data);

	if (data.key == 'chatMsg')
		updateChat(msg);
	else if (data.key == 'updateSandbox')
		updateSandbox(msg)
	else if (data.key == 'playSound')
		playSound(msg)
	else if (data.key == 'kicked')
		window.location = '/play?msg=Kicked. :(';
	else if (data.key == 'promptPlayer')
		promptUserForPlayer(data.lobbyId, data.header, data.message);
	else if (data.key == 'prompt')
		promptUser(data.lobbyId, data.header, data.message);
};

webSocket.onclose = function() {
	alert("WebSocket connection closed")
};

//Send message if "Send" is clicked
id("send").addEventListener("click", function() {
	sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function(e) {
	if (e.keyCode === 13) {
		sendMessage(e.target.value);
	}
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
	if (message !== "") {
		webSocket.send("chat.message:" + message);
		id("message").value = "";
	}
}

function cardClicked(cardId) {
	console.info(cardId);
	//var password = prompt("You clicked  " + cardName, "Cool!");

	if (message !== "") {
		webSocket.send("card.played:" + cardId);
	}
}

function promptUser(lobbyId, header, message) {
	var rsp = prompt(message, "aa");

	console.info(rsp);

	if (rsp != null) {
		webSocket.send("prompt:" + header + ":" + rsp);
	}
}

function promptUserResponse(lobbyId, header) {
	var e = id("inputSelect");
	var response = e.options[e.selectedIndex].value;

	$('#south').empty();
	webSocket.send("prompt:" + header + ":" + response);
}

function promptIntegerResponse(lobbyId, header) {
	var e = id("integerInput");
	var response = e.value;

	$('#south').empty();
	
	if (response == null) {
		console.log("Failed to retrieve integer value. Defaulting to 0.");
		response = 0;
	}
	
	webSocket.send("prompt:" + header + ":" + response);
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {

	//console.info(msg.data);

	var data = JSON.parse(msg.data);
	insert("chat", data.userMessage);
	id("userlist").innerHTML = "";
	data.userlist.forEach(function(user) {
		insert("userlist", "<li>" + user + "</li>");
	});
}

//Plays a sound
function playSound(msg) {
	var data = JSON.parse(msg.data);
	var audio = new Audio(data.sound);
	audio.play();
}

//Update the chat-panel, and the list of connected users
function updateSandbox(msg) {
	var data = JSON.parse(msg.data);
	console.info("updatingSandbox:");

	if (data.centerHtml != null) {
		$('#center').empty();
		id("center").innerHTML = data.centerHtml;
	}
	if (data.northHtml != null) {
		$('#north').empty();
		id("north").innerHTML = data.northHtml;
	}

	if (data.southHtml != null) {
		$('#south').empty();
		id("south").innerHTML = data.southHtml;
	}

	if (data.eastHtml != null) {
		$('#east').empty();
		id("east").innerHTML = data.eastHtml;
	}
	if (data.westHtml != null) {
		$('#west').empty();
		//while (id("west").hasChildNodes()) {
		///	id("west").removeChild(id("west").firstChild);
		//}
		id("west").innerHTML = data.westHtml;
	}
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
	id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
	return document.getElementById(id);
}