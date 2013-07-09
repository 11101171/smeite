/**
 * Created by zuosanshao.
 * User: Administrator
 * Email:zuosanshao@qq.com
 * @contain: 用户关注、取消关注
 * @depends: jquery.js
 * Since: 12-10-22    下午8:58
 * ModifyTime : 2012-12-30 22:00
 * ModifyContent:删除注释
 * http://www.smeite.com/
 *
 */

define(function(require, exports){
	var $ = require("$");
	window.ZeroClipboard = require("zeroclipboard");

    $(function(){
        /* 复制 */
        var clip = new ZeroClipboard.Client();
        clip.setHandCursor( true );
        clip.addEventListener('mouseover', function (client) {
            clip.setText($('#clip-text').val());
        });
        clip.glue('J_Clip');
        document.getElementById("J_Clip").onmouseover = function(){
            clip.reposition(this);
        }

        var prizeHtml=function(prize){
            var html ="<div class='handle'>您获得 "+ prize.num ;
            if(prize.handleStatus ==0){
               html +=" ,系统将在24小时内发放"
            }
            if(prize.handleStatus== 1){
                html+=" ,已发放"
            }
            if(prize.handleStatus == 2){
                html+=" ,"+prize.handleResult
            }
            html+="</div>" ;
            return html;
        }

        var getInvitePrizesHtml=function(json){
            var html ="";
            if(json.size==1){
               html+=prizeHtml(json.prizes[0])
            }
            if(json.size==2){

                html+=prizeHtml(json.prizes[0])
                html+=prizeHtml(json.prizes[1])
            }
            if(json.size==3){
                html+=prizeHtml(json.prizes[0])
                html+=prizeHtml(json.prizes[1])
                html+=prizeHtml(json.prizes[2])
            }

            return html;

        }

        var getInvitePrizes = function(uid,inviteeId,$elm){
            $.ajax({
                url:"/user/account/getInvitePrizes",
                type:"get",
                dataType:"json",
                data:{
                    uid:parseInt(uid),
                    inviteeId:parseInt(inviteeId)
                },
                success:function (json) {
                    if (json.code == 100) {
                        var html = getInvitePrizesHtml(json);
                        $elm.replaceWith(html)
                    }

                }
            });
        }

        /* 处理 用户的邀请有奖 */
           $(".getInvitePrizes").each(function(){
               var $this =$(this) ;
               var uid =$this.data("uid")  ;
               var inviteeId = $this.data("inviteeid");
               var credits =$this.data("credits") ;
               if(credits > 99 ){
                   getInvitePrizes(uid,inviteeId,$this)
               }
           })
    })


});
