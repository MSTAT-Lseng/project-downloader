function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
 var introduce = data.substring(data.indexOf('<div class="info-descr">') + '<div class="info-descr">'.length);
 introduce = introduce.substring(0, introduce.indexOf('</div>'));
 return introduce + '...';
}

function parserBrowseData() {
 var browse_data = "";

 var channel_list = data.substring(data.indexOf('<div class="tag-slider">') + '<div class="tag-slider">'.length);
 channel_list = channel_list.substring(0, channel_list.indexOf('<div class="slider-controller"'));

 var playlist_list = data.substring(data.indexOf('<div class="episode-wrap">') + '<div class="episode-wrap">'.length);
 playlist_list = playlist_list.substring(0, playlist_list.indexOf('<div class="wrap center"'));
 var playlist = playlist_list.split('<ul class="episode clearfix"');
 
 var channel = channel_list.split('<ul class="slider-list"');
 for (var i = 1; i < channel.length; i++) {
  var channel_data =  channel[i];
  var channel_name = channel_data.substring(channel_data.indexOf('padding-left:  30px;">') + 'padding-left:  30px;">'.length, channel_data.indexOf('</span>'));
  channel_name = channel_name.replace(/&nbsp;/g, ' ');
  console.log(channel_name);
  browse_data = browse_data + channel_name;

  var playlist_data = playlist[i];
  var plist = playlist_data.split('<li>');
  for (var i2 = 1; i2 < plist.length; i2++) {
   var plist_data = plist[i2];
   var ep_name = plist_data.substring(plist_data.indexOf('<span class="title">') + '<span class="title">'.length, plist_data.indexOf('</span>'));
   var ep_link = plist_data.substring(plist_data.indexOf('<a class="" href="') + '<a class="" href="'.length, plist_data.indexOf('" >'));

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
 return 'ZzzFun';
}
