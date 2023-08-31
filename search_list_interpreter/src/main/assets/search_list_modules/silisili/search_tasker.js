function checkErrorResult() {
  if (data.indexOf('<article class="post-list">') > -1) {
    return "SUCCESS";
  } else {
    return "没有搜索到匹配的条目";
  }
}

function parserResultItem() {
  var resData = "";
  var itemsplit = data.split('<article class="post-list">');
  for (var i = 1; i < itemsplit.length; i++) {
    var itemdetails = itemsplit[i];

    var namehtml = itemdetails.substring(
      itemdetails.indexOf('<div class="entry-title">'),
      itemdetails.indexOf("</header>")
    );
    document.write(namehtml);
    var name = document.getElementsByClassName("entry-title")[i - 1].innerText;

    var image = itemdetails.substring(
      itemdetails.indexOf('<img srcset="') + '<img srcset="'.length,
      itemdetails.indexOf('" referrerpolicy="no-referrer" alt="">')
    );

    var introduce = itemdetails.substring(
      itemdetails.indexOf('<div class="entry-meta">'),
      itemdetails.indexOf("</article>")
    );
    introduce = introduce.replace(/<\/div>/g, "");
    introduce = introduce + "</div>";
    document.write(introduce);

    var summary = itemdetails.substring(
      itemdetails.indexOf('<div class="entry-summary">'),
      itemdetails.indexOf('<div class="entry-meta">')
    );
    document.write(summary);
    var summary_text =
      document.getElementsByClassName("entry-summary")[i - 1].innerText;
    if (summary_text.length > 50) {
      summary_text = summary_text.substring(0, 50);
    }

    var info_text_introduce = document.getElementsByClassName("entry-meta")[i - 1].innerText;
    info_text_introduce = info_text_introduce.replace(/{bgd_br}/g, '');
    info_text_introduce = info_text_introduce.replace(/\n/g, '');
    var introduce_text =
      info_text_introduce +
      "{bgd_br}" +
      summary_text +
      "...";

    var link = pathConvert_js(
      itemdetails.substring(
        itemdetails.indexOf('<a href="') + '<a href="'.length,
        itemdetails.indexOf('" title="')
      )
    );

    resData =
      resData +
      "{bgd_items_name}" +
      name +
      "{bgd_items_image}" +
      image +
      "{bgd_items_link}" +
      link +
      "{bgd_items_info}" +
      introduce_text +
      "{bgd_items_split}";
  }
  return resData;
}

function getPagesParam() {
  if (data.indexOf('<ul class="list-page">') > -1) {
    var pageframe = data.substring(
      data.indexOf('<ul class="list-page">'),
      data.indexOf('<div class="col-md-3 widget-area" id="secondary"')
    );
    var pages = pageframe.split('<li class="hidden-xs');
    var total_pages = pages[pages.length - 1];
    return total_pages.substring(
      total_pages.indexOf('/">') + '/">'.length,
      total_pages.indexOf("</a>")
    );
  } else {
    return "1";
  }
}
