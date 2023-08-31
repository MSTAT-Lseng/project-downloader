function checkErrorResult() {
 if (data.indexOf("$('.mac_total').html('0');") > -1) {
  return '没有找到匹配资源';
 } else if (data.indexOf('<div class="text">请不要频繁操作，搜索时间间隔为10秒</div>') > -1) {
  return '搜索过于频繁，请10秒后重试';
 } else {
  return 'SUCCESS';
 }
}

function parserResultItem() {
 var result_item = '';

 var items = data.split('<li><a class="play-img"');
 for (var i = 1; i < items.length; i++) {
  var item = items[i];
  var name = item.substring(item.indexOf('<h2>') + '<h2>'.length, item.indexOf('</h2>'));
  name = name.substring(name.indexOf('">') + '">'.length, name.indexOf('</a>'));
  var image = item.substring(item.indexOf('<img src="') + '<img src="'.length, item.indexOf('" referrerpolicy="no-referrer"'));
  var link = item.substring(item.indexOf('href="') + 'href="'.length, item.indexOf('">'));
  var introduce_data = '<div id="intro_data_' + i + '">' + item.substring(item.indexOf('<dl>'), item.indexOf('</div>')) + '</div>';
  introduce_data = introduce_data.replace(/<dt>/g, '');
  introduce_data = introduce_data.replace(/<\/dt>/g, '');
  introduce_data = introduce_data.replace(/<dd>/g, '');
  introduce_data = introduce_data.replace(/<\/dd>/g, '');
  document.write(introduce_data);
  var introduce = document.getElementById('intro_data_' + i).innerText;
  result_item = result_item + "{bgd_items_name}" +
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
 var itemnum = parseInt(data.substring(data.indexOf("$('.mac_total').html('") + "$('.mac_total').html('".length, data.lastIndexOf('\');')));
 if (itemnum < 11) {
  return '1';
 } else {
  return Math.ceil(itemnum / 10) + '';
 }
}