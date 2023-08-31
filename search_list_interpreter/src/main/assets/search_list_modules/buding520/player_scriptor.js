window.onload = bgonload;

function addStyle(css) {
  var style = document.createElement("style");
  style.type = "text/css";
  style.appendChild(document.createTextNode(css));
  document.head.appendChild(style);
  return style;
}

function bgonload() {
  var desc = document.getElementsByClassName("des2")[0].innerText;
  var name = document.getElementsByClassName("name")[0].innerText;
  name = name.substring(0, name.indexOf("连载至"));
  var image = document.getElementsByClassName("lazy")[0].src;
  var info0 = document.getElementsByTagName("dd")[1].innerText;

  if (desc.length > 200) {
    desc = desc.substring(0, 200) + "...";
  }

  // playlist
  var plhtml = "";
  var playlist = document
    .getElementById("play_list_34")
    .getElementsByTagName("li");
  var infolist = document.getElementById("stab_1_71");
  var infoflag = 0;
  for (var pl = 0; pl < playlist.length; pl++) {
    // 过滤无效条目
    if (pl % 2 == 0 && playlist[pl].innerText != "百度云") {
      plhtml =
        plhtml +
        '<p style="font-size: 16px;">' +
        playlist[pl].innerText +
        "</p>";

      var infomark = infolist
        .getElementsByTagName("ul")
        [infoflag].getElementsByTagName("li");
      for (var io = 0; io < infomark.length; io++) {
        var name2 = infomark[io].getElementsByTagName("a")[0].innerText;
        var link = infomark[io].getElementsByTagName("a")[0].href;
        plhtml =
          plhtml +
          "<button onclick=\"window.location.href = '" +
          link +
          "';\">" +
          name2 +
          "</button>";
      }
      infoflag = infoflag + 1;
    }
  }

  document.write(
    `
<bgd_div>
<bgd_div style="text-align: center;">
<img src="" id="image" style="width: 125px; border-radius: 5px; margin-top: 15px; box-shadow: 0px 5px 20px #ddd;">
<h2 id="name" style="font-weight: normal; font-size: 18px;margin-bottom: 8px;padding-bottom: 0px;"></h2>
<p id="info" style="font-size: 14px;margin-top:0px;padding-top: 0px;"></p>
</bgd_div>
<p id="desc" style="color: #888; font-size: 14px;"></p>
</bgd_div>
		` + plhtml
  );

  var toggler = addStyle(`
  		div { display:none; }
  		bgd_div { display: block; }
  		button {
  			background: aliceblue;
    		border: 1px solid #ddd;
    		border-radius: 5px;
    		font-size: 14px;
    		padding-left: 12.5px;
    		padding-right: 12.5px;
    		padding-top: 7.5px;
    		padding-bottom: 7.5px;
    		color: #333;
    		margin-right: 10px;
    		margin-bottom: 10px;
  		}
	`);

  document.getElementById("name").innerText = name;
  document.getElementById("desc").innerText = desc;
  document.getElementById("image").src = image;
  document.getElementById("info").innerText = info0;
}
