<!DOCTYPE html>
<html>
	<head>	
		<#if lobby?has_content>
			<title>${lobby.name}</title>
		<#else>
			<title>Play</title>
		</#if>
		
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="/css/play.css">
		<#include "header.ftl">
	</head>


	<body>
		<#include "banner.ftl">
		
		<div id="play-chat">
			<h2>Lobby Chat</h2>
			<#include "chatSection.ftl">
		</div>
		
	</body>
	
	<#include "footer.ftl">
</html>
