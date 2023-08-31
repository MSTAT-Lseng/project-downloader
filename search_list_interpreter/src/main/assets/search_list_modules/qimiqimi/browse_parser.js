function getIntroductionInfo() {
 return 'STRING_EXTRA';
}

function getIntroduceInfo() {
 var det = data.substring(data.indexOf('<dl class="juqing">'), data.indexOf('<!--在线播放地址-->'));
 document.write(det);
 var juqing = document.getElementsByClassName('juqing')[0].innerText;
 juqing = juqing.substring(4, juqing.length - 4);
 return juqing;
}

function parserBrowseData() {
 var resdata = '';
 var jqbf = data.substring(data.indexOf('<!--在线播放地址-->'), data.indexOf('<!--迅雷下载地址-->'));
 document.write(jqbf);
 var cnlist = document.getElementsByClassName('ui-box marg');
 for (var i = 0; i < cnlist.length; i++) {
  var cnname = cnlist[i].getElementsByTagName('h2')[0].innerText;
  resdata = resdata + cnname;
  var plname = cnlist[i].getElementsByClassName('video_list fn-clear')[0].getElementsByTagName('a');
  for (var i2 = 0; i2 < plname.length; i2++) {
   var name = plname[i2].innerText;
   var link = plname[i2].getAttribute('href');
   resdata = resdata + "{bgd_name_tag}" +
        name +
        "{bgd_link_tag}" +
        pathConvert_js(link);
  }
  resdata = resdata + '{bgd_item_split}';
 }
 return resdata + '{STR_DATA_CHECKED}';
}

function waitingWatchSupport() {
 return '奇米奇米';
}
