
let userId;
let orderId;
let orderStatusTime;
let orderPayment;
var toastId = 'myToast'; // 设置 Toast 的唯一 ID

/* DataTable 開始 */
$(document).ready(function() {

	getAllOrders();
});

function getAllOrders(){
		//取得所有LIST
	$.ajax({
		url: 'http://localhost:8080/gymlife/getAllOrders',
		type: 'GET',
		dataType: "json",
		success: function(result) {
			console.log(result);
			destroyDataTable();
			createdTable(result);

		},
		error: function(error) {

			console.log(error);
		}

	})
}
function destroyDataTable() {
	$('#myTable').DataTable().destroy();
}
/* 一開始取得list 並建立表單*/
function createdTable(result) {

	var tableBody = document.getElementById("tbody");
	tableBody.innerHTML = ""; // 清空表格的内容
	let html = ""

	// 假设从服务器获取的JSON数据保存在result变量中
	for (var i = 0; i < result.length; i++) {
		var order = result[i];

		if (order.orderPayment === '處理中') {
			html = `<tr>
              <td class="orderUuid">${order.orderUuid}</td>
              <td class="orderUserId">${order.userId}</td>
              <td class="orderTime">${order.orderTime}</td>
              <td class="orderPayment">${order.orderPayment}</td>
              <td class="orderTotalPrice">${order.totalPrice}</td>
              <td class="changeStatus">
                <button class="btn btn-info" onclick="getChangeStatus('${order.orderUuid}')">發貨</button>
              </td>
            </tr>`;
		} else if (order.orderPayment === '已付款') {
			html = `<tr>
              <td class="orderUuid">${order.orderUuid}</td>
              <td class="orderUserId">${order.userId}</td>
              <td class="orderTime">${order.orderTime}</td>
              <td class="orderPayment">${order.orderPayment}</td>
              <td class="orderTotalPrice">${order.totalPrice}</td>
              <td class="changeStatus">
              	<span style="color: green;">顧客已付款</span>
              </td>
            </tr>`;
		} else if (order.orderPayment === '商家確認訂單中') {
			html = `<tr>
              <td class="orderUuid">${order.orderUuid}</td>
              <td class="orderUserId">${order.userId}</td>
              <td class="orderTime">${order.orderTime}</td>
              <td class="orderPayment">${order.orderPayment}</td>
              <td class="orderTotalPrice">${order.totalPrice}</td>
              <td class="changeStatus">
                <button class="btn btn-info" onclick="getCheckOrder(${order.orderUuid})">訂單更新</button>
              </td>
            </tr>`;
		} else if (order.orderPayment === '訂單已更新' || order.orderPayment === '已發貨') {
			html = `<tr>
              <td class="orderUuid">${order.orderUuid}</td>
              <td class="orderUserId">${order.userId}</td>
              <td class="orderTime">${order.orderTime}</td>
              <td class="orderPayment">${order.orderPayment}</td>
              <td class="orderTotalPrice">${order.totalPrice}</td>
              <td class="changeStatus">
              	<span style="color: red;">等待顧客付款中</span>
              </td>
            </tr>`;
		}


		tableBody.innerHTML += html;
		// 重新初始化DataTables实例
	}
	$('#myTable').DataTable({
		"order": [[2, "desc"]] // 設定初始排序欄位，此例中為第 1 欄（索引為 0），按照降序排列
	});
}

/* 發貨按鈕開始 */
function getChangeStatus(orderId) {
	// 在這裡執行你想要觸發的JavaScript功能
	console.log('點擊了按鈕，訂單ID為: ' + orderId);

	var data = {
		orderId: orderId,
	};
	$.ajax({
		url: 'http://localhost:8080/gymlife/sendComStatus.func',
		type: 'PUT',
		data: data,
		success: function(result) {
			console.log("result= "+result);
			
			getAllOrders();
		},
		error: function(error) {
			console.log(error);
		}

	});
	/* 發貨按鈕結束 */
}
/* DataTable 結束 */

/* 通知開始 除了已付款狀態(1)和已發貨狀態(4)外都會有通知  */ //0=處理中, 1=已付款, 2=商家確認訂單中, 3=訂單已更新, 4=已發貨
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
				showToastWhenGetReady(userId, orderId, orderStatusTime, orderPayment);
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
		var adjustContent = "User: " + userId + '<br>已建立訂單 <a href="http://localhost:8080/gymlife/getOrder.func" >' + orderId + '</a> <br><span style="color:red;">請儘快處理</span>';
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

	/*
	// 设置 Toast 的自动关闭
	setTimeout(function() {
	  toast.remove(); // 关闭 Toast
	}, 3000); // 3秒后自动关闭
	*/
}

function showToastWhenGetReady(userId, orderId, orderStatusTime, orderPayment) {

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
		var adjustContent = "User: " + userId + '<br>已建立訂單 <a href="http://localhost:8080/gymlife/getOrder.func" >' + orderId + '</a> <br><span style="color:red;">請儘快處理</span>';	
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
/* 通知結束 */

//關閉按鈕
$(document).on('click', '[data-dismiss="toast"]', function() {
	$(this).closest('.toast').remove(); // 找到最近的父级 Toast 并移除它
});