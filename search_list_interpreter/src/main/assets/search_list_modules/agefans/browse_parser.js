function getIntroductionInfo() {
    return "STRING_EXTRA";
}

function getIntroduceInfo() {
    var desc = data.substring(data.indexOf('<div class="video_detail_desc py-2">') + '<div class="video_detail_desc py-2">'.length);
    desc = desc.substring(0, desc.indexOf('</div>'));
    return desc;
}

function parserBrowseData() {
    var browse_data = '';
    
    var _channel = data.split('<li class="nav-item"><button class="nav-link btn-sm');
    var __playlist = data.split('<div class="tab-pane fade');
    
    for (var i = 1; i < _channel.length; i++) {
        var channel = _channel[i];
        var channel_name = channel.substring(channel.indexOf('">') + '">'.length, channel.indexOf('</button>'));
        channel_name = channel_name.replace('<span class="badge bg-danger">VIP</span>', '');
        
        browse_data = browse_data + channel_name;
        
        var _playlist = __playlist[i].split('<li><a href="');
        for (var i2 = 1; i2 < _playlist.length; i2++) {
            var playlist = _playlist[i2];

            var name = playlist.substring(playlist.indexOf('">') + '">'.length, playlist.indexOf('</a>'));
            var link = pathConvert_js(playlist.substring(0, playlist.indexOf('" class="')));
            browse_data = browse_data + '{bgd_name_tag}' + name + '{bgd_link_tag}' + link;
        }

        browse_data = browse_data + '{bgd_item_split}';
    }

    return browse_data + '{STR_DATA_CHECKED}';
}

function waitingWatchSupport() {
    return 'AGE 动漫';
}
