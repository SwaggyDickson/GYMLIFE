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

	var submitBtn = $(this);
	var orderId = submitBtn.attr('data-orderId');
	console.log(orderId);

	var formId = 'payCom-' + orderId;
	var form = document.getElementById(formId);

	var orderIdValue = form.elements["orderId"].value;
	var orderUserIdValue = form.elements["orderUserId"].value;
	var orderTimeValue = form.elements["orderTime"].value;
	var orderUuidValue = form.elements["orderUuid"].value;
	var totalPriceValue = form.elements["orderTotalPrice"].value;

	console.log(orderIdValue);
	console.log(orderUserIdValue);
	console.log(orderTimeValue);
	console.log(orderUuidValue);
	console.log(totalPriceValue);


	// 获取选中的支付方式的值
	var paymentMethod = $('input[name="payment"]:checked').val();

	console.log(paymentMethod);
	// 构建要发送给控制器的数据对象
	var data = {
		orderId: orderIdValue,
		orderUserId: orderUserIdValue,
		orderTime: orderTimeValue,
		orderUuid: orderUuidValue,
		orderTotalPrice: totalPriceValue,
		payment: paymentMethod
	};

	if (paymentMethod == "green") {
		$.ajax({
			url: 'http://localhost:8080/gymlife/checkoutCom', // 綠界科技方方法 URL
			type: 'POST',
			data: data,
			success: function(ecpayCheckoutFrom) {
				// 在这里处理返回的綠界表单
				// ecpayCheckoutFrom 是綠界表单的数据，可以根据需要进行处理
				// 例如，将表单插入到页面中的某个元素中
				$('#formContainer').html(ecpayCheckoutFrom);

				// 提交表单，实现跳转到綠界页面
				//$('#ecpayForm').submit();
			}
		});

	} else {

		$.ajax({
			url: 'http://localhost:8080/gymlife/getLinePay',
			type: 'POST',
			data: data,
			success: function(response) {
				console.log("res: "+response);
				
				//等待service層的cooonfirmURL
				// 提取支付链接并执行相应的操作
				var responseJson = JSON.parse(response);
				console.log("resJSON: "+responseJson);
				var webPaymentUrl = responseJson.webPaymentUrl;
				var appPaymentUrl = responseJson.appPaymentUrl;

				// 在这里执行跳转或其他操作
				// 例如，跳转到webPaymentUrl
				window.location.href = webPaymentUrl;// 跳转到支付链接
			},
			error: function(error) {
				console.log(error)
			}
		});
	}
	// 发送 AJAX 请求到控制器

});

/* 判斷打哪裡的JS結束 */



/* 更改文字顏色JS開始 */
// 取得目標Span元素
var spanElements = $('.orderDataContentState');
for (var i = 0; i < spanElements.length; i++) {
	// 取得文字內容
	var paymentStatus = spanElements.eq(i).text();
	console.log(paymentStatus);
	
	// 判斷文字內容並更改CSS樣式
	if (paymentStatus === '處理中') {
		spanElements.eq(i).css('color', 'red'); // 更改文字顏色為紅色
	} else if (paymentStatus === '已付款') {
		spanElements.eq(i).css('color', 'green'); // 更改文字顏色為綠色
		
		spanElements.eq(i).closest('.orderData').next('#dissapear2').css('display', 'none');
		
		  // 同時更改orderDataContent的文字顏色
        var orderDataContent = spanElements.eq(i).closest('.orderData').find('.orderDataContent');
        orderDataContent.css('color', 'green'); // 更改文字顏色為綠色
	}else if(paymentStatus === '已發貨'){
		spanElements.eq(i).css('color', 'red'); // 更改文字顏色為紅色
	}

}


/* 更改文字顏色JS結束 */



