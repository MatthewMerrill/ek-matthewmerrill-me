<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Browse Lobbies</title>
    <link rel="stylesheet" href="css/lobbyBrowse.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">

  </head>

<body>

  <div class="lobby-list-pane">
    <#list lobbies as lobby>
    	<div class="lobby-color-${lobby?item_cycle('a', 'b', 'c', 'd')}">
        	<#include "lobbyEntry.ftl">
        </div>
    </#list>
  </div>

  </body>
</html>
