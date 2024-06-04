$('#cpf').on('input', function () {
    var cpf = $(this).val().replace(/\D/g, '');
    cpf = cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
    $(this).val(cpf);
});

// Evento de envio do formulário para adicionar cliente
$('#cadastroForm').submit(function (event) {
    event.preventDefault(); // Impede o comportamento padrão do formulário

    // Obtém os dados do formulário
    var nome = $('#nome').val();
    var email = $('#email').val();
    var cpf = $('#cpf').val();
    var senha = $('#senha').val();
    var confirmarSenha = $('#confirmarSenha').val();

    // Validação das senhas
    if (senha !== confirmarSenha) {
        alert("As senhas não coincidem.");
        return;
    }

 
    // Cria o objeto do cliente
    var cliente = {
        nome: nome,
        email: email,
        cpf: cpf,
        senha: senha,
        funcao: "cliente", // Define a função como 'cliente'
        ativo: true // Define como ativo
    };

    // Envia os dados do cliente para o backend
    $.ajax({
        url: 'http://localhost:8080/usuarios/cadastro', // URL do seu backend para cadastrar usuário
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(cliente),
        success: function (response) {
            alert("Cliente cadastrado com sucesso.");
            // Opcional: resetar o formulário após o cadastro
            $('#cadastroForm')[0].reset();
        },
        error: function (xhr, status, error) {
            console.error('Erro ao cadastrar cliente:', error, status, xhr.responseText);
            alert("Erro ao cadastrar cliente. Por favor, tente novamente.");
        }
    });
});


$('#m-cpf').on('input', function () {
    var cpf = $(this).val().replace(/\D/g, '');
    cpf = cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
    $(this).val(cpf);
});




// Carrega os clientes ao carregar a página (se necessário)
$(document).ready(function () {
    carregarClientes();
});

// Função para carregar clientes (se necessário)
function carregarClientes() {
    $.ajax({
        url: 'http://localhost:8080/usuarios/usuariosInfo', // URL do seu backend para obter usuários
        method: 'GET',
        success: function (data) {
            // Limpa a tabela
            $('tbody').empty();

            // Adiciona os clientes na tabela
            data.forEach(function (cliente) {
                if (cliente.funcao === "cliente") { // Filtra apenas os clientes
                    // Cria uma nova linha na tabela
                    var newRow = $('<tr>');

                    // Adiciona as colunas da linha
                    newRow.append('<td>' + cliente.nome + '</td>');
                    newRow.append('<td>' + cliente.email + '</td>');
                    newRow.append('<td class="acao ativo">' + (cliente.ativo ? 'Ativo' : 'Inativo') + '</td>');
                    newRow.append('<td class="acao editar" data-id="' + cliente.id + '">Editar</td>');

                    // Adiciona a linha na tabela
                    $('tbody').append(newRow);
                }
            });
        },
        error: function (xhr, status, error) {
            console.error('Erro ao carregar clientes:', error);
        }
    });
}
