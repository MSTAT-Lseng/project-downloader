function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
    var introduce_info = data.substring(data.indexOf('<div class="des2"><b>剧情：</b>') + '<div class="des2"><b>剧情：</b>'.length);
    introduce_info = introduce_info.substring(0, introduce_info.indexOf('</div>'));
    return introduce_info;
}

function parserBrowseData() {

    var browse_data = "在线观看";

    var eplist_data = data.substring(data.indexOf('<ul class="urlli">') + '<ul class="urlli">'.length);
    eplist_data = eplist_data.substring(0, data.indexOf('<ul class="urlli">'));

    if (eplist_data.indexOf('<li style="width:100%;text-align:left">') > -1) {
        var eplist = eplist_data.split('<li style="width:100%;text-align:left">');
    } else {
        var eplist = eplist_data.split('<li>');
    }

    for (var i = 1; i < eplist.length - 2; i++) {
        var epinfo = eplist[i];

        var playlink = epinfo.substring(epinfo.indexOf('<a href="') + '<a href="'.length, epinfo.indexOf('">'));
        var playname = epinfo.substring(epinfo.indexOf('">') + '">'.length, epinfo.indexOf('</a>'));

        browse_data = browse_data + 
            "{bgd_name_tag}" +
            playname +
            "{bgd_link_tag}" +
            pathConvert_js(playlink);
    }
    browse_data = browse_data + '{bgd_item_split}{STR_DATA_CHECKED}';

    return browse_data;

}

function waitingWatchSupport() {
 return 'Dfys6';
}
