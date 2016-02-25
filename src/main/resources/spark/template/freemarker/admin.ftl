<div class="deck">
		<#if message??>
  <h4>${message}</h4>
  </#if>
    <#list cards as card>
    		<#include "card.ftl">
    </#list>
</div>
