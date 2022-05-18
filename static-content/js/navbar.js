// TODO: fix the navbar sript

$('#topheader .nav-pills a').on('click',
    function () {
        console.log(this.href)
        $('#topheader .nav-pills').find('a.active').removeClass('active');
        $(this).addClass('active');
    }
);