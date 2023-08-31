function checkErrorResult() {

  var numstr = data.substring(data.indexOf("$('.total').html('") + "$('.total').html('".length);
  numstr = numstr.substring(0, 1);

  if (numstr == "0") {
    // 没有搜索结果
    return "没有搜索到匹配的条目";
  } else {
    return "SUCCESS";
  }
}

function parserResultItem() {
  var items = data.split('<li class="search list">');
  var result_item = "";
  for (var i = 1; i < items.length; i++) {
    var item = items[i];

    // name
    var _name = item.substring(
      item.indexOf('target="_blank" class="title">') + 'target="_blank" class="title">'.length,
    );
    var name = _name.substring(0, _name.indexOf('</a>'));
    name = name.substring(0, name.length - 11);

    // image
    var image = pathConvert_js(item.substring(
      item.indexOf('"  data-echo="') + '"  data-echo="'.length,
      item.indexOf('" alt="')
    ));

    // link
    var link = item.substring(
      item.indexOf('<a href="') +
        '<a href="'.length
    );
    link = pathConvert_js(link.substring(0, link.indexOf('"')));

    // introduce
    var _introduce = item.substring(item.indexOf('<div class="type hide">'));
    _introduce = _introduce.substring(0, _introduce.indexOf('</div>'));
    _introduce = _introduce + '</div>';
    document.write(_introduce);

    var introduce = document.getElementsByClassName('type hide')[i-1].innerText;
    introduce = introduce.substring(16, introduce.length - 8);

    result_item =
      result_item +
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
  return result_item;
}

function getPagesParam() {

  var pagestr = data.substring(data.indexOf("$('.total').html('") + "$('.total').html('".length);
  pagestr = pagestr.substring(0, pagestr.indexOf("'"));
  var numint = parseInt(pagestr);
  if (numint < 9) {
    return "1";
  } else {
    return Math.ceil(numint / 8) + "";
  }
  
}
