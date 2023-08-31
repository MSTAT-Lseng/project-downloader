function checkErrorResult() {
 if (data.indexOf('<div class="text">访问此数据需要输入验证码</div>') > -1) {
  return 'require-identify';
 } else if (data.indexOf('<div class="nofound">没有找到与 "') > -1) {
  return '没有找到相关影片';
 } else {
  return 'SUCCESS';
 }
}

function parserResultItem() {
 var res_item = "";
 var items = data.split('<div class="module-card-item module-item">');
 for (var i = 1; i < items.length; i++) {
  var item = items[i];
  
  var name = item.substring(item.indexOf('<strong>') + '<strong>'.length, item.indexOf('</strong>'));
  var image = item.substring(item.indexOf('data-original="') + 'data-original="'.length, item.indexOf('" alt="'));
  var link = pathConvert_js(item.substring(item.indexOf('<a href="') + '<a href="'.length, item.indexOf('" class="module-card-item-poster">')));
  var _introduce = item.substring(item.indexOf('<div class="module-info-item">'), item.indexOf('<div class="module-card-item-footer">'));
  _introduce = _introduce.replace(/module-info-item/g, 'module-info-item-' + i);
  document.write(_introduce);
  var introduce = document.getElementsByClassName('module-info-item-' + i)[0].innerText;
  introduce = introduce + '{bgd_br}' + document.getElementsByClassName('module-info-item-' + i)[1].innerText;
  res_item =
      res_item +
      "{bgd_items_name}" +
      name +
      "{bgd_items_image}" +
      image +
      "{bgd_items_link}" +
      link +
      "{bgd_items_info}" +
      introduce +
      "{bgd_items_split}";
 }
 return res_item;
}

function getPagesParam() {
 var itemnum = data.substring(data.indexOf('$(\'.mac_total\').html(\'') + '$(\'.mac_total\').html(\''.length);
 itemnum = itemnum.substring(0, itemnum.indexOf('\''));
 var numint = parseInt(itemnum);
 if (numint < 17) {
  return "1";
 } else {
  return Math.ceil(numint / 16) + "";
 }
}