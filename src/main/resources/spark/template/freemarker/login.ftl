<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
	<#include "header.ftl">
</head>

<body>
	<#include "banner.ftl">
	
	<div class="center-content">
		<h1>Login:</h1>
		Usernames are temporary and not distinct.
		<form name="loginForm" id="loginForm" onsubmit="return validateForm()" method="post">
			Name:<input type="text" name="pname" required>
			Favorite Movie:<input type="text" name="fmovie">
			
		    	<#if path??>
					<input type="hidden" name="redirect" value="${path}">
		    	</#if>
		    	
			<input type="submit" value="Submit">
		</form> 
	</div>


	<script>
		function validateForm() {
		    var x = document.forms["myForm"]["fname"].value;
		    if (x == null || x == "") {
		        alert("Name must be filled out");
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
