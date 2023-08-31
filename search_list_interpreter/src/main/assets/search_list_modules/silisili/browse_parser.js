function getIntroductionInfo() {
  return "STRING_EXTRA";
}

function getIntroduceInfo() {
  var introduce = data.substring(0, data.indexOf('<div class="top-num top-1"><span><i>剧情</i>'));
  introduce = introduce.substring(introduce.lastIndexOf('</div>') + '</div>'.length);
  document.write('<div id="intro">' + introduce + '</div>');
  return document.getElementById('intro').innerText;
}

function parserBrowseData() {
  var resData = "{STR_DATA_CHECKED}";
  var browse_data = "";
  var tasked_data = data.substring(0, data.indexOf('<div class="entry-content clearfix ">'));

  var channel_list = tasked_data.split('<span class="nore text-muted pull-right">');
  for (var i = 1; i < channel_list.length; i++) {
    var channel_data = channel_list[i];

    // channel_name
    var channel_name = channel_data.substring(channel_data.indexOf('<div class="widget-title">') + '<div class="widget-title">'.length, channel_data.indexOf('</div>'));
    document.write('<div id="channel_name_' + i + '">' + channel_name + '</div>');
    var channel_name_tasked = document.getElementById('channel_name_' + i).innerText;
    channel_name_tasked = channel_name_tasked.replace(/{bgd_br} /g, '');
    if (channel_name.indexOf('下载') > -1) {} else {
      browse_data = browse_data + channel_name_tasked;

      // channel list
      var playlist = channel_data.split('<a href="');
      for (var i2 = 1; i2 < playlist.length; i2++) {
        var playinfo = playlist[i2];

        var playlink = pathConvert_js(playinfo.substring(0, playinfo.indexOf('"')));
        var playname = playinfo.substring(playinfo.indexOf('>') + '>'.length, playinfo.indexOf('</a>'));
        
        browse_data = browse_data +
          "{bgd_name_tag}" +
          playname +
          "{bgd_link_tag}" +
          playlink;
      }

      browse_data = browse_data + "{bgd_item_split}";
    }

  }

  browse_data = browse_data + resData;

  return browse_data;
}

function waitingWatchSupport() {
  return "嘶哩嘶哩";
}
