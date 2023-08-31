function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
    var _introduce_info = data.substring(data.indexOf('<div class="col-pd text-collapse content">') + '<div class="col-pd text-collapse content">'.length);
    _introduce_info = _introduce_info.substring(0, _introduce_info.indexOf('</div>'));

    document.write('<div id="introduce_data">' + _introduce_info + '</div>');

    var introduce_info = document.getElementById('introduce_data').innerText;

    return introduce_info.substring(12);
}

function parserBrowseData() {
 var browse_data = "";

 var channel_list = data.split('<i class="fa fa fa-circle-o-notch">');

 var playlist = channel_list;

 for (var i = 1; i < channel_list.length; i++) {
  var channel_data = channel_list[i];

  var _channel_name = '<p id="cl_name_' + i + '">' + channel_data.substring(channel_data.indexOf('</i>'), channel_data.indexOf('</h3>')) + '</p>';
  document.write(_channel_name);
  var channel_name = document.getElementById('cl_name_' + i).innerText;
  
  browse_data = browse_data + channel_name;

  var playeddata = playlist[i].split('<li class="col-lg-8 col-md-7 col-sm-6 col-xs-4">');
  
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
 return 'Gimy 剧迷';
}
