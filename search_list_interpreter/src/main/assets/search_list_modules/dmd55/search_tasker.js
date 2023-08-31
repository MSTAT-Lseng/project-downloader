function checkErrorResult() {
 if (data.indexOf('共0条数据&nbsp;当前:1/1页&nbsp;<em>') > -1) {
  return '没有找到相关影片';
 } else if (data.indexOf('<h4 class="infotitle1">请不要频繁操作，时间间隔为5秒</h4>') > -1){
  return '搜索过快，请等5秒后重新搜索';
 } else {
  return 'SUCCESS';
 }
}

function parserResultItem() {
 var result_item = '';
 var itemframe = data.substring(data.indexOf('<div class="list3_cn_box"><div class="list3_cn_box">'), data.indexOf('<div class="cn_box_r2">'));
 var items = itemframe.split('<div class="cn_box2">');
 for (var i = 1; i < items.length; i++) {
  var item = items[i];
  var name = item.substring(item.indexOf('<a class="B font_16"') + '<a class="B font_16"'.length, item.lastIndexOf('</a></li>'));
  name = name.substring(name.indexOf('">') + '">'.length);
  var link = item.substring(item.indexOf('<a class="B font_16" href="') + '<a class="B font_16" href="'.length, item.indexOf('</a></li>'));
  link = link.substring(0, link.indexOf('"'));
  var image = item.substring(item.indexOf('<img class="list_pic" src="') + '<img class="list_pic" src="'.length, item.indexOf('" border="0" alt="'));
  document.write(item.substring(item.indexOf('<ul class="list_20">'), item.indexOf('</ul>') + '</ul>'.length));
  var introduce = document.getElementsByClassName('list_20')[i - 1].innerText;
  introduce = introduce.substring(name.length + 1);
  result_item =
      result_item +
      "{bgd_items_name}" +
      name +
      "{bgd_items_image}" +
      image +
      "{bgd_items_link}" +
      pathConvert_js(link) +
      "{bgd_items_info}" +
      introduce +
      "{bgd_items_split}";
 }
 return result_item;
}

function getPagesParam() {
 if (data.indexOf('&nbsp;当前:1/1页&nbsp;<em>') > -1) {
  return '1';
 } else {
  var pages = data.substring(data.indexOf('条数据&nbsp;当前:') + '条数据&nbsp;当前:'.length);
  pages = pages.substring(0,pages.indexOf('页&nbsp;'));
  pages = pages.substring(pages.indexOf('/') + '/'.length);
  return pages;
 }
}
