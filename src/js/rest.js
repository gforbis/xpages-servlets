// Requires jQuery

function submit(restUrl, msgHandler){
	$.post(restUrl,function(data){
		if(msgHandler){
			//data should be parsed into something more readable
			$(msgHandler).html(data.status);
		}
		if('error'!=data.status && 'complete'!=data.status){
			setTimeout(function(){
				updateProgress(restUrl, msgHandler);
			},200);
		}
	},'json');
}

function updateProgress(restUrl, msgHandler){
	$.getJSON(restUrl, function(data){
		if(msgHandler){
			$(msgHandler).html(data.status);
		};
		if('error'!=data.status && 'complete'!=data.status){
			setTimeout(function(){
				updateProgress(restUrl, msgHandler);
			}, 500);
		}
	});
}
