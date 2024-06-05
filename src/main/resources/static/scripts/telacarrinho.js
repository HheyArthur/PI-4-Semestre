$(document).ready(function () {
    carregarCarrinho();
});

function carregarCarrinho() {
    var carrinho = JSON.parse(localStorage.getItem('carrinho')) || [];
    var cartItemsContainer = $('#cart-items');
    cartItemsContainer.empty();

    var total = 0;

    if (carrinho.length > 0) {
        carrinho.forEach(function (item, index) {
            total += item.preco * item.quantidade;
            var cartItem = `
                <div class="cart-item">
                    <input type="checkbox" class="cart-item-checkbox" data-index="${index}" checked>
                    <img src="${item.imagem}" alt="${item.nome}" class="cart-item-image" style="width: 200px; height: 200px;">
                    <div class="cart-item-info">
                        <h3>${item.nome}</h3>
                        <p>${item.descricao}</p>
                        <h4>R$ ${item.preco.toFixed(2)}</h4>
                        <p>Quantidade: ${item.quantidade}</p>
                    </div>
                </div>
            `;
            cartItemsContainer.append(cartItem);
        });
    } else {
        cartItemsContainer.html('<p>Seu carrinho está vazio.</p>');
    }

    $('#cart-total').text(total.toFixed(2));
    $('#cart-count').text(carrinho.length);
}

$('#confirmar-compra').click(function () {
    var carrinho = JSON.parse(localStorage.getItem('carrinho')) || [];
    var itensSelecionados = $('.cart-item-checkbox:checked');
    
    if (itensSelecionados.length > 0) {
        var itensConfirmar = [];
        itensSelecionados.each(function () {
            var index = $(this).data('index');
            itensConfirmar.push(carrinho[index]);
        });

        alert("Compra confirmada, Muito Obrigado e Volte sempre!");
      } else {
          alert("Selecione pelo menos um item para confirmar a Compra.");
      }
      

    });



function limparCarrinho() {
    localStorage.removeItem('carrinho');
    carregarCarrinho();
    alert("Carrinho limpo com sucesso!");
}
