<!DOCTYPE html>
<html>
<head>
  <title>Browse Lobbies</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="/css/lobbyBrowse.css">
  <#include "header.ftl">
</head>

  <body>
    <#include "banner.ftl">
    
   	<#if message??>
	    <div class="center-content">
	    	<h1>${message}</h1>
	    </div>
   	</#if>
   	
    <div class="center-content">
    	<h2>Browse Lobbies</h2>
    	Click the name of a lobby you wish to join. (Some may be password protected)
    	<br><br>To create your own lobby, click <a href="newlobby">here</a>.
    </div>
    	
    <div class="lobby-list-pane">
      <#list lobbies?values as lobby>
        <div class="lobby-color-${lobby?item_cycle('a', 'b', 'c', 'd')}">
          <#include "lobbyEntry.ftl">
        </div>
      </#list>
    </div>

  </body>
	<#include "footer.ftl">
</html>
