// Variáveis globais para controlar a paginação
let currentPage = 0; // Começa da página 0
const itemsPerPage = 5;

// Função para carregar os produtos da página especificada
function carregarProduto() {

}


function carregarProduto(page) {
    $.ajax({
        url: `http://localhost:8080/produtos?page=${page}&size=${itemsPerPage}`,
        method: 'GET',
        success: function (data) {
            // Limpa a tabela
            $('tbody').empty();

            // Adiciona os produtos na tabela
            data.content.forEach(function (produto) {
                // Cria uma nova linha na tabela
                var newRow = $('<tr class="item">');

                // Adiciona as colunas da linha
                newRow.append('<td>' + produto.codigo + '</td>');
                newRow.append('<td><img class="photo-icon" src="' + produto.imagemPrincipal + '"></td>');
                newRow.append('<td>' + produto.nomeProduto + '</td>');
                newRow.append('<td>' + produto.quantidade + '</td>');
                newRow.append('<td>R$ ' + produto.preco + '</td>');
                newRow.append('<td class="acao ativo"><button type="button" class="btn" data-id="' + produto.id + '" onclick="ativarDesativar(' + produto.id + ')">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</button></td>');
                newRow.append('<td class="acao"><button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button> <button type="button" class="btn btn-primary" onclick="visualizarProduto(' + produto.id + ')">Visualizar</button></td>');

                // Adiciona a linha na tabela
                $('tbody').append(newRow);

                // Define a classe do botão de acordo com o status do produto
                var button = newRow.find('button[data-id="' + produto.id + '"]');
                if (produto.ativo) {
                    button.addClass('btn-success');
                    button.removeClass('btn-danger');
                } else {
                    button.addClass('btn-danger');
                    button.removeClass('btn-success');
                }
            });
            // Atualiza a paginação
            updatePagination(data.totalPages);
        },
        error: function (xhr, status, error) {
            console.error('Erro ao carregar produtos:', error);
        }
    });
}

// Função para visualizar o produto
function visualizarProduto(id) {
    $.ajax({
        url: 'http://localhost:8080/produtos/' + id,
        type: 'GET',
        success: function (response) {
            // Limpa o carrossel
            $('#carouselExampleIndicators .carousel-indicators').empty();
            $('#carouselExampleIndicators .carousel-inner').empty();

            // Obter a imagem principal
            var imagemPrincipal = response.imagemPrincipal;

            // Criar o elemento do carrossel para a imagem principal
            var indicator = $('<li data-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active"></li>');
            var carouselItem = $('<div class="carousel-item active"><img class="d-block img-fluid" src="' + imagemPrincipal + '" alt="Imagem Principal"></div>');
            $('#carouselExampleIndicators .carousel-indicators').append(indicator);
            $('#carouselExampleIndicators .carousel-inner').append(carouselItem);

            // Preencher o carrossel com as outras imagens do objeto Produto
            $.each(response.imagens, function (index, imagens) {
                var indicator = $('<li data-target="#carouselExampleIndicators" data-bs-slide-to="' + (index + 1) + '"></li>');
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

// Função para ativar/desativar o produto
function ativarDesativar(id) {
    $.ajax({
        url: 'http://localhost:8080/produtos/ativarDesativarProduto/' + id,
        method: 'PUT',
        success: function (data) {
            // Atualiza o botão na tabela
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

// Função para pesquisar produtos
function pesquisarProduto() {
    var palavra = document.getElementById('pesquisaInput').value;

    // Se a pesquisa estiver vazia, recarrega todos os produtos
    if (palavra === '') {
        carregarProduto(currentPage);
        return;
    }

    // Adiciona evento para realizar a pesquisa ao pressionar "Enter"
    $('#pesquisaInput').keyup(function (event) {
        if (event.keyCode === 13) { // Verifica se a tecla pressionada foi "Enter"
            pesquisarProduto(); // Chama a função de pesquisa
        }
    });

    $.ajax({
        url: 'http://localhost:8080/produtos/pesquisa/' + palavra,
        method: 'GET',
        success: function (data) {
            // Limpa a tabela
            $('tbody').empty();

            // Verifica se nenhum produto foi encontrado
            if (data.length === 0) {
                var mensagemErro = $('<tr>');
                mensagemErro.append('<td colspan="7" style="text-align: center; font-weight: bold;">Nenhum produto encontrado</td>');
                $('tbody').append(mensagemErro);
                return;
            }

            // Adiciona os produtos encontrados na tabela
            data.forEach(function (produto) {
                var novaLinha = $('<tr class="item">');
                novaLinha.append('<td>' + produto.codigo + '</td>');
                novaLinha.append('<td><img class="photo-icon" src="' + produto.imagemPrincipal + '"></td>');
                novaLinha.append('<td>' + produto.nomeProduto + '</td>');
                novaLinha.append('<td>' + produto.quantidade + '</td>');
                novaLinha.append('<td>R$ ' + produto.preco + '</td>');
                novaLinha.append('<td class="acao ativo"><button type="button" class="btn" data-id="' + produto.id + '" onclick="ativarDesativar(' + produto.id + ')">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</button></td>');
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

// Função para cadastrar um produto
function cadastrarProduto() {
    var nome = document.getElementById('m-nomeprodCad').value;
    var arquivo = document.getElementById('m-imagemProdPrev').value;
    var descricao = document.getElementById('m-descricaoProdCad').value;
    var quantidade = document.getElementById('m-quantidadeprodCad').value;
    var preco = document.getElementById('m-precoprodCad').value;

    // Obtém o nome do arquivo e o caminho da imagem
    var formato = arquivo.split('\\').pop();
    var caminho = "..\\img\\" + formato;

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

// Função para editar um produto
function editarProduto(id) {
    $.ajax({
        url: 'http://localhost:8080/produtos/' + id,
        method: 'GET',
        success: function (data) {
            // Preenche os campos do modal com os dados do produto
            $('#m-imagemProdEdit').attr('src', data.imagemPrincipal);
            //$('#m-imagemProdPrevEdit').val(data.imagemPrincipal);
            $('#m-nomeprodEdit').val(data.nomeProduto);
            $('#m-descricaoEdit').val(data.descricao);
            $('#m-quantidadeprodEdit').val(data.quantidade);
            $('#m-precoprodEdit').val(data.preco);

            // Abre o modal de edição
            openModalEditar();

            // Define o evento de clique no botão "Salvar"
            $('#btnSalvar').off('click');
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

// Função para salvar as alterações do produto
function salvarProdEditado(id) {
    // Obtém os valores do formulário, incluindo o checkbox
    var arquivo = $('#m-imagemProdPrevEdit').val(); 
    var nomeProduto = $('#m-nomeprodEdit').val();
    var descricao = $('#m-descricaoEdit').val();
    var quantidade = $('#m-quantidadeprodEdit').val();
    var preco = $('#m-precoprodEdit').val();
    var alterarImagem = $('#alterarImagemCheckbox').is(':checked'); // Verifica o checkbox

    var caminho;

    // Verifica se o checkbox "Alterar Imagem" está marcado
    if (alterarImagem && arquivo) {
        // Usuário quer enviar uma nova imagem
        var formato = arquivo.split('\\').pop();
        caminho = "..\\img\\" + formato;
    } else {
        // Mantém a imagem atual
        caminho = $('#m-imagemProdEdit').attr('src'); 
    }

    // Crie o objeto com os dados do produto, incluindo o 'caminho' da imagem
    var produtoAtualizado = {
        imagemPrincipal: caminho,
        nomeProduto: nomeProduto,
        descricao: descricao,
        quantidade: quantidade,
        preco: preco
    };

    $.ajax({
        url: 'http://localhost:8080/produtos/atualizaProdutoPorId/' + id,
        method: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(produtoAtualizado),
        success: function (data) {
            console.log('Produto atualizado:', data);
            alert('Produto atualizado com sucesso!');
            fecharModal();
            carregarProduto();
            // limparCampos(); // Se você estiver limpando os campos após a edição
        },
        error: function (xhr, status, error) {
            console.error('Erro ao atualizar o produto:', error);
        }
    });
}

// Função para limpar os campos do modal
function limparCampos() {
    $('#m-imagemProdPrevEdit').val('');
    $('#m-nomeprodEdit').val('');
    $('#m-descricaoEdit').val('');
    $('#m-quantidadeprodEdit').val('');
    $('#m-precoprodEdit').val('');
}

// Função para abrir o modal de cadastro
function openModal() {
    var modal = new bootstrap.Modal(document.getElementById('modalprod'));
    modal.show();
}

// Função para abrir o modal de edição
function openModalEditar() {
    var modal = new bootstrap.Modal(document.getElementById('modalEditar'));
    modal.show();
}

// Função para fechar o modal
function fecharModal() {
    $('#modalprod').modal('dispose');
    $('#modalEditar').modal('dispose');
}

// Função para atualizar a paginação
function updatePagination(totalPages) {
    const paginationElement = document.getElementById('pagination');
    paginationElement.innerHTML = ''; // Limpa elementos existentes

    if (totalPages > 1) {
        // Adiciona botão 'Anterior'
        if (currentPage > 0) {
            paginationElement.innerHTML += `<li class="page-item"><a href="#" class="page-link" data-page="${currentPage - 1}">‹</a></li>`;
        } else {
            paginationElement.innerHTML += `<li class="page-item disabled"><a href="#" class="page-link">‹</a></li>`;
        }

        // Adiciona números das páginas
        for (let page = 0; page < totalPages; page++) {
            if (page === currentPage) {
                paginationElement.innerHTML += `<li class="page-item active"><a href="#" class="page-link" data-page="${page}">${page + 1}</a></li>`;
            } else {
                paginationElement.innerHTML += `<li class="page-item"><a href="#" class="page-link" data-page="${page}">${page + 1}</a></li>`;
            }
        }

        // Adiciona botão 'Próximo'
        if (currentPage < totalPages - 1) {
            paginationElement.innerHTML += `<li class="page-item"><a href="#" class="page-link" data-page="${currentPage + 1}">›</a></li>`;
        } else {
            paginationElement.innerHTML += `<li class="page-item disabled"><a href="#" class="page-link">›</a></li>`;
        }
    }

    // Adiciona evento de clique para os links de paginação
    $('.page-link').click(function (event) {
        event.preventDefault(); // Impede o comportamento padrão do link
        const page = parseInt($(this).data('page'));
        currentPage = page;
        carregarProduto(currentPage);
    });
}

// Carrega os produtos da primeira página ao carregar a página
$(document).ready(function () {
    carregarProduto(currentPage);
});