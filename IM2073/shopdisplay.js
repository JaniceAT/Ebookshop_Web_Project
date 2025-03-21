let totalPrice = 0;
const cartItems = document.getElementById('cart-items');
const cartTotal = document.getElementById('cart-total');

function updateQuantity(bookId, change, price) {
   var quantityInput = document.getElementById('quantity-' + bookId);
   var currentQuantity = parseInt(quantityInput.value);
   currentQuantity += change;
   if (currentQuantity < 0) currentQuantity = 0;
   quantityInput.value = currentQuantity;
   updateCartTotal(bookId, currentQuantity, price);
}

function updateCartTotal(bookId, quantity, price) {
   var bookTotal = quantity * price;
   var existingItem = cartItems.querySelector('[data-book-id="' + bookId + '"]');

   if (existingItem) {
      existingItem.querySelector('.cart-item-quantity').textContent = 'Quantity: ' + quantity;
      existingItem.querySelector('.cart-item-total').textContent = '$' + bookTotal.toFixed(2);
   } else {
      var li = document.createElement('li');
      li.setAttribute('data-book-id', bookId);
      li.innerHTML = '<span>Book ' + bookId + '</span><span class="cart-item-quantity">Quantity: ' + quantity + '</span><span class="cart-item-total">$' + bookTotal.toFixed(2) + '</span>';
      cartItems.appendChild(li);
   }

   recalculateTotalPrice();
}

function recalculateTotalPrice() {
   totalPrice = 0;
   var items = cartItems.querySelectorAll('li');
   items.forEach(function(item) {
      var itemTotal = parseFloat(item.querySelector('.cart-item-total').textContent.replace('$', ''));
      totalPrice += itemTotal;
   });
   cartTotal.textContent = 'Total: $' + totalPrice.toFixed(2);
}

function buyNow() {
   // Collect all items from the cart
   const items = [];
   const cartItemsList = cartItems.querySelectorAll('li');
   cartItemsList.forEach(item => {
      const bookId = item.getAttribute('data-book-id');
      const quantity = parseInt(item.querySelector('.cart-item-quantity').textContent.replace('Quantity: ', ''));
      const total = parseFloat(item.querySelector('.cart-item-total').textContent.replace('$', ''));
      
      items.push({ bookId, quantity, total });
   });

   // Send the order details to the servlet
   const orderData = {
      items: items, 
      finalTotalPrice: totalPrice
   };

   // Send the data to the server (e.g., to your /home endpoint)
   fetch('http://localhost:9999/IM2073/home', {
      method: 'POST',
      headers: {
         'Content-Type': 'application/json'
      },
      body: JSON.stringify(orderData)
   })
   .then(response => {
      console.log("Response status: ", response.status); // Log the response status code
      return response.text(); // Get the response as text
   })
   .then(data => {
      console.log(data);
      try {
         const jsonData = JSON.parse(data); // Try to parse the response as JSON
         console.log('Order placed successfully:', jsonData);
         alert('Your order has been placed!');
         // Optionally, you can redirect or reset the cart
         cartItems.innerHTML = ''; // Clear the cart
         cartTotal.textContent = 'Total: $0.00'; // Reset total
      } catch (error) { 
         console.error(' parsing JSON:', error); // Log the error if JSON parsing fails
         console.log(data)
         alert('There was an error placing your order. Response: ' + data); // Show the raw response
      }
   })
   .catch(error => {
      console.error('Error placing order:', error);
      alert('There was an error placing your order.');
   });

   window.location.href = 'thankyou.html'
}


function deleteBook(bookId) {
   if (confirm("Are you sure you want to delete this book?")) {
       // Send an AJAX POST request to the server with the bookId
       var xhr = new XMLHttpRequest();
       xhr.open("POST", "/IM2073/homeseller", true);  // Your servlet URL
       xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
       xhr.onreadystatechange = function () {
           if (xhr.readyState === 4 && xhr.status === 200) {
               alert(xhr.responseText);  // Display success or error message
               location.reload();  // Reload the page to reflect changes
           }
       };
       xhr.send("id=" + bookId);  // Send bookId as a POST parameter
   }
}
