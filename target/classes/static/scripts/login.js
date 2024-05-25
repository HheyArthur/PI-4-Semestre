let alertaExibido = false;

// Função para realizar o login
function loginUser() {
    var email = document.getElementById("typeEmailX").value;
    var senha = document.getElementById("senha").value;
    alertaExibido = false;
    var user = {
        email: email,
        senha: senha
    };

    // Validação dos campos
    if (email === "" || senha === "") {
        alert("Preencha todos os campos");
        return;
    }

    // Busca as informações do usuário
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
                alertaExibido = true;
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
                alertaExibido = true;
            }
        }
    });
}

document.getElementById("loginBtn").addEventListener("click", loginUser);