$(document).ready(function () {
    // Carrega informações do usuário do localStorage
    $("#nomeUsuario").text(localStorage.getItem("email"));
    $("#funcaoUsuario").text(localStorage.getItem("funcao"));

    // Verifica a função do usuário e redireciona para a página correta
    if (localStorage.getItem("funcao") === "admin" || localStorage.getItem("funcao") === "administrador") {
        document.querySelector("#pagProutos").setAttribute("href", "./CRUDproduto.html");
    } else {
        $(".adminSection").hide();
        document.querySelector("#pagProutos").setAttribute("href", "./CRUDprodutoEstoquista.html");
    }
});

// Função para logout
function sair() {
    // Limpa o localStorage
    localStorage.clear();
    // Faz uma requisição AJAX para o endpoint de logout
    $.ajax({
        url: 'http://localhost:8080/usuarios/logout', // Endereço do seu endpoint de logout
        type: 'POST',
        success: function(data) {
            // Redireciona para a página de login após o logout
            window.location.href = "http://localhost:8080/HTML/telalogin.html";
        },
        error: function(error) {
            console.error('Erro ao realizar logout:', error);
        }
    });
}