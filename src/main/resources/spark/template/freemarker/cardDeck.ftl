<div class="deck">

  <h4><#if playername??></h4>
    <#list cards as card>
      <div class="deck-card"> <img src="${card.imageUrl}"/> </div>
    </#list>

</div>
