function _bgd_function(fun) {
  if (typeof fun != "function") {
    throw new typeError("The argument passed in is not a function");
  } else {
    var doc = document;
    if (doc.addEventListener) {
      doc.addEventListener("DOMContentLoaded", fun, false);
    } else {
      doc.onreadystatechange = function () {
        if (
          doc.readyState == "loader" ||
          doc.readyState == "complete" ||
          doc.readyState == "interactive"
        ) {
          fun();
        }
      };
    }
  }
}
