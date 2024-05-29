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

//* funciona mas puxa do crudprodutos dando zoom ...


// $(document).ready(function() {
//     // Adiciona evento de submit ao formulário de pesquisa
//     $('.search-form').submit(function(event) {
//         event.preventDefault(); // Impede o comportamento padrão do formulário
//         pesquisarProduto(); // Chama a função de pesquisa
//     });

//     // Função para pesquisar produtos
//     function pesquisarProduto() {
//         var palavra = $('.search-form input[type="text"]').val();

//         // Se a pesquisa estiver vazia, recarrega todos os produtos
//         if (palavra === '') {
//             carregarProduto(currentPage);
//             return;
//         }

//         $.ajax({
//             url: 'http://localhost:8080/produtos/pesquisa/' + palavra,
//             method: 'GET',
//             success: function(data) {
//                 // Limpa a grade de produtos
//                 $('#produto-grid').empty();

//                 // Verifica se nenhum produto foi encontrado
//                 if (data.length === 0) {
//                     var mensagemErro = $('<div class="produto-item">');
//                     mensagemErro.append('<p style="text-align: center; font-weight: bold;">Nenhum produto encontrado</p>');
//                     $('#produto-grid').append(mensagemErro);
//                     return;
//                 }

//                 data.forEach(function(produto) {
//                     var produtoItem = $('<div class="produto-item">');
//                     produtoItem.append('<img src="' + produto.imagemPrincipal + '" alt="' + produto.nomeProduto + '">');
//                     produtoItem.append('<h3>' + produto.nomeProduto + '</h3>');
//                     produtoItem.append('<p>Quantidade: ' + produto.quantidade + '</p>');
//                     produtoItem.append('<p>R$ ' + produto.preco.toFixed(2) + '</p>');
//                     produtoItem.append('<div class="acoes"><button type="button" class="btn ' + (produto.ativo ? 'btn-success' : 'btn-danger') + '" data-id="' + produto.id + '" onclick="ativarDesativar(' + produto.id + ')">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</button> <button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button> <button type="button" class="btn btn-info" onclick="visualizarProduto(' + produto.id + ')">Visualizar</button></div>');
//                     $('#produto-grid').append(produtoItem);
//                 });
//             },
//             error: function(xhr, status, error) {
//                 console.error('Erro ao realizar a pesquisa:', error);
//             }
//         });
//     }

//     function carregarProduto(pagina) {
//         $.ajax({
//             url: 'http://localhost:8080/produtos' + pagina,
//             method: 'GET',
//             success: function(data) {
//                 $('#produto-grid').empty();

//                 data.forEach(function(produto) {
//                     var produtoItem = $('<div class="produto-item">');
//                     produtoItem.append('<img src="' + produto.imagemPrincipal + '" alt="' + produto.nomeProduto + '">');
//                     produtoItem.append('<h3>' + produto.nomeProduto + '</h3>');
//                     produtoItem.append('<p>Quantidade: ' + produto.quantidade + '</p>');
//                     produtoItem.append('<p>R$ ' + produto.preco.toFixed(2) + '</p>');
//                     produtoItem.append('<div class="acoes"><button type="button" class="btn ' + (produto.ativo ? 'btn-success' : 'btn-danger') + '" data-id="' + produto.id + '" onclick="ativarDesativar(' + produto.id + ')">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</button> <button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button> <button type="button" class="btn btn-info" onclick="visualizarProduto(' + produto.id + ')">Visualizar</button></div>');
//                     $('#produto-grid').append(produtoItem);
//                 });
//             },
//             error: function(xhr, status, error) {
//                 console.error('Erro ao carregar produtos:', error);
//             }
//         });
//     }

//     // Carrega os produtos iniciais
//     carregarProduto(1); // Ajuste conforme necessário
// });

