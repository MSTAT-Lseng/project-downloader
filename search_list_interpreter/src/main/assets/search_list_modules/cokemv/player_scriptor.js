_bgd_function(function(){
    document.getElementsByClassName('sidebar')[0].style.display = 'none';
    setInterval("document.getElementsByTagName('ins')[0].style.display = 'none'", 1000); // block advertisement.
    document.getElementsByClassName('module-player-side')[0].style.display = 'none';
    document.getElementsByClassName('module')[1].style.display = 'none';
    document.getElementsByClassName('module')[2].style.display = 'none';
    document.getElementsByClassName('footer')[0].style.display = 'none';
    document.getElementsByClassName('fixedGroup')[0].style.display = 'none';
});
