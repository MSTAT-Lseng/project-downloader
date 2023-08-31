function checkErrorResult() {
    if (data == "[]") {
        return '没有搜索到匹配的条目';
    } else {
        return 'SUCCESS';
    }
}

function parserResultItem() {
    var tasked_data = data.replace(/{bgd_br}/g, '');
    var result_item = '';
    
    var json = JSON.parse(tasked_data);
    for (var i = 0; i < json.length; i++) {
        var result = json[i];
        var name = result['title'];
        var image = result['thumb'];
        var link = pathConvert_js(result['url']);
        var introduce = result['time'] + '年 ' + result['area'] + ' 表演：' + result['star'];

        result_item = result_item + 
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
    // 无需实现页面跳转
}
