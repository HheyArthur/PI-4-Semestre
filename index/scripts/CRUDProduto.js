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
                var newRow = $('<tr>');
                newRow.append('<td>' + produto.codigo + '</td>');
                newRow.append('<td><img class="photo-icon" src="' + produto.imagem + '"></td>');
                newRow.append('<td>' + produto.nomeProduto + '</td>');
                newRow.append('<td>' + produto.quantidade + '</td>');
                newRow.append('<td>R$ ' + produto.preco + '</td>');
                newRow.append('<td class="acao ativo"><button type="button" class="btn btn-success" data-id="' + produto.id + '" onclick="ativarDesativar(' + produto.id + ')">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</button></td>');
                newRow.append('<td class="acao"><button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button></td>');
                $('tbody').append(newRow);
            });
        },
        error: function (xhr, status, error) {
            console.error('Erro ao carregar produtos:', error);
        }
    });
}

document.getElementById('pesquisaInput').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        pesquisarProduto();
    }
});


function ativarDesativar(id) {
    $.ajax({
        url: 'http://localhost:8080/produtos/ativarDesativarProduto/' + id,
        method: 'PUT',
        success: function(data) {
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
        error: function(xhr, status, error) {
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
        success: function(data) {
            if (data.length === 0) {
                $('tbody').empty();
                var mensagemErro = $('<tr>');
                messageRow.append('<td colspan="7" style="text-align: center; font-weight: bold;">Nenhum produto encontrado</td>');
                $('tbody').append(mensagemErro);
                return;
            }
            $('tbody').empty();
            data.forEach(function (produto) {
                var novaLinha = $('<tr>');
                novaLinha.append('<td>' + produto.codigo + '</td>');
                novaLinha.append('<td><img class="photo-icon" src="' + produto.imagem + '"></td>');
                novaLinha.append('<td>' + produto.nomeProduto + '</td>');
                novaLinha.append('<td>' + produto.quantidade + '</td>');
                novaLinha.append('<td>R$ ' + produto.preco + '</td>');
                novaLinha.append('<td class="acao ativo"><button type="button" class="btn btn-success">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</button></td>');
                novaLinha.append('<td class="acao"><button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button></td>');
                $('tbody').append(novaLinha);
            });
        },
        error: function(xhr, status, error) {
            console.error('Erro ao realizar a pesquisa:', error);
        }
    });
}

// forms dados
$('#produtoForm').submit(function (event) {
    event.preventDefault(); // Evitar o comportamento padrão do formulário

    var imagem = $('#m-fotoprod').val();
    var nomeProduto = $('#m-nomeprod').val();
    var quantidade = $('#m-quantidadeprod').val();
    var precoProd = $('#m-precoprod').val();

    // Monta o objeto de Produto
    var produto = {
        codigoProd: codigoprod,
        imagem: imagem,
        nomeProduto: nomeProduto,
        quantidade: quantidade,
        preco: precoProd,
    }
});

//TODO: Implementar a lógica para enviar o objeto 'produto' para o backend - Angelo 22:55 19/03/2024
function cadastrarProduto() {
    var nome = document.getElementById('m-nomeprodCad').value;
    var arquivo = document.getElementById('m-imagemProdPrev').value;
    var descricao = document.getElementById('m-descricaoProdCad').value;
    var quantidade = document.getElementById('m-quantidadeprodCad').value;
    var preco = document.getElementById('m-precoprodCad').value;
    var formato = arquivo.split('.').pop();
    var caminho = "..\\img\\" + nome + '.' + formato;

    $.ajax({
        url: 'http://localhost:8080/produtos',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            nome: nome,
            caminho: caminho
        }),
        success: function(data) {
            console.log('Produto cadastrado:', data);
        },
        error: function(xhr, status, error) {
            console.error('Erro ao cadastrar o produto:', error);
        }
    });
}


function editarProduto(id) {
    $.ajax({
        url: 'http://localhost:8080/produtos/' + id,
        method: 'GET',
        success: function(data) {
            // Preenche os campos do formulário com os dados do produto
            $('#m-imagemProdEdit').attr('src', data.imagem);
            $('#m-nomeprodEdit').val(data.nomeProduto);
            $('#m-quantidadeprodEdit').val(data.quantidade);
            $('#m-precoprodEdit').val(data.preco);
            
            // Abre o modal de edição
            openModalEditar();
        },
        error: function(xhr, status, error) {
            console.error('Erro ao buscar dados do produto:', error);
        }
    });
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
    $('modalprod').hide();
}

// footer
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

// Exemplo de uso:
// Suponha que totalItems seja a quantidade total de itens a serem paginados
const totalItems = 10; // Exemplo com 20 itens
updatePagination(totalItems);