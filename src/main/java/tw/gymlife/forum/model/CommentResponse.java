package tw.gymlife.forum.model;



public class CommentResponse {

	private CommentBean comment;
	private String commentContent;
	private String commentImg; // if you are sending the image as a base64 string
	private long commentTime; // timestamp of the comment
	
	private int currentPage;
	private int pageSize;
	private int totalComments; // total number of comments
	private int floorNumber; // the floor number of the comment
	
	
	public CommentBean getComment() {
		return comment;
	}
	public void setComment(CommentBean comment) {
		this.comment = comment;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentImg() {
		return commentImg;
	}
	public void setCommentImg(String commentImg) {
		this.commentImg = commentImg;
	}
	public long getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(long commentTime) {
		this.commentTime = commentTime;
	}
	public int getTotalComments() {
		return totalComments;
	}
	public void setTotalComments(int totalComments) {
		this.totalComments = totalComments;
	}
	public int getFloorNumber() {
		return floorNumber;
	}
	public void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
	}
	
	

}
