
function carregarProduto() {
	$.ajax({
		url: 'http://localhost:8080/produtos/produtosInfo', // Insira a URL do seu backend aqui
		method: 'GET',
		success: function (data) {
			$('tbody').empty();
			data.forEach(function (Produto) {
				var newRow = $('<tr>');
				newRow.append('<td>' + produto.codigoprod + '</td>');
				newRow.append('<td>' + produto.fotoProd + '</td>');
                newRow.append('<td>' + produto.nomeProd + '</td>');
				newRow.append('<td>' + produto.quantidadeProd + '</td>');
				newRow.append('<td>' + produto.precoProd + '</td>');
				newRow.append('<td class="acao ativo">' + (produto.statusProd ? 'Ativo' : 'Inativo') + '</td>');


                // nao sei oq fazer aqui para adicionar o botao de excluir e editar
				newRow.append('<td class="acao editar" data-id="' + produto.id + '">Editar</td>');
				newRow.append('<td class="acao excluir" data-id="' + produto.id + '">Excluir</td>');
				$('tbody').append(newRow);
			});
		},
		error: function (xhr, status, error) {
			console.error('Erro ao carregar produtos:', error);
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
$('#prodForm').submit(function (event) {
    event.preventDefault(); // Evitar o comportamento padrão do formulário

    var codigoProd = $('#m-codigoprod').val();
    var fotoProd = $('#m-fotoprod').val();
    var nomeProd = $('#m-nomeprod').val();
    var quantidadeProd = $('#m-quantidadeprod').val();
    var precoProd = $('#m-precoprod').val();

    // Monta o objeto de Produto
    var produto = {
        codigoProd: codigoprod,
        fotoProd: fotoprod,
         nomeProd: nomeprod,
        quantidadeProd:  quantidadeprod,
        precoProd:  precoprod,
        statusProd: true
    }});
    

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


