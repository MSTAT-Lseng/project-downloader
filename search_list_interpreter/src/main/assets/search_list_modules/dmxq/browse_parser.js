function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
 var infotext = data.substring(data.indexOf('<div class="module-info-introduction-content show-desc"'), data.indexOf('<div style="margin-top: -15px;display: flex;margin-bottom: 10px;'));
 infotext = infotext.replace(/<\/div>/g, '');
 document.write(infotext);
 var info = document.getElementsByClassName('show-desc')[0].innerText;
 info = info.substring(10, info.length - 26);
 info = info.replace(/\n/g, '{bgd_br}');
 return info.substring(0, info.length - 16);
}

function parserBrowseData() {
 var browse_data = "";
 var channel_list = data.split('<div class="module-tab-item tab-item" data-dropdown-value="');
 var playlist = data.split('<div class="module-list sort-list tab-list his-tab-list" id="panel1">');
 for (var i = 1; i < channel_list.length; i++) {
  var channel_data = channel_list[i];
  var channel_name = channel_data.substring(0, channel_data.indexOf('\"'));
  browse_data = browse_data + channel_name;
  var playeddata = playlist[i].split('<a  class="module-play-list-link"');
  
  for (var i2 = 1; i2 < playeddata.length; i2++) {
   var playl = playeddata[i2];
   var plink = playl.substring(playl.indexOf('href="') + 'href="'.length);
   plink = plink.substring(0, plink.indexOf('\"'));
   var pname = playl.substring(playl.indexOf('<span>') + '<span>'.length, playl.indexOf('</span>'));
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