let alertaExibido = false;

function loginUser() {
  var email = document.getElementById("typeEmailX").value;
  var senha = document.getElementById("senha").value;
  alertaExibido = false;
  var user = {
    email: email,
    senha: senha
  };

  if (email === "" || senha === "") {
    alert("Preencha todos os campos");
    return;
  }

  $.ajax({
    type: "GET",
    url: "http://localhost:8080/usuarios/getUser/" + user.email,
    success: function (data) {
      let funcao = data.funcao;
      console.log(funcao);
      if (funcao === "admin" || funcao === "administrador") {
        $.ajax({
          type: "POST",
          url: "http://localhost:8080/usuarios/login",
          data: JSON.stringify(user),
          contentType: "application/json",
          success: function (response) {
            localStorage.setItem("email", email);
            localStorage.setItem("funcao", funcao);
            window.location.href = "http://127.0.0.1:5500/index/HTML/backoffice.html"
          },
          error: function (xhr, status, erro) {
            alert("Erro ao logar: " + xhr.responseText);
            if (!alertaExibido) { // Passo 2
              alert("Erro ao logar: " + xhr.responseText);
              alertaExibido = true; // Passo 3
            }
          }
        });
      } else if (funcao == "estoquista") {
        $.ajax({
          type: "POST",
          url: "http://localhost:8080/usuarios/login",
          data: JSON.stringify(user),
          contentType: "application/json",
          success: function (response) {
            localStorage.setItem("email", email);
            localStorage.setItem("funcao", funcao);
            window.location.href = "http://127.0.0.1:5500/index/HTML/backoffice.html"
          },
          error: function (xhr, status, erro) {
            if (!alertaExibido) { // Passo 2
              alert("Erro ao logar: " + xhr.responseText);
              alertaExibido = true; // Passo 3
            }
          }
        });
      }
    },
    error: function (xhr, status, erro) {
      if (!alertaExibido) { // Passo 2
        alert("Erro ao logar: " + xhr.responseText);
        alertaExibido = true; // Passo 3
      }
    }
  });
}


function Openmodal() {
  document.getElementById("myPopup").style.display = "block";
}

function OpenmodalEstoquista() {
  document.getElementById("myPopupEstoquista").style.display = "block";
}

function Closemodal() {
  document.getElementById("myPopup").style.display = "none";
  document.getElementById("myPopupEstoquista").style.display = "none";
}


document.getElementById("loginBtn").addEventListener("click", loginUser);