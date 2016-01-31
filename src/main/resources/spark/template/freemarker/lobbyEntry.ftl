<div class="lobby-entry">
      <a href="/play/${lobby.id}"> <h3>
        <#if lobby.password?has_content >
          <i class="fa fa-lock"></i> <!-- Lock Character -->
        <#else>
          <i class="fa fa-unlock"></i> <!-- Unlock Character -->
        </#if>
       
        ${lobby.name}
      </h3></a>
      
  <table>
  <tr>
    <td>
  
      
      <div class="playerCount">
        ${lobby.players?size}/${lobby.maxPlayers} Players
      </div>
  
    </td>
  
  
    <td>
      <ul class="playerlist">
      
        <#list lobby.players as player>
          <li>${player.name}</li>
        </#list>
      
      </ul>
    </td>
  </table>
</div>
  
  
