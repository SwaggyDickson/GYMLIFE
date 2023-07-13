
let userId;
let orderId;
let orderStatusTime;
let orderPayment;
var toastId = 'myToast'; // 设置 Toast 的唯一 ID


$(document).ready(function() {

	$.ajax({
		url: 'http://localhost:8080/gymlife/orderAnnotation',
		type: 'POST',
		dataType: "json",
		success: function(result) {
			console.log(result);

			if (Object.keys(result).length === 0) {
				$('.notif-bullet').css('display', 'none');
			}
			$.each(result, function(index, item) {
				userId = item.userId;
				orderId = item.orderUuid;
				orderStatusTime = item.orderStatusTime;
				orderPayment = item.orderPayment;

				showToast(userId, orderId, orderStatusTime, orderPayment);
			});
		},
		error: function(error) {
			console.log(error);
		}
	});


	//setInterval(showToast, 3000); // 每十秒执行一次showToast()
});

$('.notifyBtn').on('click', () => {

	// 检查页面中是否已经存在具有该 ID 的 Toast
	if ($('#' + toastId).length) {
		// 如果存在，则移除该 Toast
		$('#toastContainer').empty();
	} else {
		$.ajax({
			url: 'http://localhost:8080/gymlife/orderAnnotation',
			type: 'POST',
			dataType: "json",
			success: function(result) {
				console.log(result);

				$.each(result, function(index, item) {
					userId = item.userId;
					orderId = item.orderUuid;
					orderStatusTime = item.orderStatusTime;
					orderPayment = item.orderPayment;

					showToast(userId, orderId, orderStatusTime, orderPayment);
				});

			},
			error: function(error) {
				console.log(error);
			}
		});
	}
});




function showToast(userId, orderId, orderStatusTime, orderPayment) {

	// 获取当前时间
	var currentTime = new Date();

	// 将订单状态时间转换为日期对象
	var statusTime = new Date(orderStatusTime);

	// 计算时间差（以分钟为单位）
	var timeDiff = Math.round((currentTime - statusTime) / (1000 * 60));

	// 计算小时数和分钟数
	var hours = Math.floor(timeDiff / 60);
	var minutes = timeDiff % 60;

	// 生成时间描述
	var timeDescription = '';
	if (hours > 0) {
		timeDescription += hours + ' hr ';
	}
	timeDescription += minutes + ' min ago';

	if (orderPayment === '處理中') {
		// 如果订单付款状态为"已付款"
		// 修改 Toast 内容
		var adjustContent = "User: " + userId + '<br>已建立訂單 ' + orderId + '<br><span style="color:red;">請儘快處理</span>';
	}
	//------------------------------------------------------------------

	// 创建 Toast 组件
	var toast = $('<div class="toast myToast" id="' + toastId + '" role="alert" aria-live="assertive" aria-atomic="true" data-autohide="false">')
		.addClass('show') // 添加 show 类以显示 Toast
		.appendTo('#toastContainer'); // 将 Toast 添加到容器中

	// 创建 Toast 头部
	var toastHeader = $('<div class="toast-header">')
		.appendTo(toast); // 将 Toast 头部添加到 Toast 组件中

	// 创建 Toast 头部内容
	var toastHeaderContent = $('<strong class="mr-auto">')
		.text('通知')
		.appendTo(toastHeader); // 将 Toast 头部内容添加到 Toast 头部中

	// 创建 Toast 头部时间戳
	var toastTimestamp = $('<small>')
		.text(timeDescription)
		.appendTo(toastHeader); // 将 Toast 头部时间戳添加到 Toast 头部中

	// 创建 Toast 关闭按钮
	var toastCloseButton = $('<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">')
		.appendTo(toastHeader); // 将 Toast 关闭按钮添加到 Toast 头部中

	// 创建 Toast 关闭按钮图标
	$('<span aria-hidden="true">&times;</span>')
		.appendTo(toastCloseButton); // 将 Toast 关闭按钮图标添加到 Toast 关闭按钮中

	// 创建 Toast 内容
	var toastContent = $('<div class="toast-body">')
		.html(adjustContent)
		.appendTo(toast); // 将 Toast 内容添加到 Toast 组件中
	 
	 // 设置 Toast 的自动关闭
	 setTimeout(function() {
	   toast.remove(); // 关闭 Toast
	 }, 3000); // 3秒后自动关闭
	 
}

//關閉按鈕
$(document).on('click', '[data-dismiss="toast"]', function() {
	$(this).closest('.toast').remove(); // 找到最近的父级 Toast 并移除它
});
