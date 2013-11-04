domain = location.protocol + "//" + location.host;

var $commentSection = $('#commentSection');
var $commentContainer = $('#commentContainer');
var $showMore = $('#showMore');
var $commentBody = $('#commentBody');

function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

function getParent(data){
	return $.ajax({
		type: "GET",
		url: "/comment/parent",
		data: data
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

function getComments(threadUUID, page, size){
	return $.ajax({
		      type: "GET",
		      url: "/comment/all",
		      data: {threadUUID:threadUUID, page:page, size:size}
		   }); 
}

function render(threadUUID, page, size){
	getComments(threadUUID, page, size).done(function(data){
		var page = parseInt($commentContainer.attr('page'));
		$commentContainer.attr('page', page + 1);
		$commentContainer.append(data); 
		
		var numOfComments = data.match(/class="comment"/g);
		if(numOfComments === null || numOfComments.length < parseInt($commentContainer.attr('size'))){
			$showMore.hide();
		}
	});
}

$(document).ready(function(){
	render(getURLParameter("thread"), $commentContainer.attr('page'), $commentContainer.attr('size'));
	
	$('#commentSection').on('click', '#showMore', function(){
		render(getURLParameter("thread"), $commentContainer.attr('page'), $commentContainer.attr('size'));
	}).on('click', '.like', function(){
		like($(this).closest('.comment').attr('id'));
	}).on('click', '.dislike', function(){
		dislike($(this).closest('.comment').attr('id'));
	}).on('click', '#addComment', function(){
		var data = {};
		data.threadUUID = getURLParameter("thread");
		data.body = $commentBody.val();
		
		addComment(data).done(function(data){
			$commentBody.val('');
			$commentContainer.prepend(data);
		});
	}).on('click', '.reply', function(){
		var data = {};
		data.commentUUID = $(this).closest('.comment').attr('id');
		data.body = $commentBody.val();
		
		replyComment(data).done(function(data){
			$commentBody.val('');
			$commentContainer.prepend(data);
		});
	}).on('click', '.show-parent', function(){
		var data = {};
		data.commentUUID = $(this).closest('.comment').attr('id');
		
		getParent(data).done(function(parent){
			var $childComment = $('#' + data.commentUUID);
			$childComment.before(parent);
			$childComment.addClass('child-comment');
			$childComment.find('.show-parent').first().parent().remove();
		});
	});
});