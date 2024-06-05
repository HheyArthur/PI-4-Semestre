$(document).ready(function () {
    atualizarCarrinho(); 
});

document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');

    if (productId) {
        fetchProductDetails(productId);
    }
});

// Variáveis globais para armazenar o preço unitário e a imagem principal do produto
let productPrice = 0; 
let productImage = "";

function fetchProductDetails(id) {
    fetch(`http://localhost:8080/produtos/${id}`)
        .then(response => response.json())
        .then(product => {
            // Armazena o preço unitário e a imagem principal do produto
            productPrice = product.preco; 
            productImage = product.imagemPrincipal; 

            document.getElementById('currentImage').src = productImage;
            document.getElementById('currentImage').alt = product.nomeProduto;
            document.getElementById('productName').textContent = product.nomeProduto;
            updatePriceDisplay(1); 

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
    // Atualiza a imagem principal também para garantir consistência
    productImage = imageUrl;
}

function updatePriceDisplay(quantity) {
    const totalPrice = (productPrice * quantity).toFixed(2);
    document.getElementById('price').textContent = 'R$ ' + totalPrice; 
}

function decreaseQuantity() {
    let quantity = parseInt(document.getElementById('quantity').textContent);
    if (quantity > 1) {
        quantity -= 1;
        document.getElementById('quantity').textContent = quantity;
        updatePriceDisplay(quantity); 
    }
}

function increaseQuantity() {
    let quantity = parseInt(document.getElementById('quantity').textContent);
    quantity += 1;
    document.getElementById('quantity').textContent = quantity;
    updatePriceDisplay(quantity); 
}

