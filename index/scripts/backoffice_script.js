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
    localStorage.clear();
    window.location.href = "http://127.0.0.1:5500/index/HTML/telalogin.html";
}