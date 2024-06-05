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

$('#confirmar-entrega').click(function () {
    var carrinho = JSON.parse(localStorage.getItem('carrinho')) || [];
    var itensSelecionados = $('.cart-item-checkbox:checked');

    if (itensSelecionados.length > 0) {
        var itensConfirmar = [];
        itensSelecionados.each(function () {
            var index = $(this).data('index');
            itensConfirmar.push(carrinho[index]);
        });

        // Obter o ID do usuário do localStorage (ajuste a chave se necessário)
        var usuarioId = 26; 

        // Construir o array de itens no formato correto
        var itensCompra = itensConfirmar.map(function (item) {
            return {
                produtoId: item.id, // Supondo que o ID do produto esteja em item.id
                quantidade: item.quantidade // Supondo que a quantidade esteja em item.quantidade
            };
        });

        // Obter o valor total da compra do elemento HTML
        var valorTotal = parseFloat($('#cart-total').text().replace(',', '.'));

        // Criar o objeto JSON para enviar na requisição POST
        var dadosCompra = {
            usuarioId: usuarioId,
            itens: itensCompra,
            valorTotal: valorTotal
        };

        // Enviar a requisição POST para a API
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/compras/criarCompra",
            contentType: "application/json",
            data: JSON.stringify(dadosCompra),
            success: function (response) {
                console.log("Compra registrada com sucesso:", response);
                alert("Entrega confirmada!");

                // Limpar o localStorage
                localStorage.removeItem('carrinho'); 

                history.go(0);
            },
            error: function (error) {
                console.error("Erro ao registrar a compra:", error);
                alert("Ocorreu um erro ao confirmar a entrega. Por favor, tente novamente.");
            }
        });
    } else {
        alert("Selecione pelo menos um item para confirmar a entrega.");
    }
});

$('#acompanhar-pedido').click(function () {
    alert("Funcionalidade de acompanhamento de pedido ainda não implementada.");
});
