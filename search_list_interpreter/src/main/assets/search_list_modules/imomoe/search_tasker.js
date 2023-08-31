function checkErrorResult() {
  if (data.indexOf("<h4>请不要频繁操作，搜索时间间隔为5秒前</h4>") > -1) {
    // 搜索过快
    return "您的搜索过快，请5秒后再查询";
  } else if (
    data.indexOf(
      '<h4>没有找到您想要的结果哦！您可以 <a class="text-color" href="/gbook.html">'
    ) > -1
  ) {
    // 没有搜索结果
    return "没有搜索到匹配的条目";
  } else {
    return "SUCCESS";
  }
}

function parserResultItem() {
  var items = data.split('<li class="clearfix">');
  var result_item = "";
  for (var i = 1; i < items.length; i++) {
    var item = items[i];

    // name
    var _name = item.substring(
      item.indexOf('<h4 class="title">'),
      item.indexOf("<p>")
    );
    document.write(_name);
    var name = document.getElementsByClassName("title")[i - 1].innerText;

    // image
    var image = item.substring(
      item.indexOf('data-original="') + 'data-original="'.length,
      item.indexOf('<span class="play hidden-xs"></span>')
    );
    image = image.substring(0, image.indexOf('"'));

    // link
    var link = item.substring(
      item.indexOf('<a class="searchkey" href="') +
        '<a class="searchkey" href="'.length
    );
    link = pathConvert_js(link.substring(0, link.indexOf('"')));

    // introduce
    var _introduce = item.substring(item.indexOf('<div class="detail">'));
    _introduce = '<div class="detail">' + 
      _introduce.substring(_introduce.indexOf('</h4>') + '</h4>'.length, _introduce.indexOf('<p class="margin-0">')) + "</div>";
    document.write(_introduce);
    var introduce = document.getElementsByClassName("detail")[i - 1].innerText;
    introduce = introduce.replace(/详情 >/g, "");
    introduce = introduce.replace(/\n/g, ' ');
    introduce = introduce.replace(/简介：/g, '{bgd_br}');
    introduce = introduce.substring(8);

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
  if (data.indexOf('<ul class="myui-page text-center clearfix">') > -1) {
    var pages = data.split(
      '<a class="btn btn-default" href="/search.asp?page='
    );
    var _page = pages[pages.length - 1];
    var page = _page.substring(0, _page.indexOf("&"));
    return page;
  } else {
    return "1";
  }
}
