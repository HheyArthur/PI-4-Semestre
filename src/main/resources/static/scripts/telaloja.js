$(document).ready(function () {
    carregarProdutos();
});

function carregarProdutos() {
    $.ajax({
        url: 'http://localhost:8080/produtos', // Insira a URL do seu backend aqui
        method: 'GET',
        success: function (data) {
            $('#produto-grid').empty();
            data.content.forEach(function (produto) {
                if (produto.ativo) { // Verifica se o produto está ativo
                    var produtoCard = `
                        <div class="produto">
                            <img src="${produto.imagemPrincipal}" alt="${produto.nomeProduto}">
                            <h3>${produto.nomeProduto}</h3>
                            <h4>${"R$ " + produto.preco}</h4>
                            <p>${produto.descricao}</p>
                            <button class="btn-comprar" data-produto-id="${produto.id}" onclick="visualizarProduto(${produto.id})">Ver mais</button>
                        </div>
                    `;
                    $('#produto-grid').append(produtoCard);
                }
            });
        },
        error: function (xhr, status, error) {
            console.error('Erro ao carregar produtos:', error);
        }
    });
}

function visualizarProduto(id) {
    $.ajax({
        url: 'http://localhost:8080/produtos/' + id,
        method: 'GET',
        success: function (produto) {
            $('#carouselExampleIndicators .carousel-indicators').empty();
            $('#carouselExampleIndicators .carousel-inner').empty();

            // Imagem principal
            var indicator = $('<button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0"></button>');
            var carouselItem = $('<div class="carousel-item active"><img class="d-block w-100" src="' + produto.imagemPrincipal + '" alt="Imagem Principal"></div>');
            $('#carouselExampleIndicators .carousel-indicators').append(indicator);
            $('#carouselExampleIndicators .carousel-inner').append(carouselItem);

            // Outras imagens
            produto.imagens.forEach(function (imagem, index) {
                var indicator = $('<button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="' + (index + 1) + '"></button>');
                var carouselItem = $('<div class="carousel-item"><img class="d-block w-100" src="' + imagem.url + '" alt="Imagem ' + (index + 1) + '"></div>');
                $('#carouselExampleIndicators .carousel-indicators').append(indicator);
                $('#carouselExampleIndicators .carousel-inner').append(carouselItem);
            });

            $('#modal-product-name').text(produto.nomeProduto);
            $('#modal-product-description').text(produto.descricao);
            $('#modal-product-price').text('R$ ' + produto.preco);

            $('#productModal').modal('show');
        },
        error: function (xhr, status, error) {
            console.error('Erro ao carregar detalhes do produto:', error);
        }
    });
}

function visualizarProduto(id) {
    window.location.href = `produtoinfo.html?id=${id}`;
}
