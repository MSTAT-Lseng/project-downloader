function checkErrorResult() {
  if (data.indexOf('<div class="text">请不要频繁操作，搜索时间间隔为3秒前</div>') > -1) {
    return '请求频繁，请3秒后重试';
  } else if (data.indexOf('<li class="active clearfix">') < 0) {
    return '没有找到相关影片';
  } else {
    return 'SUCCESS';
  }
}

function parserResultItem() {
 var res_item = "";
 var items = data.split('<li class="active ');
 for (var i = 1; i < items.length; i++) {
  var item = items[i];

  var image = item.substring(item.indexOf('data-original="') + 'data-original="'.length, item.indexOf('"><span class="'));

  var link = pathConvert_js(item.substring(item.indexOf('lazyload" href="') + 'lazyload" href="'.length, item.indexOf('" title="')));

  var _introduce = item.substring(item.indexOf('<div class="detail">'));
  _introduce = _introduce.substring(0, _introduce.indexOf('</div>') + '</div>'.length);
  document.write(_introduce);

  var introduce = document.getElementsByClassName('detail')[i - 1].innerText;
  var name = document.getElementsByClassName('title')[i - 1].innerText;

  introduce = introduce.substring(name.length + 2, introduce.length - 16);
  introduce = introduce.replace(/\n\n/g, '{bgd_br}');

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
  if (data.indexOf('<ul class="myui-page text-center clearfix">') < 0) {
    return '1'; // 只有一页
  } else {
    var lastPage = data.substring(data.lastIndexOf('<li><a class="btn btn-default" href="') + '<li><a class="btn btn-default" href="'.length, data.indexOf('">尾页</a></li>'));
    lastPage = lastPage.substring(lastPage.indexOf('----------') + '----------'.length, lastPage.indexOf('---.html'));
    return lastPage;
  }
}
