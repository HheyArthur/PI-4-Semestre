function carregarUsuarios() {
	$.ajax({
		url: 'http://localhost:8080/usuarios/usuariosInfo', // Insira a URL do seu backend aqui
		method: 'GET',
		success: function (data) {
			$('tbody').empty();
			data.forEach(function (usuario) {
				var newRow = $('<tr>');
				newRow.append('<td>' + usuario.nome + '</td>');
				newRow.append('<td>' + usuario.email + '</td>');
				newRow.append('<td>' + (usuario.funcao ? usuario.funcao : '') + '</td>');
				newRow.append('<td class="acao ativo">' + (usuario.ativo ? 'Ativo' : 'Inativo') + '</td>');
				newRow.append('<td class="acao editar" data-id="' + usuario.id + '">Editar</td>');
				$('tbody').append(newRow);
			});
		},
		error: function (xhr, status, error) {
			console.error('Erro ao carregar usuários:', error);
		}
	});
}

// Função para abrir o modal
function openModal() {
	$('.modal-container').show();
	var modal = $('.modal');
	var windowHeight = $(window).height();
	var modalHeight = modal.outerHeight();
	var marginTop = (windowHeight - modalHeight) / 2;
	modal.css('margin-top', marginTop);
}


// Função para fechar o modal
function fecharModal() {
	$('.modal-container').hide();
}
$(document).ready(function () {

	// Adiciona o evento de clique no botão "Incluir"
	$('#new').click(function () {
		openModal();
	});

	// Adiciona o evento de clique no botão "Fechar" do modal
	$('.modal button[type="button"]').click(function () {
		fecharModal();
	});

	// Função para carregar os dados dos usuários


	// Chama a função para carregar os usuários ao carregar a página
	carregarUsuarios();

	// Adiciona evento de envio de formulário para adicionar usuário
	$('#usuarioForm').submit(function (event) {
		event.preventDefault(); // Evitar o comportamento padrão do formulário

		var nome = $('#m-nome').val();
		var cpf = $('#m-cpf').val().replace(/\./g, '');
		cpf = parseInt(cpf);
		var email = $('#m-email').val();
		var senha = $('#m-senha').val();
		var confirmarSenha = $('#m-confirmar-senha').val();
		var grupo = $('#m-grupo').val();

		// Verifica se as senhas são iguais
		if (senha !== confirmarSenha) {
			alert("As senhas não coincidem.");
			return;
		}

		// Monta o objeto de usuário
		var usuario = {
			nome: nome,
			cpf: cpf,
			email: email,
			senha: senha,
			funcao: grupo,
			ativo: true // Define como ativo
		};

		// Envia os dados do usuário para o backend
		$.ajax({
			url: 'http://localhost:8080/usuarios/cadastro',
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(usuario),
			success: function (response) {
				alert("Usuário cadastrado com sucesso.");
				carregarUsuarios(); // Atualiza a lista de usuários após o cadastro
				fecharModal(); // Fecha o modal após o cadastro
			},
			error: function (xhr, status, error) {
				console.error('Erro ao cadastrar usuário:', error, status, xhr.responseText);
				alert("Erro ao cadastrar usuário. Por favor, tente novamente.");
			}
		});
	});

	// Adiciona evento de clique nos botões de editar e excluir
})

$(document).on('click', '.excluir', function () {
	var email = $(this).closest('tr').find('td:nth-child(2)').text();
	console.log(email);
	if (email) {
		if (confirm("Tem certeza que deseja excluir este usuário?")) {
			$.ajax({
				url: 'http://localhost:8080/usuarios/deleteUser/' + email, // URL do endpoint para exclusão por email
				method: 'DELETE',
				contentType: 'application/json',
				data: JSON.stringify({ email: email }), // Envia o email do usuário para exclusão
				success: function (response) {
					alert("Usuário excluído com sucesso.");
					carregarUsuarios(); // Atualiza a lista de usuários após a exclusão
				},
				error: function (xhr, status, error) {
					console.error('Erro ao excluir usuário:', error, status, xhr.responseText);
					alert("Erro ao excluir usuário. Por favor, tente novamente.");
				}
			});
		}
	} else {
		alert("Email do usuário não encontrado.");
	}
});


$(document).on('click', '.editar', function () {
	var email = $(this).closest('tr').find('td:nth-child(2)').text();
	console.log(email);
	if (email) {
		$.ajax({
			url: 'http://localhost:8080/usuarios/getUser/' + email, // URL do endpoint para obter usuário por email
			method: 'GET',
			success: function (data) {
				$('#m-nome').val(data.nome);
				$('#m-cpf').val(data.cpf.toString().replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4"));
				$('#m-grupo').val(data.funcao);
				// Abre o modal e preenche os campos com os dados do usuário
				openModal();
				$('#m-email').hide();
				$('#m-email').removeAttr('required');
				$('#m-email').prev('label').hide();
			},
			error: function (error) {
				console.error('Erro ao obter dados do usuário:', error);
				alert("Erro ao obter dados do usuário. Por favor, tente novamente.");
			}
		});
	} else {
		alert("Email do usuário não encontrado.");
	}

	$('#usuarioForm').submit(function (event) {
		event.preventDefault(); // Evitar o comportamento padrão do formulário

		var nome = $('#m-nome').val();
		var cpf = $('#m-cpf').val().replace(/\./g, '');
		cpf = parseInt(cpf);
		var senha = $('#m-senha').val();
		var confirmarSenha = $('#m-confirmar-senha').val();
		var funcao = $('#m-grupo').val();

		// Verifica se as senhas são iguais
		if (senha !== confirmarSenha) {
			alert("As senhas não coincidem.");
			return;
		}

		// Monta o objeto de usuário
		var usuario = {
			nome: nome,
			cpf: cpf,
			senha: senha,
			funcao: funcao,
			ativo: true // Define como ativo
		};

		// Envia os dados do usuário para o backend
		$.ajax({
			url: 'http://localhost:8080/usuarios/updateUser/' + email,
			method: 'PUT',
			contentType: 'application/json',
			data: JSON.stringify(usuario),
			success: function (response) {
				alert("Usuário atualizado com sucesso.");
				carregarUsuarios(); // Atualiza a lista de usuários após a atualização
			},
			error: function (xhr, status, error) {
				console.error('Erro ao atualizar usuário:', error, status, xhr.responseText);
				alert("Erro ao atualizar usuário. Por favor, tente novamente.");
			}
		});
	});
});

$(document).on('click', '.ativo', function () {
    var email = $(this).closest('tr').find('td:nth-child(2)').text();
    console.log(email);
    if (email) {
        $.ajax({
            url: 'http://localhost:8080/usuarios/desativarAtivarUsuario/' + email, // URL do endpoint para ativar/desativar usuário
            method: 'PUT',
            success: function (data) {
                alert("Status do usuário atualizado com sucesso.");
                carregarUsuarios(); // Atualiza a lista de usuários após a atualização
            },
            error: function (error) {
                console.error('Erro ao atualizar status do usuário:', error);
                alert("Erro ao atualizar status do usuário. Por favor, tente novamente.");
            }
        });
    } else {
        alert("Email do usuário não encontrado.");
    }
});

