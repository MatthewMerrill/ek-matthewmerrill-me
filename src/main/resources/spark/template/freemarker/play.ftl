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
		
		
		<ul class="container">
		
			<li class="play-li" id="play-sandbox-container">
				<#include "sandbox.ftl">
	  </li>
	    	
			<li class="play-li"  id="play-chat">
				<h2>Lobby Chat</h2>
				<#include "chatSection.ftl">
			</li>
		</ul>
		
		
	</body>
	
	<#include "footer.ftl">
</html>
