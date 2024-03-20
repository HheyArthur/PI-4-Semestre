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
                let produtoEndereco = produto.imagem.replace('../index', '');
                console.log(produtoEndereco);
                var newRow = $('<tr>');
                newRow.append('<td>' + produto.codigo + '</td>');
                newRow.append('<td><img class="photo-icon" src="' + produtoEndereco + '"></td>');
                newRow.append('<td>' + produto.nomeProduto + '</td>');
                newRow.append('<td>' + produto.quantidade + '</td>');
                newRow.append('<td>' + produto.preco + '</td>');
                newRow.append('<td class="acao ativo">' + (produto.ativo ? 'Ativo' : 'Inativo') + '</td>');
                newRow.append('<td class="acao"><button type="button" class="btn btn-primary" onclick="editarProduto(' + produto.id + ')">Editar</button></td>');
                $('tbody').append(newRow);
            });
        },
        error: function (xhr, status, error) {
            console.error('Erro ao carregar produtos:', error);
        }
    });
}

function editarProduto(id) {
    $.ajax({
        url: 'http://localhost:8080/produtos/atualizaProdutoPorId/' + id,
        method: 'GET',
        success: function(data) {
            // Aqui você pode adicionar o código para preencher o formulário de edição com os dados do produto
            console.log('Dados do produto:', data);
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

function fecharModal() {
    $('modalprod').hide();
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