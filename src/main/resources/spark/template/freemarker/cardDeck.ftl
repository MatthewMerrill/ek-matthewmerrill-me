<div class="deck">
		<#if playername??>
  <h4>You are: <em>${playername}</em></h4>
  </#if>
    <#list cards as card>
    		<#include "card.ftl">
    </#list>
</div>
