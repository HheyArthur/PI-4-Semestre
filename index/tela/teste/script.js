// Dados de usuários (simulados)
let users = [
    { id: 1, nome: "Admin", email: "admin@example.com", status: "ativo", grupo: "admin" },
    { id: 2, nome: "Usuário 1", email: "user1@example.com", status: "ativo", grupo: "estoquista" },
    { id: 3, nome: "Usuário 2", email: "user2@example.com", status: "ativo", grupo: "estoquista" },
    { id: 4, nome: "Usuário 3", email: "user3@example.com", status: "ativo", grupo: "cliente" }
];

// Função para renderizar a lista de usuários na tabela
function renderUserList() {
    const userList = document.getElementById('userList');
    userList.innerHTML = '';

    users.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${user.nome}</td>
            <td>${user.email}</td>
            <td>${user.status}</td>
            <td>${user.grupo}</td>
            <td>
                <button class="editBtn" data-id="${user.id}">Editar</button>
                <button class="deleteBtn" data-id="${user.id}">Excluir</button>
            </td>
        `;
        userList.appendChild(row);
    });

    // Adiciona event listeners para os botões de editar e excluir
    document.querySelectorAll('.editBtn').forEach(btn => {
        btn.addEventListener('click', editUser);
    });

    document.querySelectorAll('.deleteBtn').forEach(btn => {
        btn.addEventListener('click', deleteUser);
    });
}

// Função para abrir modal de cadastro/edição de usuário
function openModal() {
    document.getElementById('modalSection').classList.remove('hidden');
    document.getElementById('modalContent').innerHTML = `
        <!-- Formulário de cadastro/edição de usuário -->
    `;
}

// Função para fechar modal
function closeModal() {
    document.getElementById('modalSection').classList.add('hidden');
}

// Função para editar usuário
function editUser(event) {
    const userId = parseInt(event.target.dataset.id);
    const user = users.find(u => u.id === userId);

    // Preenche o formulário de edição com os dados do usuário selecionado
    document.getElementById('modalContent').innerHTML = `
        <!-- Formulário de edição de usuário preenchido com os dados -->
    `;
    document.getElementById('modalSection').classList.remove('hidden');

    // Adiciona event listener para o botão de salvar edição
    document.getElementById('saveBtn').addEventListener('click', function () {
        // Lógica para salvar as alterações do usuário
    });
}

// Função para excluir usuário
function deleteUser(event) {
    const userId = parseInt(event.target.dataset.id);
    const userIndex = users.findIndex(u => u.id === userId);

    // Remove o usuário do array
    users.splice(userIndex, 1);
    renderUserList();
}

// Inicialização da tabela de usuários
renderUserList();
