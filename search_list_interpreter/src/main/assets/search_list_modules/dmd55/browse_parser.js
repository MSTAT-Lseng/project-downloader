function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
 var introduce = data.substring(data.indexOf('<div class="des">') + '<div class="des">'.length);
 introduce = introduce.substring(0, introduce.indexOf('</div>'));
 return introduce;
}

function parserBrowseData() {
 var browse_data = "";

 var channel_list = data.substring(data.indexOf('<div class="tabs from-tabs">') + '<div class="tabs from-tabs">'.length);
 channel_list = channel_list.substring(0, channel_list.indexOf('</div>'));

 var playlist_list = data.substring(data.indexOf('<div class="box_bor3">') + '<div class="box_bor3">'.length);
 playlist_list = playlist_list.substring(0, playlist_list.indexOf('<div class="list0"></div>'));
 var playlist = playlist_list.split('<div class="tabs-list');
 
 var channel = channel_list.split('<label ');
 for (var i = 1; i < channel.length; i++) {
  var channel_data =  channel[i];
  var channel_name = channel_data.substring(channel_data.indexOf('>') + '>'.length, channel_data.indexOf('<'));
  browse_data = browse_data + channel_name;

  var playlist_data = playlist[i];
  var plist = playlist_data.split('<li>');
  for (var i2 = 1; i2 < plist.length; i2++) {
   var plist_data = plist[i2];
   var ep_name = plist_data.substring(plist_data.indexOf('target="_blank">') + 'target="_blank">'.length, plist_data.indexOf('</a>'));
   var ep_link = plist_data.substring(plist_data.indexOf('<a href="') + '<a href="'.length, plist_data.indexOf('" target="_blank">'));

   browse_data = browse_data + 
    "{bgd_name_tag}" +
    ep_name +
    "{bgd_link_tag}" +
    pathConvert_js(ep_link);
  }
  browse_data = browse_data + '{bgd_item_split}';
 }
 
 return browse_data + '{STR_DATA_CHECKED}';
}

function waitingWatchSupport() {
 return '动漫岛';
}
