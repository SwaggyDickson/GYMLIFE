//更新數量紐
function updateCorderBtn(corderId){
	let updateinput = document.getElementById(corderId);
	updateinput.disabled = false;
	$(updateinput).css('background-color','lightgray');
	let cost = $('#courseCost-'+corderId).val();
	$(updateinput).on('change',function(e){
	$('#updatebtn-'+corderId).css('display','none');
	$('#updateclick-'+corderId).css('display','block');
	let quantity = $(updateinput).val()
	$('#corderCost-'+corderId).val(quantity*cost);
})
}
//function updateCorder(corderId) {
//$('#corderCost-'+corderId).val();
//	websocket(corderId);
//
//}

function updateCorder(corderId) {
	let corderQuantity = document.getElementById(corderId).value;
	let corderCost = $('#corderCost-'+corderId).val();
	var stompClient = null;
	var socket = new SockJS('/gymlife/websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({ corderId: corderId }, function(frame) {
		console.log("開啟連線");
//		var message = '更新訂單訊息';
		//要傳送的更新物件
		var message = { corderId: corderId ,corderQuantity:corderQuantity,corderCost:corderCost };
		var destination = '/send/updateCorder';
		stompClient.send(destination, {}, JSON.stringify(message));
		console.log("成功寄出");
		disconnect();
		Swal.fire(
						'訂單更新通知送出!',
						'',
						'success'
					)
//		stompClient.subscribe('/reply/notifications', function(notification) {
//			var notificationText = notification.body;
//			console.log('收到后台通知:', notificationText);
//			disconnect();
//		});
	}, function(error) {
		console.log("連線失敗：" + error);
	});
	function disconnect() {
		if (stompClient !== null) {
			stompClient.disconnect();
		}
		console.log("斷開連線");
	}
}