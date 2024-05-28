document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');

    if (productId) {
        fetchProductDetails(productId);
    }
});

let productPrice = 0; // Variável global para armazenar o preço unitário do produto

function fetchProductDetails(id) {
    fetch(`http://localhost:8080/produtos/${id}`)
        .then(response => response.json())
        .then(product => {
            productPrice = product.preco; // Armazena o preço unitário do produto

            document.getElementById('currentImage').src = product.imagemPrincipal;
            document.getElementById('currentImage').alt = product.nomeProduto;
            document.getElementById('productName').textContent = product.nomeProduto;
            updatePriceDisplay(1); // Atualiza o preço inicial exibido

            const thumbnailContainer = document.getElementById('thumbnailImages');
            thumbnailContainer.innerHTML = '';

            product.imagens.forEach((imagem, index) => {
                const thumbnail = document.createElement('img');
                thumbnail.src = imagem.url;
                thumbnail.alt = `Thumbnail ${index + 1}`;
                thumbnail.onclick = function () {
                    changeImage(imagem.url);
                };
                thumbnailContainer.appendChild(thumbnail);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar detalhes do produto:', error);
        });
}

function changeImage(imageUrl) {
    document.getElementById('currentImage').src = imageUrl;
}

function updatePriceDisplay(quantity) {
    const totalPrice = (productPrice * quantity).toFixed(2);
    document.getElementById('price').textContent = 'R$ ' + totalPrice; // Atualiza o valor exibido
}

function decreaseQuantity() {
    let quantity = parseInt(document.getElementById('quantity').textContent);
    if (quantity > 1) {
        quantity -= 1;
        document.getElementById('quantity').textContent = quantity;
        updatePriceDisplay(quantity); // Atualiza o valor exibido
    }
}

function increaseQuantity() {
    let quantity = parseInt(document.getElementById('quantity').textContent);
    quantity += 1;
    document.getElementById('quantity').textContent = quantity;
    updatePriceDisplay(quantity); // Atualiza o valor exibido
}
