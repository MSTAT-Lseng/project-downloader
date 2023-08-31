function getIntroductionInfo() {
    var _introduction = data.substring(data.indexOf('<div class="drama-data">'), data.indexOf('<div class="drama-bottom">'));
    document.write(_introduction);

    var introduction = document.getElementsByClassName('drama-data')[0].innerText;
    introduction = introduction.replace(/{bgd_br}/g, '');
    introduction = introduction.substring(2);
    return introduction;
}

function getIntroduceInfo() {
    var introtext = data.substring(data.lastIndexOf('<label class="intro">'));
    introtext = introtext.substring(introtext.indexOf('</label>'));
    introtext = introtext + '</label>';
    document.write(introtext);
    var info = document.getElementsByClassName('intro')[1].innerText;
    info = info.replace(/\n/g, '{bgd_br}');
    return info;
}

function parserBrowseData() {
 var browse_data = "";
 var channel_list = data.split('<div class="playbox playlist clearfix" id="');
 for (var i = 1; i < channel_list.length; i++) {
  var channel_data = channel_list[i];
  var channel_name = "喵播云";
  browse_data = browse_data + channel_name;
  var playeddata = channel_list[i].split('<li><a title=\'');
  
  for (var i2 = 1; i2 < playeddata.length; i2++) {
   var playl = playeddata[i2];
   var plink = playl.substring(playl.indexOf('href=\'') + 'href=\''.length);
   plink = plink.substring(0, plink.indexOf('\'>'));
   var pname = playl.substring(playl.indexOf('\'>') + '\'>'.length, playl.indexOf('</a>'));
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
 return 'OCOC';
}