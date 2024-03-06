$(document).ready(function () {

	// Função para abrir o modal
	function openModal() {
		// Mostra a modal container
		$('.modal-container').show();
		// Centraliza o modal na tela
		var modal = $('.modal');
		var windowHeight = $(window).height();
		var modalHeight = modal.outerHeight();
		var marginTop = (windowHeight - modalHeight) / 2;
		modal.css('margin-top', marginTop);
	}

	// Adiciona o evento de clique no botão "Incluir"
	$('#new').click(function () {
		openModal();
	});

	// Função para carregar os dados dos usuários
	function carregarUsuarios() {
		$.ajax({
			url: 'http://localhost:8080/usuarios/usuariosInfo', // Insira a URL do seu backend aqui
			method: 'GET',
			success: function (data) {
				// Limpa o corpo da tabela
				$('tbody').empty();
				// Itera sobre os dados recebidos e adiciona cada usuário à tabela
				data.forEach(function (usuario) {
					var newRow = $('<tr>');
					newRow.append('<td>' + usuario.nome + '</td>');
					newRow.append('<td>' + usuario.email + '</td>');
					newRow.append('<td>' + usuario.funcao.charAt(0).toUpperCase() + usuario.funcao.slice(1) + '</td>');
					newRow.append('<td>' + (usuario.ativo ? 'Ativo' : 'Inativo') + '</td>');
					newRow.append('<td class="acao">Editar</td>');
					newRow.append('<td class="acao">Excluir</td>');
					$('tbody').append(newRow);
				});
			},
			error: function (xhr, status, error) {
				console.error('Erro ao carregar usuários:', error);
			}
		});
	}

	// Chama a função para carregar os usuários ao carregar a página
	carregarUsuarios();

	// Aqui você pode adicionar mais funcionalidades, como adicionar um novo usuário,
	// editar ou excluir um usuário, utilizando AJAX para se comunicar com o backend.


});