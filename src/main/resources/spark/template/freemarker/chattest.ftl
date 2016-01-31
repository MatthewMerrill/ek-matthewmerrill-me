<!DOCTYPE html>
<html>
<head>
  <title>Browse Lobbies</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/chatstyle.css">
  
  <#include "header.ftl">
</head>

    <#include "banner.ftl">
    
  <body>
  
    <div class="chat-section">
      <div id="chatControls">
        <button id="send">Send</button>
        <span><input id="message" placeholder="Type your message"></span>
      </div>
      
      <ul id="userlist"> <!-- Built by JS --> </ul>
      <div id="chat">    <!-- Built by JS --> </div>
      <script src="/js/websocketDemo.js"></script>
    </chatSection>
  </body>
</html>