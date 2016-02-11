<!DOCTYPE html>
<html>
<head>
<title>Login</title> <#include "header.ftl">
</head>

<body>
	<#include "banner.ftl">

	<div class="center-content">
		<h1>New Lobby:</h1>
		
		<#if invalid??>
		  <font color="#E94E25">Name may only contain alpha-numerics and underscore.</font>
		  <br>
		</#if>
		
		Lobbies will be removed when empty.
		<form name="newLobbyForm" id="newLobbyForm" onsubmit="return validateForm()"
			method="post">
			Name:<input type="text" name="lname" required>
			Password:<input type="text" name="lpassword">
			<input type="submit" value="Submit">
		</form>
	</div>


	<script>
		function validateForm() {
			   var regex = /[a-zA-Z0-9_]/;
		    var x = document.forms["newLobbyForm"]["lname"].value;
		    if (x == null || x == "") {
		        alert("Lobby Name must be filled out");
		        return false;
		    } else if (! regex.test(x) ){
		        alert("Names may only include alphanumerics and underscore.");
		    				return false;
      } else {
		    	<#if path??>
		    		window.location = "${path}";
		    	<#else>
		    		window.location = "/";
		    	</#if>
		    	return true;
		    }
		}
	</script>
</body>
<#include "footer.ftl">
</html>
