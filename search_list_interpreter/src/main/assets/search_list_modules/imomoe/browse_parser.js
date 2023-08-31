function getIntroductionInfo() {
  return "STRING_EXTRA";
}

function getIntroduceInfo() {
  var _introduce = data.substring(
    data.indexOf('<span class="data" style="display: none;">'),
    data.indexOf('<a class="details"')
  );
  document.write(_introduce);

  return document.getElementsByClassName("data")[0].innerText;
}

function parserBrowseData() {
  var _channel = data.substring(
    data.indexOf('<ul class="nav nav-tabs active">'),
    data.indexOf('<div class="tab-content myui-panel_bd">')
  );
  var channel = _channel.split("<li");

  var ___playlist = data.split('<div id="playlist');

  var browse_data = "";
  for (var i = 1; i < channel.length; i++) {
    var __playlist = ___playlist[i];

    // channel_name
    var channel_name = channel[i].substring(
      channel[i].indexOf('data-toggle="tab">') + 'data-toggle="tab">'.length,
      channel[i].indexOf("</a>")
    );
    browse_data = browse_data + channel_name;

    var _playlist = __playlist.split(
      '<li class="col-lg-10 col-md-8 col-sm-6 col-xs-4">'
    );
    for (var i2 = 1; i2 < _playlist.length; i2++) {
      var playlist = _playlist[i2];

      var name = playlist.substring(
        playlist.indexOf('">') + '">'.length,
        playlist.indexOf("</a>")
      );
      var link = pathConvert_js(
        playlist.substring(
          playlist.indexOf('href="') + 'href="'.length,
          playlist.indexOf('">')
        )
      );
      browse_data =
        browse_data + "{bgd_name_tag}" + name + "{bgd_link_tag}" + link;
    }

    browse_data = browse_data + "{bgd_item_split}";
  }

  return browse_data + "{STR_DATA_CHECKED}";
}

function waitingWatchSupport() {
  return "樱花动漫";
}
