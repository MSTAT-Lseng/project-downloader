setInterval(jumpVideo, 500);
function jumpVideo() {if (document.getElementById('playiframe') == null) {} else {window.location.href = document.getElementById('playiframe').src;}}
