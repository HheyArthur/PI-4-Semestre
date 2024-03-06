  function loginUser() {
    var email = document.getElementById("typeEmailX").value;
    var senha = document.getElementById("senha").value;

    var user = {
      email: email,
      senha: senha
    };

  
    $.ajax({
      type: "POST",
      url: "http://localhost:8080/usuarios/login",
      data: JSON.stringify(user),
      contentType: "application/json",
      success: function (response) {
        console.log("Login successful!" + response);
      },
      error: function (xhr, status, erro) {
        console.log("Erro: " + erro + " Status: " + status + " " + xhr.responseText);
      }
    });
  }


  function Openmodal() {
    document.getElementById("myPopup").style.display = "block";
  }


  function Closemodal() {
    document.getElementById("myPopup").style.display = "none";
  }


  document.getElementById("loginBtn").addEventListener("click", loginUser);