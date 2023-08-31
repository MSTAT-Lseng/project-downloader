function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
    var introduce = data.substring(data.indexOf('<div class="info">') + '<div class="info">'.length);
    introduce = introduce.substring(0, introduce.indexOf('</div>'));
    introduce = introduce.substring(8);
    return introduce;
}

function parserBrowseData() {
    var browse_data = "";

    var list_data = data.substring(data.indexOf('">播放列表</li>'), data.indexOf('<div class="blank">'))
    var playlist = list_data.split('<a href="');

    var channel_name = "播放列表";
    browse_data = browse_data + channel_name;

    for (var i2 = 1; i2 < playlist.length; i2++) {
        var playl = playlist[i2];

        var plink = playl.substring(0, playl.indexOf('\"'));

        var pname = playl.substring(playl.indexOf('">') + '">'.length, playl.indexOf('</a>'));

        browse_data = browse_data + 
            "{bgd_name_tag}" +
            pname +
            "{bgd_link_tag}" +
            pathConvert_js(plink);
    }
    browse_data = browse_data + '{bgd_item_split}';

    return browse_data + '{STR_DATA_CHECKED}';
}

function waitingWatchSupport() {
 return '樱花 CD';
}
