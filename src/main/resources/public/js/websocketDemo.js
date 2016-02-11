//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat/" + location.search);
webSocket.onmessage = function (msg) { 
	/*
	console.info(msg);
	console.info(msg.data);
	console.info(msg.data.key);
	*/
    var data = JSON.parse(msg.data);
	
	if (data.key == 'chatMsg')
		updateChat(msg);
	else if (data.key == 'updateSandbox')
		updateSandbox(msg)
	};
	
webSocket.onclose = function () { alert("WebSocket connection closed") };

//Send message if "Send" is clicked
id("send").addEventListener("click", function () {
    sendMessage(id("message").value);
});

//Send message if enter is pressed in the input field
id("message").addEventListener("keypress", function (e) {
    if (e.keyCode === 13) { sendMessage(e.target.value); }
});

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        webSocket.send("chat.message:" + message);
        id("message").value = "";
    }
}

//Update the chat-panel, and the list of connected users
function updateChat(msg) {
	
	//console.info(msg.data);
	
    var data = JSON.parse(msg.data);
    insert("chat", data.userMessage);
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>");
    });
}

//Update the chat-panel, and the list of connected users
function updateSandbox(msg) {
  var data = JSON.parse(msg.data);
  id("play-sandbox-container").innerHTML = data.playHtml;
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}