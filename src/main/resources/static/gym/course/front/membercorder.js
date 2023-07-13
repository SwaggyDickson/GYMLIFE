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
//送出更新訂單通知websocket
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
		updateCorderState(corderId);
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
//修改訂單狀態
function updateCorderState(corderId){
	axios({
		method:'put',
		url:'http://localhost:8080/gymlife/course/corder/state',
		params:{
		'corderId':corderId,
		'corderState':1	
		}
	})
	.then(res=>{
		console.log('res:'+JSON.stringify(res))
		corderHtmlMaker(res.data)
	})
}
function corderHtmlMaker(data){
	let alltable = document.getElementById('alltable');
	let html = '';
	html += `<thead>
							<tr class="table-light">
								<th scope="col"style="width:90px">訂單編號</th>
								<th scope="col"style="width:90px">會員編號</th>
								<th scope="col"style="width:100px">姓名</th>
								<th scope="col">電話</th>
								<th scope="col">E-mail</th>
								<th scope="col">課程名稱</th>
								<th scope="col"style="width:90px">付款狀態</th>
								<th scope="col">購買時間</th>
								<th scope="col" style="width:90px">購買數量</th>
								<th scope="col" style="width:110px">訂單總金額</th>
								<th class="editth " colspan="2" style="width:170px">編輯</th>
							</tr>
						</thead>
						<tbody>`;
							for(i=0;i<data.corder.length;i++){
								html+=`<tr class="table-light">
									<th scope="row" >${data.corder[i].corderId}</th>
									<td >${data.userId}</td>
									<td  >${data.userName}</td>
									<td >${data.userTel}</td>
									<td >${data.userEmail}</td>
									<td >${data.courseNameOrder[i]}</td>
									<td >${data.corder[i].corderPayment}</td>
									<td >${data.corder[i].corderTime}</td>
									<td ><input id="${data.corder[i].corderId}" class="form-control"type="number" name="corderQuantity" value="${data.corder[i].corderQuantity}" disabled style="background-color:white;"></td>
<!-- 									<td >${data.corder[i].corderQuantity}</td> -->
									<td ><input id="corderCost-${data.corder[i].corderId}"class="form-control " value="${data.corder[i].corderCost}" disabled style="background-color:white"></td>
									<td class="review"colspan="2" style="${data.corder[i].corderState===0?'display:none':'display:block'}"><input type="text" value="等待管理員審核" class="form-control" disabled style="width:140px"></td>
									<td class="updatebtn" style="${data.corder[i].corderState===0?'display:block':'display:none'};float:left;"><button
											class="btn btn-outline-primary btn-sm"
											id="updatebtn-${data.corder[i].corderId}"
											onclick="updateCorderBtn(${data.corder[i].corderId})" >
											更新</button>
										<input type="hidden" id="courseCost-${data.corder[i].corderId}" value="${data.courseCostOrder[i]}">
										<button class="btn btn-outline-primary btn-sm"
											id="updateclick-${data.corder[i].corderId}" onclick="updateCorder(${data.corder[i].corderId})" style="display:none;">提交</button>
									</td>
									<td class="deletebtn" style="${data.corder[i].corderState===0?'display:block':'display:none'};float:right;">
											<button type="button" class="btn btn-outline-danger btn-sm deletechbtn" name="coachId" data-deleteid="${data.corder[i].corderId}"
											onclick="deletecoach(${data.corder[i].corderId})">刪除</button>
									</td>
								</tr>`
							};

						html+=`</tbody>`;
						console.log(html)
						alltable.innerHTML=html;
}
