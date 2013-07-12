var video = {};
(function () {
    video.init = function () {
        addUrlChangeListener($G("videoUrl"));
        addOkListener();
        (function () {
            var img = editor.selection.getRange().getClosedNode(), url;
            if (img && img.className == "edui-faked-video") {
                $G("videoUrl").value = url = img.getAttribute("_url")
            }
            createPreviewVideo(url)
        })()
    };
    function addOkListener() {
        dialog.onok = function () {
            $G("preview").innerHTML = "";
            insertSingle()
        };
        dialog.oncancel = function () {
            $G("preview").innerHTML = ""
        }
    }

    function selectTxt(node) {
        if (node.select) {
            node.select()
        } else {
            var r = node.createTextRange && node.createTextRange();
            r.select()
        }
    }

    function insertSingle() {
        var url = $G("videoUrl").value, align = "none";
        if (!url) {
            return false
        }
        url = url.replace(/<[^>]+>|\'|\"|>|</g, "");
        editor.execCommand("insertvideo", {url: convert_url(url), width: 600, height: 400, align: align})
    }

    function convert_url(s) {
        return s.replace(/http:\/\/www\.tudou\.com\/programs\/view\/([\w\-]+)\/?/i, "http://www.tudou.com/v/$1").replace(/http:\/\/www\.youtube\.com\/watch\?v=([\w\-]+)/i, "http://www.youtube.com/v/$1").replace(/http:\/\/v\.youku\.com\/v_show\/id_([\w\-=]+)\.html/i, "http://player.youku.com/player.php/sid/$1").replace(/http:\/\/www\.56\.com\/u\d+\/v_([\w\-]+)\.html/i, "http://player.56.com/v_$1.swf").replace(/http:\/\/www.56.com\/w\d+\/play_album\-aid\-\d+_vid\-([^.]+)\.html/i, "http://player.56.com/v_$1.swf").replace(/http:\/\/www\.duobei\.com\/record\/(\d+)/i, "http://www.duobei.com/player/$1/d.swf").replace(/http:\/\/v\.ku6\.com\/.+\/([^.]+)\.html/i, "http://player.ku6.com/refer/$1/v.swf")
    }

    function checkNum(nodes) {
        for (var i = 0, ci; ci = nodes[i++];) {
            var value = ci.value;
            if (!isNumber(value) && value) {
                alert(lang.numError);
                ci.value = "";
                ci.focus();
                return false
            }
        }
        return true
    }

    function isNumber(value) {
        return/(0|^[1-9]\d*$)/.test(value)
    }

    function addUrlChangeListener(url) {
        if (browser.ie) {
            url.onpropertychange = function () {
                createPreviewVideo(this.value)
            }
        } else {
            url.addEventListener("input", function () {
                createPreviewVideo(this.value)
            }, false)
        }
    }

    function createPreviewVideo(url) {
        if (!url) {
            return
        }
        url = url.replace(/<[^>]+>|\'|\"|>|</g, "");
        var matches = url.match(/youtu.be\/(\w+)$/) || url.match(/youtube\.com\/watch\?v=(\w+)/) || url.match(/youtube.com\/v\/(\w+)/), youku = url.match(/youku\.com\/v_show\/id_(\w+)/), youkuPlay = /player\.youku\.com/ig.test(url), tmpurl = convert_url(url);
        if (!youkuPlay) {
            if (matches) {
                url = "https://www.youtube.com/v/" + matches[1] + "?version=3&feature=player_embedded"
            } else {
                if (youku) {
                    url = "http://player.youku.com/player.php/sid/" + youku[1] + "/v.swf"
                } else {
                    url = tmpurl
                }
            }
        } else {
            url = url.replace(/\?f=.*/, "")
        }
        $G("preview").innerHTML = '<embed type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" src="' + url + '" width="' + 440 + '" height="' + 365 + '" wmode="transparent" play="true" loop="false" menu="false" allowscriptaccess="never" allowfullscreen="true" ></embed>'
    }

    function endWith(str, endStrArr) {
        for (var i = 0, len = endStrArr.length; i < len; i++) {
            var tmp = endStrArr[i];
            if (str.length - tmp.length < 0) {
                return false
            }
            if (str.substring(str.length - tmp.length) == tmp) {
                return true
            }
        }
        return false
    }

    function getMovie() {
        var keywordInput = $G("videoSearchTxt");
        if (!keywordInput.getAttribute("hasClick") || !keywordInput.value) {
            selectTxt(keywordInput);
            return
        }
        $G("searchList").innerHTML = lang.loading;
        var keyword = keywordInput.value, type = $G("videoType").value, str = "";
        ajax.request(editor.options.getMovieUrl, {searchKey: keyword, videoType: type, onsuccess: function (xhr) {
            try {
                var info = eval("(" + xhr.responseText + ")")
            } catch (e) {
                return
            }
            var videos = info.multiPageResult.results;
            var html = ["<table width='530'>"];
            for (var i = 0, ci; ci = videos[i++];) {
                html.push("<tr><td><img title='" + lang.clickToSelect + "' ue_video_url='" + ci.outerPlayerUrl + "' alt='" + ci.tags + "' width='106' height='80' src='" + ci.picUrl + "' /> </td><td><p><a target='_blank' title='" + lang.goToSource + "' href='" + ci.itemUrl + "'>" + ci.title.substr(0, 30) + "</a></p><p style='height: 62px;line-height: 20px' title='" + ci.description + "'> " + ci.description.substr(0, 95) + " </p></td></tr>")
            }
            html.push("</table>");
            $G("searchList").innerHTML = str = html.length == 2 ? lang.noVideo : html.join("");
            var imgs = domUtils.getElementsByTagName($G("searchList"), "img");
            if (!imgs) {
                return
            }
            for (var i = 0, img; img = imgs[i++];) {
                domUtils.on(img, "click", function () {
                    changeSelected(this)
                })
            }
        }})
    }

    function changeSelected(o) {
        if (o.getAttribute("selected")) {
            o.removeAttribute("selected");
            o.style.cssText = "filter:alpha(Opacity=100);-moz-opacity:1;opacity: 1;border: 2px solid #fff"
        } else {
            o.setAttribute("selected", "true");
            o.style.cssText = "filter:alpha(Opacity=50);-moz-opacity:0.5;opacity: 0.5;border:2px solid blue;"
        }
    }
})();