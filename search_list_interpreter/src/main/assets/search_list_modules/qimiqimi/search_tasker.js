function checkErrorResult() {
 if (data.indexOf('<h1>没有找到匹配数据</h1>') > -1) {
  return '没有找到匹配数据';
 } else if (data.indexOf('<div class="text">请不要频繁操作，搜索时间间隔为3秒</div>') > -1){
  return '搜索间隔过短，请3秒后重试';
 } else {
  return 'SUCCESS';
 }
}

function parserResultItem() {
 var result_item = '';
 var __result = data.substring(data.indexOf('<ul class="show-list">'), data.indexOf('<div class="ui-bar list-page fn-clear">'));
 var _result = __result.split('<li>');
 for (var i = 1; i < _result.length; i++) {
  var result = _result[i];
  document.write('<li>' + result);

  var name = document.getElementsByTagName('h2')[i-1].innerText;
  var image = result.substring(result.indexOf('<img src="') + '<img src="'.length, result.indexOf('" alt="'));
  var link = document.getElementsByTagName('h2')[i-1].innerHTML;
  link = pathConvert_js(link.substring(link.indexOf('<a href="') + '<a href="'.length, link.indexOf('">')));
  var _info = document.getElementsByTagName('li')[i-1].getElementsByTagName('dl');
  var info = '';
  for (var inf = 0; inf < _info.length; inf++) {
   info = info + '{bgd_br}' + _info[inf].innerText.replace(/\n/g, '');
  }
  result_item = result_item + "{bgd_items_name}" +
      name +
      "{bgd_items_image}" +
      image +
      "{bgd_items_link}" +
      link +
      "{bgd_items_info}" +
      info +
      "{bgd_items_split}";
 }
 return result_item;
}

function getPagesParam() {
 if (data.indexOf('条数据,当前1/1页</div>') > -1) {
  return '1';
 } else {
  var page_args = data.split('<a class="page_link" href="');
  var page = page_args[page_args.length - 1];
  var pagenum = page.substring(page.indexOf('/page/') + '/page/'.length, page.indexOf('.html'));
  return pagenum;
 }
}
