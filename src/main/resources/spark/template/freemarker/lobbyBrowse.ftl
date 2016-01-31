<!DOCTYPE html>
<html>
<head>
  <title>Browse Lobbies</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/lobbyBrowse.css">
  
  <#include "header.ftl">
</head>

  <body>
    <#include "banner.ftl">

    <div class="lobby-list-pane">
      <#list lobbies as lobby>
        <div class="lobby-color-${lobby?item_cycle('a', 'b', 'c', 'd')}">
          <#include "lobbyEntry.ftl">
        </div>
      </#list>
    </div>

  </body>
</html>
