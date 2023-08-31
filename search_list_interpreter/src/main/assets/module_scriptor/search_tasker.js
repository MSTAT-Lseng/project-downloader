/*
    需要传参的变量
    var data;
    var moduleProtocol;
    var moduleDomain;
*/
function pathConvert_js(url) {
  var outputUrl; // 定义输出字符
  if (url.startsWith("//")) {
    outputUrl = moduleProtocol + ":" + url; // 网站协议拼接
  } else if (url.startsWith("/")) {
    outputUrl = moduleProtocol + "://" + moduleDomain + url; // 网站根目录拼接
  } else {
    outputUrl = url; // 已经是绝对网址
  }
  return outputUrl; // 返回处理过的链接
}
