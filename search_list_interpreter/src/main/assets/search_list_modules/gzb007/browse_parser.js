function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
    var _introduce_info = data.substring(data.indexOf('<span class="data" style="display: none;">') + '<span class="data" style="display: none;">'.length);
    _introduce_info = _introduce_info.substring(0, _introduce_info.indexOf('</span>'));
    return _introduce_info;
}

function parserBrowseData() {
 var browse_data = "";
 var channel_list = data.split('<li><a href="#playlist');
 var playlist = data.split('" class="tab-pane fade in clearfix">');
 for (var i = 1; i < channel_list.length; i++) {
  var channel_data = channel_list[i];
  var channel_name = channel_data.substring(channel_data.indexOf('" data-toggle="tab">') + '" data-toggle="tab">'.length, channel_data.indexOf('</a>'));
  browse_data = browse_data + channel_name;
  var playeddata = playlist[i].split('<a class="btn btn-default"');
  
  for (var i2 = 1; i2 < playeddata.length; i2++) {
   var playl = playeddata[i2];
   var plink = playl.substring(playl.indexOf('href="') + 'href="'.length);
   plink = plink.substring(0, plink.indexOf('\"'));
   var pname = playl.substring(playl.indexOf('">') + '">'.length, playl.indexOf('</a>'));
   browse_data = browse_data + 
    "{bgd_name_tag}" +
    pname +
    "{bgd_link_tag}" +
    pathConvert_js(plink);
  }
  browse_data = browse_data + '{bgd_item_split}';
 }
 return browse_data + '{STR_DATA_CHECKED}';
}

function waitingWatchSupport() {
 return 'NONE';
}
