setInterval(jumpVideo, 500);
function jumpVideo() {if (document.getElementsByTagName('iframe')[2] == null) {} else {window.location.href = document.getElementsByTagName('iframe')[2].src;}}