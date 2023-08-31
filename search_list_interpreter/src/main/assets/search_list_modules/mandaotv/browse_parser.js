
function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
 var introduce = data.substring(data.indexOf('<div class="desdd"><b>简介：</b>') + '<div class="desdd"><b>简介：</b>'.length, data.indexOf('<a href="#quanjq">详细'));
 return introduce;
}

function parserBrowseData() {
 var browse_data = "";
 var channel_name = "资源链接";
 browse_data = browse_data + channel_name;

 var playlist = data.substring(data.indexOf('<ul class="urlli">'));
 playlist = playlist.substring(playlist.indexOf('<li>'), playlist.indexOf('</div>'));

 var playitem = playlist.split('<li>');
 for (var i = 1; i < playitem.length; i++) {
  var itemdata = playitem[i];
  var epname = itemdata.substring(itemdata.indexOf('target="_self">') + 'target="_self">'.length, itemdata.indexOf('</a>'));
  var eplink = itemdata.substring(itemdata.indexOf("' href='") + "' href='".length, itemdata.indexOf("' target=\""));
  browse_data = browse_data + 
    "{bgd_name_tag}" +
    epname +
    "{bgd_link_tag}" +
    pathConvert_js(eplink);
 }
 browse_data = browse_data + "{bgd_item_split}";
 
 return browse_data + '{STR_DATA_CHECKED}';
}

function waitingWatchSupport() {
 return '漫岛TV';
}