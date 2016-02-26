<a href="javascript:void(0);" <#if card.id??>onclick="cardClicked('${card.id}');</#if>"<div id="deck-card" <#if card.active??>class="active"</#if>">
	<h4>${card.name}</h4>
	<#if card.description??><h6>${card.description}</h6></#if>

	<!--<img src="/card/${card.imageUrl}.png"/>-->
</div></a>