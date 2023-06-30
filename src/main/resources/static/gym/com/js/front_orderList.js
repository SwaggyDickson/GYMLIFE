/* 回到上方按鈕開始 */
// 當網頁滾動超過 20px 時，按鈕才會顯示
window.onscroll = function() {
	scrollFunction();
};

function scrollFunction() {
	if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
		document.getElementById("back-to-top-btn").classList.remove("hide");
	} else {
		document.getElementById("back-to-top-btn").classList.add("hide");
	}
}

// 按下按鈕後回到頁面頂端
document.getElementById("back-to-top-btn").onclick = function() {
	document.body.scrollTop = 0;
	document.documentElement.scrollTop = 0;
};
/* 回到上方按鈕結束 */
/* 判斷打哪裡的JS開始 */

$('.checkOutBtn').on('click', function(e) {
	e.preventDefault(); // 阻止表单默认提交行为

	// 获取选中的支付方式的值
	var paymentMethod = $('input[name="payment"]:checked').val();

	console.log(paymentMethod);
	// 构建要发送给控制器的数据对象
	var data = {
		orderId: $('[name="orderId"]').val(),
		orderUserId: $('[name="orderUserId"]').val(),
		orderTime: $('[name="orderTime"]').val(),
		orderUuid: $('[name="orderUuid"]').val(),
		orderTotalPrice: $('[name="orderTotalPrice"]').val(),
		payment: paymentMethod
	};
	if (paymentMethod == "green") {
		$.ajax({
			url: 'http://localhost:8080/gymlife/ecpayCheckoutCom', // 替换为你的控制器 URL
			type: 'POST',
			data: data,
			
		});

	} else {

	}
	// 发送 AJAX 请求到控制器

});

/* 判斷打哪裡的JS結束 */

