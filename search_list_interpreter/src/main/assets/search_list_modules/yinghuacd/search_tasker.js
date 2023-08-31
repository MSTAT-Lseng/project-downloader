function checkErrorResult() {
  if (data.indexOf('抱歉，未找到相关信息~</ul>') > -1) {
    return '没有找到相关影片';
  } else if (data.indexOf('<center><h1>404 Not Found</h1></center>') > -1) {
    return '没有找到相关影片';
  } else {
    return 'SUCCESS';
  }
}

function parserResultItem() {
  var item_data = data.substring(data.indexOf('<div class="lpic">'));

 var res_item = "";
 var items = item_data.split('<li><a href=');
 for (var i = 1; i < items.length; i++) {
  var item = items[i];

  var image = item.substring(item.indexOf('<img src="') + '<img src="'.length, item.indexOf('" alt="'));

  var link = pathConvert_js(item.substring(item.indexOf('<a href="') + '<a href="'.length, item.indexOf('" title="')));

  var _introduce = '<span id="intro_tag_' + i + '">类型：' + item.substring(item.indexOf('<span>类型：') + '<span>类型：'.length);
  _introduce = _introduce.substring(0, _introduce.indexOf('</span>') + '</span>'.length);
  document.write(_introduce);

  var introduce = document.getElementById('intro_tag_' + i).innerText;
  var name = item.substring(item.indexOf('" alt="') + '" alt="'.length, item.indexOf('"></a>'));

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
  if (data.indexOf('<a id="totalnum" class="a1">') > -1) {
    var total_item = data.substring(data.indexOf('<a id="totalnum" class="a1">') + '<a id="totalnum" class="a1">'.length);
    total_item = total_item.substring(0, total_item.indexOf('</a>'));

    var item_int = parseInt(total_item);

    if (item_int < 21) {
      return "1"
    } else {
      return "" + parseInt(Math.ceil(item_int / 20));
    }
  } else {
    return "1";
  }
}
