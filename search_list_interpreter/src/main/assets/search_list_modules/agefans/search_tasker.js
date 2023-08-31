function checkErrorResult() {
    if (data.indexOf('共 <span class="text-danger">0</span> 条记录') > -1) {
        return '没有搜索到匹配的条目';
    } else {
        return 'SUCCESS';
    }
}


function parserResultItem() {
    var result_item = '';
    
    var _result = data.split('<div class="video_cover_wrapper">');

    for (var i = 1; i < _result.length; i++) {
        var result = _result[i];

        // name
        var _name = result.substring(result.indexOf('<h5 class="card-title">'));
        _name = _name.substring(0, _name.indexOf('</h5>'));

        document.write(_name + '</h5>');
        var name = document.getElementsByClassName('card-title')[i-1].innerText;

        // image
        var _image = result.substring(result.indexOf('" data-original="') + '" data-original="'.length);
        var image = _image.substring(0, _image.indexOf('"'));
        
        // link
        var link = result.substring(result.indexOf('<a href="') + '<a href="'.length);
        link = pathConvert_js(link.substring(0, link.indexOf('"')));
        
        // info
        var _info = result.substring(result.indexOf('<div class="video_detail_info">'), result.indexOf('<div class="video_btns">')) + '</div>';
        document.write(_info);
        var info = document.getElementsByClassName('video_detail_info')[i-1].innerText;
        info = info.replace(/\n/g, '{bgd_br}');
        info = info.replace(/{bgd_br}{bgd_br}/g, '');

        
        result_item =
          result_item +
          "{bgd_items_name}" +
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
    var result_count = data.substring(data.indexOf('共 <strong>') + '共 <strong>'.length);
    result_count = result_count.substring(0, result_count.indexOf('</strong>'));

    var int_count = parseInt(result_count);
    if (result_count < 25) {
        return '1';
    } else {
        return Math.ceil(result_count / 24) + '';
    }
}
