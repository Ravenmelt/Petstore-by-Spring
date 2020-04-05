var xmlHttpRequest;
function updateCart() {
    var quantity = document.getElementById("quantity").value;
    alert(quantity);
    sendRequest("updateCartJSServlet?quantity="+ quantity);
}

function sendRequest(url) {
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open("GET", url, true);
    xmlHttpRequest.onreadystatechange = processResponse;
    xmlHttpRequest.send(null);
}

function processResponse() {
    if (xmlHttpRequest.readyState == 4) {
        if (xmlHttpRequest.status == 200) {
            var resp = xmlHttpRequest.responseText;
            var array = resp.split(",");
            var quantity = document.getElementById("quantity");
            var total = document.getElementById("total");
            var subtotal = document.getElementById("subtotal");
            quantity.innerText = array[0];
            total.innerText = array[1];
            subtotal.innerText = array[2];
        }
    }
}
