<a href="javascript:void(0);" <#if card.id??>onclick="cardClicked('${card.id}');</#if>"<div class="deck-card<#if card.active??>-active</#if>">
	<h4>${card.name}</h4>

	<!--<img src="/card/${card.imageUrl}.png"/>-->
</div></a>