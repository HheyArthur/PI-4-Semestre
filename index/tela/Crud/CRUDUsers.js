$(document).ready(function () {

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

    // Adiciona o evento de clique no botão "Incluir"
    $('#new').click(function () {
        openModal();
    });

    // Adiciona o evento de clique no botão "Fechar" do modal
    $('.modal button[type="button"]').click(function () {
        fecharModal();
    });

    // Função para carregar os dados dos usuários
    function carregarUsuarios() {
        $.ajax({
            url: 'http://localhost:8080/usuarios/usuariosInfo', // Insira a URL do seu backend aqui
            method: 'GET',
            success: function (data) {
                $('tbody').empty();
                data.forEach(function (usuario) {
                    var newRow = $('<tr>');
                    newRow.append('<td>' + usuario.nome + '</td>');
                    newRow.append('<td>' + (usuario.cpf ? usuario.cpf : '') + '</td>');
                    newRow.append('<td>' + usuario.email + '</td>');
                    newRow.append('<td>' + (usuario.grupo ? usuario.grupo : '') + '</td>');
                    newRow.append('<td>' + (usuario.ativo ? 'Ativo' : 'Inativo') + '</td>');
                    newRow.append('<td class="acao editar" data-id="' + usuario.id + '">Editar</td>');
                    newRow.append('<td class="acao excluir" data-id="' + usuario.id + '">Excluir</td>');
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

    // Adiciona evento de envio de formulário para adicionar usuário
    $('#usuarioForm').submit(function (event) {
        event.preventDefault(); // Evitar o comportamento padrão do formulário

        var nome = $('#m-nome').val();
        var cpf = $('#m-cpf').val();
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
            grupo: grupo,
            ativo: true // Define como ativo
        };

        // Envia os dados do usuário para o backend
        $.ajax({
            url: 'http://localhost:8080/usuarios',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(usuario),
            success: function (response) {
                alert("Usuário cadastrado com sucesso.");
                carregarUsuarios(); // Atualiza a lista de usuários após o cadastro
                fecharModal(); // Fecha o modal após o cadastro
            },
            error: function (xhr, status, error) {
                console.error('Erro ao cadastrar usuário:', error);
                alert("Erro ao cadastrar usuário. Por favor, tente novamente.");
            }
        });
    });

    // Adiciona evento de clique nos botões de editar e excluir
})

$(document).on('click', '.excluir', function () {
    var idUsuario = $(this).data('id');
    if (idUsuario) {
        if (confirm("Tem certeza que deseja excluir este usuário?")) {
            $.ajax({
                url: 'http://localhost:8080/usuarios/' + idUsuario, // URL do endpoint para exclusão
                method: 'POST', // Método POST para exclusão
                contentType: 'application/json',
                data: JSON.stringify({ _method: 'DELETE' }), // Envia um parâmetro especial para indicar a exclusão
                success: function (response) {
                    alert("Usuário excluído com sucesso.");
                    carregarUsuarios(); // Atualiza a lista de usuários após a exclusão
                },
                error: function (xhr, status, error) {
                    console.error('Erro ao excluir usuário:', error);
                    alert("Erro ao excluir usuário. Por favor, tente novamente.");
                }
            });
        }
    } else {
        alert("ID do usuário não encontrado.");
    }
});


$(document).on('click', '.editar', function () {
    var idUsuario = $(this).data('id');
    if (idUsuario) {
        // Aqui você pode redirecionar o usuário para uma página de edição ou abrir um modal de edição
        // Se abrir um modal, pode carregar os dados do usuário através de uma requisição GET para um endpoint específico
        alert("Editar usuário com ID: " + idUsuario);
    } else {
        alert("ID do usuário não encontrado.");
    }
});

