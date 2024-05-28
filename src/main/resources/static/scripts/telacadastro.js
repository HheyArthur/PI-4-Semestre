document.getElementById('cadastroForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Impede o envio padrão do formulário

    // Pegue os valores dos campos
    const nome = document.getElementById('nome').value;
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    const confirmarSenha = document.getElementById('confirmarSenha').value;

    // Validação básica
    if (senha !== confirmarSenha) {
        alert('As senhas não coincidem!');
        return;
    }

    // Aqui você pode adicionar mais validações ou enviar os dados para o servidor
    // Por exemplo, usando fetch() para enviar os dados via POST
    // fetch('URL_DO_SEU_SERVIDOR', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json'
    //     },
    //     body: JSON.stringify({ nome, email, senha })
    // })
    // .then(response => response.json())
    // .then(data => {
    //     // Lógica após o cadastro com sucesso
    //     console.log('Success:', data);
    // })
    // .catch((error) => {
    //     console.error('Error:', error);
    // });

    // Exemplo de mensagem de confirmação
    alert('Cadastro realizado com sucesso!');
    // Opcional: resetar o formulário após o cadastro
    document.getElementById('cadastroForm').reset();
});