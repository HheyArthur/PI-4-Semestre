$(document).ready(function () {
    carregarProduto();
});

function carregarProduto() {
    $.ajax({
        url: 'http://localhost:8080/produtos', // Insira a URL do seu backend aqui
        method: 'GET',
        success: function (data) {
            $('tbody').empty();
            data.forEach(function (produto) {
                var newRow = $('<tr class="item">');
                newRow.append('<td>' + produto.codigo + '</td>');
                newRow.append('<td><img class="photo-icon" src="' + produto.imagemPrincipal + '"></td>');
                newRow.append('<td>' + produto.nomeProduto + '</td>');
                newRow.append('<td>' + produto.quantidade + '</td>');
                newRow.append('<td>R$ ' + produto.preco + '</td>');
                newRow.append('<td class="acao ativo"><button type="button" class="btn" data-id="' + produto.id + '" onclick="ativarDesativar(' + produto.id + ')">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</button></td>');
                newRow.append('<td class="acao"><button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button> <button type="button" class="btn btn-primary" onclick="visualizarProduto(' + produto.id + ')">Visualizar</button></td>');
                $('tbody').append(newRow);

                var button = newRow.find('button[data-id="' + produto.id + '"]');
                if (produto.ativo) {
                    button.addClass('btn-success');
                    button.removeClass('btn-danger');
                } else {
                    button.addClass('btn-danger');
                    button.removeClass('btn-success');
                }
            });
        },
        error: function (xhr, status, error) {
            console.error('Erro ao carregar produtos:', error);
        }
    });
}

function visualizarProduto(id) {
    // Fazer a requisição AJAX
    console.log('ID:', id);
    $.ajax({
        url: 'http://localhost:8080/produtos/' + id,
        type: 'GET',
        success: function (response) {
            // Limpar o carrossel
            $('#carouselExampleIndicators .carousel-indicators').empty();
            $('#carouselExampleIndicators .carousel-inner').empty();

            // Obter a imagem principal
            var imagemPrincipal = response.imagemPrincipal;

            // Criar o elemento do carrossel para a imagem principal
            var indicator = $('<button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0"></li>');
            var carouselItem = $('<div class="carousel-item"><img class="d-block img-fluid" src="' + imagemPrincipal + '" alt="Imagem Principal"></div>');
            indicator.addClass('active');
            carouselItem.addClass('active');
            $('#carouselExampleIndicators .carousel-indicators').append(indicator);
            $('#carouselExampleIndicators .carousel-inner').append(carouselItem);

            // Preencher o carrossel com as outras imagens do objeto Produto
            $.each(response.imagens, function (index, imagens) {
                var indicator = $('<button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="' + (index + 1) + '"></li>');
                var carouselItem = $('<div class="carousel-item"><img class="d-block img-fluid" src="' + imagens.url + '" alt="Imagem ' + (index + 1) + '"></div>');
                $('#carouselExampleIndicators .carousel-indicators').append(indicator);
                $('#carouselExampleIndicators .carousel-inner').append(carouselItem);
            });

            $('#imageModal').modal('show');
        },
        error: function (xhr, status, error) {
            console.error('Erro ao carregar imagens:', error);
        }
    });
}

document.getElementById('pesquisaInput').addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
        pesquisarProduto();
    }
});


function ativarDesativar(id) {
    $.ajax({
        url: 'http://localhost:8080/produtos/ativarDesativarProduto/' + id,
        method: 'PUT',
        success: function (data) {
            // Aqui você pode adicionar o código para lidar com os dados retornados pela requisição
            console.log('Produto atualizado:', data);
            var button = document.querySelector('button[data-id="' + id + '"]');
            if (data.ativo) {
                button.textContent = 'Ativo';
                button.classList.remove('btn-danger');
                button.classList.add('btn-success');
            } else {
                button.textContent = 'Inativo';
                button.classList.remove('btn-success');
                button.classList.add('btn-danger');
            }
        },
        error: function (xhr, status, error) {
            console.error('Erro ao atualizar o produto:', error);
        }
    });
}


function pesquisarProduto() {
    var palavra = document.getElementById('pesquisaInput').value;
    if (palavra === '') {
        carregarProduto();
        return;
    }
    $.ajax({
        url: 'http://localhost:8080/produtos/pesquisa/' + palavra,
        method: 'GET',
        success: function (data) {
            if (data.length === 0) {
                $('tbody').empty();
                var mensagemErro = $('<tr>');
                mensagemErro.append('<td colspan="7" style="text-align: center; font-weight: bold;">Nenhum produto encontrado</td>');
                $('tbody').append(mensagemErro);
                return;
            }
            $('tbody').empty();
            data.forEach(function (produto) {
                var novaLinha = $('<tr class="item">');
                novaLinha.append('<td>' + produto.codigo + '</td>');
                novaLinha.append('<td><img class="photo-icon" src="' + produto.imagemPrincipal + '"></td>');
                novaLinha.append('<td>' + produto.nomeProduto + '</td>');
                novaLinha.append('<td>' + produto.quantidade + '</td>');
                novaLinha.append('<td>R$ ' + produto.preco + '</td>');
                novaLinha.append('<td class="acao ativo"><button type="button" class="btn" data-id="' + produto.id + '" onclick="ativarDesativar(' + produto.id + ')">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</button></td>');
                novaLinha.append('<td class="acao"><button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button></td>');
                novaLinha.append('<td class="acao"><button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button> <button type="button" class="btn btn-primary" onclick="visualizarProduto(' + produto.id + ')">Visualizar</button></td>');
                $('tbody').append(novaLinha);

                var button = novaLinha.find('button[data-id="' + produto.id + '"]');
                if (produto.ativo) {
                    button.addClass('btn-success');
                    button.removeClass('btn-danger');
                } else {
                    button.addClass('btn-danger');
                    button.removeClass('btn-success');
                }

            });
        },
        error: function (xhr, status, error) {
            console.error('Erro ao realizar a pesquisa:', error);
        }
    });
}

function cadastrarProduto() {
    var nome = document.getElementById('m-nomeprodCad').value;
    var arquivo = document.getElementById('m-imagemProdPrev').value;
    console.log(arquivo);
    var descricao = document.getElementById('m-descricaoProdCad').value;
    var quantidade = document.getElementById('m-quantidadeprodCad').value;
    var preco = document.getElementById('m-precoprodCad').value;
    var formato = arquivo.split('\\').pop();
    var caminho = "..\\img\\" + formato;
    console.log(caminho);

    $.ajax({
        url: 'http://localhost:8080/produtos/cadastrarProduto',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nomeProduto: nome,
            descricao: descricao,
            imagemPrincipal: caminho,
            quantidade: quantidade,
            preco: preco
        }),
        success: function (data) {
            console.log('Produto cadastrado:', data);
            fecharModal();
        },
        error: function (_, _, error) {
            console.error('Erro ao cadastrar o produto:', error);
        }
    });
}

function editarProduto(id) {
    console.log('ID:', id);
    $.ajax({
        url: 'http://localhost:8080/produtos/' + id,
        method: 'GET',
        success: function (data) {

            $('#m-imagemProdEdit').attr('src', data.imagemPrincipal);
            $('m-imagemProdPrevEdit').val(data.imagemPrincipal);
            $('#m-nomeprodEdit').val(data.nomeProduto);
            $('#m-descricaoEdit').val(data.descricao);
            $('#m-quantidadeprodEdit').val(data.quantidade);
            $('#m-precoprodEdit').val(data.preco);
            // Abre o modal de edição
            openModalEditar();

            // Remove todos os ouvintes de evento existentes do botão de salvar
            $('#btnSalvar').off('click');

            // Anexa um novo ouvinte de evento ao botão de salvar
            $('#btnSalvar').on('click', function () {
                var confirma = window.confirm('Deseja realmente salvar essas alterações?');
                if (!confirma) {
                    alert('Operação cancelada!');
                    fecharModal();
                } else {
                    salvarProdEditado(id);
                }
            });
        },
        error: function (xhr, status, error) {
            console.error('Erro ao buscar dados do produto:', error);
        }
    });
}

function salvarProdEditado(id) {
    var arquivo = $('#m-imagemProdPrevEdit').val();
    var nomeProduto = $('#m-nomeprodEdit').val();
    var descricao = $('#m-descricaoEdit').val();
    var quantidade = $('#m-quantidadeprodEdit').val();
    var preco = $('#m-precoprodEdit').val();
    var caminho;

    if (arquivo == '' || arquivo == null || arquivo == undefined) {
        $.ajax({
            url: 'http://localhost:8080/produtos/' + id,
            method: 'GET',
            async: false,
            success: function (data) {
                caminho = data.imagemPrincipal;
            }
        });
    } else {
        var formato = arquivo.split('\\').pop();
        caminho = "..\\img\\" + formato;
    }

    $.ajax({
        url: 'http://localhost:8080/produtos/atualizaProdutoPorId/' + id,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({
            imagemPrincipal: caminho,
            nomeProduto: nomeProduto,
            descricao: descricao,
            quantidade: quantidade,
            preco: preco
        }),
        success: function (data) {
            console.log('Produto atualizado:', data);
            alert('Produto atualizado com sucesso!');
            fecharModal();
            carregarProduto();
            limparCampos();
        },
        error: function (xhr, status, error) {
            console.error('Erro ao atualizar o produto:', error);
        }
    });
}

function limparCampos() {
    $('#m-imagemProdPrevEdit').val('');
    $('#m-nomeprodEdit').val('');
    $('#m-descricaoEdit').val('');
    $('#m-quantidadeprodEdit').val('');
    $('#m-precoprodEdit').val('');
}

function openModal() {
    var modal = new bootstrap.Modal(document.getElementById('modalprod'));
    modal.show();
}

function openModalEditar() {
    var modal = new bootstrap.Modal(document.getElementById('modalEditar'));
    modal.show();
}

function fecharModal() {
    $('modalprod').modal('dispose');
    $('modalEditar').modal('dispose');
}


function updatePagination(totalItems) {
    const itemsPerPage = 10;
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const paginationElement = document.getElementById('pagination');

    paginationElement.innerHTML = ''; // Limpar elementos existentes

    if (totalPages > 1) {
        // Adiciona botão 'Anterior'
        paginationElement.innerHTML += `<li id="previous" class="disabled page-item"><a href="#" class="page-link">‹</a></li>`;

        // Adiciona números das páginas
        for (let page = 1; page <= totalPages; page++) {
            paginationElement.innerHTML += `<li class="page-item"><a href="#" class="page-link">${page}</a></li>`;
        }

        // Adiciona botão 'Próximo'
        paginationElement.innerHTML += `<li id="next" class="page-item"><a href="#" class="page-link">›</a></li>`;
        paginationElement.innerHTML += `<li id="last" class="page-item"><a href="#" class="page-link">»</a></li>`;
    } else {
        // Se houver apenas uma página, desabilita os botões 'Anterior' e 'Próximo'
        paginationElement.innerHTML += `<li id="previous" class="disabled page-item"><a href="#" class="page-link">‹</a></li>`;
        paginationElement.innerHTML += `<li class="active page-item"><a href="#" class="page-link">1</a></li>`;
        paginationElement.innerHTML += `<li id="next" class="disabled page-item"><a href="#" class="page-link">›</a></li>`;
    }
}

const totalItems = 10;
updatePagination(totalItems);