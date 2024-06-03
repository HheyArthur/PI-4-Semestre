$('#loginForm').submit(function (event) {
    event.preventDefault(); // Impede o comportamento padrão do formulário

    // Obtém os dados do formulário
    var email = $('#email').val();
    var senha = $('#senha').val();

    // Cria o objeto de login
    var loginData = {
        email: email,
        senha: senha
    };

    // Envia os dados de login para o backend
    $.ajax({
        url: 'http://localhost:8080/usuarios/login', // URL do seu backend para login
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(loginData),
        success: function (response) {
            alert("Login realizado com sucesso.");
            // Armazena o token de autenticação no localStorage (ou qualquer outro mecanismo)
            localStorage.setItem('authToken', response.token);
            // Redireciona para a página desejada após o login
            window.location.href = './telaloja.html';
        },
        error: function (xhr, status, error) {
            if (xhr.status === 401 || xhr.status === 403) {
                alert(xhr.responseText);
            } else {
                console.error('Erro ao realizar login:', error, status, xhr.responseText);
                alert("Erro ao realizar login. Por favor, tente novamente.");
            }
        }
    });
});
