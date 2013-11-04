//pure javascript comments

domain = location.protocol + "//" + location.host;

htmlTemplate = null;
var $commentContainer = $('#commentContainer');

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

function getParent(commentUUID){
	return $.ajax({
		type: "GET",
		url: "/comment/parent",
		data: {commentUUID:commentUUID}
	});
}

function renderParent(commentUUID){
	$.when(getParent(commentUUID))
	 .done(function(parent){
		 alert(parent.body);
	 });
}

function replyComment(data){
	return $.ajax({
		type: "POST",
		url: "/comment/reply",
		data: data 
	});
}

function addComment(data){
	return $.ajax({
		type: "POST",
		url: "/comment/add",
		data: data
	});
}

function like(commentUUID){
	return $.ajax({
		type: "POST",
		url: "/comment/like",
		data: {commentUUID:commentUUID}
	});
}

function dislike(commentUUID){
	return $.ajax({
		type: "POST",
		url: "/comment/dislike",
		data: {commentUUID:commentUUID}
	});
}

function getTemplate(){
	return $.ajax({
		      type: "GET",
			  url: "/comment/template"
		   });
}

function getComments(threadUUID, page){
	return $.ajax({
		      type: "GET",
		      url: "/comment/all",
		      data: {threadUUID:threadUUID, page:page}
		   });
}

function fetchCommentTemplate(){
	if(typeof htmlTemplate !== 'undefined' && htmlTemplate !== null){
		var d = new $.Deferred();
		d.resolve();
		
		return d.promise();
	}
	
	return $.ajax({
		type: 'GET',
		url: "/comment/template",
		success: function(data){
			htmlTemplate = data;
		}
	});
};

function formatDate(commentTimestamp){
	var timeDiff = (new Date().getTime() - commentTimestamp) / 1000;
	
	if(timeDiff <= 3600){
		return Math.floor(timeDiff / 60) + " minutes ago";
	}else if(timeDiff <= 86400){
		return Math.floor(timeDiff / 3600) + " hours ago";
	}else if(timeDiff <= 604800){
		return Math.floor(timeDiff / 86400) + " days ago";
	}else if(timeDiff <= 2629800){
		return Math.floor(timeDiff / 7257600) + " weeks ago";
	}else if(timeDiff <= 31557600){
		return Math.floor(timeDiff / 2629800) + " months ago";
	}else{
		return Math.floor(timeDiff / 31557600) + " years ago";
	}
}

function fillHtmlTemplate(template, comment){
	template = template.replace("{comment.user.displayName}", comment.user.displayName);
	template = template.replace("{comment.user.imageUrl}", comment.user.imageUrl);
	template = template.replace("{comment.body}", comment.body);
	template = template.replace("{comment.likes}", comment.likes);
	template = template.replace("{comment.uuid}", comment.uuid);
	template = template.replace("{comment.profileUrl}", domain + "/profile/" + comment.uuid);
	template = template.replace("{comment.time}", formatDate(comment.created));
	
	if(comment.hasParent){
		template = template.replace("{comment.hasParent}", "in reply to (show)");
	}else{
		template = template.replace("{comment.hasParent}", "");
	}
	
	return template;
}

function appendComment(template, comment){
	/*template = template.replace("{comment.user.displayName}", comment.user.displayName);
	template = template.replace("{comment.user.imageUrl}", comment.user.imageUrl);
	template = template.replace("{comment.body}", comment.body);
	template = template.replace("{comment.likes}", comment.likes);
	template = template.replace("{comment.uuid}", comment.uuid);
	template = template.replace("{comment.profileUrl}", domain + "/profile/" + comment.uuid);
	template = template.replace("{comment.time}", formatDate(comment.created));
	
	if(comment.hasParent){
		template = template.replace("{comment.hasParent}", "in reply to (show)");
	}else{
		template = template.replace("{comment.hasParent}", "");
	}*/
	
	template = fillHtmlTemplate(template, comment);
	
	if($('#commentContainer').children().last().length == 0){
		$('#commentContainer').html(template);
	}else{
		$('#commentContainer').children().last().append(template);
	}
}

function render(){
	var page = parseInt($('#commentContainer').attr('page'));
	var threadUUID = getURLParameter("thread");
	
	$.when(fetchCommentTemplate(), getComments(threadUUID, page)).done(function(templateResult, commentsResult){
		var template;
		var comments = commentsResult[0];
		if(typeof templateResult !== 'undefined' && templateResult !== null){
			template = templateResult[0];
		}else{
			template = htmlTemplate;
		}

		for(var i = 0; i < comments.length; i++){
			appendComment(template, comments[i]);
		}
		
		$('#commentContainer').attr('page', page + 1);
	});
}

function insertComment(comment){
	var template = fillHtmlTemplate(htmlTemplate, comment);
	$('#commentContainer').append(template);
}

$(document).ready(function(){
	render();
	
	
	$('#addComment').on('click', function(){
		var data = {};
		data.threadUUID = getURLParameter("thread");
		data.body = $('#body').val();
		
		addComment(data).done(function(data){
			$('#body').val('');
			insertComment(data);
		});
	});
	
	$('#commentContainer').on('click', '.like', function(){
		like($(this).closest('.comment').attr('id'));
	}).on('click', '.dislike', function(){
		dislike($(this).closest('.comment').attr('id'));
	}).on('click', '.reply', function(){
		var data = {};
		data.commentUUID = $(this).closest('.comment').attr("id");
		data.body = $("#body").val();
		
		replyComment(data);
	});
	
	$(document).on('click', '#more', function(){
		render();
	});
	
});