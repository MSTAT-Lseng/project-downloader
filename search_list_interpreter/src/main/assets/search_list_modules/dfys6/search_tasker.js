function checkErrorResult() {
  if (data == "    []") {
    return '没有找到相关影片';
  } else {
    return 'SUCCESS';
  }
}

function parserResultItem() {
  var item_list = data.split('},{');

 var res_item = "";

 for (var i = 0; i < item_list.length; i++) {
  var item = item_list[i];
  
  var name = item.substring(item.indexOf('\"title\":\"') + '\"title\":\"'.length, item.indexOf('\",{bgd_br}\"time'));
  var image = pathConvert_js(item.substring(item.indexOf('\"thumb\":\"') + '\"thumb\":\"'.length, item.indexOf('\",{bgd_br}\"title')));
  var link = pathConvert_js(item.substring(item.indexOf('\"url\":\"') + '\"url\":\"'.length, item.indexOf('\",{bgd_br}\"thumb')));
  var introduce = '地区：' + item.substring(item.indexOf('\"area\":\"') + '\"area\":\"'.length, item.indexOf('\",{bgd_br}\"sort'))
    + ' / 演出：' + item.substring(item.indexOf('\"star\":\"') + '\"star\":\"'.length, item.indexOf('\",{bgd_br}\"lianzaijs'));
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
  return "1";
}
