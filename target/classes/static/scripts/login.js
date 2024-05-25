let alertaExibido = false; // Declaração global

// Função para realizar o login
function loginUser() {
    var email = document.getElementById("typeEmailX").value;
    var senha = document.getElementById("senha").value;
    var user = {
        email: email,
        senha: senha
    };

    // Validação dos campos (antes da requisição AJAX)
    if (email === "" || senha === "") {
        alert("Preencha todos os campos");
        return; // Sai da função se os campos estiverem vazios
    }

    // Busca as informações do usuário e realiza o login em uma única requisição
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/usuarios/getUser/" + user.email,
        success: function (data) {
            let funcao = data.funcao;
            console.log(funcao);

            // Verifica a função do usuário e realiza o login se a função for válida
            if (funcao === "admin" || funcao === "administrador" || funcao === "estoquista") {
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
            } else {
                if (!alertaExibido) {
                    alert("Usuário não autorizado.");
                    alertaExibido = true; // Define alertaExibido como true para evitar o alerta duplo
                }
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
// Adiciona evento de clique no botão "Login"
document.getElementById("loginBtn").addEventListener("click", loginUser);