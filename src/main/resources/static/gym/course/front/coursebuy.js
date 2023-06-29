$('#buybtn').on('click', function(e) {
	e.preventDefault();
	$('#buycourseId').click();
})
//課程價格計算
$('#buynumber').on('change', function(e) {
	let courseCost = $('#courseCost').val();
	let buynumber = $('#buynumber').val();
	//  console.log(buynumber);
	$('#allcost').text(buynumber * courseCost);
	$('#allcost').text(buynumber * courseCost);
	$('#corderCost').val(buynumber * courseCost);
	console.log($('#corderCost').val())
});
//$('#paybtn').on('click', function(e) {
//	e.preventDefault();
//	let paymodal = document.getElementById('paymodal');
//	let userName = document.getElementById('userName').value;
//	let allcost = $('#allcost').text();
//	let html = '';
//	html += `<hr><div class="row">
//						<div class="col-lg-12">
//							<span>購買人:</span>${userName}
//						</div>
//						<div class="col-lg-12">
//							<a>總金額:</a><a id="payallcost">${allcost}</a>
//						</div>
//						<div class="col-lg-12">
//							<a>付款方式:</a>
//						</div>
//						<div class="col-lg-12">
//							<button type="button" class="btn btn-danger" onclick="insertcorder()">確認付款</button>
//						</div>
//					</div>`;
//	paymodal.innerHTML = html;
//})
//新增訂單
//function insertcorder() {
//	let userId = document.getElementById('userId').value;
//	let courseId = document.getElementById('courseId').value;
//	let corderQuantity = document.getElementById('buynumber').value;
//	let corderCost = document.getElementById('allcost').text;
//	console.log(userId);
//	console.log(courseId);
//	console.log(corderQuantity);
//	console.log(corderCost);
//	Swal.fire({
//		title: '確定要購買嗎?',
//		showDenyButton: true,
//		confirmButtonText: '確定',
//		denyButtonText: `取消`,
//	}).then((result) => {
//		if (result.isConfirmed) {
//			axios({
//				method: 'post',
//				url: 'http://localhost:8080/gymlife/course/order/insert',
//				params: {
//					'userId': userId,
//					'courseId': courseId,
//					'corderPayment': '完成',
//					'corderQuantity': corderQuantity,
//					'corderCost': corderCost
//				}
//			}).then(res => {
//				axios.post('http://localhost:8080/gymlife/ecpayCheckout')
//				//跳轉回教練課程
////				console.log('res' + JSON.stringify(res));
////				window.location.href = 'http://localhost:8080/gymlife/ecpayCheckout';
////				Swal.fire(
////					'購買成功!',
////					'',
////					'success'
////				)
//
//			})
//		} else if (result.isDenied) {
//		}
//	})
//}


