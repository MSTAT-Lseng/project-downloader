setInterval(function(){
    if ((typeof MacPlayer.Parse === 'undefined') || (typeof MacPlayer.PlayUrl === 'undefined')) {
    } else {
        window.location.href = MacPlayer.Parse + MacPlayer.PlayUrl;
    }
}, 500);
