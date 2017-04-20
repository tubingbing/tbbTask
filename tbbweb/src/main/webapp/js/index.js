// JavaScript Document

$(document).ready(function() {
    if(manager == false){
        config.style.display="none";
        zookeeperData.style.display="none";
        zookeeperDataExport.style.display="none";
        zookeeperDataImport.style.display="none";
    }
});

//策略处理
function strategyDeal(action,strategyName,url){
    $.ajax({
        url : "/com.jd.launcher.schedule/Strategy/deal.do",
        type : "post",
        data :  {"action":action,"strategyName" :strategyName} ,
        success : function(data){
            refresh(url);
        }
    });
}

//刷新页面
function refresh(url){
    $("#content").load("/com.jd.launcher.schedule/"+url);
}

	
	