let alertaExibido = false; // Declaração global

// Função para realizar o login
function loginUser() {
    var email = document.getElementById("typeEmailX").value;
    var senha = document.getElementById("senha").value;
    var user = {
        email: email,
        senha: senha
    };

    // Busca as informações do usuário (após a validação)
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/usuarios/getUser/" + user.email,
        success: function (data) {
            let funcao = data.funcao;
            console.log(funcao);

            // Verifica a função do usuário e redireciona para a página correta
            if (funcao === "admin" || funcao === "administrador") {
                realizarLogin(user, funcao);
            } else if (funcao === "estoquista") {
                realizarLogin(user, funcao);
            }
        },
        error: function (xhr, status, erro) {
            if (!alertaExibido) {
                alert("Erro ao logar: " + xhr.responseText);
                alertaExibido = true; // Define alertaExibido como true para evitar o alerta duplo
            }
        }
    });
}

// Função para realizar o login
function realizarLogin(user, funcao) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/usuarios/login",
        data: JSON.stringify(user),
        contentType: "application/json",
        success: function (response) {
            // Salva as informações do usuário no localStorage
            localStorage.setItem("email", user.email);
            localStorage.setItem("funcao", funcao);

            // Redireciona para a página do backoffice
            window.location.href = "http://localhost:8080/HTML/backoffice.html";
        },
        error: function (xhr, status, erro) {
            if (!alertaExibido) {
                alert("Erro ao logar: " + xhr.responseText);
                alertaExibido = true; // Define alertaExibido como true para evitar o alerta duplo
            }
        }
    });
}

// Adiciona evento de clique no botão "Login"
document.getElementById("loginBtn").addEventListener("click", loginUser);