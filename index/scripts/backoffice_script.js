$(document).ready(function () {
    
    $("#nomeUsuario").text(localStorage.getItem("email"));
    $("#funcaoUsuario").text(localStorage.getItem("funcao"));
    console.log(localStorage.getItem("funcao"));
    console.log(localStorage.getItem("email"));

    if (localStorage.getItem("funcao") === "admin" || localStorage.getItem("funcao") === "administrador"){
        document.querySelector("#pagProutos").setAttribute("href", "./CRUDproduto.html");
    } else {
        $(".adminSection").hide();
        document.querySelector("#pagProutos").setAttribute("href", "./CRUDprodutoEstoquista.html");
    }

});

function sair() {
    localStorage.clear();
    window.location.href = "http://127.0.0.1:5500/index/HTML/telalogin.html";
}