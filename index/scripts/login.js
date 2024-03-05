$(document).ready(function(){
    // Show registration modal when register link is clicked
    $('#registerLink').click(function(){
      $('#registrationModal').modal('show');
    });
  
    // Handle form submission
    $('#loginForm').submit(function(e){
      e.preventDefault();
      var username = $('#username').val();
      var password = $('#password').val();
  
      // Make AJAX request to backend
      $.ajax({
        type: 'POST',
        url: '/login', // Coloque o URL correto do seu endpoint de login
        contentType: 'application/json',
        data: JSON.stringify({ username: username, password: password }),
        success: function(response) {
          // Handle successful authentication
          alert('Login Successful!');
        },
        error: function(xhr, status, error) {
          // Handle authentication error
          alert('Login Failed! Please check your credentials.');
        }
      });
    });
  });
  