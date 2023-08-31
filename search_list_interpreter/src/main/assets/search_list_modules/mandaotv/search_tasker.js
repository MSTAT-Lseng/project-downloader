function checkErrorResult() {
 if (data.indexOf('对不起，没有找到任何记录,<a target=') > -1) {
  return '没有找到相关影片';
 } else if (data.indexOf('<span class="tip1 red">您所提交的请求含有不合法的参数，已被网站管理员设置拦截！') > -1){
  return '搜索关键词被拦截，请缩小关键词范围';
 } else {
  return 'SUCCESS';
 }
}

function parserResultItem() {
 var result_item = '';
 var items = data.split('<li class="mb');
 for (var i = 1; i < items.length; i++) {
  var item = items[i];
  item = item.substring(0, item.indexOf('</li>'));
  var name = item.substring(item.indexOf('<p class="name">') + '<p class="name">'.length, item.lastIndexOf('</p>'));
  var link = item.substring(item.indexOf('<a class="li-hv" href="') + '<a class="li-hv" href="'.length, item.indexOf('" title="'));
  var image = item.substring(item.indexOf('<img referrerpolicy="no-referrer" class="lazy" data-original="') + '<img referrerpolicy="no-referrer" class="lazy" data-original="'.length, item.indexOf('" src="'));
  var introduce = item.substring(item.indexOf('<p class="bz">') + '<p class="bz">'.length, item.indexOf('</div>'));
  introduce = introduce.substring(0, introduce.indexOf('</p>'));
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
 var page_frame = data.substring(data.indexOf('<div class="page mb clearfix">'));
 page_frame = page_frame.substring(0, page_frame.indexOf('</div>'));
 if (page_frame.indexOf('<a ') > -1) {
  if (page_frame.indexOf('">..') > -1) {
    // 中间页判断
   var page = page_frame.substring(page_frame.indexOf('">..') + '">..'.length, page_frame.lastIndexOf('</a>'));
   return page;
  } else {
    // 最后一页判断
   return page_frame.substring(page_frame.indexOf('<em>') + '<em>'.length, page_frame.indexOf('</em>'));
  }
 } else {
  // 只有一页判断
  return '1';
 }
}