/**
 * Created by zuosanshao.
 * Email:zuosanshao@qq.com
 * @contain:
 * @depends:
 * Since:13-1-15下午8:21
 * ModifyTime :
 * ModifyContent:
 * http://www.smeite.com/
 *
 */
/* menu slide  */
$(function(){

    $('.submenu > a').click(function(){
        var submenu = $(this).siblings('ul');
        var li = $(this).parents('li');
        var submenus = $('#sidebar li.submenu ul');
        var submenus_parents = $('#sidebar li.submenu');
        if(li.hasClass('open'))
        {
            if(($(window).width() > 768) || ($(window).width() < 479)) {
                submenu.slideUp();
            } else {
                submenu.fadeOut(250);
            }
            li.removeClass('open');
        } else
        {
            if(($(window).width() > 768) || ($(window).width() < 479)) {
                submenus.slideUp();
                submenu.slideDown();
            } else {
                submenus.fadeOut(250);
                submenu.fadeIn(250);
            }
            submenus_parents.removeClass('open');
            li.addClass('open');
        }
    });

    var ul = $('#sidebar > ul');

    $('#sidebar > a').click(function(e)
    {
        e.preventDefault();
        var sidebar = $('#sidebar');
        if(sidebar.hasClass('open'))
        {
            sidebar.removeClass('open');
            ul.slideUp(250);
        } else
        {
            sidebar.addClass('open');
            ul.slideDown(250);
        }
    });

    /* form select  */
    $(" th input:checkbox").click(function() {
        var checkedStatus = this.checked;
        var checkbox = $(this).parents('.table').find('tr td:first-child input:checkbox');
        checkbox.each(function() {
            this.checked = checkedStatus;
            if (checkedStatus == this.checked) {
                $(this).closest('.checker > span').removeClass('checked');
            }
            if (this.checked) {
                $(this).closest('.checker > span').addClass('checked');
            }
        });
    });


    /* 批量处理*/
    $("#J_batch").click(function(){
        var url =window.location.href
        $("#J_url").val(url)
        $("#J_batchForm").submit()
    })
    /* filter page */
    $(".pagination span").click(function(){
        var page=$(this).data('page')
        $("#J_currentPage").val(page)
        $("#J_filterForm").submit()
    })

    $(".date").dateinput({
        format:"yyyy-mm-dd"
    })
    $(".taobaokeDate").dateinput({
        format:"yyyymmdd"
    })
})



