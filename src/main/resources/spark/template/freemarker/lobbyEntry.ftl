<div class="lobby-entry">
	<#if lobby.password?has_content >
		<a href="javascript:void(0);" onclick="promptPassword('${lobby.id}', '${lobby.name}');"><h3>
		<i class="fa fa-lock"></i> <!-- Lock Character -->
	<#else>
		<a href="/play?id=${lobby.id}"><h3>
		<i class="fa fa-unlock"></i><!-- Unlock Character -->
	</#if>
	
	${lobby.name}</h3></a>

	<table>
		<tr>
			<td>


				<div class="playerCount">
					${lobby.players?size}/${lobby.maxPlayers} Players</div>

			</td>


			<td>
				<ul class="playerlist">

					<#list lobby.players as player>
					<li>${player.name}</li> </#list>

				</ul>
			</td>
	</table>
</div>


